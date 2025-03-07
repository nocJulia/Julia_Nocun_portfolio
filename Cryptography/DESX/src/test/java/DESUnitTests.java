import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import org.example.DES;

//public class DESUnitTests {

//    DES des = new DES("HelloWorld", "SecretKey");
//    byte[] inputBlock = {1, 2, 3, 4, 5, 6, 7, 8};
//
//    @Test
//    public void testGet8BytesBlock() {
//        // Testowanie metody get8BytesBlock() dla indeksu 0
//        byte[] expectedBlock = {72, 101, 108, 108, 111, 87, 111, 114};
//        assertArrayEquals(expectedBlock, des.get8BytesBlock(0));
//
//        // Testowanie metody get8BytesBlock() dla indeksu 1 (poza zakresem)
//        byte[] expectedEmptyBlock = {108, 100, 0, 0, 0, 0, 0, 0};
//        assertArrayEquals(expectedEmptyBlock, des.get8BytesBlock(1));
//
//        byte[] expectedEmptyBlock2 = {0, 0, 0, 0, 0, 0, 0, 0};
//        assertArrayEquals(expectedEmptyBlock2, des.get8BytesBlock(4));
//    }
//
//    @Test
//    public void testPermuteIP() {
//        byte[] expectedOutput = {0, 0, 120, 85, 0, 0, -128, 102};
//        byte[] result = des.permute(inputBlock, des.IP);
//
//        assertArrayEquals(expectedOutput, result);
//
//        byte[] expectedL = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 0, 1, 0, 1, 0, 1};
//        byte[] expectedR = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0, 1, 1, 0};
//
//        assertArrayEquals(des.getL(), des.bitsToBytes(expectedL));
//        assertArrayEquals(des.getR(), des.bitsToBytes(expectedR));
//    }
//
//    @Test
//    public void testPermutePC1AndSubkeys() {
//        byte[] expectedOutput = {0, 0, 0, 6, 103, -120, 0};
//        byte[] result = des.permute(inputBlock, des.PC_1);
//        assertArrayEquals(expectedOutput, result);
//
//        byte[] expectedC = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//        byte[] expectedD = {0, 1, 1, 0, 0, 1, 1, 0, 0, 1, 1, 1, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//
//        assertArrayEquals(des.getC(), expectedC);
//        assertArrayEquals(des.getD(), expectedD);
//
//        byte[] expected1stSubkey = {0, 0, 0, 19, 42, -126};
//
//        des.generateSubkeys();
//        byte[][] subkeys = des.getSubkeys();
//
//        assertArrayEquals(expected1stSubkey, subkeys[0]);
//    }
//
//    @Test
//    public void testLeftShift() {
//         byte[] expectedShiftedArray = {3, 4, 5, 6, 7, 8, 1, 2};
//         byte[] shiftedArray = des.leftShift(inputBlock, 2);
//         assertArrayEquals(expectedShiftedArray, shiftedArray);
//    }
//
//    @Test
//    public void testConcatenateArrays() {
//        byte[] array1 = {1, 2, 3};
//        byte[] array2 = {4, 5, 6};
//        byte[] expectedConcatenatedArray = {1, 2, 3, 4, 5, 6};
//        byte[] concatenatedArary = des.concatenateArrays(array1, array2);
//        assertArrayEquals(expectedConcatenatedArray, concatenatedArary);
//    }
//
//    @Test
//    public void testXOR() {
//        byte[] array1 = {1, 2, 3, 4, 5};
//        byte[] array2 = {5, 4, 3, 2, 1};
//        int bits = 5;
//
//        byte[] expected = {4, 6, 0, 6, 4};
//        byte[] result = des.XOR(array1, array2, bits);
//
//        assertArrayEquals(expected, result);
//    }
//
////    @Test
////    public void testSBoxFunction() {
////        byte[] input = {1, 1, 0, 0, 1, 1,
////                0, 1, 1, 0, 0, 1,
////                1, 0, 0, 1, 1, 0};
////
////        byte[] expectedOutput = {11, 6, 9};
////        byte[] output = des.SBoxFunction(input);
////
////        assertArrayEquals(expectedOutput, output);
////    }
//
//    @Test
//    public void testBytesToBits() {
//        byte[] bytes = {1, 2, 3};
//
//        byte[] expectedBits = {
//                0, 0, 0, 0, 0, 0, 0, 1,
//                0, 0, 0, 0, 0, 0, 1, 0,
//                0, 0, 0, 0, 0, 0, 1, 1,
//        };
//
//        byte[] actualBits = des.bytesToBits(bytes);
//
//        assertArrayEquals(expectedBits, actualBits);
//    }
//
//    @Test
//    public void testConvertToFourBytes() {
//        byte[] input = {(byte) 0b00001111, (byte) 0b00001011};
//        byte[] expectedOutput = {(byte) 0b11111011};
//
//        byte[] output = des.convertToFourBytes(input);
//
//        assertArrayEquals(expectedOutput, output);
//    }
//
//    @Test
//    public void testFunctionF() {
//        byte[] subkey = {0, 0, 0, 0, 0, 0}; // Przykładowy subklucz
//        byte[] R = {1, 1, 1, 1}; // Przykładowe R
//        des.setR(R);
//        // Oczekiwany wynik dla powyższych danych testowych
//        byte[] expected = {-104, 0, 40, 56}; // Puste dane dla tej funkcji - przykładowe dane
//
//        byte[] result = des.functionF(subkey);
//        assertArrayEquals(expected, result);
//    }
//
//}