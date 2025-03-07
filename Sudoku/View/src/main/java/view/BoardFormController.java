package view;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.binding.StringBinding;
import javafx.beans.binding.StringExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;
import javafx.util.converter.NumberStringConverter;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import org.apache.log4j.Logger;
import org.example.SudokuBoard;
import org.example.*;

import java.awt.event.ActionEvent;
import java.beans.PropertyDescriptor;
import java.beans.IntrospectionException;

import javafx.scene.control.ComboBox;
import org.example.exceptions.DaoException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import java.io.File;


public class BoardFormController {

    @FXML
    private Button button_clear;

    @FXML
    private Stage primaryStage;


    @FXML
    private SudokuGame sudokuGame;


    @FXML
    private SudokuBoard sudokuBoard;

    @FXML
    private GridPane gridPane;

    private int selectedRow = -1;
    private int selectedCol = -1;


    @FXML
    private Button backToMenuButton;


    @FXML
    private Button button_one;


    @FXML
    private Button button_two;


    @FXML
    private Button button_three;


    @FXML
    private Button button_four;


    @FXML
    private Button button_five;


    @FXML
    private Button button_six;


    @FXML
    private Button button_seven;


    @FXML
    private Button button_eight;


    @FXML
    private Button button_nine;

    @FXML
    private Button button_save;

    private Dao<SudokuBoard> sudokuBoardDao = null;

    private final Logger logger = Logger.getLogger(MainFormController.class);

    private Set<SudokuField> initialFields = new HashSet<>();


    // Metoda do wczytania planszy z pliku
    private void loadBoardFromFile() throws DaoException {
        if (sudokuBoardDao != null) {
            SudokuBoard loadedBoard = sudokuBoardDao.read();
            if (loadedBoard != null) {
                sudokuBoard = loadedBoard;
                drawBoard();
            } else {
                // Obsługa błędu wczytywania planszy
            }
        } else {
            // Obsługa braku zainicjowanego sudokuBoardDao
        }
    }

    private void handleSaveButtonClick() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Zapisywanie planszy");
        dialog.setHeaderText("Podaj nazwę pod jaką chcesz zapisać planszę:");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String fileName = result.get();
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sudoku_db",
                    "root", "510046470Km#")) {
                Dao<SudokuBoard> sudokuBoardDao = SudokuBoardDaoFactory.createJdbcSudokuBoardDao(connection);

                try {
                    sudokuBoardDao.setFileName(fileName);
                    sudokuBoardDao.write(sudokuBoard);
                    showInfoAlert("Zapisano planszę pomyślnie do bazy danych!");
                    logger.info("The game has been saved to the database");
                } catch (DaoException e) {
                    throw new RuntimeException(e);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to establish a database connection", e);
            }
        }
    }

    private void showInfoAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacja");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }


    public void setSudokuGame(SudokuGame sudokuGame) {
        this.sudokuGame = sudokuGame;
    }


    public void setSudokuBoard(SudokuBoard sudokuBoard) {
        this.sudokuBoard = sudokuBoard;
        drawBoard();
        bindSudokuBoard();
    }

    private Button createButton(int row, int col) {
        Button button = new Button();
        button.setMinSize(50, 50);
        button.setMaxSize(50, 50);

        updateButton(button, row, col);

        button.setOnAction(event -> handleButtonClick(row, col, button));

        return button;
    }


    private void updateButton(Button button, int row, int col) {
        int value = sudokuBoard.get(row, col);
        if (value != 0) {
            button.setText(String.valueOf(value));
        }

        if (selectedRow == row && selectedCol == col) {
            // Zaznaczony przycisk ma inny styl
            button.setStyle("-fx-background-color: lightblue;");
        } else {
            // Zwykły przycisk ma domyślny styl
            button.setStyle(null);
        }
    }

    private void handleButtonClick(int row, int col, Button button) {
        SudokuField currentField = sudokuBoard.getRow(row).getField(col);

        if (!initialFields.contains(currentField)) {
            if (selectedRow == row && selectedCol == col) {
                // Odznacz przycisk, jeśli jest już zaznaczony
                selectedRow = -1;
                selectedCol = -1;
            } else {
                // Zaznacz klikniętą komórkę
                selectedRow = row;
                selectedCol = col;
            }

            drawBoard(); // Przerysuj planszę, aby odzwierciedlić zaznaczenie
            updateButton(button, row, col);  // Aktualizuj wygląd bieżącego przycisku
        }
    }

    private void drawBoard() {
        gridPane.getChildren().clear();

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button button = createButton(row, col);
                gridPane.add(button, col, row);

                if (sudokuBoard.get(row, col) != 0) {
                    initialFields.add(sudokuBoard.getRow(row).getField(col));
                }
            }
        }

        initializeNumberButtons();
    }

    private void initializeNumberButtons() {
        button_one.setOnAction(event -> handleNumberButtonClick(1));
        button_two.setOnAction(event -> handleNumberButtonClick(2));
        button_three.setOnAction(event -> handleNumberButtonClick(3));
        button_four.setOnAction(event -> handleNumberButtonClick(4));
        button_five.setOnAction(event -> handleNumberButtonClick(5));
        button_six.setOnAction(event -> handleNumberButtonClick(6));
        button_seven.setOnAction(event -> handleNumberButtonClick(7));
        button_eight.setOnAction(event -> handleNumberButtonClick(8));
        button_nine.setOnAction(event -> handleNumberButtonClick(9));
        button_clear.setOnAction(event -> handleClearButtonClick());
        button_save.setOnAction(event -> handleSaveButtonClick());
        backToMenuButton.setOnAction(event -> {
            try {
                backToMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void handleNumberButtonClick(int number) {
        if (selectedRow != -1 && selectedCol != -1) {
            sudokuBoard.set(selectedRow, selectedCol, number);
            drawBoard();
        }
    }

    private void handleClearButtonClick() {
        if (selectedRow != -1 && selectedCol != -1) {
            sudokuBoard.set(selectedRow, selectedCol, 0);
            drawBoard();
        }
    }

    private void bindSudokuBoard() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                Button button = (Button) gridPane.getChildren().get(row * 9 + col);

                SimpleIntegerProperty property = new SimpleIntegerProperty(sudokuBoard.get(row, col));

                Bindings.bindBidirectional(
                        button.textProperty(),
                        property,
                        new StringConverter<>() {
                            @Override
                            public String toString(Number object) {
                                return (object != null && object.intValue() != 0) ? object.toString() : "";
                            }

                            @Override
                            public Number fromString(String string) {
                                try {
                                    return (string != null && !string.isEmpty()) ? Integer.parseInt(string) : null;
                                } catch (NumberFormatException e) {
                                    return null;
                                }
                            }
                        }
                );
            }
        }
    }

    protected void backToMenu() throws IOException {
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainForm.fxml"));
            ResourceBundle newBundle = ResourceBundle.getBundle("messages");
            loader.setResources(newBundle);

            try {
                Parent root = loader.load();
                MainFormController controller = loader.getController();
                Stage newPrimaryStage = new Stage();  // Utwórz nowy primaryStage
                controller.setPrimaryStage(newPrimaryStage);
                controller.setSudokuGame(sudokuGame);

                Stage currentStage = (Stage) backToMenuButton.getScene().getWindow();
                currentStage.setScene(new Scene(root, 400, 400));
                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}

