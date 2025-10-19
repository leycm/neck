package de.leycm.neck.identifier.type;

import de.leycm.neck.identifier.Identifier;
import lombok.NonNull;

/**
 * Identifier implementation for string-based identifiers.
 * <p>
 * Also implements CharSequence for direct usage in string contexts.
 *
 * @author LeyCM
 * @since 1.0.1
 */
public record StringIdentifier(String value)
        implements Identifier<String>, CharSequence {

    public StringIdentifier(final @NonNull String value) {
        this.value = value;
    }

    @Override
    public String original() {
        return value;
    }

    @Override
    public int compareTo(final @NonNull String other) {
        return value.compareTo(other);
    }

    @Override
    @NonNull
    public String toString() {
        return "StringIdentifier[" + value + "]";
    }

    @Override
    public int length() {
        return value.length();
    }

    @Override
    public char charAt(int index) {
        return value.charAt(index);
    }

    @Override
    @NonNull
    public CharSequence subSequence(int start, int end) {
        return value.subSequence(start, end);
    }
}

