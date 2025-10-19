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
import lombok.NonNull;
import java.util.UUID;

/**
 * Identifier implementation for UUID-based identifiers.
 *
 * @author LeyCM
 * @since 1.0.1
 */
public record UUIDIdentifier(UUID value) implements Identifier<UUID> {

    public UUIDIdentifier(final @NonNull UUID value) {
        this.value = value;
    }

    @Override
    public UUID original() {
        return value;
    }

    @Override
    public int compareTo(final @NonNull UUID other) {
        return value.compareTo(other);
    }

    @Override
    @NonNull
    public String toString() {
        return "UUIDIdentifier[" + value + "]";
    }
}
