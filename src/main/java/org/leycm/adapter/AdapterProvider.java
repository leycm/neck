package org.leycm.adapter;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Central registry for object adapters.
 */
public final class AdapterProvider {

    private static final Map<Key<?, ?>, ObjectAdapter<?, ?>> adapters = new ConcurrentHashMap<>();

    private AdapterProvider() {
    }

    /**
     * Registers an adapter for a pair of classes.
     */
    public static <T, F> void register(@NotNull ObjectAdapter<T, F> adapter) {
        adapters.put(Key.of(adapter.getTo(), adapter.getFrom()), adapter);
    }

    /**
     * Finds an adapter for the given classes.
     */
    @SuppressWarnings("unchecked")
    public static <T, F> @NotNull Optional<ObjectAdapter<T, F>> find(@NotNull Class<T> to, @NotNull Class<F> from) {
        return Optional.ofNullable((ObjectAdapter<T, F>) adapters.get(Key.of(to, from)));
    }

    /**
     * Converts an object if an adapter is found.
     */
    public static <T, F> F convert(@NotNull T input, @NotNull Class<F> targetType) {
        @SuppressWarnings("unchecked")
        Class<T> sourceClass = (Class<T>) input.getClass();

        return find(sourceClass, targetType)
                .map(adapter -> adapter.to(input))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No adapter registered for " + sourceClass.getName() + " -> " + targetType.getName()
                ));
    }

    /**
     * Internal key for adapter lookup.
     */
    private record Key<T, F>(Class<T> to, Class<F> from) {
        @Contract("_, _ -> new")
        static <T, F> @NotNull Key<T, F> of(Class<T> to, Class<F> from) {
            return new Key<>(to, from);
        }
    }
}
