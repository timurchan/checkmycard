package com.gtimurchan.checkmycard;

public class GeneralHelper {

    public static String replaceLabelWithValue(String url, String label, int value) {
        return url.replace(label, String.valueOf(value));
    }
}
