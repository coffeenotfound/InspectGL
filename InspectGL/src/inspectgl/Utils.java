package inspectgl;

import java.io.Closeable;
import java.util.Random;

public class Utils {
	
	public static void tryClose(Closeable parClosable) {
		try {
			if(parClosable != null) parClosable.close();
		}
		catch(Exception e) {}
	}
	
	public static byte[] generateUUID() {
		Random random = new Random(Runtime.getRuntime().freeMemory() ^ System.currentTimeMillis());
		
		final byte version = 0x0;
		long time = System.nanoTime();
		long seed = random.nextLong();
		
		byte[] uuid = new byte[16];
		for(int i = 0; i < 8; i++) {
			uuid[i] = (byte)((time >>> (i * 8)) & 0xFF);
		}
		for(int i = 8, j = 0; i < 16; i++, j++) {
			uuid[i] = (byte)((seed >>> (j * 8)) & 0xFF);
		}
		uuid[15] = version;
		return uuid;
	}
	
	public static String byteArrayToHexString(byte[] parBytes) {
		final char[] hexChars = "0123456789abcdef".toCharArray();
		char[] string = new char[parBytes.length * 2];
		for(int i = 0; i < parBytes.length; i++) {
			int v = parBytes[i] & 0xFF;
			string[i * 2] = hexChars[v >>> 4];
			string[i * 2 + 1] = hexChars[v & 0x0F];
		}
		return new String(string);
	}
}
