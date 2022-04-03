package me.metropanties.jokeapi.util;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class APIKeyGenerator {

    private static final String COMBINATION = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    @NotNull
    public static String generate() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 35; i++) {
            sb.append(COMBINATION.charAt(random.nextInt(COMBINATION.length())));
        }
        return sb.toString();
    }

}
