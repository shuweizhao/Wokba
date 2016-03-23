package com.example.shuweizhao.wokba;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by shuweizhao on 3/23/16.
 */
public class Encryption {
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static final String normalEncrypt(String s) {
        StringBuilder sb = new StringBuilder(s);
        sb = sb.reverse();
        return "WOkbA11@35Ao" + sb.toString() + "heyLLc#$22ACkoW0";
    }

    public static final String encryptData(String data) {
        return md5(normalEncrypt(data));
    }
}
