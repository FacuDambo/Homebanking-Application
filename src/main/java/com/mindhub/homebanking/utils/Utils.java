package com.mindhub.homebanking.utils;

import java.util.ArrayList;
import java.util.List;

public final class Utils {
    private static List<Long> longArray = new ArrayList<>();

    private static List<Integer> intArray = new ArrayList<>();

    public static long getRandomLong(long min, long max) {
        long number;

        do {
            number = (long) ((Math.random() * (max - min)) + min);
        } while (longArray.contains(number));
        longArray.add(number);

        return number;
    }

    public static int getRandomInteger(int min, int max) {
        int number;

        do {
            number = (int) ((Math.random() * (max - min)) + min);
        } while (intArray.contains(number));
        intArray.add(number);

        return number;
    }

    public static int getRandomRepeat(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }
}
