package com.devlhse.minhasfinancas.utils;

import java.security.SecureRandom;

public final class RandomUtils {
    public static String getSixDigitsRandomNumberString() {
        int number = new SecureRandom().nextInt(999999);
        return String.format("%06d", number);
    }
}
