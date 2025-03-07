/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

package org.example;

public class SudokuRow extends SudokuUnit {
    public SudokuRow(SudokuField[] fields) {
        this.fields = fields;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SudokuRow clonedUnit = (SudokuRow) super.clone();

        clonedUnit.fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            clonedUnit.fields[i] = (SudokuField) this.fields[i].clone();
        }

        return clonedUnit;
    }
}
