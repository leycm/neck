package org.leycm.neck.share.sharing;

import org.leycm.neck.share.config.SharingDefaults;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field whose value is shared globally.
 * <p>
 * This annotation can be used to distinguish values across different sub-projects
 * under the same key and scope combination.
 * </p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalisedSharing {

    /**
     * The scope of the shared data.
     * <p>
     * The same key and UUID can have different values in different subprojects.
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
