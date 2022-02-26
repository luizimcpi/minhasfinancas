package com.devlhse.minhasfinancas.utils;

import java.util.Random;

public final class RandomUtils {
    public static String getSixDigitsRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }
}
