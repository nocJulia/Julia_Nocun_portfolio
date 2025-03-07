import org.example.BacktrackingSudokuSolver;
import org.example.ISudokuSolver;
import org.example.SudokuBoard;
import org.example.SudokuField;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CloneTest {

    @Test
    void testCloneSudokuBoard() {
        ISudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard originalBoard = new SudokuBoard(solver);

        try {
            SudokuBoard clonedBoard = (SudokuBoard) originalBoard.clone();

            assertNotSame(originalBoard, clonedBoard);
            assertEquals(originalBoard.toString(), clonedBoard.toString());

        } catch (CloneNotSupportedException e) {
            fail("Cloning not supported for SudokuBoard");
        }
    }

    @Test
    void testCreateCloneSudokuBoard() {
        ISudokuSolver solver = new BacktrackingSudokuSolver();
        SudokuBoard originalBoard = new SudokuBoard(solver);

        SudokuBoard clonedBoard = originalBoard.createClone();

        assertNotSame(originalBoard, clonedBoard);
        assertEquals(originalBoard.toString(), clonedBoard.toString());
    }
}
