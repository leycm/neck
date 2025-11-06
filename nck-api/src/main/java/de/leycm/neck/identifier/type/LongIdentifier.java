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
 * An immutable identifier implementation for long integer values.
 * <p>
 * This class wraps a {@code long} value and provides type-safe identification
 * while maintaining numeric functionality through inheritance from {@link Number}.
 * It implements the {@link Identifier} interface for generic identifier handling
 * and supports comparison, hashing, and numeric conversions.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * LongIdentifier id = new LongIdentifier(123456789L);
 * long original = id.original(); // 123456789L
 * int intValue = id.intValue();  // 123456789 (if within int range)
 * String text = id.toString();   // "long:123456789"
 * }</pre>
 *
 * <p><b>Note:</b> The {@link #intValue()} method uses {@link Math#toIntExact(long)}
 * which may throw {@link ArithmeticException} if the value exceeds the int range.</p>
 *
 * <p><b>Thread Safety:</b> This class is immutable and thread-safe.</p>
 *
 * @author LeyCM
 * @see Identifier
 * @see Number
 * @since 1.0.1
 */
@EqualsAndHashCode(callSuper = false)
public final class LongIdentifier extends Number implements Identifier<Long> {

    /**
     * The underlying long value for this identifier.
     */
    private final long value;

    /**
     * Constructs a new {@code LongIdentifier} with the specified long value.
     *
     * @param value the long value to use as identifier (cannot be changed after construction)
     */
    public LongIdentifier(final long value) {
        this.value = value;
    }

    /**
     * Returns the original long value wrapped by this identifier.
     *
     * @return the original long value, guaranteed to be non-null
     */
    @Override
    public Long original() {
        return value;
    }

    /**
     * Compares this identifier with the specified long value.
     * <p>
     * The comparison follows the same rules as {@link Long#compare(long, long)}.
     * </p>
     *
     * @param other the long value to compare to (must not be null)
     * @return a negative integer, zero, or a positive integer as this identifier's value
     *         is less than, equal to, or greater than the specified value
     * @throws NullPointerException if the specified value is null
     */
    @Override
    public int compareTo(final @NonNull Long other) {
        return Long.compare(value, other);
    }

    /**
     * Returns a string representation of this identifier.
     * <p>
     * The format is: {@code "long:" + value}
     * </p>
     *
     * @return a string representation containing the identifier type and value
     */
    @Override
    public String toString() {
        return "long:" + value;
    }

    /**
     * Returns the value of this identifier as an {@code int}.
     * <p>
     * This method uses {@link Math#toIntExact(long)} to convert the long value to int.
     * </p>
     *
     * @return the numeric value represented by this object after conversion to type {@code int}
     * @throws ArithmeticException if the numeric value exceeds the range of an {@code int}
     */
    @Override
    public int intValue() {
        return Math.toIntExact(value);
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
     * <p>
     * <b>Note:</b> This may result in loss of precision for very large long values.
     * </p>
     *
     * @return the numeric value represented by this object after conversion to type {@code float}
     */
    @Override
    public float floatValue() {
        return value;
    }

    /**
     * Returns the value of this identifier as a {@code double}.
     * <p>
     * <b>Note:</b> This may result in loss of precision for very large long values.
     * </p>
     *
     * @return the numeric value represented by this object after conversion to type {@code double}
     */
    @Override
    public double doubleValue() {
        return value;
    }
}