package pppoe.encrypt;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.annotation.SuppressLint;

import pppoe.school.AppLog;


/**
 * 
 * ÷¥––shell√¸¡Ó
 * 
 * @author yawen
 *
 */
@SuppressLint("NewApi") public class Command {

	public void rootCommand(final String strCmd) {
		try {
			final Process localProcess = Runtime.getRuntime().exec("su");
			final OutputStream localOutputStream = localProcess.getOutputStream();
			final DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);

			localDataOutputStream.writeBytes(strCmd + "\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();

			localProcess.waitFor();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void debugRootCommand(final String strCmd) {
		final String strRes = retRootCommand(strCmd);
		AppLog.Log("debugRootCommand: " + strRes);
	}

	public String retRootCommand(final String strCmd) {
		try {
			final Process localProcess = Runtime.getRuntime().exec("su");
			final OutputStream localOutputStream = localProcess.getOutputStream();
			final DataOutputStream localDataOutputStream = new DataOutputStream(localOutputStream);

			localDataOutputStream.writeBytes(strCmd + "\n");
			localDataOutputStream.writeBytes("exit\n");
			localDataOutputStream.flush();

			final BufferedReader bufferReader = new BufferedReader(new InputStreamReader(localProcess.getInputStream()));

			StringBuffer strResult = new StringBuffer();

			String line = null;
			while ((line = bufferReader.readLine()) != null) {
				strResult.append(line);
			}

			localProcess.waitFor();
			localOutputStream.close();
			localDataOutputStream.close();

			return strResult.toString();
		} catch (final Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public String getEthnet(final String strType) {
		try {
			final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			
			String strEth = null;
			while (interfaces.hasMoreElements()) {
				final NetworkInterface ni = interfaces.nextElement();
				final byte[] bna = ni.getDisplayName().getBytes();
				final byte[] dst = new byte[bna.length];
				for (int i = 0; i < dst.length; i++) {
					dst[i] = '\n';
				}
				for (int i = 0; i < bna.length; i++) {
					dst[i] = bna[i];
				}
				strEth = new String(dst).trim();
				
				if (strEth.contains(strType))
					return strEth;
			}
		} catch (final SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		return "";
	}

	public String getDNS1(final String strface) {
		return retRootCommand("getprop net." + strface + ".dns1").trim();
	}

	public String getDNS2(final String strface) {
		return retRootCommand("getprop net." + strface + ".dns2").trim();
	}

	public void setDNS1(final String strDns1) {
		rootCommand("setprop net.dns1 " + strDns1);
	}

	public void setDNS2(final String strDns2) {
		rootCommand("setprop net.dns2 " + strDns2);
	}

	public boolean dnsReset() {
		final String strPPP = getEthnet("ppp");

		if (strPPP.isEmpty())
			return false;

		final String strDns1 = getDNS1(strPPP);
		final String strDns2 = getDNS2(strPPP);

		if (strDns1.isEmpty() && strDns2.isEmpty())
			return false;

		setDNS1(strDns1);
		setDNS2(strDns2);

		AppLog.Log("dnsReset dns1: " + strDns1 + " dns2: " + strDns2);
		
		return true;
	}

	public boolean isRoot() {
		if (retRootCommand("ps").isEmpty()) {
			return false;
		}
		return true;
	}

	public boolean isFileExists(final String strPath) {
		final File file = new File(strPath);
		return file.exists();
	}
}
