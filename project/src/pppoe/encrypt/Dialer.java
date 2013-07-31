package pppoe.encrypt;

import android.annotation.SuppressLint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import pppoe.school.AppLog;
import pppoe.school.AppValue;


/**
 * 
 * µ˜”√rp-pppoe÷¥––≤¶∫≈√¸¡Ó
 * 
 * @author yawen
 *
 */
@SuppressLint("NewApi") public class Dialer {

	public int pppoeConnect(String strUser, String strPassword,
			boolean bEthernet) {
		final Command command = new Command();

		try {
			if (strUser.isEmpty())
				return -1;

			AppLog.Log("dial name: " + strUser);

			String strInterface = command.getEthnet("wlan");

			if (bEthernet || strInterface.isEmpty())
				strInterface = command.getEthnet("eth");

			if (strInterface.isEmpty()) {
				strInterface = command.getEthnet("wlan");
				if (strInterface.isEmpty())
					return -1;
			}

			AppLog.Log("dial interface: " + strInterface);

			final String strPppPath = AppValue.SELF_PPPD_PATH;
			final String strRoutePath = AppValue.SELF_ROUTE_PATH;
			final String strPppoePath = AppValue.PPPOE_PATH;
			final String strPppoePid = AppValue.PPPOE_PID;

			command.rootCommand("chmod 777 " + strPppPath);
			command.rootCommand("chmod 777 " + strRoutePath);
			command.rootCommand("chmod 777 " + strPppoePath);

			final String strCmd = 
					strPppPath + " pty '" + 
			        strPppoePath + " -p " + 
					strPppoePid + " -I " + 
			        strInterface + " -T 30 -U -m 1412' noipdefault noauth default-asyncmap defaultroute hide-password nodetach usepeerdns mtu 1492 mru 1492 noaccomp nodeflate nopcomp novj novjccomp user " + 
					strUser + " password " + 
			        strPassword + " lcp-echo-interval 20 lcp-echo-failure 3 &";

			command.rootCommand(strCmd);

			String strPPP = command.getEthnet("ppp");

			for (int i = 0; i <= 100; i++) {
				strPPP = command.getEthnet("ppp");
				Thread.sleep(100);

				if (!strPPP.isEmpty())
					break;
			}

			if (strPPP.isEmpty())
				return -1;

			command.rootCommand("chmod 777 " + strPppoePid);

			AppLog.Log("dial ppp: " + strPPP);
			
			command.rootCommand(strRoutePath + " del default");
			command.rootCommand(strRoutePath + " add default " + strPPP);
			command.rootCommand(strRoutePath + " add -net 0.0.0.0 netmask 0.0.0.0 dev " + strPPP);

			Thread.sleep(1000);

			String strDns1 = command.getDNS1(strPPP);
			String strDns2 = command.getDNS2(strPPP);

			if (strDns1.isEmpty())
				strDns1 = "8.8.8.8";

			command.setDNS1(strDns1);
			command.setDNS2(strDns2);
			
			AppLog.Log("dial dns1:" + strDns1 + " dns2:" + strDns2);
			
		} catch (final NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 1;
	}

	public boolean pppoeHangup() {
		try {
			final File localFile = new File(AppValue.PPPOE_PID);
			final Command command = new Command();
			if (localFile.exists()) {
				final FileReader localFileReader = new FileReader(localFile);
				final BufferedReader localBufferedReader = new BufferedReader(localFileReader);
				final int j = Integer.parseInt(localBufferedReader.readLine());
				final String strPID = "kill -9 " + j;
				command.rootCommand(strPID);
				
				AppLog.Log("pppoeHangup: " + strPID);
			}
			return true;
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}