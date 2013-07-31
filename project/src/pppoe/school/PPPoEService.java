package pppoe.school;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class PPPoEService extends Service {

	private Handler handlerCheck = null;
	private Runnable runnableCheck = null;

	@Override
	public void onCreate() {
		super.onCreate();

		final AppStatus appStatus = new AppStatus(PPPoEService.this);
		final AppNotify appNotify = new AppNotify(PPPoEService.this);

		handlerCheck = new Handler();
		runnableCheck = new Runnable() {
			@Override
			public void run() {

				int nStatus = appStatus.getStatus();
				switch (nStatus) {
				case AppStatus.STATE_CONNECT:
					break;

				case AppStatus.STATE_ERROR:
					stopThis();
					appNotify.notifyStatus("网络异常！", "现在已经断开网络，点击此处查看信息！");
					return;

				case AppStatus.STATE_DISCONNECT:
					stopThis();
					appNotify.notifyStatus("网络已断开!", "点击此处查看信息！");
					return;
				}

				// 每两秒执行一次_handler_Net.
				handlerCheck.postDelayed(this, 2000);
			}
		};

		handlerCheck.postDelayed(runnableCheck, 0);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		
		AppLog.Log("server: start");
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		stopRunning();
		
		AppLog.Log("server: stop");
		super.onDestroy();
	}

	private void stopRunning() {
		if (handlerCheck != null) {
			handlerCheck.removeCallbacks(runnableCheck);
			handlerCheck = null;
		}
	}

	private void stopThis() {
		stopRunning();
		stopSelf();
	}
}
