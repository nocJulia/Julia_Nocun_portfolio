/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

package org.example;

import org.example.exceptions.SolverException;

import java.io.Serializable;


public interface ISudokuSolver extends Serializable {
    void solve(SudokuBoard board) throws SolverException;
}
