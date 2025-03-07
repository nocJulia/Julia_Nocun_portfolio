/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

import org.example.SudokuBoard;
import org.example.SudokuField;
import org.example.SudokuRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SudokuUnitTests {

    SudokuField[] fields1 = new SudokuField[9];
    SudokuField[] fields2 = new SudokuField[9];
    SudokuField[] fields3 = new SudokuField[9];

    @BeforeEach
    public void SetUp() {
        for (int i = 0; i < 9; i++) {
            fields1[i] = new SudokuField();
            fields1[i].setFieldValue(i + 1);
        }

        for (int i = 0; i < 9; i++) {
            fields2[i] = new SudokuField();
            fields2[i].setFieldValue(i + 1);
        }

        for (int i = 0; i < 9; i++) {
            fields3[i] = new SudokuField();
            fields3[i].setFieldValue(9 - i);
        }
    }

    @Test
    public void testEqualsInSudokuUnit(){
        SudokuRow SudokuRow1 = new SudokuRow(fields1);
        SudokuRow SudokuRow2 = new SudokuRow(fields2);
        SudokuRow SudokuRow3 = new SudokuRow(fields3);
        SudokuField field = new SudokuField();

        assertTrue(SudokuRow1.equals(SudokuRow1));
        assertTrue(SudokuRow1.equals(SudokuRow2));
        assertFalse(SudokuRow1.equals(SudokuRow3));
        assertFalse(SudokuRow1.equals(null));
        assertFalse(SudokuRow2.equals(field));

    }

    @Test
    public void testHashCodeInSudokuUnit() {

        SudokuRow SudokuRow1 = new SudokuRow(fields1);
        SudokuRow SudokuRow2 = new SudokuRow(fields2);
        SudokuRow SudokuRow3 = new SudokuRow(fields3);

        assertEquals(SudokuRow1.hashCode(), SudokuRow2.hashCode());
        assertNotEquals(SudokuRow1.hashCode(), SudokuRow3);
    }

    @Test
    public void testToStringInSudokuBoard(){

        SudokuRow SudokuRow1 = new SudokuRow(fields1);
        SudokuRow SudokuRow3 = new SudokuRow(fields3);

        String result = SudokuRow1.toString();

        assertNotEquals(SudokuRow1.toString(),SudokuRow3.toString());

        assertTrue(result.contains("fields"), "The result must contain fields");
        assertNotNull(result, "The result cannot be empty");
    }

}
