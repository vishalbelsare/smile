/*
 * Copyright (c) 2010-2025 Haifeng Li. All rights reserved.
 *
 * Smile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Smile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Smile.  If not, see <https://www.gnu.org/licenses/>.
 */
package smile.data.vector;

import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.DoubleStream;
import smile.data.measure.CategoricalMeasure;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.math.MathEx;
import smile.util.Index;

/**
 * A double vector.
 *
 * @author Haifeng Li
 */
public class DoubleVector extends PrimitiveVector {
    /** The vector data. */
    private final double[] vector;

    /**
     * Constructor.
     * @param name the name of vector.
     * @param vector the elements of vector.
     */
    public DoubleVector(String name, double[] vector) {
        this(new StructField(name, DataTypes.DoubleType), vector);
    }

    /**
     * Constructor.
     * @param field the struct field of vector.
     * @param vector the elements of vector.
     */
    public DoubleVector(StructField field, double[] vector) {
        super(checkMeasure(field, CategoricalMeasure.class));
        this.vector = vector;
    }

    /**
     * Fills NaN/Inf values using the specified value.
     * @param value the value to replace NAs.
     */
    public void fillna(double value) {
        if (index == null) {
            for (int i = 0; i < vector.length; i++) {
                if (Double.isNaN(vector[i]) || Double.isInfinite(vector[i])) {
                    vector[i] = value;
                }
            }
        } else {
            indexStream().filter(i -> Double.isNaN(vector[at(i)]))
                    .forEach(i -> vector[at(i)] = value);
        }
    }

    @Override
    int length() {
        return vector.length;
    }

    @Override
    public DoubleStream asDoubleStream() {
        if (nullMask == null) {
            if (index == null) {
                return Arrays.stream(vector);
            } else {
                return index.stream().mapToDouble(i -> vector[i]);
            }
        } else {
            return indexStream().filter(i -> !nullMask.get(i)).mapToDouble(i -> vector[i]);
        }
    }

    @Override
    public void set(int i, Object value) {
        int index = at(i);
        if (value == null) {
            if (nullMask == null) {
                nullMask = new BitSet(vector.length);
            }
            nullMask.set(index);
        } else if (value instanceof Number n) {
            vector[index] = n.doubleValue();
        } else {
            throw new IllegalArgumentException("Invalid value type: " + value.getClass());
        }
    }

    @Override
    public DoubleVector get(Index index) {
        DoubleVector copy = new DoubleVector(field, vector);
        return slice(copy, index);
    }

    @Override
    public Double get(int i) {
        int index = at(i);
        if (nullMask == null) {
            return vector[index];
        } else {
            return nullMask.get(index) ? null : vector[index];
        }
    }

    @Override
    public double getDouble(int i) {
        return vector[at(i)];
    }

    @Override
    public boolean getBoolean(int i) {
        return MathEx.isZero(getDouble(i));
    }

    @Override
    public char getChar(int i) {
        return (char) getDouble(i);
    }

    @Override
    public byte getByte(int i) {
        return (byte) getDouble(i);
    }

    @Override
    public short getShort(int i) {
        return (short) getDouble(i);
    }

    @Override
    public int getInt(int i) {
        return (int) getDouble(i);
    }

    @Override
    public long getLong(int i) {
        return (long) getDouble(i);
    }

    @Override
    public float getFloat(int i) {
        return (float) getDouble(i);
    }
}