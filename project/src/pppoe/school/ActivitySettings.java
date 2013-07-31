package pppoe.school;

import pppoe.control.WiperSwitchTouch;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;

public class ActivitySettings extends Activity {

	private WiperSwitchTouch switchEthernet = null;
	private WiperSwitchTouch switchMoniter = null;

	private PopDialog popMenu = null;
	private AppConfig appConfig = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_settings);

		appConfig = new AppConfig(this);
		popMenu = new PopDialog(ActivitySettings.this);

		final Button btnBack = (Button) findViewById(R.id.btnSettingsBack);
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on click
				ActivitySettings.this.finish();
			}
		});

		final Button btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on click
				resetConfig();
			}
		});

		switchEthernet = (WiperSwitchTouch) findViewById(R.id.switchEthernet);
		switchEthernet.setOnChangedListener(new WiperSwitchTouch.OnChangedListener() {

					@Override
					public void OnChanged(WiperSwitchTouch wiperSwitch, boolean checkState) {
						// TODO Auto-generated method stub
						
						appConfig.setEthernet(checkState);

						AppLog.Log("switchEthernet state: " + checkState);
					}
				});

		switchMoniter = (WiperSwitchTouch) findViewById(R.id.switchMonitor);
		switchMoniter.setOnChangedListener(new WiperSwitchTouch.OnChangedListener() {

					@Override
					public void OnChanged(WiperSwitchTouch wiperSwitch,
							boolean checkState) {
						// TODO Auto-generated method stub

						appConfig.setMoniter(checkState);

						AppLog.Log("switchMoniter state: " + checkState);
					}
				});

		if (switchEthernet != null)
			switchEthernet.setChecked(appConfig.getEthernet());

		if (switchMoniter != null)
			switchMoniter.setChecked(appConfig.getMoniter());
	}

	public void typeClick(View v) {
		popMenu.show();
	}

	public void aboutClick(View v) {
		Intent intent = new Intent(ActivitySettings.this, ActivityAbout.class);
		startActivity(intent);
	}

	public void downloadClick(View v) {
		Intent intent = new Intent();
		intent.setAction("android.intent.action.VIEW");
		Uri content_url = Uri.parse("http://www.baidu.com");
		intent.setData(content_url);
		startActivity(intent);
	}

	public void resetConfig() {
		final TipDialog tip = new TipDialog(ActivitySettings.this, "ÕýÔÚÖØÖÃ......");

		tip.show();

		new Thread() {
			@Override
			public void run() {
				appConfig.resetConfig();

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ActivitySettings.this.finish();

				tip.dismiss();
			}
		}.start();
	}
}
