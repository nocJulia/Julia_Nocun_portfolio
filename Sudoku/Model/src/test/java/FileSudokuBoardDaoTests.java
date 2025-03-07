///**
// * This program is distributed under the GNU General Public License (GPL).
// * For more information, please refer to the LICENSE.txt file.
// */
//
//import org.example.*;
//
//import org.example.exceptions.FileException;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//public class FileSudokuBoardDaoTests {
//
//    ISudokuSolver solver = new BacktrackingSudokuSolver();
//    private static final String testFileName = "testFile.txt";
//
//    private SudokuBoard testSudokuBoard;
//    private FileSudokuBoardDao fileSudokuBoardDao;
//
//    @BeforeEach
//    void setUp() {
//        testSudokuBoard = new SudokuBoard(solver);
//        fileSudokuBoardDao = new FileSudokuBoardDao(testFileName);
//    }
//
//    @AfterEach
//    void tearDown() {
//        try {
//            if (fileSudokuBoardDao != null) {
//                fileSudokuBoardDao.close();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        File testFile = new File(testFileName);
//        if (testFile.exists()) {
//            testFile.delete();
//        }
//    }
//
//    @Test
//    void testFileSudokuBoardDao() {
//        try {
//            fileSudokuBoardDao.write(testSudokuBoard);
//        } catch (FileException e) {
//            throw new RuntimeException(e);
//        }
//
//        SudokuBoard readSudokuBoard = null;
//        try {
//            readSudokuBoard = fileSudokuBoardDao.read();
//        } catch (FileException e) {
//            throw new RuntimeException(e);
//        }
//
//        assertNotNull(readSudokuBoard);
//
//        assertEquals(testSudokuBoard, readSudokuBoard);
//    }
//
//}