package org.g2faplugin;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class AESUtil {
    private static String enscyptPassword = "GHJguiyio3434";
    private static String salt = "45476688";
    private static IvParameterSpec iv = AESUtil.generateIv();
    private static SecretKey key;

    static {
        try {
            key = AESUtil.getKeyFromPassword(enscyptPassword, salt);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("no ok");
            System.exit(1);
        } catch (InvalidKeySpecException e) {
            System.out.println("no ok");
            System.exit(1);
        }
    }

    private static String algorithm = "AES/CBC/PKCS5Padding";

    public static String encrypt(String input) {

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("no ok");
            System.exit(1);
        } catch (NoSuchPaddingException e) {
            System.out.println("no ok");
            System.exit(1);
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        } catch (InvalidKeyException e) {
            System.out.println("no ok");
            System.exit(1);
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("no ok");
            System.exit(1);
        }
        byte[] cipherText = new byte[0];
        try {
            cipherText = cipher.doFinal(input.getBytes());
        } catch (IllegalBlockSizeException e) {
            System.out.println("no ok");
            System.exit(1);
        } catch (BadPaddingException e) {
            System.out.println("no ok");
            System.exit(1);
        }
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    public static String decrypt(String cipherText) {

        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("no ok");
            System.exit(1);
        } catch (NoSuchPaddingException e) {
            System.out.println("no ok");
            System.exit(1);
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
        } catch (InvalidKeyException e) {
            System.out.println("no ok");
            System.exit(1);
        } catch (InvalidAlgorithmParameterException e) {
            System.out.println("no ok");
            System.exit(1);
        }
        byte[] plainText = new byte[0];
        try {
            plainText = cipher.doFinal(Base64.getDecoder()
                    .decode(cipherText));
        } catch (IllegalBlockSizeException e) {
            System.out.println("no ok");
            System.exit(1);
        } catch (BadPaddingException e) {
            System.out.println("no ok");
            System.exit(1);
        }
        return new String(plainText);
    }

    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static SecretKey getKeyFromPassword(String password, String salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                .getEncoded(), "AES");
        return secret;
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

}
