package org.example;

import java.sql.Connection;

public class SudokuBoardDaoFactory {
    public static Dao<SudokuBoard> createJdbcSudokuBoardDao(Connection connection) {
        return new JdbcSudokuBoardDao(connection);
    }
}
