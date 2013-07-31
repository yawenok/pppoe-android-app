package pppoe.school;

import pppoe.encrypt.Dialer;
import pppoe.encrypt.Encrypt;

import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;

@SuppressLint("NewApi") public class ActivityMain extends Activity {

	private EditText editPassword = null;
	private EditText editUser = null;

	private AppConfig appConfig = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		appConfig = new AppConfig(ActivityMain.this);

		editUser = (EditText) findViewById(R.id.editUser);
		editPassword = (EditText) findViewById(R.id.editPassword);

		editUser.setText(appConfig.getUser());
		editPassword.setText(appConfig.getPassword());

		final Button btnConnect = (Button) findViewById(R.id.btnConnect);
		btnConnect.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on click
				final String strUser = editUser.getText().toString();
				final String strPassword = editPassword.getText().toString();

				if (strUser.isEmpty() || strPassword.isEmpty()) {
					Toast.makeText(ActivityMain.this, "账号和密码不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}

				connectNet(strUser, strPassword);
			}
		});

		final TextView textSettings = (TextView) findViewById(R.id.textSettings);
		textSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on click
				Intent intent = new Intent(ActivityMain.this, ActivitySettings.class);
				startActivity(intent);
			}
		});
		
		final TextView textCopy = (TextView) findViewById(R.id.textCopy);
		textCopy.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on click
				final Encrypt encrypt = new Encrypt();
				final String strUser = editUser.getText().toString();
//				final String strPassword = editPassword.getText().toString();
				final ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				
				String strRealName = null;

				switch (appConfig.getTelecom()) {
				case AppConfig.DialType.NOARMAL:
					strRealName = strUser;
					break;

//				case AppConfig.DialType.TELECOM:
//					strRealName = encrypt.stringTelecomEncrypt(strUser, strPassword);
//					break;

				case AppConfig.DialType.HENANUNICOM:
					strRealName = encrypt.stringHenanEncrypt(strUser);
					break;
					
				default:
					strRealName = strUser;
					break;
				}
				
				clipboard.setText(strRealName);
				
				Toast.makeText(ActivityMain.this, "真实账号已复制！", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void connectNet(final String strUser, final String strPassword) {
		final TipDialog tip = new TipDialog(ActivityMain.this, "正在拨号...");
		final boolean bEthernet = appConfig.getEthernet();
		final Encrypt encrypt = new Encrypt();

		tip.show();

		AppLog.Log("user: " + strUser + " password: " + strPassword);
		
		appConfig.setUser(strUser);
		appConfig.setPassword(strPassword);

		new Thread() {
			@Override
			public void run() {
				final Dialer dialer = new Dialer();
				final Intent intent = new Intent(ActivityMain.this, ActivityStatus.class);
				String strRealName = null;

				switch (appConfig.getTelecom()) {
				case AppConfig.DialType.NOARMAL:
					strRealName = strUser;
					break;

//				case AppConfig.DialType.TELECOM:
//					strRealName = encrypt.stringTelecomEncrypt(strUser, strPassword);
//					break;

				case AppConfig.DialType.HENANUNICOM:
					strRealName = encrypt.stringHenanEncrypt(strUser);
					break;
					
				default:
					strRealName = strUser;
					break;
				}

				try {
					dialer.pppoeConnect(strRealName, strPassword, bEthernet);
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				startActivity(intent);
				ActivityMain.this.finish();

				tip.dismiss();
			}
		}.start();
	}
}
