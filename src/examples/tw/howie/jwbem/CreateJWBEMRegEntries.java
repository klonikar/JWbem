package tw.howie.jwbem;

// Execute using java -cp 
import java.net.UnknownHostException;

import org.jinterop.dcom.common.JIDefaultAuthInfoImpl;
import org.jinterop.dcom.common.JIErrorCodes;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.common.JISystem;
import org.jinterop.winreg.IJIWinReg;
import org.jinterop.winreg.JIPolicyHandle;
import org.jinterop.winreg.JIWinRegFactory;

public class CreateJWBEMRegEntries {

	public static void main(String[] args) {
		// create the registry entries.
		String clsid = "76A64158-CB41-11D1-8B02-00600806D9B6";
		try {
			boolean isSSOEnabled = Boolean.getBoolean(System.getProperty("SSOEnabled", "false"));
			String targetServer = System.getProperty("TargetServer", "16.184.47.120");
			IJIWinReg registry = null;
			if (isSSOEnabled)
			{
				registry = JIWinRegFactory.getSingleTon().getWinreg(targetServer, true);	
			}
			else
			{
				String userName = System.getProperty("UserName", "hyperadm");
				String domain = System.getProperty("Domain", "IWFLABS");
				String password = System.getProperty("Password", "1iso*help");
				registry = JIWinRegFactory.getSingleTon().getWinreg(new JIDefaultAuthInfoImpl(domain, userName, password), targetServer, true);	
			}
			
			JIPolicyHandle hklm = null;
			JIPolicyHandle hkwow6432 = null;
			try
			{
			    // Try 64bit first...
			    hklm = registry.winreg_OpenHKLM();
			    hkwow6432 = registry.winreg_OpenHKCR();
			    //hkwow6432 = registry.winreg_OpenKey(hklm,"SOFTWARE\\Classes\\Wow6432Node", IJIWinReg.KEY_ALL_ACCESS);
			}
			catch (JIException jie) {
				jie.printStackTrace();
			}

			if (hklm != null)
			    registry.winreg_CloseKey(hklm);

			if (hkwow6432 != null)
			{
			    JISystem.getLogger().info("Attempting to register on 64 bit");
			    // HKEY_CLASSES_ROOT\CLSID\{76A64158-CB41-11D1-8B02-00600806D9B6}\ -- "AppID"
			    JIPolicyHandle key = registry.winreg_CreateKey(hkwow6432, "CLSID\\{" + clsid + "}", IJIWinReg.REG_OPTION_NON_VOLATILE, IJIWinReg.KEY_ALL_ACCESS);
			    registry.winreg_SetValue(key, "AppId", ("{" + clsid + "}").getBytes(), false, false);
			    registry.winreg_CloseKey(key);
			    JISystem.getLogger().info("--- winreg_SetValue --- SOFTWARE\\Classes\\Wow6432Node\\CLSID\\" + clsid + " -- AppID");

			    // HKEY_CLASSES_ROOT\AppID\{76A64158-CB41-11D1-8B02-00600806D9B6}\ -- "DllSurrogate"
			    key = registry.winreg_CreateKey(hkwow6432, "AppID\\{" + clsid + "}", IJIWinReg.REG_OPTION_NON_VOLATILE, IJIWinReg.KEY_ALL_ACCESS);
			    registry.winreg_SetValue(key, "DllSurrogate", "".getBytes(), false, false);
			    registry.winreg_CloseKey(key);

			    JISystem.getLogger().info("--- winreg_SetValue --- SOFTWARE\\Classes\\Wow6432Node\\AppID\\" + clsid + " -- DllSurrogate");
			    registry.winreg_CloseKey(hkwow6432);
			}
			else
			{
				JISystem.getLogger().info("Attempting to register on 32 bit");
			    JIPolicyHandle hkcr = registry.winreg_OpenHKCR();
			    JIPolicyHandle key = registry.winreg_CreateKey(hkcr,"CLSID\\{" + clsid + "}",IJIWinReg.REG_OPTION_NON_VOLATILE,IJIWinReg.KEY_ALL_ACCESS );
			    registry.winreg_SetValue(key,"AppID",("{" + clsid + "}").getBytes(),false,false);
			    registry.winreg_CloseKey(key);
			    key = registry.winreg_CreateKey(hkcr,"AppID\\{" + clsid + "}",IJIWinReg.REG_OPTION_NON_VOLATILE,IJIWinReg.KEY_ALL_ACCESS );
			    registry.winreg_SetValue(key,"DllSurrogate", "  ".getBytes(),false,false);

			    registry.winreg_CloseKey(key);
			    registry.winreg_CloseKey(hkcr);
			}
			registry.closeConnection();
		} catch (UnknownHostException | JIException e1) {
			//auto registration failed as well...
			JISystem.getLogger().warning("Error in creating registry entries: " + Integer.toHexString(JIErrorCodes.JI_WINREG_EXCEPTION3));
			JISystem.getLogger().throwing("JIComServer","initialise",e1);
			e1.printStackTrace();
		}
	}

}
