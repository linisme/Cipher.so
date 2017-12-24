package net.idik.lib.cipher.so;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by linshuaibin on 2017/12/24.
 */

public class SignatureUtils {

    @SuppressLint("PackageManagerGetSignatures")
    public String getSignature(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            return packageInfo.signatures[0].toCharsString();
        }
        return null;
    }
}
