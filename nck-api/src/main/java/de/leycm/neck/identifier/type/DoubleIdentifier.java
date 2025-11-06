/**
 * LECP-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the LECP-LICENSE. <br>
 * License at: <a href="https://github.com/leycm/leycm/blob/main/LICENSE">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <a href="mailto:leycm@proton.me">leycm@proton.me</a> l <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package de.leycm.neck.identifier.type;

import de.leycm.neck.identifier.Identifier;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * An immutable identifier implementation for double-precision floating-point values.
 * <p>
 * This class wraps a {@code double} value and provides type-safe identification
 * while maintaining numeric functionality through inheritance from {@link Number}.
 * It implements the {@link Identifier} interface for generic identifier handling
 * and supports comparison, hashing, and numeric conversions.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * DoubleIdentifier id = new DoubleIdentifier(123.45);
 * double original = id.original(); // 123.45
 * int intValue = id.intValue();    // 123
 * String text = id.toString();     // "double:123.45"
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This class is immutable and thread-safe.</p>
 *
 * @author LeyCM
 * @see Identifier
 * @see Number
 * @since 1.0.1
 */
@EqualsAndHashCode(callSuper = false)
public final class DoubleIdentifier extends Number implements Identifier<Double> {

    /**
     * The underlying double value for this identifier.
     */
    private final double value;

    /**
     * Constructs a new {@code DoubleIdentifier} with the specified double value.
     *
     * @param value the double value to use as identifier (cannot be changed after construction)
     */
    public DoubleIdentifier(final double value) {
        this.value = value;
    }

    /**
     * Returns the original double value wrapped by this identifier.
     *
     * @return the original double value, guaranteed to be non-null
     */
    @Override
    public Double original() {
        return value;
    }

    /**
     * Compares this identifier with the specified double value.
     * <p>
     * The comparison follows the same rules as {@link Double#compare(double, double)}.
     * </p>
     *
     * @param other the double value to compare to (must not be null)
     * @return a negative integer, zero, or a positive integer as this identifier's value
     *         is less than, equal to, or greater than the specified value
     * @throws NullPointerException if the specified value is null
     */
    @Override
    public int compareTo(final @NonNull Double other) {
        return Double.compare(value, other);
    }

    /**
     * Returns a string representation of this identifier.
     * <p>
     * The format is: {@code "double:" + value}
     * </p>
     *
     * @return a string representation containing the identifier type and value
     */
    @Override
    public String toString() {
        return "double:" + value;
    }

    /**
     * Returns the value of this identifier as an {@code int} (by casting to int).
     * <p>
     * <b>Note:</b> This may result in loss of precision for fractional values.
     * </p>
     *
     * @return the numeric value represented by this object after conversion to type {@code int}
     */
    @Override
    public int intValue() {
        return (int) value;
    }

    /**
     * Returns the value of this identifier as a {@code long} (by casting to long).
     * <p>
     * <b>Note:</b> This may result in loss of precision for fractional values.
     * </p>
     *
     * @return the numeric value represented by this object after conversion to type {@code long}
     */
    @Override
    public long longValue() {
        return (long) value;
    }

    /**
     * Returns the value of this identifier as a {@code float} (by casting to float).
     * <p>
     * <b>Note:</b> This may result in loss of precision.
     * </p>
     *
     * @return the numeric value represented by this object after conversion to type {@code float}
     */
    @Override
    public float floatValue() {
        return (float) value;
    }

    /**
     * Returns the value of this identifier as a {@code double}.
     * <p>
     * This method returns the exact value without any precision loss.
     * </p>
     *
     * @return the numeric value represented by this object after conversion to type {@code double}
     */
    @Override
    public double doubleValue() {
        return value;
    }
}