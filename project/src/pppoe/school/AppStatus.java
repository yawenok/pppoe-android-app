package pppoe.school;

import pppoe.encrypt.Command;
import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

@SuppressLint("NewApi") public class AppStatus {

	public static final int STATE_CONNECT = 1;
	public static final int STATE_DISCONNECT = 0;
	public static final int STATE_ERROR = -1;
	public static final int STATE_HAS_PPPD = -2;

	private Context context = null;

	public AppStatus(Context context) {
		this.context = context;
	}

	public int getStatus() {
		final Command command = new Command();
		
		if (command.getEthnet("wlan").isEmpty() && command.getEthnet("eth").isEmpty()) {
			return STATE_ERROR;
		} else if (!getNetConnected()) {
			return STATE_ERROR;
		} else if (command.getEthnet("ppp").isEmpty()) {
			return STATE_DISCONNECT;
		}

		return STATE_CONNECT;
	}

	public boolean getNetConnected() {
		final ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo wifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return wifi.isConnected();
	}

	public String getError() {
		Command command = new Command();

		String strError = "故障问题：";

		// 检测网卡
		if (command.getEthnet("wlan").isEmpty() && command.getEthnet("eth").isEmpty()) {
			strError += "\n没有可用网卡！PS:无线网络已连接？或网线已插好？";
		} else if (!getNetConnected()) {
			strError += "\n靠...PS:网络没有连接，请确认已接入Wifi热点或有线网络！";
		}
		// 检测root权限
		else if (!command.isRoot()) {
			strError += "\n没有Root权限！PS:把手机Root后再试试吧！";
		} else {
			strError += "\n囧...PS:要不再试试吧，还不行就到论坛刷入支持拨号的Rom吧！";
		}

		return strError;
	}
}
