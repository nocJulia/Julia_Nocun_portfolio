package org.example;

import java.math.BigInteger;
import java.util.Random;

public class rabinMillerTest {
    public static boolean rabinMillerTest(BigInteger n, int k) {
        if (n.compareTo(BigInteger.valueOf(1)) <= 0) {
            return false; // Warunek wejściowy: n > 1
        }

        // Wyliczanie wartości n - 1 = 2^s * d
        BigInteger d = n.subtract(BigInteger.ONE);
        int s = 0;
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.TWO);
            s++;
        }

        // Testowanie k razy
        for (int i = 0; i < k; i++) {
            BigInteger a = getRandomA(n);
            if (!testWitness(a, d, n, s)) {
                return false; // Liczba złożona
            }
        }

        return true; // Prawdopodobnie pierwsza
    }

    // Losowanie liczby a z przedziału [1, n-1]
    private static BigInteger getRandomA(BigInteger n) {
        Random random = new Random();
        BigInteger a;
        do {
            a = new BigInteger(n.bitLength(), random);
        } while (a.compareTo(BigInteger.ONE) <= 0 || a.compareTo(n.subtract(BigInteger.ONE)) >= 0);
        return a;
    }

    // Testowanie świadka pierwszości
    private static boolean testWitness(BigInteger a, BigInteger d, BigInteger n, int s) {
        BigInteger x = a.modPow(d, n);
        if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
            return true; // Warunek zgodności
        }
        for (int r = 1; r < s; r++) {
            x = x.modPow(BigInteger.TWO, n);
            if (x.equals(BigInteger.ONE)) {
                return false; // Liczba złożona
            }
            if (x.equals(n.subtract(BigInteger.ONE))) {
                return true; // Warunek zgodności
            }
        }
        return false; // Liczba złożona
    }
}