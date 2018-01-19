package net.idik.lib.cipher.so.encrypt;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by linshuaibin on 2018/1/19.
 */

public final class AESEncryptor {

    private static final String CHARSET = "UTF-8";

    private static final String HASH_ALGORITHM = "MD5";
    private static final String AES_MODE = "AES/CBC/PKCS5Padding";

    private static byte[] iv = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    public static void setIv(String iv) {
        try {
            AESEncryptor.iv = iv.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String key, String message) {
        byte[] result = null;
        try {
            SecretKeySpec keySpec = generateKeySpec(key);
            result = encrypt(keySpec, iv, message.getBytes(CHARSET));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        if (result == null) {
            return "";
        }
        return Base64.encodeToString(result, Base64.DEFAULT);
    }

    public static String decrypt(String key, String cipherMessage) {
        String result = "";
        try {
            SecretKeySpec keySpec = generateKeySpec(key);
            byte[] cipherBytes = Base64.decode(cipherMessage, Base64.DEFAULT);
            result = new String(decrypt(keySpec, iv, cipherBytes), CHARSET);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static SecretKeySpec generateKeySpec(final String key) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        final MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.update(key.getBytes(CHARSET));
        byte[] keyBytes = digest.digest();
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static byte[] encrypt(SecretKeySpec keySpec, byte[] iv, byte[] message) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
        return cipher.doFinal(message);
    }

    private static byte[] decrypt(SecretKeySpec keySpec, byte[] iv, byte[] cipherMessage) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(AES_MODE);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);
        return cipher.doFinal(cipherMessage);
    }

    private AESEncryptor() throws IllegalAccessException {
        throw new IllegalAccessException();
    }
}
