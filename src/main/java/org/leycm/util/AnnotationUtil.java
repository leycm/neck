package org.leycm.util;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * Utility class for working with annotations on static fields.
 */
public class AnnotationUtil {

    /**
     * Get the annotation of a static field that is currently uninitialized (null).
     * This method inspects the call stack to find the class where it was called from,
     * then checks all static fields in that class to see if any are null and have the specified annotation.
     *
     * @param annotationClass The class of the annotation to look for.
     * @param <A>             The type of the annotation.
     * @return The annotation instance if found, otherwise null.
     */
    public static <A extends Annotation> @Nullable A getAnnoFromField(Class<A> annotationClass) {
        try {
            StackTraceElement creation = Thread.currentThread().getStackTrace()[2];
            Class<?> clazz = Class.forName(creation.getClassName());

            for (Field f : clazz.getDeclaredFields()) {
                f.setAccessible(true);
                if (f.isAnnotationPresent(annotationClass)) {
                    return f.getAnnotation(annotationClass);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * Check if there is a static field with the specified annotation that is currently uninitialized (null).
     *
     * @param annotationClass The class of the annotation to look for.
     * @return True if such an annotation is found, otherwise false.
     */
    public static boolean hasFieldAnno(Class<? extends Annotation> annotationClass) {
        return getAnnoFromField(annotationClass) != null;
    }
}

