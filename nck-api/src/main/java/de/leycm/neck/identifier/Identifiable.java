package de.leycm.neck.identifier;


import lombok.NonNull;

/**
 * Represents an object that can be uniquely identified by an {@link Identifier}.
 * This interface provides a standardized way to handle object identification and comparison.
 * Implementations can use various identifier types like integers, strings, UUIDs, or custom types.
 *
 * @param <Ob> the type of the original object being identified
 * @param <I>  the type of identifier used, must extend {@link Identifier}
 *
 * @author LeyCM
 * @since 1.0.1
 * @see Identifier
 */
public interface Identifiable<Ob, I extends Identifier<Ob>> {

    /**
     * Returns the unique identifier for this object.
     * This is the primary method for obtaining the object's identifier.
     *
     * @return the non-null identifier instance associated with this object
     */
    I identifier();

    /**
     * Returns the unique identifier for this object.
     * This is a convenience method that delegates to {@link #identifier()}.
     *
     * @return the non-null identifier instance associated with this object
     *
     * @see #identifier()
     */
    default I getIdentifier() {
        return identifier();
    }

    /**
     * Returns the unique identifier for this object.
     * This is a convenience method that delegates to {@link #identifier()}.
     *
     * @return the non-null identifier instance associated with this object
     *
     * @see #identifier()
     */
    default I getId() {
        return identifier();
    }

    /**
     * Returns the original object that this identifiable represents.
     * This typically returns the underlying value that the identifier wraps.
     *
     * @return the original object, may be null depending on the implementation
     *
     * @see Identifier#original()
     */
    default Ob original() {
        return identifier().original();
    }

    /**
     * Returns the original object that this identifiable represents.
     * This is a convenience method that delegates to {@link #original()}.
     *
     * @return the original object, may be null depending on the implementation
     *
     * @see #original()
     */
    default Ob getOriginal() {
        return identifier().original();
    }

    /**
     * Determines whether this identifiable object represents the same entity
     * as the given identifiable object by comparing their identifiers.
     *
     * @param <T> the type of the identifiable object to compare with
     * @param identifiable the non-null identifiable object to compare with
     * @return {@code true} if both objects have equal identifiers,
     *         {@code false} otherwise
     * @throws NullPointerException if the provided identifiable is null
     *
     * @see Object#equals(Object)
     */
    default <T extends Identifiable<Ob, I>> boolean identify(final @NonNull T identifiable) {
        return identifier().equals(identifiable.identifier());
    }

    /**
     * Determines whether this identifiable object is identified by the given identifier.
     *
     * @param identifier the non-null identifier to compare with
     * @return {@code true} if this object's identifier equals the provided identifier,
     *         {@code false} otherwise
     * @throws NullPointerException if the provided identifier is null
     *
     * @see Object#equals(Object)
     */
    default boolean identify(final @NonNull I identifier) {
        return identifier().equals(identifier);
    }

    /**
     * Determines whether this identifiable object represents the given original object.
     *
     * @param object the non-null original object to compare with
     * @return {@code true} if this object's identifier compares equal to the original object,
     *         {@code false} otherwise
     * @throws NullPointerException if the provided object is null
     *
     * @see Comparable#compareTo(Object)
     */
    default boolean identify(final @NonNull Ob object) {
        return identifier().compareTo(object) == 0;
    }
}
