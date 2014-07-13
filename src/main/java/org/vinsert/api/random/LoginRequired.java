package org.vinsert.api.random;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * An annotation for randoms that require login to execute
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginRequired {
}
