package org.vinsert.api.debug.impl;

import org.vinsert.api.debug.AbstractDebug;
import org.vinsert.api.event.EventHandler;
import org.vinsert.api.event.PaintEvent;
import org.vinsert.api.wrappers.Widget;
import org.vinsert.core.Session;

import java.awt.*;

/**
 * Date: 15/05/2014
 * Time: 09:46
 *
 * @author Matt Collinge
 */
public final class WidgetDebug extends AbstractDebug {

    public static final Color HIGHLIGHT_COLOR = new Color(135, 135, 135, 135);
    public static final Color HIGHLIGHT_BORDER_COLOR = Color.RED.darker();

    private Widget selectedWidget = null;
    private Rectangle widgetArea = null;

    public WidgetDebug(Session session) {
        super(session);
    }

    @Override
    public String getName() {
        return "Widget Debug";
    }

    @Override
    public String getShortcode() {
        return "widgets";
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            selectedWidget = null;
        }
    }

    public void setSelectedWidget(Widget widget) {
        if (widget == null) {
            return;
        }
        selectedWidget = widget;
        widgetArea = selectedWidget.getArea();
    }

    @EventHandler
    public void onPaint(PaintEvent event) {
        if (selectedWidget != null && widgetArea != null) {
            Graphics g = event.getGraphics();
            g.setColor(HIGHLIGHT_COLOR);
            g.fillRect(widgetArea.x, widgetArea.y, widgetArea.width, widgetArea.height);
            g.setColor(HIGHLIGHT_BORDER_COLOR);
            g.drawRect(widgetArea.x, widgetArea.y, widgetArea.width, widgetArea.height);
        }
    }

}
