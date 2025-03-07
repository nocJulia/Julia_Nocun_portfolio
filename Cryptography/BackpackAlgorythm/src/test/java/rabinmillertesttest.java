import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.example.rabinMillerTest.rabinMillerTest;

public class rabinmillertesttest {

    @Test
    void testLargePrimes() {
        Assertions.assertTrue(rabinMillerTest(new BigInteger("104729"), 5)); // Prime number
        Assertions.assertTrue(rabinMillerTest(new BigInteger("1000000000000066600000000000001"), 5)); // Large prime number
    }

    @Test
    void testCompositeNumbers() {
        Assertions.assertFalse(rabinMillerTest(BigInteger.valueOf(441434326), 5));
        Assertions.assertFalse(rabinMillerTest(BigInteger.valueOf(157567650), 5));
        Assertions.assertFalse(rabinMillerTest(BigInteger.valueOf(1255500523), 5));
    }
}
