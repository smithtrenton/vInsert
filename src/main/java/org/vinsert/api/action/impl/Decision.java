package org.vinsert.api.action.impl;

import org.vinsert.api.action.Step;

/**
 * A step that evaluates an expression to decide which of the
 * two supplied steps should be executed.
 */
public final class Decision implements Step {
    private final Expression predicate;
    private final Step eq;
    private final Step or;

    Decision(Expression predicate, Step eq, Step or) {
        this.predicate = predicate;
        this.eq = eq;
        this.or = or;
    }

    @Override
    public boolean validate() {
        return true;
    }

    @Override
    public Step execute() {
        return predicate.evaluate() ? eq : or;
    }

    public static Decision get(Expression predicate, Step eq, Step or) {
        return new Decision(predicate, eq, or);
    }

    /**
     * An expression that explains how the decision is made.
     */
    public interface Expression {

        /**
         * Evaluates the expression
         *
         * @return true or false.
         */
        boolean evaluate();

    }

}
