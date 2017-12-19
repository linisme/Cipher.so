package net.idik.lib.cipher.so.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class StringUtils {
    static String md5(String string) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            String result = "";
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase(Locale.US) + str.substring(1)
    }

    static String convertToUnix (String winPath) {
        String unixPath = winPath.replace("\\", "/").replace(" ", "\\ ");
        return unixPath;
    }
}