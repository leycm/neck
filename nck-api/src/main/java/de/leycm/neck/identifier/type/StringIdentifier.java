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
import lombok.NonNull;

/**
 * An immutable identifier implementation for string values.
 * <p>
 * This record wraps a {@link String} value and provides type-safe identification.
 * It implements both the {@link Identifier} interface for generic identifier handling
 * and the {@link CharSequence} interface for direct usage in string manipulation contexts.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * StringIdentifier id = new StringIdentifier("user-123");
 * String original = id.original();        // "user-123"
 * int length = id.length();               // 8
 * char firstChar = id.charAt(0);          // 'u'
 * String text = id.toString();            // "string:user-123"
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This record is immutable and thread-safe, as it only
 * contains an immutable String value.</p>
 *
 * @param value the string value used as identifier
 * @author LeyCM
 * @see Identifier
 * @see CharSequence
 * @since 1.0.1
 */
public record StringIdentifier(String value)
        implements Identifier<String>, CharSequence {

    /**
     * Constructs a new {@code StringIdentifier} with the specified string value.
     *
     * @param value the string value to use as identifier (must not be null)
     * @throws NullPointerException if the specified value is null
     */
    public StringIdentifier(final @NonNull String value) {
        this.value = value;
    }

    /**
     * Returns the original string value wrapped by this identifier.
     *
     * @return the original string value, guaranteed to be non-null
     */
    @Override
    public String original() {
        return value;
    }

    /**
     * Compares this identifier with the specified string value lexicographically.
     * <p>
     * The comparison follows the same rules as {@link String#compareTo(String)}.
     * </p>
     *
     * @param other the string value to compare to (must not be null)
     * @return a negative integer, zero, or a positive integer as this identifier's value
     *         is lexicographically less than, equal to, or greater than the specified value
     * @throws NullPointerException if the specified value is null
     */
    @Override
    public int compareTo(final @NonNull String other) {
        return value.compareTo(other);
    }

    /**
     * Returns a string representation of this identifier.
     * <p>
     * The format is: {@code "string:" + value}
     * </p>
     *
     * @return a string representation containing the identifier type and value
     */
    @Override
    @NonNull
    public String toString() {
        return "string:" + value;
    }

    /**
     * Returns the length of the underlying string value.
     *
     * @return the length of the character sequence
     */
    @Override
    public int length() {
        return value.length();
    }

    /**
     * Returns the {@code char} value at the specified index from the underlying string.
     *
     * @param index the index of the {@code char} value to be returned
     * @return the specified {@code char} value
     * @throws IndexOutOfBoundsException if the {@code index} argument is negative or not less than {@code length()}
     */
    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    /**
     * Returns a {@code CharSequence} that is a subsequence of the underlying string.
     *
     * @param start the start index, inclusive
     * @param end the end index, exclusive
     * @return the specified subsequence
     * @throws IndexOutOfBoundsException if {@code start} or {@code end} are negative,
     *         if {@code end} is greater than {@code length()}, or if {@code start} is greater than {@code end}
     */
    @Override
    @NonNull
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }
}