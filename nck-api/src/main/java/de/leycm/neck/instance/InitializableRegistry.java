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
import java.util.HashMap;
import java.util.Map;

/**
 * Registry for managing singleton-like instances of {@link Initializable} objects.
 * <p>
 * This class allows registering, retrieving, and unregistering instances
 * that implement the {@link Initializable} interface.
 * </p>
 *
 * @author LeyCM
 * @since 1.0.1
 * @see Initializable
 */
public class InitializableRegistry {

    /**
     * Internal map storing the registered instances.
     */
    private static final Map<Class<?>, Initializable> REGISTRY = new HashMap<>();

    /**
     * Retrieves the registered instance for the specified class.
     *
     * @param <T>   the type of the instance
     * @param clazz the class of the instance to retrieve
     * @return the registered instance
     * @throws NullPointerException if no instance is registered for the class
     * @throws ClassCastException   if the registered instance is not assignable to the class
     */
    @SuppressWarnings("unchecked")
    protected static <T extends Initializable> @NonNull T getInstance(final @NonNull Class<T> clazz) {
        Initializable instance = REGISTRY.get(clazz);

        if (instance == null)
            throw new NullPointerException("No instance registered for " + clazz.getSimpleName());

        if (!clazz.isInstance(instance))
            throw new ClassCastException("Registered instance is not of type " + clazz.getSimpleName());

        return (T) instance;
    }

    /**
     * Registers an instance of {@link Initializable} for the specified class.
     * Calls {@link Initializable#onInstall()} on the instance before storing it.
     *
     * @param <T>      the type of the instance
     * @param instance the instance to register
     * @param clazz    the class the instance should be associated with
     * @throws RuntimeException if an instance is already registered for the class
     */
    protected static <T extends Initializable> void register(final @NonNull T instance,
                                                             final @NonNull Class<T> clazz) {
        if (REGISTRY.containsKey(clazz))
            throw new RuntimeException("An instance of " + clazz.getSimpleName() + " is already registered");

        instance.onInstall();
        REGISTRY.put(clazz, instance);
    }

    /**
     * Unregisters the instance associated with the specified class.
     * Calls {@link Initializable#onUninstall()} on the instance before removal.
     *
     * @param <T>   the type of the instance
     * @param clazz the class whose instance should be unregistered
     * @throws RuntimeException if no instance is registered for the class
     */
    protected static <T extends Initializable> void unregister(final @NonNull Class<T> clazz) {
        if (!REGISTRY.containsKey(clazz))
            throw new RuntimeException("There is no instance of " + clazz.getSimpleName());

        REGISTRY.get(clazz).onUninstall();
        REGISTRY.remove(clazz);
    }
}
