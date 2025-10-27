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
package de.leycm.neck.instance;

import lombok.NonNull;

/**
 * Interface for classes that can be registered in the {@link InitializableRegistry}.
 * Provides methods for singleton-like instance management and lifecycle hooks.
 * <p>
 * Implementing classes can be registered, retrieved, and unregistered using the static methods.
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 * @see InitializableRegistry
 */
public interface Initializable {

    /**
     * Retrieves the registered instance of the specified class.
     *
     * @param <T>   the type of the instance
     * @param clazz the class of the instance to retrieve
     * @return the registered instance
     * @throws NullPointerException if no instance is registered
     * @throws ClassCastException   if the registered instance is not assignable to the class
     * @see InitializableRegistry#getInstance(Class)
     */
    static <T extends Initializable> @NonNull T getInstance(final @NonNull Class<T> clazz) {
        return InitializableRegistry.getInstance(clazz);
    }

    /**
     * Checks whether an instance is registered for the specified class.
     *
     * @param clazz the class to check for a registered instance
     * @return {@code true} if an instance is registered for the class, {@code false} otherwise
     * @see InitializableRegistry#hasInstance(Class)
     */
    static boolean hasInstance(final @NonNull Class<?> clazz) {
        return InitializableRegistry.hasInstance(clazz);
    }

    /**
     * Registers an instance of {@link Initializable} for the specified class.
     *
     * @param <T>      the type of the instance
     * @param instance the instance to register
     * @param clazz    the class to associate with the instance
     * @throws RuntimeException if an instance is already registered
     * @see InitializableRegistry#register(Initializable, Class)
     */
    static <T extends Initializable> void register(final @NonNull T instance,
                                                   final @NonNull Class<T> clazz) {
        InitializableRegistry.register(instance, clazz);
    }

    /**
     * Unregisters the instance associated with the specified class.
     *
     * @param <T>   the type of the instance
     * @param clazz the class whose instance should be unregistered
     * @throws RuntimeException if no instance is registered
     * @see InitializableRegistry#unregister(Class)
     */
    static <T extends Initializable> void unregister(final @NonNull Class<T> clazz) {
        InitializableRegistry.unregister(clazz);
    }

    /**
     * Lifecycle hook called when the instance is installed in the registry.
     * <p>
     * Default implementation does nothing.
     * </p>
     */
    default void onInstall() {}

    /**
     * Lifecycle hook called when the instance is uninstalled from the registry.
     * <p>
     * Default implementation does nothing.
     * </p>
     */
    default void onUninstall() {}
}
