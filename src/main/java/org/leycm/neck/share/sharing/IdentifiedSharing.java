package org.leycm.neck.share.sharing;


import org.leycm.neck.share.config.SharingDefaults;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field that is uniquely identified within a specific group.
 * <p>
 * This annotation can be used to store group-specific values and distinguish them
 * by key, ID, and scope.
 * </p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdentifiedSharing {

    /**
     * The group to which the field belongs.
     *
     * @return the name of the group, default is {@link SharingDefaults#DEFAULT_GROUP}
     */
    String group() default SharingDefaults.DEFAULT_GROUP;

    /**
     * The unique ID for this field.
     *
     * @return the field ID
     */
    String id();

    /**
     * The scope of the shared data.
     * <p>
     * The same key and ID can have different values in different subprojects.
     * </p>
     *
     * @return the scope, default is {@link SharingDefaults#DEFAULT_SCOPE}
     */
    String scope() default SharingDefaults.DEFAULT_SCOPE;

    /**
     * The key used to sort or identify the value.
     *
     * @return the unique key
     */
    String key();
}
