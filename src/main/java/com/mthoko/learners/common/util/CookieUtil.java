package com.mthoko.learners.common.util;

import com.mthoko.learners.common.util.encryption.AESUtil;
import com.mthoko.learners.common.util.encryption.SlowAES;

public class CookieUtil {

    public static String getCookieFromURLData(String urlData) {
        String[] ABC = retrieveABC(urlData);
        String a = ABC[0];
        String b = ABC[1];
        String c = ABC[2];
        return getCookie(a, b, c);
    }

    public static String getCookie(String aStr, String bStr, String cStr) {
        int[] a = AESUtil.toNumbers(aStr);
        int[] b = AESUtil.toNumbers(bStr);
        int[] c = AESUtil.toNumbers(cStr);
        SlowAES aes = new SlowAES();
        int[] decrypt = aes.decrypt(c, 2, a, b);
        String toHex = AESUtil.toHex(decrypt);
        return toHex;
    }

    public static String[] retrieveABC(String urlData) {
        StringBuilder sb = new StringBuilder(urlData);
        int startIndex = sb.indexOf("a=toNumbers");
        sb.delete(0, startIndex);
        int endIndex = sb.indexOf(";");
        sb.delete(endIndex, sb.length());
        String[] ABC = sb.toString().split(",");
        String a = ABC[0].substring(ABC[0].indexOf('"') + 1, ABC[0].lastIndexOf('"'));
        String b = ABC[1].substring(ABC[1].indexOf('"') + 1, ABC[1].lastIndexOf('"'));
        String c = ABC[2].substring(ABC[2].indexOf('"') + 1, ABC[2].lastIndexOf('"'));
        return new String[]{a, b, c};
    }

}
