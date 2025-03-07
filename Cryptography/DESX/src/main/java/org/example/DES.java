/** Autorzy:
 * Julia Nocuń 247744
 * Kevin Makarewicz 247728
 * Zestaw IV
 */

package org.example;
import java.util.Arrays;

public class DES {

    public final byte[] PC_1 = {57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34,
            26, 18, 10, 2, 59, 51, 43, 35, 27, 19, 11, 3, 60, 52, 44,
            36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30,
            22, 14, 6, 61, 53, 45, 37, 29, 21, 13, 5, 28, 20, 12, 4};

    public final byte[] PC_2 = {14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44,
            49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};

    public final byte[] IP = {58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20,
            12, 4, 62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40,
            32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43,
            35, 27, 19, 11, 3, 61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};

    public final byte[] E = {32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11,
            12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21, 22,
            23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};

    public final byte[][] SBoxes = {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7, // S1
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13},
            {15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10, // S2
                    3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
                    0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
                    13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9},
            {10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8, // S3
                    13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
                    13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
                    1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12},
            {7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15, // S4
                    13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
                    10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
                    3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14},
            {2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9, // S5
                    14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
                    4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
                    11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3},
            {12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11, // S6
                    10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
                    9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
                    4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13},
            {4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1, // S7
                    13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
                    1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
                    6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12},
            {13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7, // S8
                    1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
                    7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
                    2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11}
    };

    public final byte[] P = {16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25};

    public final byte[] IPMinus1 = {40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25};

    static final int[] SHIFTS = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    private String plainText;
    private byte[] key;

    // zapisujemy w bytes plainText jako tablicę bajtów żeby wygodniej się korzystało
    private byte[] bytes;
    private byte[] ciphertextBytes;

    private String ciphertext;

    private byte[] R1;

    private byte[] C; // przechowujemy pierwszą połowę klucza po permutacji PC_1
    private byte[] D; // przechowujemy drugą połowę klucza po permutacji PC_1

    private byte[] L; // przechowujemy pierwszą połowę klucza po permutacji IP
    private byte[] R; // przechowujemy drugą połowę klucza po permutacji IP

    public byte[][] subkeys;

    private byte[] secondKey;
    private byte[] thirdKey;
    private boolean flagGeneratingKeys;
    private byte[] decryptedTextBytes;

    public DES() {
        this.subkeys = new byte[16][];
        this.ciphertextBytes = new byte[8];
        this.R1 = new byte[4];
        this.decryptedTextBytes = new byte[8];
    }

    // bierzemy bloki 64bitowe z plaintextu z danym indeksem żeby potem z IP permutować
    public byte[] get8BytesBlock(int index, byte[] text) {
        int startIndex = index * 8;
        int endIndex = startIndex + 8;

        byte[] bytesBlock = new byte[8];
        for (int i = startIndex; i < endIndex; i++) {
            if (i < text.length) { // Sprawdzenie, czy indeks nie przekracza długości tablicy bytes
                bytesBlock[i - startIndex] = text[i];
            } else {
                bytesBlock[i - startIndex] = 0; // Ustawienie pozostałych bajtów na zero, jeśli brakuje danych
            }
        }

        return bytesBlock;
    }

    public byte[] permute(byte[] block, byte[] permutationTable) {
        byte[] permutedBlock = new byte[permutationTable.length >> 3];

        for (int i = 0; i < permutationTable.length; i++) {
            int index = permutationTable[i] - 1; // Indeks w tablicy permutacji, pomniejszamy o jeden bo pozycja 1 oznacza element w tablicy [0]
            int blockIndex = index >> 3; // Indeks bajtu w bloku (pierwsze 8 bitów ma indeks 0)
            int bitIndex = index % 8; // Indeks bitu w bajcie

            // Sprawdzamy czy odpowiedni bit jest ustawiony w bajcie i ustawiamy go w odpowiednim miejscu w wynikowej tablicy
            // 7 - bitIndex wyznacza nam kolumne, a blockIndex wyznacza nam wiersz
            byte mask = (byte) (1 << (7 - bitIndex));
            byte bit = (byte) ((block[blockIndex] & mask) >> (7 - bitIndex));  // kłopoty jakieś ze znakiem
            if (bit == -1) {
                bit = 1;
            }

            // Indeks w tablicy wynikowej to i/8, ponieważ chcemy dodać kolejne bajty, |= operator dodawania bitowego
            permutedBlock[i >> 3] |= (byte) (bit << (7 - (i % 8)));
        }

        if (permutationTable == PC_1) {
            int middleIndex = 3;
            byte middleByte = permutedBlock[middleIndex];

            C = new byte[middleIndex + 1];
            D = new byte[middleIndex + 1];

            C[middleIndex] = (byte) (middleByte & 0xF0);
            D[0] = (byte) ((middleByte & 0x0F) << 4);
            D[0] = (byte) (D[0] | ((permutedBlock[middleIndex + 1] & 0xF0) >> 4));
            D[1] = (byte) ((permutedBlock[middleIndex + 1] & 0x0F) << 4);
            D[1] = (byte) (D[1] | ((permutedBlock[middleIndex + 2] & 0xF0) >> 4)); // ręcznie zrobione bo brak pomysłu na pętle
            D[2] = (byte) ((permutedBlock[middleIndex + 2] & 0x0F) << 4);
            D[2] = (byte) (D[2] | ((permutedBlock[middleIndex + 3] & 0xF0) >> 4));  // generalnie to bierzemy pierwsze 28 bitów z permutedBlock i zapisujemy to w C, drugie 28 bitów w D
            D[3] = (byte) ((permutedBlock[middleIndex + 3] & 0x0F) << 4);


            System.arraycopy(permutedBlock, 0, C, 0, middleIndex);
        }

        if (permutationTable == IP) {
            int halfLength = permutedBlock.length >> 1;

            L = new byte[halfLength];
            R = new byte[halfLength];

            System.arraycopy(permutedBlock, 0, L, 0, halfLength);
            System.arraycopy(permutedBlock, halfLength, R, 0, halfLength);
        }

        return permutedBlock;
    }

    public void generateSubkeys() {
        for (int round = 0; round < 16; round++) {
            C = leftShift(C, SHIFTS[round]);
            D = leftShift(D, SHIFTS[round]);

            byte[] concatenatedCD = concatenateArrays(C, D);
            byte[] subkey = permute(concatenatedCD, PC_2);

            subkeys[round] = subkey;
        }
        flagGeneratingKeys = false;    }

    private byte[] leftShift(byte[] input, int shift) {
        byte[] out = new byte[4];
        int halfKeySize = 28;
        for (int i = 0; i < halfKeySize; i++) {
            boolean bit = checkBit(input, (i + shift) % halfKeySize);
            setBit(out, i, bit);
        }
        return out;
    }

    public byte[] concatenateArrays(byte[] array1, byte[] array2) {
        if (flagGeneratingKeys) { // to robimy żeby połączyć dwie tablice po 28 bitów
            byte [] concatenatedArray = new byte[7];
            concatenatedArray[0] = array1[0];
            concatenatedArray[1] = array1[1];
            concatenatedArray[2] = array1[2];
            concatenatedArray[3] = (byte) (array1[3] & 0xF0);
            concatenatedArray[3] = (byte) (concatenatedArray[3] | ((array2[0] & 0xF0) >> 4));
            concatenatedArray[4] = (byte) (((array2[0] & 0x0F) << 4));
            concatenatedArray[4] = (byte) (concatenatedArray[4] | ((array2[1] & 0xF0) >> 4));
            concatenatedArray[5] = (byte) (((array2[1] & 0x0F) << 4));
            concatenatedArray[5] = (byte) (concatenatedArray[5] | ((array2[2] & 0xF0) >> 4));
            concatenatedArray[6] = (byte) (((array2[2] & 0x0F) << 4));
            concatenatedArray[6] = (byte) (concatenatedArray[6] | ((array2[3] & 0xF0) >> 4));
            return concatenatedArray;
        }
        else {
            byte[] concatenatedArray = new byte[array1.length + array2.length];
            System.arraycopy(array1, 0, concatenatedArray, 0, array1.length);
            System.arraycopy(array2, 0, concatenatedArray, array1.length, array2.length);
            return concatenatedArray;
        }
    }

    public byte[] functionF(byte[] subkey) {
        byte[] expansionArray = permute(R, E);
        byte[] XORedArray = XOR(expansionArray, subkey, subkey.length);
        byte[] SBoxedArray = SBoxFunction(XORedArray);
        return permute(SBoxedArray, P);
    }


    private byte[] SBoxFunction(byte[] input) {
        input = splitBytes(input);
        byte[] output = new byte[8];
        int SBoxIndex = 0;

        for (byte splitByte : input) {
            int row = 2 * (splitByte >> 7 & 0x0001) + (splitByte >> 2 & 0x0001);
            int col = splitByte >> 3 & 0x000F;
            output[SBoxIndex] = SBoxes[SBoxIndex][16 * row + col];
            SBoxIndex++;
        }
        return convertToFourBytes(output);
    }

    private byte[] splitBytes(byte[] in) {  //tworzymy 8 bajtowa tablicę z 6 bajtowej dla funkcji SBox
        int numOfBytes = 8;
        byte[] out = new byte[numOfBytes];
        for (int i = 0; i < numOfBytes; i++) {
            for (int j = 0; j < 6; j++) {
                boolean val = checkBit(in, (6 * i) + j);
                setBit(out, (8 * i) + j, val);
            }
        }
        return out;
    }


    public byte[] convertToFourBytes(byte[] input) {
        byte[] output = new byte[input.length >> 1];
        int outputIndex = 0;

        for (int i = 0; i < input.length; i += 2) {
            // Przekształcenie dwóch 8-bitowych bajtów w dwa 4-bitowe bloki
            byte firstHalf = (byte) ((input[i] & 0x0F) << 4); // XXXX0000
            byte secondHalf = (byte) (input[i + 1] & 0x0F); // 0000YYYY
            byte combinedByte = (byte) (firstHalf | secondHalf); // XXXXYYYY

            // Zapisanie przekształconego bajtu do wynikowej tablicy
            output[outputIndex++] = combinedByte;
        }

        return output;
    }

    public byte[] XOR(byte[] array1, byte[] array2, int bits) {
        byte[] arrayXORED = new byte[bits];
        int i = 0;
        for (byte b : array1) {
            arrayXORED[i] = (byte) (b ^ array2[i++]);
        }
        return arrayXORED;
    }

    public byte[] process16Rounds() {
        for (int round = 0; round <= 14; round++) {
            R1 = R;
            R = XOR(L, functionF(subkeys[round]), 4); //tutaj akurat R i L mają bajty a nie bity
            L = R1;
        }
        byte[] placeholder1 = functionF(subkeys[15]);
        L = XOR(L, placeholder1, 4);
        return permute(concatenateArrays(L, R), IPMinus1);
    }

    public void encrypt(byte[] givenPlainText, String givenKey, String givenSecondKey, String givenThirdKey) {
        this.flagGeneratingKeys = true;
        C = null;
        D = null;
        R1 = null;
        L = null;
        R = null;
        this.bytes = givenPlainText;
        this.key = hexStringToBytes(givenKey);
        this.secondKey = hexStringToBytes(givenKey);
        this.thirdKey = hexStringToBytes(givenKey);
        permute(key, PC_1);
        generateSubkeys();
        int i = 0;
        do {
            byte[] bytesBlock = XOR(get8BytesBlock(i, bytes), secondKey, 8);
            permute(bytesBlock, IP);
            if (Arrays.equals(ciphertextBytes, new byte[]{0, 0, 0, 0, 0, 0, 0, 0})) {
                ciphertextBytes = XOR(process16Rounds(), thirdKey, 8);
            } else {
                ciphertextBytes = concatenateArrays(ciphertextBytes, XOR(process16Rounds(), thirdKey, 8));
            }
            i++;
        } while (i * 8 < this.bytes.length);
    }

    public void decrypt(byte[] givenCipherText, String givenKey, String givenSecondKey, String givenThirdKey) {
        this.flagGeneratingKeys = true;
        C = null;
        D = null;
        R1 = null;
        L = null;
        R = null;
        this.ciphertextBytes = givenCipherText;
        this.key = hexStringToBytes(givenKey);
        this.secondKey = hexStringToBytes(givenKey);
        this.thirdKey = hexStringToBytes(givenKey);
        this.plainText = "";
        this.bytes = plainText.getBytes();
        permute(key, PC_1);
        generateSubkeys();
        reverseSubkeysOrder();
        int i = 0;
        do {
            byte[] bytesBlock = XOR(get8BytesBlock(i, ciphertextBytes), thirdKey, 8);
            permute(bytesBlock, IP);
            if (Arrays.equals(bytes, new byte[]{0, 0, 0, 0, 0, 0, 0, 0})) {
                bytes = XOR(process16Rounds(), secondKey, 8);
            } else {
                bytes = concatenateArrays(bytes, XOR(process16Rounds(), secondKey, 8));
            }
            i++;
        } while (i * 8 < this.ciphertextBytes.length);
        plainText = new String(bytes);
    }

    public void reverseSubkeysOrder() {
        int left = 0;
        int right = subkeys.length - 1;

        while (left < right) {
            byte[] temp = subkeys[left];
            subkeys[left] = subkeys[right];
            subkeys[right] = temp;

            left++;
            right--;
        }
    }

    public byte[] getCiphertextBytes() {
        return ciphertextBytes;
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for (byte b : a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    private boolean checkBit(byte[] data, int index) {        // sprawdzamy interesujący nas bit
        int byteIndex = index / 8;
        int bitIndex = index % 8;

        return (data[byteIndex] >> (8 - (bitIndex + 1)) & 1) == 1;
    }

    private void setBit(byte[] data, int index, boolean bit) {        // ustawiamy interesujący nas bit
        int byteIndex = index / 8;                                    // jeśli bit jest true to ustawiamy na 1
        int bitIndex = index % 8;
        if(bit)
            data[byteIndex] |= (byte) (0x80 >> bitIndex);
        else
            data[byteIndex] &= (byte) ~(0x80 >> bitIndex);

    }

    public String getPlainText() {
        return plainText;
    }

    public static byte[] hexStringToBytes(String hexString) {
        byte[] byteArray = new byte[hexString.length() >> 1];
        for (int i = 0; i < hexString.length(); i += 2) {
            String hexByte = hexString.substring(i, i + 2);
            byteArray[i / 2] = (byte) Integer.parseInt(hexByte, 16);
        }
        return byteArray;
    }

    public byte[] getBytes() {
        return bytes;
    }
}
