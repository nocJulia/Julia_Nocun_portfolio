package view;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import javafx.stage.FileChooser;

import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.*;
import org.apache.log4j.Logger;
import org.example.exceptions.DaoException;


public class MainFormController {

    @FXML
    public Button Lang2;
    @FXML
    public Button Lang1;
    @FXML
    public Label Lang0;
    public Label authorsLabel;

    @FXML
    private Button easyButton;

    @FXML
    private Button mediumButton;

    @FXML
    private Button hardButton;

    @FXML
    private Label newGameLabel;

    @FXML
    private Button loadButton;

    @FXML
    private Stage primaryStage;

    private final Logger logger = Logger.getLogger(String.valueOf(MainFormController.class));

    @FXML
    public void initialize() {
        initializeLoadButton();
    }

    private void initializeLoadButton() {
        loadButton.setOnAction(event -> {
            handleLoadButtonClick();
        });
    }

    private SudokuBoard loadedBoard = null;

    private SudokuGame sudokuGame;

    private Dao<SudokuBoard> sudokuBoardDao = null;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private static Languages.Language language;

    private final ResourceBundle langBundle = ResourceBundle.getBundle("messages");

    private final Authors authorsBundle = new Authors();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setSudokuGame(SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
    }


    @FXML
    private void startGame(ActionEvent event) throws Exception {
        if (sudokuGame != null) {
            if (loadedBoard == null) {
                // Obsługa rozpoczęcia nowej gry na podstawie poziomu trudności
                DifficultyLevel difficultyLevel;


                if (event.getSource() == easyButton) {
                    difficultyLevel = DifficultyLevel.EASY;
                    logger.info("The game has been loaded on Easy mode");
                } else if (event.getSource() == mediumButton) {
                    difficultyLevel = DifficultyLevel.MEDIUM;
                    logger.info("The game has been loaded on Medium mode");
                } else if (event.getSource() == hardButton) {
                    difficultyLevel = DifficultyLevel.HARD;
                    logger.info("The game has been loaded on Hard mode");
                } else {
                    throw new IllegalArgumentException("Unknown difficulty level");
                }


                sudokuGame.startGame(difficultyLevel, null);
            } else {
                if (loadedBoard != null) {
                    sudokuGame.startGame(null, loadedBoard);  // Rozpocznij nową grę z wczytaną planszą
                }
            }
        }
    }

    @FXML
    private void showSudokuBoard() throws Exception {
        if (sudokuGame != null) {
            FXMLLoader boardFormLoader = new FXMLLoader(getClass().getResource("boardForm.fxml"), langBundle);
            Parent boardFormRoot = boardFormLoader.load();


            BoardFormController boardFormController = boardFormLoader.getController();
            boardFormController.setSudokuGame(sudokuGame);

            primaryStage.setScene(new Scene(boardFormRoot, 600, 600));
        }
    }

    private void handleLoadButtonClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Wczytywanie planszy");
        dialog.setHeaderText("Podaj nazwę planszy do wczytania z bazy danych:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(boardName -> {
            try {
                // Establishing database connection
                try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sudoku_db",
                        "root", "510046470Km#")) {
                    Dao<SudokuBoard> sudokuBoardDao = SudokuBoardDaoFactory.createJdbcSudokuBoardDao(connection);

                    String boardname = result.get();
                    sudokuBoardDao.setFileName(boardname);

                    // Loading the board from the database
                    try {
                        SudokuBoard loadedBoard = sudokuBoardDao.read();

                        if (loadedBoard != null) {
                            try {
                                sudokuGame.startGame(null, loadedBoard);
                                logger.info("The game has been loaded from the database");
                            } catch (Exception e) {
                                e.printStackTrace();
                                logger.error("Error while starting the game");
                            }
                        } else {
                            logger.warn("Board with the given name does not exist in the database");
                        }
                    } catch (DaoException e) {
                        throw new RuntimeException("Error while reading the board from the database", e);
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to establish a database connection", e);
            }
        });
    }

    @FXML
    protected void langPL(ActionEvent event) throws IOException {
        language = Languages.Language.PL;
        changeLanguage(event);
    }

    @FXML
    protected void langEN(ActionEvent event) throws IOException {
        language = Languages.Language.EN;
        changeLanguage(event);
    }

    protected void changeLanguage(ActionEvent event) throws IOException {
        Platform.runLater(() -> {
            if (language == Languages.Language.PL) {
                Locale.setDefault(new Locale("pl"));
            } else if (language == Languages.Language.EN) {
                Locale.setDefault(new Locale("en"));
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainForm.fxml"));
            ResourceBundle newBundle = ResourceBundle.getBundle("messages");
            loader.setResources(newBundle);

            try {
                Parent root = loader.load();
                MainFormController controller = loader.getController();
                controller.setPrimaryStage(new Stage());  // Utwórz nowy primaryStage
                controller.setSudokuGame(sudokuGame);
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.setScene(new Scene(root, 400, 400));
                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    protected void authors() {
        Enumeration<String> authors = authorsBundle.getKeys();
        StringBuilder authorsString = new StringBuilder();
        while (authors.hasMoreElements()) {
            String key = authors.nextElement();
            authorsString.append(key).append(authorsBundle.getString(key)).append("\n");
        }
        authorsLabel.setText(authorsString.toString());
    }
}
