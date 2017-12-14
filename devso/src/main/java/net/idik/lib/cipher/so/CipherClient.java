package net.idik.lib.cipher.so;

import android.content.Context;

/**
 * Created by linshuaibin on 2017/12/7.
 */

public class CipherClient {
    static {
        System.loadLibrary("cipher-lib");
        init();
    }

    public static String get(String key) {
        return getString(key);
    }

    private static native void init();

    private static native String getString(String key);
}
