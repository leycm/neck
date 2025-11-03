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
 * Identifier implementation for long-based identifiers.
 *
 * @author LeyCM
 * @since 1.0.1
 */
@EqualsAndHashCode(callSuper = false)
public final class LongIdentifier extends Number implements Identifier<Long> {

    private final long value;

    public LongIdentifier(final long value) {
        this.value = value;
    }

    @Override
    public Long original() {
        return value;
    }

    @Override
    public int compareTo(final @NonNull Long other) {
        return Long.compare(value, other);
    }

    @Override
    public String toString() {
        return "LongIdentifier[" + value + "]";
    }

    @Override
    public int intValue() {
        return Math.toIntExact(value);
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

