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
package de.leycm.neck.identifier;

import lombok.NonNull;

/**
 * A generic identifier for objects that provides type-safe identification
 * and comparison capabilities. Implementations should be immutable and
 * properly implement {@code equals()} and {@code hashCode()}.
 *
 * @param <Ob> the type of the original object being identified
 *
 * @author LeyCM
 * @since 1.0.1
 * @see Comparable
 * @see Identifiable
 */
public interface Identifier<Ob> extends Comparable<Ob> {

    /**
     * Returns the original object that this identifier represents.
     * This is typically the underlying value that the identifier wraps.
     *
     * @return the original object, implementation specific whether null is allowed
     */
    Ob original();

    /**
     * Compares this identifier with another identifier for order based on their
     * original objects. The comparison is delegated to the original objects.
     *
     * @param identifier the non-null identifier to be compared
     * @return a negative integer, zero, or a positive integer as this identifier
     *         is less than, equal to, or greater than the specified identifier
     * @throws NullPointerException if the specified identifier is null
     * @throws ClassCastException if the original objects are not mutually comparable
     *
     * @see #compareTo(Object)
     */
    default int compareTo(final @NonNull Identifier<Ob> identifier) {
        return compareTo(identifier.original());
    }

    /**
     * Compares this identifier with the specified identifier for equality.
     * Two identifiers are considered equal if their original objects compare as equal.
     *
     * @param identifier the identifier to compare with for equality
     * @return {@code true} if this identifier is equal to the specified identifier,
     *         {@code false} otherwise
     *
     * @author LeyCM
     * @since 1.0.1
     * @see Object#equals(Object)
     */
    default boolean equals(final @NonNull Identifier<Ob> identifier) {
        return compareTo(identifier) == 0;
    }
}
