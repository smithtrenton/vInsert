package org.vinsert.api.action.impl;

import org.vinsert.api.action.Step;
import org.vinsert.api.random.Validated;
import org.vinsert.api.script.exception.ExecutionException;

/**
 * Await is a step that waits for an expression to evaluate to
 * true (or a timeout to be reached) before continuing execution.
 */
@Validated
public final class Await implements Step {
    private final Expression expression;
    private final int timeout;
    private final Step next;

    Await(Expression expression, int timeout, Step next) {
        this.expression = expression;
        this.timeout = timeout;
        this.next = next;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Step execute() {
        final long startMarker = System.currentTimeMillis();
        while ((System.currentTimeMillis() - startMarker) < timeout) {
            if (expression.evaluate()) {
                return next;
            }

            try {
                Thread.sleep(60);
            } catch (InterruptedException e) {
                throw new ExecutionException("Oops!", e);
            }
        }
        return null;
    }

    /**
     * Creates a new Await step
     * Note: Default timeout of 10 seconds used.
     *
     * @param expression Expression to wait for
     * @param next       Next node
     * @return await
     */
    public static Await get(Expression expression, Step next) {
        return get(expression, 10000, next);
    }

    /**
     * Creates a new Await step
     *
     * @param expression Expression to wait for
     * @param timeout    A timeout value in milliseconds
     * @param next       Next node
     * @return await
     */
    public static Await get(Expression expression, int timeout, Step next) {
        return new Await(expression, timeout, next);
    }

    /**
     * An expression that shouldn't return true until
     * the thing we're awaiting has succeeded, or a timeout has been reached.
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
