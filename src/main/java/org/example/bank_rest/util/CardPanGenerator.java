package org.example.bank_rest.util;

import java.security.SecureRandom;

public class CardPanGenerator {

    private final static SecureRandom secureRandom = new SecureRandom();

    public static String generatePan() {
        var pan = new StringBuilder();
        for (int i = 0; i < 16; i++){
            pan.append(secureRandom.nextInt(0, 10));
        }
        return pan.toString();
    }
}
