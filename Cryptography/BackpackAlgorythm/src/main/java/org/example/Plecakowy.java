/** Autorzy:
 * Julia Nocuń 247744
 * Kevin Makarewicz 247728
 * Zestaw IV
 */

package org.example;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

public class Plecakowy {

    public BigInteger[] publicKey, privateKey, cipherText;
    public byte[] plainText;
    private final int KEY_LENGTH = 8;
    private BigInteger r_prim, q;
    public Plecakowy() throws Exception {
        generateKeys();
    }

    public void generateKeys() throws Exception {
        BigInteger[] privateKey = new BigInteger[KEY_LENGTH];
        SecureRandom random = new SecureRandom();

        int[] lengths = {2, 2, 3, 3, 4, 5, 6, 7}; // robimy tak, zeby dlugosc calego klucza wynosila 128 bitow
        privateKey[0] = BigInteger.valueOf(random.nextInt(16) + 30);
        BigInteger sum = privateKey[0];

        for (int i = 1; i < KEY_LENGTH; i++) {
            privateKey[i] = sum.add(BigInteger.valueOf(random.nextInt((int) Math.pow(16, lengths[i]) - 1)));
            sum = sum.add(privateKey[i]);
        }

        this.privateKey = privateKey;

        BigInteger q = BigInteger.probablePrime(116, new Random());
        BigInteger r = BigInteger.probablePrime(16, new Random());
        if (!rabinMillerTest.rabinMillerTest(q, 5)) {
            throw new Exception("Liczba q nie jest pierwsza: " + q);
        }
        if (!rabinMillerTest.rabinMillerTest(r, 5)) {
            throw new Exception("Liczba r nie jest pierwsza: " + r);
        }

        BigInteger[] publicKey = new BigInteger[KEY_LENGTH];

        for (int i = 0; i < KEY_LENGTH; i++) {
            publicKey[i] = r.multiply(privateKey[i]).mod(q);
        }

        this.q = q;
        this.r_prim = r.modInverse(q);

        this.publicKey = publicKey;
    }

    public void encrypt(byte[] message) {
        BigInteger sum;
        BigInteger[] cipherText = new BigInteger[message.length];

        for (int i = 0; i < message.length; i++) {
            sum = BigInteger.ZERO;
            for (int j = 0; j < KEY_LENGTH; j++) { // dla kazdego bitu w bajcie
                if (checkBit(message[i], j)) {
                    sum = sum.add(publicKey[j]);
                }
            }
            cipherText[i] = sum;
        }

        this.cipherText = cipherText;
    }

    public void decrypt(BigInteger[] cipher) {
        byte[] plainText = new byte[cipher.length];
        BigInteger[] c_prim = new BigInteger[cipher.length];

        for (int i = 0; i < cipher.length; i++) {
            c_prim[i] = cipher[i].multiply(r_prim).mod(q);
            List<Integer> indexList = solveSubsetSum(c_prim[i]);

            for (Integer integer : indexList) {
                plainText[i] = setBit(plainText[i], integer); // ustawiamy odpowiednio bity na podstawie indexListy
            }
        }

        this.plainText = plainText;
    }

    public static byte setBit(byte b, int position) {
        return (byte) (b | (1 << (7 - position)));
    }

    public boolean checkBit(byte b, int position) {
        return (b & (1 << (7 - position))) != 0;
    }

    public List<Integer> solveSubsetSum(BigInteger cPrime) {
        List<Integer> indexList = new ArrayList<>();
        BigInteger remainingSum = cPrime;

        for (int i = privateKey.length - 1; i >= 0; i--) {
            if (privateKey[i].compareTo(remainingSum) <= 0) {
                remainingSum = remainingSum.subtract(privateKey[i]);
                indexList.add(i);
            }
        }

        return indexList;
    }

    public static String BIToHexString(BigInteger[] array) {
        StringJoiner joiner = new StringJoiner(", ");

        for (BigInteger num : array) {
            String hexString = num.toString(16);
            joiner.add(hexString);
        }

        return joiner.toString();
    }
}
