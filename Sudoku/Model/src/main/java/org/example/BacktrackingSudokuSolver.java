/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

package org.example;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BacktrackingSudokuSolver implements ISudokuSolver {

    private int[] findEmptyCell(SudokuBoard board) {
        int[] emptyCell = {-1, -1};
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board.get(row, col) == 0) {
                    emptyCell[0] = row;
                    emptyCell[1] = col;
                    return emptyCell;
                }
            }
        }
        return emptyCell;
    }

    private boolean isBoardFilled(SudokuBoard board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (board.get(i, j) == 0) {
                    return false; // Jeśli jest puste pole, to plansza nie jest wypełniona
                }
            }
        }
        return true; // Plansza jest wypełniona
    }

    @Override
    public void solve(SudokuBoard board) {

            Integer[] numbersToTryArray = new Integer[9];
            for (int i = 1; i <= 9; i++) {
                numbersToTryArray[i - 1] = i;
            }
            List<Integer> numbersToTry = Arrays.asList(numbersToTryArray);
            Collections.shuffle(numbersToTry);

            int[] emptyCell = findEmptyCell(board);
            int row = emptyCell[0]; // przypisanie współrzędnych wolnego pola do zmiennych row i col
            int col = emptyCell[1];

            if (row == -1) { // jeśli nie ma wolnego pola to metoda findEmptyCell ustawiła 'współrzędne'
                return;                   // wolnego pola na [-1, -1]
            }

            for (int number : numbersToTry) {
                board.set(row, col, number);

                if (board.get(row, col) == number) {
                    solve(board);
                    if (isBoardFilled(board)) {
                        return; // Jeśli plansza jest wypełniona, zakończ działanie
                    }
                }
            }

            board.set(row, col, 0); // Cofnięcie ruchu,
        }

}
