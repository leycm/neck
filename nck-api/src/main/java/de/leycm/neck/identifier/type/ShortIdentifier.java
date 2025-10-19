package de.leycm.neck.identifier.type;

import de.leycm.neck.identifier.Identifier;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Identifier implementation for short-based identifiers.
 *
 * @author LeyCM
 * @since 1.0.1
 */
@EqualsAndHashCode(callSuper = false)
public final class ShortIdentifier extends Number implements Identifier<Short> {

    private final short value;

    public ShortIdentifier(final short value) {
        this.value = value;
    }

    @Override
    public Short original() {
        return value;
    }

    @Override
    public int compareTo(final @NonNull Short other) {
        return Short.compare(value, other);
    }

    @Override
    public String toString() {
        return "ShortIdentifier[" + value + "]";
    }

    @Override
    public int intValue() {
        return value;
    }

    @Override
    public long longValue() {
        return value;
    }

    @Override
    public float floatValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}

