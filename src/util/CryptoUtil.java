package util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtil {
    private static final String KEY = "1234567890123456"; // 16 chars = 128-bit key

    public static String encrypt(String str) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return Base64.getEncoder().encodeToString(cipher.doFinal(str.getBytes()));
    }

    public static String decrypt(String str) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return new String(cipher.doFinal(Base64.getDecoder().decode(str)));
    }
}
