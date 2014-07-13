package org.vinsert.api.impl.game;

import com.google.inject.Inject;
import org.vinsert.api.MethodContext;
import org.vinsert.api.Widgets;
import org.vinsert.api.collection.queries.WidgetQuery;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.api.wrappers.WidgetGroup;
import org.vinsert.api.wrappers.interaction.Result;
import org.vinsert.game.engine.media.IWidget;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * An API implementation for obtaining and interacting with widgets.
 *
 * @see org.vinsert.api.Widgets
 */
public final class WidgetsImpl implements Widgets {
    private final MethodContext ctx;
    private final WidgetQuery continueQuery;

    @Inject
    public WidgetsImpl(MethodContext ctx) {
        this.ctx = ctx;
        continueQuery = find().text("Click here to continue");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetGroup getGroup(int parent) {
        if (ctx.client.getWidgets()[parent] != null) {
            return new WidgetGroup(ctx, ctx.client.getWidgets()[parent], parent);
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WidgetGroup> getGroups() {
        IWidget[][] cache = ctx.client.getWidgets();
        List<WidgetGroup> groups = newArrayList();
        if (cache == null) {
            return groups;
        }
        for (int i = 0; i < cache.length; i++) {
            WidgetGroup group = getGroup(i);
            if (group != null) {
                groups.add(group);
            }
        }
        return groups;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Widget> getAll() {
        List<Widget> widgets = newArrayList();
        List<WidgetGroup> groups = getGroups();
        for (WidgetGroup group : groups) {
            for (Widget widget : group.getAll()) {
                if (widget != null) {
                    widgets.add(widget);
                    if (widget.getChildren() != null &&
                            widget.getChildren().length > 0) {
                        for (Widget child : widget.getChildren()) {
                            if (child != null) {
                                widgets.add(child);
                            }
                        }
                    }
                }
            }
        }
        return widgets;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WidgetQuery find() {
        return new WidgetQuery(ctx);
    }

    /**
     * {@inheritDoc}
     *
     * @param parent parent id
     * @param child  child id
     * @return query
     */
    @Override
    public WidgetQuery find(int parent, int child) {
        return find().id(parent, child);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean canContinue() {
        return continueQuery.exists();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Result clickContinue() {
        Widget continueWidget = continueQuery.single();
        if (continueWidget != null) {
            continueWidget.click(true);
            return Result.OK;
        }
        return Result.NOT_ON_SCREEN;
    }
}
