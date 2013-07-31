package pppoe.school;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PPPoEBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		boolean isServiceRunning = false;

		if (intent.getAction().equals(Intent.ACTION_TIME_TICK)) {
			// ¼ì²éService×´Ì¬
			ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
				if ("so.xxxx.PPPoEService".equals(service.service.getClassName())) {
					isServiceRunning = true;
				}
			}
			if (!isServiceRunning) {
				Intent i = new Intent(context, PPPoEService.class);
				context.startService(i);
			}

			AppLog.Log("service status: " + isServiceRunning);
		}
	}

}
