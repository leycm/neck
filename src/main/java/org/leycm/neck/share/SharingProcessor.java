package org.leycm.neck.share;

import org.jetbrains.annotations.NotNull;
import org.leycm.neck.share.sharing.GlobalisedSharing;
import org.leycm.neck.share.sharing.IdentifiedSharing;
import org.leycm.neck.share.shared.SharedValue;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Component
public class SharingProcessor {

    private final Map<String, Object> globalStorage = new HashMap<>();
    private final Map<String, Object> identifiedStorage = new HashMap<>();

    public void processSharedFields(@NotNull Object instance) {
        Class<?> clazz = instance.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);

            if (field.isAnnotationPresent(GlobalisedSharing.class)) {
                processGlobalisedField(instance, field);
            } else if (field.isAnnotationPresent(IdentifiedSharing.class)) {
                processIdentifiedField(instance, field);
            }
        }
    }

    private void processGlobalisedField(Object instance, @NotNull Field field) {
        GlobalisedSharing annotation = field.getAnnotation(GlobalisedSharing.class);
        String storageKey = annotation.scope() + ":" + annotation.key();

        try {
            Object fieldValue = field.get(instance);
            if (fieldValue instanceof SharedValue) {
                SharedValue<?> sharedValue = (SharedValue<?>) fieldValue;

                if (globalStorage.containsKey(storageKey)) {
                    Object storedValue = globalStorage.get(storageKey);
                    //noinspection unchecked
                    ((SharedValue<Object>) sharedValue).set(storedValue);
                }

                if (sharedValue.isPresent()) {
                    globalStorage.put(storageKey, sharedValue.get().orElse(null));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to process globalised field: " + field.getName(), e);
        }
    }

    private void processIdentifiedField(Object instance, @NotNull Field field) {
        IdentifiedSharing annotation = field.getAnnotation(IdentifiedSharing.class);
        String storageKey = annotation.scope() + ":" + annotation.group() + ":" + annotation.id() + ":" + annotation.key();

        try {
            Object fieldValue = field.get(instance);
            if (fieldValue instanceof SharedValue<?> sharedValue) {

                if (identifiedStorage.containsKey(storageKey)) {
                    Object storedValue = identifiedStorage.get(storageKey);
                    //noinspection unchecked
                    ((SharedValue<Object>) sharedValue).set(storedValue);
                }

                if (sharedValue.isPresent()) {
                    identifiedStorage.put(storageKey, sharedValue.get().orElse(null));
                }
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed to process identified field: " + field.getName(), e);
        }
    }

    public void storeGlobalValue(String scope, String key, Object value) {
        globalStorage.put(scope + ":" + key, value);
    }

    public Object getGlobalValue(String scope, String key) {
        return globalStorage.get(scope + ":" + key);
    }

    public void storeIdentifiedValue(String scope, String group, String id, String key, Object value) {
        identifiedStorage.put(scope + ":" + group + ":" + id + ":" + key, value);
    }

    public Object getIdentifiedValue(String scope, String group, String id, String key) {
        return identifiedStorage.get(scope + ":" + group + ":" + id + ":" + key);
    }
}
