package org.vinsert.api.script.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Annotation containing information about the script and it's author.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ScriptManifest {

    String name();

    String author();

    String description();

    String category();

    String forumLink();

    double version();

}
