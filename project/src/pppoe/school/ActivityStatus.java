package pppoe.school;

import pppoe.encrypt.Command;
import pppoe.encrypt.Dialer;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;

public class ActivityStatus extends Activity {

	public static class NetFlags {
		public static final int DEFAULT_NET = 0;
		public static final int CONNECT = 1;
		public static final int CONNECT_ERROR = 2;
		public static final int DIS_CONNECT = 3;
	};

	public static class DnsFlags {
		public static final int DEFAULT_DNS = 0;
		public static final int DNS_GET = 1;
		public static final int DNS_ERROR = 2;
		public static final int DNS_GET_OK = 5;
		public static final int DNS_MAX = 65535;
	}

	private int netFlags = NetFlags.DEFAULT_NET;
	private int dnsFlags = DnsFlags.DEFAULT_DNS;

	private boolean nReceiver = false;
	
	private Handler handlerCheck = null;
	private Runnable runnableCheck = null;

	private AppStatus appStatus = null;
	private AppNotify appNotify = null;
	private PPPoEBroadcastReceiver receiver  = null;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_status);

		appStatus = new AppStatus(ActivityStatus.this);
		appNotify = new AppNotify(ActivityStatus.this);
		receiver = new PPPoEBroadcastReceiver();
		
		final Button btnHangUp = (Button) findViewById(R.id.btnHangUp);
		final TextView textStatus = (TextView) findViewById(R.id.textStatus);
		final ImageView imgStatus = (ImageView) findViewById(R.id.imgStatus);
		
		btnHangUp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on click

				switch (netFlags) {
				case NetFlags.CONNECT:
					hangupNet();
					break;

				case NetFlags.CONNECT_ERROR:
					checkError(textStatus, btnHangUp);
					break;

				case NetFlags.DIS_CONNECT:
					backMain();
					break;
				}
			}
		});
		
		final Command command = new Command();
		final TipDialog tipDialog = new TipDialog(ActivityStatus.this, "状态检测......");
		final AppConfig appConfig = new AppConfig(ActivityStatus.this);
		
		handlerCheck = new Handler();
		runnableCheck = new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				// 首次执行检查Root错误，避免时刻弹出Toast
				if (dnsFlags == DnsFlags.DEFAULT_DNS) {
					if (!command.isRoot() || appStatus.getStatus() == AppStatus.STATE_ERROR) {
						stopRunning();

						setStatusView(R.xml.img_style_2, R.string.pppoe_check, R.string.status_error);
						dnsFlags = DnsFlags.DNS_ERROR;
						netFlags = NetFlags.CONNECT_ERROR;
						tipDialog.dismiss();
						
						return;
					}
				}

				// 累加轮询
				if (dnsFlags < DnsFlags.DNS_MAX)
					dnsFlags++;

				AppLog.Log("dial status: " + appStatus.getStatus());
				
				switch (appStatus.getStatus()) {
				case AppStatus.STATE_CONNECT:
					setStatusView(R.xml.img_style_1, R.string.pppoe_disconnect, R.string.status_connect);
					
					// 轮训到此条件时，可以判读DNS的获取情况了
					if (dnsFlags == DnsFlags.DNS_GET_OK && netFlags == NetFlags.CONNECT) {
						if (command.dnsReset()) {
							if (appConfig.getMoniter()) {
								startMoniter();
							}
							tipDialog.dismiss();
						} else
							dnsFlags = DnsFlags.DNS_ERROR;

						AppLog.Log("dnsflags: " + dnsFlags);
					}
					netFlags = NetFlags.CONNECT;
					break;

				case AppStatus.STATE_ERROR:
					stopRunning();
					stopMoniter();
					setStatusView(R.xml.img_style_2, R.string.pppoe_check, R.string.status_error);
					netFlags = NetFlags.CONNECT_ERROR;
					tipDialog.dismiss();
					return;

				case AppStatus.STATE_DISCONNECT:
					stopRunning();
					stopMoniter();
					setStatusView(R.xml.img_style_3, R.string.pppoe_return, R.string.status_disconnect);
					netFlags = NetFlags.DIS_CONNECT;
					tipDialog.dismiss();
					return;
				}

				// 每两秒执行一次_handler_Net.
				handlerCheck.postDelayed(this, 2000);
			}

			private void setStatusView(final int img_resid, final int btn_resid, final int text_resid) {
				imgStatus.setBackgroundResource(img_resid);
				final AnimationDrawable animationDrawable = (AnimationDrawable) imgStatus.getBackground();
				imgStatus.post(new Runnable() {
					@Override
					public void run() {
						animationDrawable.start();
					}
				});

				btnHangUp.setText(btn_resid);
				textStatus.setText(text_resid);
			}
		};

		// 在启动检查前 显示检查对话框
		tipDialog.show();
		handlerCheck.postDelayed(runnableCheck, 0);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		appNotify.cancelNotify();
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		stopRunning();
		appNotify.cancelNotify();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (appStatus.getStatus()) {
			case AppStatus.STATE_ERROR:
			case AppStatus.STATE_DISCONNECT:
				backMain();
				break;

			case AppStatus.STATE_CONNECT: 
				Intent intent = new Intent(Intent.ACTION_MAIN);
				intent.addCategory(Intent.CATEGORY_HOME);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				break;

			default:
				break;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void hangupNet() {
		final TipDialog tip = new TipDialog(ActivityStatus.this, "正在断开......");
		
		stopRunning();
		
		stopMoniter();
		
		tip.show();

		new Thread() {
			@Override
			public void run() {
				final Dialer dialer = new Dialer();
				final Intent intent = new Intent(ActivityStatus.this, ActivityMain.class);

				try {
					dialer.pppoeHangup();
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				startActivity(intent);
				ActivityStatus.this.finish();
				
				tip.dismiss();
			}
		}.start();
	}

	private void stopRunning() {
		if (handlerCheck != null) {
			handlerCheck.removeCallbacks(runnableCheck);
			handlerCheck = null;
		}
	}
	
	private void startMoniter() {
		startService(new Intent(ActivityStatus.this, PPPoEService.class));
		registerReceiver(receiver, new IntentFilter(Intent.ACTION_TIME_TICK));
		nReceiver = true;
	}
	
	private void stopMoniter() {
		stopService(new Intent(ActivityStatus.this, PPPoEService.class));
		if (nReceiver)
			unregisterReceiver(receiver);
		nReceiver = false;
	}

	private void backMain() {
		final Intent intent = new Intent(ActivityStatus.this, ActivityMain.class);
		startActivity(intent);
		ActivityStatus.this.finish();
	}

	private void checkError(final TextView textState, final Button btnHangUp) {
		final TipDialog tipDialog = new TipDialog(ActivityStatus.this, "正在检测......");

		tipDialog.show();

		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				tipDialog.dismiss();
			}
		}.start();

		String strError = appStatus.getError();

		btnHangUp.setText(R.string.pppoe_return);
		textState.setText(strError);

		netFlags = NetFlags.DIS_CONNECT;
	}
}
