/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

package org.example;

import org.example.exceptions.DaoException;

import java.sql.*;

public class JdbcSudokuBoardDao implements Dao<SudokuBoard>, AutoCloseable {
    private Connection connection;
    private String boardName;

    public JdbcSudokuBoardDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SudokuBoard read() throws DaoException {
        try {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM sudoku_board WHERE name = ?")) {
                preparedStatement.setString(1, boardName);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    SudokuBoard sudokuBoard = new SudokuBoard(new BacktrackingSudokuSolver());

                    try (PreparedStatement cellPreparedStatement = connection.prepareStatement(
                            "SELECT * FROM sudoku_cell WHERE board_id = ?")) {
                        cellPreparedStatement.setInt(1, resultSet.getInt("id"));
                        ResultSet cellResultSet = cellPreparedStatement.executeQuery();

                        while (cellResultSet.next()) {
                            int row = cellResultSet.getInt("board_row");
                            int col = cellResultSet.getInt("board_col");
                            int value = cellResultSet.getInt("cell_value");
                            sudokuBoard.set(row, col, value);
                        }
                    }

                    return sudokuBoard;
                } else {
                    throw new DaoException(new Throwable("Board with name '" + boardName + "' not found"));
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void write(SudokuBoard obj) throws DaoException {
        try {
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "INSERT INTO sudoku_board (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, boardName);
                preparedStatement.executeUpdate();

                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int boardId = generatedKeys.getInt(1);

                    try (PreparedStatement cellStatement = connection.prepareStatement(
                            "INSERT INTO sudoku_cell (board_row, board_col, cell_value, board_id)"
                                    + " VALUES (?, ?, ?, ?)")) {
                        for (int row = 0; row < 9; row++) {
                            for (int col = 0; col < 9; col++) {
                                int value = obj.get(row, col);
                                cellStatement.setInt(1, row);
                                cellStatement.setInt(2, col);
                                cellStatement.setInt(3, value);
                                cellStatement.setInt(4, boardId);
                                cellStatement.addBatch();
                            }
                        }

                        cellStatement.executeBatch();
                    }
                }

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw new DaoException(e);
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                throw new DaoException(e);
            }
        }
    }

    @Override
    public void setFileName(String filename) {
        this.boardName = filename;
    }

    @Override
    public void close() throws Exception {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new Exception("Error closing the connection", e);
        }
    }

}
