package pppoe.school;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppConfig {

	private Context context = null;

	public static final String APP_SAVE_INFO = "store";
	public static final String APP_VERSION = "2.0";

	public static final String INFO_VERSION = "Version";
	public static final String INFO_USER = "User";
	public static final String INFO_PASSWORD = "Password";
	public static final String INFO_ETHERNET = "EThernet";
	public static final String INFO_TELECOM = "Telecom";
	public static final String INFO_MONITOR = "Monitor";

	public static final int TYPE_DEFAULT = 0;
	public static final int TYPE_NORMAL = 1;
	public static final int TYPE_TELECOM = 2;

	public static class DialType {
		public static final int NOARMAL = 0;
		public static final int TELECOM = 1;
		public static final int HENANUNICOM = 2;
	}
	
	public AppConfig(Context context) {
		this.context = context;
	}

	public String getUser() {
		return getPreferences().getString(INFO_USER, "");
	}

	public boolean setUser(String strUser) {
		Editor spEd = getEditor();
		spEd.putString(INFO_USER, strUser);
		return spEd.commit();
	}

	public String getPassword() {
		return getPreferences().getString(INFO_PASSWORD, "");
	}

	public boolean setPassword(String strPassword) {
		Editor spEd = getEditor();
		spEd.putString(INFO_PASSWORD, strPassword);
		return spEd.commit();
	}

	public int getTelecom() {
		return getPreferences().getInt(INFO_TELECOM, DialType.NOARMAL);
	}

	public boolean setTelecom(int type) {
		Editor spEd = getEditor();
		spEd.putInt(INFO_TELECOM, type);
		return spEd.commit();
	}

	public boolean getMoniter() {
		return getPreferences().getBoolean(INFO_MONITOR, true);
	}

	public boolean setMoniter(boolean bMonite) {
		Editor spEd = getEditor();
		spEd.putBoolean(INFO_MONITOR, bMonite);
		return spEd.commit();
	}

	public boolean getEthernet() {
		return getPreferences().getBoolean(INFO_ETHERNET, true);
	}

	public boolean setEthernet(boolean bEthernet) {
		Editor spEd = getEditor();
		spEd.putBoolean(INFO_ETHERNET, bEthernet);
		return spEd.commit();
	}

	public boolean getNewVersion() {
		final String strVersion = getPreferences().getString(INFO_VERSION, "");

		if (strVersion.contentEquals(APP_VERSION))
			return false;

		return true;
	}

	public boolean setNewVersion() {
		Editor spEd = getEditor();
		spEd.putString(INFO_VERSION, APP_VERSION);
		return spEd.commit();
	}

	public void resetConfig() {
		Editor spEd = getEditor();

		spEd.clear();
		spEd.commit();
	}

	private SharedPreferences getPreferences() {
		return context.getSharedPreferences(APP_SAVE_INFO, 0);
	}

	private Editor getEditor() {
		return getPreferences().edit();
	}

}
