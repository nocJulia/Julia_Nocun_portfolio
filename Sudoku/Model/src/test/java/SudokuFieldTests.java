/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

import org.example.SudokuField;
import org.example.SudokuUnit;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SudokuFieldTests {

    SudokuField field1 = new SudokuField();
    SudokuField field2 = new SudokuField();
    SudokuField field3 = new SudokuField();
    @Test
    public void testToStringSudokuField() {
        field1.setFieldValue(2);

        String expected = "{\"field value\":2}";
        assertEquals(expected, field1.toString());
    }
    @Test
    public void testEqualSudokuField() {
        field1.setFieldValue(3);
        field2.setFieldValue(3);
        field3.setFieldValue(5);

        SudokuUnit unit1 = new SudokuUnit();

        assertTrue(field1.equals(field1));
        assertFalse(field1.equals(null));
        assertFalse(field1.equals(unit1));

        assertTrue(field1.equals(field2));  // objects are equal
        assertNotEquals(field1, field3);    // objects are not equal
    }

    @Test
    public void testHashCodeInSudokuField(){
        field1.setFieldValue(7);
        field2.setFieldValue(7);
        field3.setFieldValue(3);

        assertEquals(field1.hashCode(), field2.hashCode());     // objects have same hashCode
        assertNotEquals(field1.hashCode(), field3.hashCode());  // objects have not same hashCode
    }

}
