/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

package org.example;

public class SudokuColumn extends SudokuUnit {
    public SudokuColumn(SudokuField[] fields) {
        this.fields = fields;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        SudokuColumn clonedUnit = (SudokuColumn) super.clone();

        clonedUnit.fields = new SudokuField[9];
        for (int i = 0; i < 9; i++) {
            clonedUnit.fields[i] = (SudokuField) this.fields[i].clone();
        }

        return clonedUnit;
    }
}
