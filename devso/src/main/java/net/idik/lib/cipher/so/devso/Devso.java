package net.idik.lib.cipher.so.devso;

import net.idik.lib.cipher.so.CipherClient;

/**
 * Created by linshuaibin on 2017/12/7.
 */

public class Devso {

    public static String stringFromJNI() {
        return CipherClient.get("dbkey");
    }
}
