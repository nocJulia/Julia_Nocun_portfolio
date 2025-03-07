/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class SudokuUnit implements Serializable, Cloneable {
    protected SudokuField[] fields = new SudokuField[9];

    public boolean verify() {
        boolean[] usedValues = new boolean[10];

        for (int i = 0; i < 9; i++) {
            int value = fields[i].getFieldValue();
            if (value != 0) {
                if (usedValues[value]) {
                    return false;
                }
                usedValues[value] = true;
            }
        }

        return true;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("fields", fields)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(fields)
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (obj.getClass() != getClass()) {
            return false;
        }

        SudokuUnit other = (SudokuUnit) obj;

        return new EqualsBuilder()
                .append(fields, other.fields)
                .isEquals();
    }

    public void setFields(SudokuField[] fields) {
        this.fields = fields;
    }

    public SudokuField getField(int number) {
        return fields[number];
    }
}

