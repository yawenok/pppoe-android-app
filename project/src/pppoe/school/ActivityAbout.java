package pppoe.school;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActivityAbout extends Activity {

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);

		final Button btn_Back = (Button) findViewById(R.id.btnAboutBack);
		btn_Back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Perform action on click

				ActivityAbout.this.finish();
			}
		});
	}
}
