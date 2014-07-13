package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.core.Session;

/**
 *
 */
public final class AnimationDebug extends AbstractDebug {
    public AnimationDebug(Session session) {
        super(session);
    }

    @Override
    public String getName() {
        return "Animation";
    }

    @Override
    public String getShortcode() {
        return "animation";
    }
}
