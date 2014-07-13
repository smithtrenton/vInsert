package org.vinsert.api.collection.queries;

import org.vinsert.api.MethodContext;
import org.vinsert.api.collection.Filter;
import org.vinsert.api.collection.StatePredicate;
import org.vinsert.api.collection.WaitingPolicy;
import org.vinsert.api.wrappers.Widget;

import java.awt.*;
import java.util.List;

import static org.vinsert.api.util.Utilities.sleepUntil;

/**
 * A widget finder implemented with a query-like pattern.
 */
public final class WidgetQuery extends Query<Widget> {
    private final MethodContext ctx;
    private Rectangle lastResult;

    public WidgetQuery(MethodContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public Widget single() {
        List<Widget> widgets = asList();
        if (!widgets.isEmpty()) {
            Widget widget = widgets.get(0);
            lastResult = widget.getArea();
            return widget;
        }
        return null;
    }

    public Widget waitFor(WaitingPolicy policy, int timeOut) {
        if (lastResult != null) {
            switch (policy) {
                case HOVER:
                    ctx.mouse.move((int) lastResult.getCenterX(), (int) lastResult.getCenterY());
                    break;

                default:
                    break;
            }
        }
        sleepUntil(new StatePredicate() {
            @Override
            public boolean apply() {
                return exists();
            }
        }, timeOut);
        return single();
    }

    @Override
    public List<Widget> asList() {
        return filterSet(orderSet(ctx.widgets.getAll()));
    }

    /**
     * Selects widgets matching the specified ID
     *
     * @param group     group number
     * @param component component number within group
     * @return query
     */
    public WidgetQuery id(final int group, final int component) {
        addCondition(new Filter<Widget>() {
            @Override
            public boolean accept(Widget acceptable) {
                return acceptable.getGroup() == group &&
                        acceptable.getId() == component;
            }
        });
        return this;
    }

    /**
     * Selects widgets matching the specified group ID
     *
     * @param group group number
     * @return query
     */
    public WidgetQuery group(final int group) {
        addCondition(new Filter<Widget>() {
            @Override
            public boolean accept(Widget acceptable) {
                return acceptable.getGroup() == group;
            }
        });
        return this;
    }

    /**
     * Selects widgets containing the specified text
     *
     * @param text text to look for
     * @return query
     */
    public WidgetQuery text(final String text) {
        addCondition(new Filter<Widget>() {
            @Override
            public boolean accept(Widget acceptable) {
                return acceptable.getText().toLowerCase().contains(text.toLowerCase());
            }
        });
        return this;
    }

    /**
     * Only selects widgets that are visible
     *
     * @return query
     */
    public WidgetQuery visible() {
        addCondition(new Filter<Widget>() {
            @Override
            public boolean accept(Widget acceptable) {
                return acceptable.isValid();
            }
        });
        return this;
    }

    public WidgetQuery storedAs(String name) {
        Query query = retrieveFromStorage(name);
        if (query instanceof WidgetQuery) {
            return (WidgetQuery) query;
        } else {
            throw new IllegalArgumentException("Query '" + name + "' is not of type " + getClass().getSimpleName());
        }
    }
}
