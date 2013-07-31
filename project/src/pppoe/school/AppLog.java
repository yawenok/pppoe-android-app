package pppoe.school;

import android.util.Log;

public class AppLog {

	private static final String TAG = "PPPoE";

	public static void Log(final String strLog) {
		if (BuildConfig.DEBUG)
			Log.d(TAG, strLog);
	}
}
