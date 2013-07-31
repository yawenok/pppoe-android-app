package pppoe.school;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class ActivityWelcome extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_welcome);

		final AppConfig appConfig = new AppConfig(this);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = null;
				
				AppLog.Log("version: " + appConfig.getNewVersion());

				if (appConfig.getNewVersion())
					intent = new Intent(ActivityWelcome.this,
							ActivityWhatsNew.class);
				else
					intent = new Intent(ActivityWelcome.this,
							ActivityMain.class);

				startActivity(intent);
				ActivityWelcome.this.finish();
			}
		}, 2000);
	}
}
