package pppoe.school;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AppNotify {

	private Context context = null;
	private NotificationManager notifyManager = null;

	public AppNotify(Context context) {
		this.context = context;
		notifyManager = (NotificationManager) this.context.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public void cancelNotify() {
		notifyManager.cancel(AppValue.NOTIFY_ID);
	}

	public void notifyStatus(final String contentTitle, final String contentText) {
		Notification notification = new Notification(R.drawable.share_face_04, "网络通知", System.currentTimeMillis());

		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		notification.flags |= Notification.FLAG_NO_CLEAR;
		notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;

		// 定义下拉通知栏时要展现的内容信息
		Intent intent = new Intent(context, ActivityStatus.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
		notification.setLatestEventInfo(context, contentTitle, contentText, pendingIntent);

		notifyManager.notify(AppValue.NOTIFY_ID, notification);
	}
}
