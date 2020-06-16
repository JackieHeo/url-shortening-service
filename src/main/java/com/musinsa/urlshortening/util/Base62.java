package com.musinsa.urlshortening.util;

public class Base62 {
    private final static int RADIX = 62;
    private final static String CODEC = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public static String encode(long param) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(CODEC.charAt((int) (param % RADIX)));
            param /= RADIX;
        } while (param > 0);
        return sb.toString();
    }

    public static long decode(String param) {
        long sum = 0;
        long power = 1;
        for (int i = 0; i < param.length(); i++) {
            sum += CODEC.indexOf(param.charAt(i)) * power;
            power *= RADIX;
        }
        return sum;
    }
}
