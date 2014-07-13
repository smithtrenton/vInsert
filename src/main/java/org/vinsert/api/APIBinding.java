package org.vinsert.api;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation pointing to a certain implementation of the API module.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface APIBinding {

    Class<? extends APIModule> value() default APIModule.class;

}
