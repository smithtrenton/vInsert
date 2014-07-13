package org.vinsert.api.random;


import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RandomManifest {

    String name();

    String author() default "Team Aurora";

    double version();
}

