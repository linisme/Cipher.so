package net.idik.lib.cipher.so;

/**
 * Created by linshuaibin on 2017/12/7.
 */

class CipherCore {
    static {
        System.loadLibrary("cipher-lib");
        init();
    }

    private CipherCore() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    static String get(String key) {
        return getString(key);
    }

    private static native void init();

    private static native String getString(String key);
}
