package util;

import model.QueueItem;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.Key;
import java.util.LinkedList;
import java.util.Queue;

public class FileUtil {
    private static final String KEY = "MySecretKey12345"; // 16 chars = 128-bit AES

    public static void saveQueueToFile(Queue<QueueItem<String>> queue, String filename) {
        try (FileOutputStream fos = new FileOutputStream(filename);
             CipherOutputStream cos = new CipherOutputStream(fos, getCipher(Cipher.ENCRYPT_MODE));// Cryptography
             ObjectOutputStream oos = new ObjectOutputStream(cos)) {

            oos.writeObject(queue); //Serialization

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Queue<QueueItem<String>> loadQueueFromFile(String filename) {
        try (FileInputStream fis = new FileInputStream(filename);
             CipherInputStream cis = new CipherInputStream(fis, getCipher(Cipher.DECRYPT_MODE)); //Cryptography
             ObjectInputStream ois = new ObjectInputStream(cis)) {

            Object obj = ois.readObject(); //Serialization
            if (obj instanceof Queue) {
                return (Queue<QueueItem<String>>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new LinkedList<>(); // fallback
    }

    private static Cipher getCipher(int mode) {
        try {
            Key aesKey = new SecretKeySpec(KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(mode, aesKey);
            return cipher;
        } catch (Exception e) {
            throw new RuntimeException("Cipher init failed", e);
        }
    }
}
