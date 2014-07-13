package org.vinsert.core.model;

/**
 * @author const_
 */
public enum BreakConditionType {

    SKILL, RUNTIME, TIME;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
