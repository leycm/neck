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
 * An immutable identifier implementation for short integer values.
 * <p>
 * This class wraps a {@code short} value and provides type-safe identification
 * while maintaining numeric functionality through inheritance from {@link Number}.
 * It implements the {@link Identifier} interface for generic identifier handling
 * and supports comparison, hashing, and numeric conversions without precision loss
 * (since short values fit within all larger numeric types).
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * ShortIdentifier id = new ShortIdentifier((short) 42);
 * short original = id.original(); // 42
 * int intValue = id.intValue();   // 42
 * String text = id.toString();    // "short:42"
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
public final class ShortIdentifier extends Number implements Identifier<Short> {

    /**
     * The underlying short value for this identifier.
     */
    private final short value;

    /**
     * Constructs a new {@code ShortIdentifier} with the specified short value.
     *
     * @param value the short value to use as identifier (cannot be changed after construction)
     */
    public ShortIdentifier(final short value) {
        this.value = value;
    }

    /**
     * Returns the original short value wrapped by this identifier.
     *
     * @return the original short value, guaranteed to be non-null
     */
    @Override
    public Short original() {
        return value;
    }

    /**
     * Compares this identifier with the specified short value.
     * <p>
     * The comparison follows the same rules as {@link Short#compare(short, short)}.
     * </p>
     *
     * @param other the short value to compare to (must not be null)
     * @return a negative integer, zero, or a positive integer as this identifier's value
     *         is less than, equal to, or greater than the specified value
     * @throws NullPointerException if the specified value is null
     */
    @Override
    public int compareTo(final @NonNull Short other) {
        return Short.compare(value, other);
    }

    /**
     * Returns a string representation of this identifier.
     * <p>
     * The format is: {@code "short:" + value}
     * </p>
     *
     * @return a string representation containing the identifier type and value
     */
    @Override
    public String toString() {
        return "short:" + value;
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