/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

import org.example.*;
import org.example.exceptions.SolverException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class SudokuBoardTests {

    ISudokuSolver solver = new BacktrackingSudokuSolver();

    @Test
    public void IsBoardCorrectTest() throws SolverException {
        SudokuBoard board = new SudokuBoard(solver);
        board.solveGame();
        boolean is_board_correct = true;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int number = board.get(row, col);
                if (number != 0 && !board.getRow(row).verify() && !board.getColumn(col).verify()
                && !board.getBox(row, col).verify()) {
                    is_board_correct = false;
                }
            }
        }

        assertTrue(is_board_correct);
    }

    @Test
    public void AreBoardsDifferentTest() throws SolverException {
        SudokuBoard board1 = new SudokuBoard(solver);
        SudokuBoard board2 = new SudokuBoard(solver);

        board1.solveGame();
        board2.solveGame();

        int numberOfRandomChecks = 10;
        boolean atLeastOneDifference = false;

        for (int i = 0; i < numberOfRandomChecks; i++) {
            int randomRow = (int) (Math.random() * 9);
            int randomCol = (int) (Math.random() * 9);

            int value1 = board1.get(randomRow, randomCol);
            int value2 = board2.get(randomRow, randomCol);

            if (value1 != value2) {
                atLeastOneDifference = true;
                break;
            }
        }

        assertTrue(atLeastOneDifference);
    }

    @Test
    public void testHashCodeInSudokuBoard() throws SolverException {
        SudokuBoard board3 = new SudokuBoard(solver);
        SudokuBoard board4 = new SudokuBoard(solver);

        board3.solveGame();
        board4.solveGame();

        assertNotEquals(board3.hashCode(),board4.hashCode());
        assertEquals(board3.hashCode(),board3.hashCode());
    }

    @Test
    public void testEqualsInSudokuBoard() throws SolverException {
        SudokuBoard board5 = new SudokuBoard(solver);
        SudokuBoard board6 = new SudokuBoard(solver);
        SudokuField field1 = new SudokuField();
        field1.setFieldValue(3);

        board5.solveGame();
        board6.solveGame();

        assertTrue(board5.equals(board5));
        assertFalse(board6.equals(null));
        assertFalse(board5.equals(field1));
        assertFalse(board5.equals(board6));
    }

    @Test
    public void testToStringInSudokuBoard() throws SolverException {
        SudokuBoard board7 = new SudokuBoard(solver);
        SudokuBoard board8 = new SudokuBoard(solver);

        board7.solveGame();
        board8.solveGame();

        String result = board7.toString();

        assertNotEquals(board7.toString(),board8.toString());

        assertTrue(result.contains("board"), "The result must contain board");
        assertTrue(result.contains("solver"), "The result must contain solver");
        assertTrue(result.contains("rows"), "The result must contain rows");
        assertTrue(result.contains("columns"), "The result must contain columns");
        assertTrue(result.contains("boxes"), "The result must contain boxes");

        assertNotNull(result, "The result cannot be empty");
    }
}

