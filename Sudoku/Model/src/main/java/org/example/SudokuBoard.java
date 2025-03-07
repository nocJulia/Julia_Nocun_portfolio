/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.exceptions.SolverException;

import java.io.Serializable;

public class SudokuBoard implements Serializable, Cloneable {
    private SudokuField[][] board = new SudokuField[9][9];
    private ISudokuSolver solver;
    private SudokuRow[] rows = new SudokuRow[9];
    private SudokuColumn[] columns = new SudokuColumn[9];
    private SudokuBox[] boxes = new SudokuBox[9];

    // ustala numer kwadratu na podstawie kolumny i wiersza
    private int boxIndex(int row, int col) {
        int boxRow = row / 3;
        int boxCol = col / 3;
        int boxIndex = boxRow * 3 + boxCol;

        return boxIndex;
    }

    public SudokuBoard(ISudokuSolver solver) {
        this.solver = solver;

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                board[row][col] = new SudokuField();
            }
        }

        // Inicjalizacja i przekazanie pól dla każdej jednostki
        for (int i = 0; i < 9; i++) {
            SudokuField[] rowFields = new SudokuField[9];
            SudokuField[] colFields = new SudokuField[9];
            SudokuField[] boxFields = new SudokuField[9];

            for (int j = 0; j < 9; j++) {
                rowFields[j] = board[i][j];
                colFields[j] = board[j][i];
                int boxRow = (i / 3) * 3 + j / 3;
                int boxCol = (i % 3) * 3 + j % 3;
                boxFields[j] = board[boxRow][boxCol];
            }

            rows[i] = new SudokuRow(rowFields);
            columns[i] = new SudokuColumn(colFields);
            boxes[i] = new SudokuBox(boxFields);
        }
    }

    // Getter
    public int get(int x, int y) {
        return board[x][y].getFieldValue();
    }

    // Setter
    public void set(int x, int y, int value) {
        int oldValue = board[x][y].getFieldValue();

        board[x][y].setFieldValue(value);
        if (!getRow(x).verify() || !getColumn(y).verify() || !getBox(x, y).verify()) {
            // Jeśli ruch jest nielegalny, przywróć poprzednią wartość
            board[x][y].setFieldValue(oldValue);
        }
    }

    public void solveGame() throws SolverException {
        solver.solve(this);
    }

    public SudokuRow getRow(int x) {
        return rows[x];
    }

    public SudokuColumn getColumn(int y) {
        return columns[y];
    }

    public SudokuBox getBox(int x, int y) {
        return boxes[boxIndex(x, y)];
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("board", board)
                .append("solver", solver)
                .append("rows", rows)
                .append("columns", columns)
                .append("boxes", boxes)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(board)
                .append(solver)
                .append(rows)
                .append(columns)
                .append(boxes)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SudokuBoard other = (SudokuBoard) obj;

        return new EqualsBuilder()
                .append(board, other.board)
                .append(rows, other.rows)
                .append(columns, other.columns)
                .append(boxes, other.boxes)
                .isEquals();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SudokuBoard clonedBoard = (SudokuBoard) super.clone();

        clonedBoard.board = new SudokuField[9][9];
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                clonedBoard.board[row][col] = (SudokuField) this.board[row][col].clone();
            }
        }

        clonedBoard.rows = new SudokuRow[9];
        clonedBoard.columns = new SudokuColumn[9];
        clonedBoard.boxes = new SudokuBox[9];

        for (int i = 0; i < 9; i++) {
            clonedBoard.rows[i] = (SudokuRow) this.rows[i].clone();
            clonedBoard.columns[i] = (SudokuColumn) this.columns[i].clone();
            clonedBoard.boxes[i] = (SudokuBox) this.boxes[i].clone();
        }

        return clonedBoard;
    }

    public SudokuBoard createClone() {
        try {
            return (SudokuBoard) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Cloning not supported for SudokuBoard", e);
        }
    }
}