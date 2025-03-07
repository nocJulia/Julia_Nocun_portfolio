import org.example.SudokuField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CompareToTest {

        @Test
        public void testCompare() {

            SudokuField field1 = new SudokuField();
            SudokuField field2 = new SudokuField();
            SudokuField field3 = new SudokuField();

            field1.setFieldValue(3);
            field2.setFieldValue(5);
            field3.setFieldValue(7);

            assertEquals(field2.compareTo(field2), 0);
            assertEquals(field1.compareTo(field2), -1);
            assertEquals(field3.compareTo(field2), 1);
        }

    @Test
    void testCompareToThrowsException() {
        SudokuField field1 = new SudokuField();

        assertThrows(NullPointerException.class, () -> {
            field1.compareTo(null);
        });
    }
}
