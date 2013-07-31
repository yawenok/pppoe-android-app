package pppoe.encrypt;

/**
 * 
 * 调用账号加密算法，这个嘛就~~
 * 
 * @author yawen
 *
 */
public class Encrypt {
	
//
// 算了吧还是不把xxx加上，免得被人利用
//	static {
//		try {
//			System.loadLibrary("encrypt-jni");
//		} catch (final UnsatisfiedLinkError ule) {
//			AppLog.Log("Could not load library!");
//		}
//	}
//
//	public native String stringFromEncrypt(String strPath, String strUser,String strPassword);
//
//	public String stringTelecomEncrypt(final String strUser, final String strPassword) {
//		return stringFromEncrypt(AppValue.LUA_ENCRYPT_PATH, strUser, strPassword);
//	}

	public String stringHenanEncrypt(final String strInput) {
		return "2:" + stringUnicomEncrypt(strInput);
	}

	private String stringUnicomEncrypt(final String strInput) {
		char[] beforeDecode = new char[128];
		char[] afterDecode = new char[128];
		char dictionary[] = { '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
				'a', 'b', 'c', 'd', 'e', 'A', 'B', 'C', 'D', 'E', 'F', 'G',
				'H', 'I', 'J', 'K', 'L', 'M', 'N', 'f', 'g', 'h', 'i', 'j',
				'k', 'l', 'm', 'n', 'U', 'V', 'W', 'X', 'Y', 'Z', 'x', 'y',
				'z', 'u', 'v', 'w', 'o', 'p', 'q', 'r', 's', 't', 'O', 'P',
				'Q', 'R', 'S', 'T' };

		int[] buffer = new int[16];
		int i, j, k, l, m, temp = 0, cache = 37;

		buffer[15] = 25;
		buffer[14] = 35;
		buffer[13] = 182;
		buffer[12] = 236;
		buffer[11] = 43;
		buffer[10] = 41;
		buffer[9] = 53;
		buffer[8] = 18;
		buffer[7] = 226;
		buffer[6] = 215;
		buffer[5] = 24;
		buffer[4] = 117;
		buffer[3] = 35;
		buffer[2] = 201;
		buffer[1] = 52;
		buffer[0] = 17;
		
		for (i = 0; i < strInput.toCharArray().length; i++) {
			beforeDecode[i] = strInput.toCharArray()[i];
		}
		for (i = 0; i < beforeDecode.length; i++) {
			for (j = 0; j < dictionary.length; j++) {
				if (beforeDecode[i] == dictionary[j]) {
					if (i < 16) {
						l = buffer[i];
					} else {
						k = i % 6;
						l = buffer[k];
					}
					m = cache + cache * 2;
					l = l ^ m;
					l = l ^ temp;
					l = l + j;
					k = l % 62;
					afterDecode[i] = dictionary[k];
					m = k;
					cache = cache ^ (m + 9433);
					break;
				}
			}
			if (afterDecode[i] == '\0')
				afterDecode[i] = beforeDecode[i];
			temp = temp + 5;
		}
		return String.valueOf(afterDecode).trim();
	}
}