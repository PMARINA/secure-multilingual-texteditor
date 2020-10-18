package crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;

public class Crypto {
	final Cipher cipher;
	static {
		// get the provider only once
		Security.addProvider(new BouncyCastleProvider());
	}
	private static long hash(byte[] key, int start, int len) {
		long sum = 0xF293A47B1925AC07L;
		int end = start + len;
		if (end >= key.length) {
			end = key.length;
			start -= 3;
			if (start < 0)
				start = 0;
		}
		for (int i = start; i < end; i++)
			sum = (sum << 13 | sum >>> 51) ^ (sum << 7 | sum >>> 57) ^ key[i];
		for (int i = start; i < end; i++)
			sum = (sum << 3 | sum >>> 61) ^ (sum << 17 | sum >>> 47) ^ key[i];
		return sum;
	}
	/*
		Crypto takes a password from which it generates a key
		It is responsible to generate random larger keys to use in 
		encrypting the document for more security.
		Ultimately, the security of your keys rests on your password.
		If it is small and uses only letters then it's easy to guess.
	 */
	public Crypto(String key) throws NoSuchAlgorithmException,
																	 InvalidKeyException,
																	 IllegalBlockSizeException,
																	 NoSuchPaddingException {
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		 byte[] keyBytes = key.getBytes();
		 byte[] saltedKey  = new byte[]{
			 90,-41,22,43,64,-45,106,27,18,19,40,-121,72,-83,114,2
		 };
		 long hash1 = hash(keyBytes, 0, 8); // hash key twice
		 long hash2 = hash(keyBytes, 8, 8);
		 saltedKey[0] ^= hash1;
		 saltedKey[1] ^= hash1 >> 8;
		 saltedKey[2] ^= hash1 >> 16;
		 saltedKey[3] ^= hash1 >> 24;
		 saltedKey[4] ^= hash1 >> 32;
		 saltedKey[5] ^= hash1 >> 40;
		 saltedKey[6] ^= hash1 >> 48;
		 saltedKey[7] ^= hash1 >> 56;

		 saltedKey[8] ^= hash2;
		 saltedKey[9] ^= hash2 >> 8;
		 saltedKey[10] ^= hash2 >> 16;
		 saltedKey[11] ^= hash2 >> 24;
		 saltedKey[12] ^= hash2 >> 32;
		 saltedKey[13] ^= hash2 >> 40;
		 saltedKey[14] ^= hash2 >> 48;
		 saltedKey[15] ^= hash2 >> 56;

		 int j = 0;
		 for (int i = 0; i < keyBytes.length; i += 2) {
			 saltedKey[j++] ^= keyBytes[i];
			 if (j >= saltedKey.length)
				 j = 0;
			 saltedKey[j++] ^= keyBytes[i+1];
			 if (j >= saltedKey.length)
				 j = 0;
		 }
		 // now at least you have 16 bytes as a key
		 String algorithm  = "RawBytes";
		 SecretKeySpec keySpec = new SecretKeySpec(keyBytes, algorithm);
		 cipher.init(Cipher.ENCRYPT_MODE, keySpec);
	}
	public final byte[] encrypt(final String text) throws IllegalBlockSizeException, BadPaddingException {
		byte[] plainText = text.getBytes();
		byte[] cypherText = cipher.doFinal(plainText);
		return cypherText;
	}
}
