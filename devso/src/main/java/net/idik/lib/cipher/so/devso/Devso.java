package net.idik.lib.cipher.so.devso;

/**
 * Created by linshuaibin on 2017/12/7.
 */

public class Devso {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();
}
