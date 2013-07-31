package pppoe.school;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class PopDialog extends Dialog {

	private RadioGroup radioGroup = null;
	private AppConfig appConfig = null;
	private Context context = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// 根据ID找到RadioGroup实例
		radioGroup = (RadioGroup) this.findViewById(R.id.radio_Group);
		// 绑定一个匿名监听器
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub

				switch (arg0.getCheckedRadioButtonId()) {
				case R.id.radioBtnNormal:
					appConfig.setTelecom(AppConfig.DialType.NOARMAL);
					break;

//				case R.id.radioBtnTelecom:
//					appConfig.setTelecom(AppConfig.DialType.TELECOM);
//					break;
					
				case R.id.radioBtnHenanUnicom:
					appConfig.setTelecom(AppConfig.DialType.HENANUNICOM);
					break;
				}

				AppLog.Log("setTelecom: " + arg0.getCheckedRadioButtonId());
				
				PopDialog.this.dismiss();
			}
		});

		if (appConfig != null) {
			switch (appConfig.getTelecom()) {
			case AppConfig.DialType.NOARMAL:
				radioGroup.check(R.id.radioBtnNormal);
				break;

//			case AppConfig.DialType.TELECOM:
//				radioGroup.check(R.id.radioBtnTelecom);
//				break;
				
			case AppConfig.DialType.HENANUNICOM:
				radioGroup.check(R.id.radioBtnHenanUnicom);
				break;
			}

			AppLog.Log("getTelecom: " + appConfig.getTelecom());
		}
	}

	public PopDialog(final Context context) {
		super(context, R.style.Dialog);
		this.context = context;
		appConfig = new AppConfig(this.context);
		this.setContentView(R.layout.viewpopup);
	}
}
