package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.*;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

public class SudokuGame extends Application {

    public Stage primaryStage;
    private SudokuBoard sudokuBoard;

    private ResourceBundle bundle = ResourceBundle.getBundle("messages");

    public static void main(String[] args) {
        launch(args);
    }

    public void removeCells(int cellsToRemove) {

        Random random = new Random();

        // Usuń określoną liczbę pól z planszy
        for (int i = 0; i < cellsToRemove; i++) {
            int row = random.nextInt(9);
            int col = random.nextInt(9);

            // Ustaw wartość pola na 0 (usuń)
            sudokuBoard.set(row, col, 0);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        FXMLLoader mainFormLoader = new FXMLLoader(getClass().getResource("mainForm.fxml"), bundle);
        Parent mainFormRoot = mainFormLoader.load();

        MainFormController mainFormController = mainFormLoader.getController();
        mainFormController.setSudokuGame(this);

        primaryStage.setTitle("Sudoku Game");
        primaryStage.setScene(new Scene(mainFormRoot, 400, 400));
        primaryStage.show();

    }

    public void startGame(DifficultyLevel difficultyLevel, SudokuBoard loadedBoard) throws Exception {
        if (loadedBoard != null) {
            sudokuBoard = loadedBoard;
        } else {
            ISudokuSolver solver = new BacktrackingSudokuSolver();
            sudokuBoard = new SudokuBoard(solver);
            sudokuBoard.solveGame();

            if (difficultyLevel != null) {
                removeCells(difficultyLevel.getCellsToRemove());
            }
        }

        showSudokuBoard();
    }

    private void showSudokuBoard() throws Exception {
        FXMLLoader boardFormLoader = new FXMLLoader(getClass().getResource("boardForm.fxml"), bundle);
        Parent boardFormRoot = boardFormLoader.load();

        BoardFormController boardFormController = boardFormLoader.getController();
        boardFormController.setSudokuGame(this);
        boardFormController.setSudokuBoard(sudokuBoard);

        primaryStage.setScene(new Scene(boardFormRoot, 700, 500));
    }
}
