/**
 * LECP-LICENSE NOTICE
 * <br><br>
 * This Sourcecode is under the LECP-LICENSE. <br>
 * License at: <a href="https://github.com/leycm/leycm/blob/main/LICENSE">GITHUB</a>
 * <br><br>
 * Copyright (c) LeyCM <leycm@proton.me> <br>
 * Copyright (c) maintainers <br>
 * Copyright (c) contributors
 */
package de.leycm.neck.identifier.type;

import de.leycm.neck.identifier.Identifier;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

/**
 * Identifier implementation for int-based identifiers.
 *
 * @author LeyCM
 * @since 1.0.1
 */
@EqualsAndHashCode(callSuper = false)
public final class IntIdentifier extends Number implements Identifier<Integer> {

    private final int value;

    public IntIdentifier(final int value) {
        this.value = value;
    }

    @Override
    public Integer original() {
        return value;
    }

    @Override
    public int compareTo(final @NonNull Integer other) {
        return Integer.compare(value, other);
    }

    @Override
    public String toString() {
        return "IntIdentifier[" + value + "]";
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
