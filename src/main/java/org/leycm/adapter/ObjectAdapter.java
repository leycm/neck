package org.leycm.adapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * A generic adapter interface for converting between two types.
 *
 * @param <T> the source type
 * @param <F> the target type
 */
public interface ObjectAdapter<T, F> {

    /**
     * Converts an object of type {@code T} to type {@code F}.
     *
     * @param to the object to convert
     * @return the converted object of type {@code F}
     */
    F to(T to);

    /**
     * Converts an object of type {@code F} to type {@code T}.
     *
     * @param from the object to convert
     * @return the converted object of type {@code T}
     */
    T from(F from);

    /**
     * Returns the {@link Class} object for the source type {@code T}.
     *
     * @return the class of type {@code T}
     */
    Class<T> getTo();

    /**
     * Returns the {@link Class} object for the target type {@code F}.
     *
     * @return the class of type {@code F}
     */
    Class<F> getFrom();

    /**
     * Creates a new {@code ObjectAdapter} instance using the provided conversion functions and classes.
     *
     * @param toClass  the class of type {@code T}
     * @param fromClass the class of type {@code F}
     * @param toFn     the function to convert from {@code T} to {@code F}
     * @param fromFn   the function to convert from {@code F} to {@code T}
     * @param <T>      the source type
     * @param <F>      the target type
     * @return a new {@code ObjectAdapter} instance
     */
    @Contract(value = "_, _, _, _ -> new", pure = true)
    static <T, F> @NotNull ObjectAdapter<T, F> of(
            Class<T> toClass,
            Class<F> fromClass,
            java.util.function.Function<T, F> toFn,
            java.util.function.Function<F, T> fromFn) {
        return new ObjectAdapter<>() {
            @Override public F to(T to) { return toFn.apply(to); }
            @Override public T from(F from) { return fromFn.apply(from); }
            @Override public Class<T> getTo() { return toClass; }
            @Override public Class<F> getFrom() { return fromClass; }
        };
    }

}