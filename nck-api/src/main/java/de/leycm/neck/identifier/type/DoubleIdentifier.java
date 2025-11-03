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
 * Identifier implementation for double-based identifiers.
 *
 * @author LeyCM
 * @since 1.0.1
 */
@EqualsAndHashCode(callSuper = false)
public final class DoubleIdentifier extends Number implements Identifier<Double> {

    private final double value;

    public DoubleIdentifier(final double value) {
        this.value = value;
    }

    @Override
    public Double original() {
        return value;
    }

    @Override
    public int compareTo(final @NonNull Double other) {
        return Double.compare(value, other);
    }

    @Override
    public String toString() {
        return "DoubleIdentifier[" + value + "]";
    }

    @Override
    public int intValue() {
        return (int) value;
    }

    @Override
    public long longValue() {
        return (long) value;
    }

    @Override
    public float floatValue() {
        return (float) value;
    }

    @Override
    public double doubleValue() {
        return value;
    }
}

