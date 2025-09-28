package com.prueba.inicio.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	private static final String KEY = "0123456789ABCDEF0123456789ABCDEF"; // 32 chars -> 256-bit when using ASCII

	public static String encrypt(String plain) {
		try {
			byte[] keyBytes = KEY.getBytes("UTF-8");
			SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skey);
			byte[] encrypted = cipher.doFinal(plain.getBytes("UTF-8"));
			return Base64.getEncoder().encodeToString(encrypted);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String decrypt(String cipherText) {
		try {
			byte[] keyBytes = KEY.getBytes("UTF-8");
			SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.DECRYPT_MODE, skey);
			byte[] decoded = Base64.getDecoder().decode(cipherText);
			byte[] original = cipher.doFinal(decoded);
			return new String(original, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
