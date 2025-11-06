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
import java.util.UUID;

/**
 * An immutable identifier implementation for UUID (Universally Unique Identifier) values.
 * <p>
 * This record wraps a {@link UUID} value and provides type-safe identification
 * for universally unique identifiers. It implements the {@link Identifier} interface
 * for generic identifier handling and supports comparison based on the natural
 * ordering of UUIDs.
 * </p>
 *
 * <p><b>Usage Example:</b></p>
 * <pre>{@code
 * UUID uuid = UUID.randomUUID();
 * UUIDIdentifier id = new UUIDIdentifier(uuid);
 * UUID original = id.original(); // same as input UUID
 * String text = id.toString();   // "UUIDIdentifier[123e4567-e89b-12d3-a456-426614174000]"
 * }</pre>
 *
 * <p><b>Thread Safety:</b> This record is immutable and thread-safe, as it only
 * contains an immutable UUID value.</p>
 *
 * @param value the UUID value used as identifier
 * @author LeyCM
 * @see Identifier
 * @see UUID
 * @since 1.0.1
 */
public record UUIDIdentifier(UUID value) implements Identifier<UUID> {

    /**
     * Constructs a new {@code UUIDIdentifier} with the specified UUID value.
     *
     * @param value the UUID value to use as identifier (must not be null)
     * @throws NullPointerException if the specified value is null
     */
    public UUIDIdentifier(final @NonNull UUID value) {
        this.value = value;
    }

    /**
     * Returns the original UUID value wrapped by this identifier.
     *
     * @return the original UUID value, guaranteed to be non-null
     */
    @Override
    public UUID original() {
        return value;
    }

    /**
     * Compares this identifier with the specified UUID value.
     * <p>
     * The comparison follows the same rules as {@link UUID#compareTo(UUID)},
     * which compares the two UUIDs by their signed numerical values.
     * </p>
     *
     * @param other the UUID value to compare to (must not be null)
     * @return a negative integer, zero, or a positive integer as this identifier's value
     *         is less than, equal to, or greater than the specified value
     * @throws NullPointerException if the specified value is null
     */
    @Override
    public int compareTo(final @NonNull UUID other) {
        return value.compareTo(other);
    }

    /**
     * Returns a string representation of this identifier.
     * <p>
     * The format is: {@code "uuid:" + value}
     * </p>
     *
     * @return a string representation containing the identifier type and UUID value
     */
    @Override
    @NonNull
    public String toString() {
        return "uuid:" + value;
    }

}