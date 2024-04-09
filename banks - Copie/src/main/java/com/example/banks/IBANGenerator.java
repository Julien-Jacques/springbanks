package com.example.banks;

import java.util.Random;

public class IBANGenerator {
    public static String generateIBAN() {
        String country = "FR";
        String bankCode = generateNumericString(5);
        String branchCode = generateNumericString(5);
        String accountNumber = generateNumericString(11);
        return country + bankCode + branchCode + accountNumber;
    }
    private static String generateNumericString(int length) {
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

}
