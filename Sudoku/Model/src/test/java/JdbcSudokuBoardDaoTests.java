/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

import org.example.*;

import org.example.exceptions.BadFiledValueException;
import org.example.exceptions.DaoException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSudokuBoardDaoTest {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/sudoku_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "510046470Km#";

    private Connection connection;
    private SudokuBoard originalBoard;
    private SudokuBoard loadedBoard;

    JdbcSudokuBoardDao sudokuBoardDao;

    @BeforeEach
    void setUp() {
        try {
            connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
            sudokuBoardDao = new JdbcSudokuBoardDao(connection);
            originalBoard = new SudokuBoard(new BacktrackingSudokuSolver());
            loadedBoard = null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        JdbcSudokuBoardDao sudokuBoardDao = null;
        SudokuBoard originalBoard = null;
        SudokuBoard loadedBoard = null;
    }

    @Test
    public void testWriteAndRead() throws DaoException {
        // Write
        try {
            sudokuBoardDao.setFileName("oryginal");
            sudokuBoardDao.write(originalBoard);
        } catch (DaoException e) {
            fail("Failed to write to the database: " + e.getMessage());
        }

        // Read
        try {
            sudokuBoardDao.setFileName("oryginal");
            loadedBoard = sudokuBoardDao.read();
        } catch (DaoException e) {
            fail("Failed to read from the database: " + e.getMessage());
        }

        // Assertions
        assertNotNull(loadedBoard, "Loaded board is null");
        assertNotSame(originalBoard, loadedBoard, "Loaded board is the same instance as the original");
        assertEquals(originalBoard, loadedBoard, "Loaded board is not equal to the original");
    }

    @Test
    public void testWriteReadAndCompareResources() throws DaoException {
        try {
            sudokuBoardDao.setFileName("oryginal");
            sudokuBoardDao.write(originalBoard);

            // Read
            sudokuBoardDao.setFileName("oryginal");
            loadedBoard = sudokuBoardDao.read();

            assertNotNull(loadedBoard, "Loaded board is null");
            assertNotSame(originalBoard, loadedBoard, "Loaded board is the same instance as the original");
            assertEquals(originalBoard, loadedBoard, "Loaded board is not equal to the original");
        } catch (DaoException e) {
            fail("Failed to write or read from the database: " + e.getMessage());
        }
    }

    @Test
    void testReadWithException() {
        sudokuBoardDao.setFileName("nonexistent_board");
        assertThrows(DaoException.class, () -> loadedBoard = sudokuBoardDao.read(),
                "Expected DaoException, but it was not thrown");
    }
}
