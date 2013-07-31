package pppoe.school;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

public class TipDialog extends Dialog {

	public TipDialog(final Context context, final String tip) {
		super(context, R.style.Dialog);

		this.setContentView(R.layout.viewdialog);

		setCancelable(false);

		TextView textTitle = (TextView) this.findViewById(R.id.textTip);
		textTitle.setText(tip);
	}

	public void setTip(final String tip) {
		TextView textTitle = (TextView) this.findViewById(R.id.textTip);
		textTitle.setText(tip);
	}
}
