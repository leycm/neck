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
 * An immutable identifier implementation for integer values.
 * <p>
 * This class wraps an {@code int} value and provides type-safe identification
 * while maintaining numeric functionality through inheritance from {@link Number}.
 * It implements the {@link Identifier} interface for generic identifier handling
 * and supports comparison, hashing, and numeric conversions without precision loss.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * IntIdentifier id = new IntIdentifier(42);
 * int original = id.original(); // 42
 * long longValue = id.longValue(); // 42L
 * String text = id.toString(); // "int:42"
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
public final class IntIdentifier extends Number implements Identifier<Integer> {

    /**
     * The underlying integer value for this identifier.
     */
    private final int value;

    /**
     * Constructs a new {@code IntIdentifier} with the specified integer value.
     *
     * @param value the integer value to use as identifier (cannot be changed after construction)
     */
    public IntIdentifier(final int value) {
        this.value = value;
    }

    /**
     * Returns the original integer value wrapped by this identifier.
     *
     * @return the original integer value, guaranteed to be non-null
     */
    @Override
    public Integer original() {
        return value;
    }

    /**
     * Compares this identifier with the specified integer value.
     * <p>
     * The comparison follows the same rules as {@link Integer#compare(int, int)}.
     * </p>
     *
     * @param other the integer value to compare to (must not be null)
     * @return a negative integer, zero, or a positive integer as this identifier's value
     *         is less than, equal to, or greater than the specified value
     * @throws NullPointerException if the specified value is null
     */
    @Override
    public int compareTo(final @NonNull Integer other) {
        return Integer.compare(value, other);
    }

    /**
     * Returns a string representation of this identifier.
     * <p>
     * The format is: {@code "int:" + value}
     * </p>
     *
     * @return a string representation containing the identifier type and value
     */
    @Override
    public String toString() {
        return "int:" + value;
    }

    /**
     * Returns the value of this identifier as an {@code int}.
     *
     * @return the numeric value represented by this object after conversion to type {@code int}
     */
    @Override
    public int intValue() {
        return value;
    }

    /**
     * Returns the value of this identifier as a {@code long}.
     *
     * @return the numeric value represented by this object after conversion to type {@code long}
     */
    @Override
    public long longValue() {
        return value;
    }

    /**
     * Returns the value of this identifier as a {@code float}.
     *
     * @return the numeric value represented by this object after conversion to type {@code float}
     */
    @Override
    public float floatValue() {
        return value;
    }

    /**
     * Returns the value of this identifier as a {@code double}.
     *
     * @return the numeric value represented by this object after conversion to type {@code double}
     */
    @Override
    public double doubleValue() {
        return value;
    }
}