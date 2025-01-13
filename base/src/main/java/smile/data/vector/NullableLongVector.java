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
import java.util.stream.LongStream;
import smile.data.measure.NumericalMeasure;
import smile.data.type.DataTypes;
import smile.data.type.StructField;
import smile.util.Index;

/**
 * A nullable long vector.
 *
 * @author Haifeng Li
 */
public class NullableLongVector extends NullablePrimitiveVector {
    /** The vector data. */
    private final long[] vector;

    /**
     * Constructor.
     * @param name the name of vector.
     * @param vector the elements of vector.
     * @param nullMask The null bitmap. The bit is 1 if the value is null.
     */
    public NullableLongVector(String name, long[] vector, BitSet nullMask) {
        this(new StructField(name, DataTypes.NullableLongType), vector, nullMask);
    }

    /**
     * Constructor.
     * @param field the struct field of vector.
     * @param vector the elements of vector.
     * @param nullMask The null bitmap. The bit is 1 if the value is null.
     */
    public NullableLongVector(StructField field, long[] vector, BitSet nullMask) {
        super(field, nullMask);
        if (field.dtype() != DataTypes.NullableLongType) {
            throw new IllegalArgumentException("Invalid data type: " + field);
        }
        this.vector = vector;
    }

    @Override
    public int size() {
        return vector.length;
    }

    @Override
    public LongStream asLongStream() {
        return index().filter(i -> !nullMask.get(i)).mapToLong(i -> vector[i]);
    }

    @Override
    public DoubleStream asDoubleStream() {
        return asLongStream().mapToDouble(i -> i);
    }

    @Override
    public void set(int i, Object value) {
        if (value == null) {
            nullMask.set(i);
        } else if (value instanceof Number n) {
            vector[i] = n.longValue();
        } else {
            throw new IllegalArgumentException("Invalid value type: " + value.getClass());
        }
    }

    @Override
    public NullableLongVector get(Index index) {
        int n = index.size();
        long[] data = new long[n];
        BitSet mask = new BitSet(n);
        for (int i = 0; i < n; i++) {
            int idx = index.apply(i);
            data[i] = vector[idx];
            mask.set(i, nullMask.get(idx));
        }
        return new NullableLongVector(field, data, mask);
    }

    @Override
    public Long get(int i) {
        return nullMask.get(i) ? null : vector[i];
    }

    @Override
    public long getLong(int i) {
        return vector[i];
    }

    @Override
    public boolean getBoolean(int i) {
        return getLong(i) != 0;
    }

    @Override
    public char getChar(int i) {
        return (char) getLong(i);
    }

    @Override
    public byte getByte(int i) {
        return (byte) getLong(i);
    }

    @Override
    public short getShort(int i) {
        return (short) getLong(i);
    }

    @Override
    public int getInt(int i) {
        return (int) getLong(i);
    }

    @Override
    public float getFloat(int i) {
        return getLong(i);
    }

    @Override
    public double getDouble(int i) {
        return getLong(i);
    }
}