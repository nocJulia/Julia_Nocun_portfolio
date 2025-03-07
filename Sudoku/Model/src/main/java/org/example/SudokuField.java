/**
 * This program is distributed under the GNU General Public License (GPL).
 * For more information, please refer to the LICENSE.txt file.
 */

package org.example;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.example.exceptions.BadFiledValueException;

import java.io.Serializable;
import java.util.ResourceBundle;

public class SudokuField implements Serializable, Cloneable, Comparable<SudokuField> {
    private int value;
    private final ResourceBundle listBundle = ResourceBundle.getBundle("org.example.Language");

    public SudokuField() {
        this.value = 0;
    }

    public int getFieldValue() {
        return value;
    }

    public void setFieldValue(int value) {
        if (value < 0 || value > 9) {
            throw new BadFiledValueException(listBundle.getObject("_wrongFieldValue").toString());
        }
        this.value = value;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("field value", value)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        SudokuField other = (SudokuField) obj;

        return new EqualsBuilder()
                .append(value, other.value)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(value)
                .toHashCode();
    }

    @Override
    public int compareTo(SudokuField o) {
        if (o == null) {
            throw new NullPointerException("Cannot compare to a null object.");
        } else if (this.getFieldValue() == o.getFieldValue()) {
            return 0;
        } else if (this.getFieldValue() > o.getFieldValue()) {
            return 1;
        } else {
            return -1;
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
