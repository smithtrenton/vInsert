package org.vinsert.api.event;

import org.vinsert.api.event.exeption.HandlerInvocationException;
import org.vinsert.api.script.AbstractScript;
import org.vinsert.core.Environment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.reflect.Modifier.isStatic;

/**
 * A annotation-based event bus with support for asynchronous events.
 */
public final class Events {
    private final CopyOnWriteArrayList<EventHandlerBridge> bridges = new CopyOnWriteArrayList<EventHandlerBridge>();
    private Environment environment;

    public Events(Environment environment) {
        this.environment = environment;
    }

    /**
     * Register methods of an object that have the EventHandler annotation
     *
     * @param object object to be registered.
     * @param script <t>true if the object is a script/from a script</t> otherwise false
     */
    public void register(Object object, boolean script) {
        Class<?> clazz = object.getClass();
        for (Method method : clazz.getDeclaredMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation != null) {
                EventHandlerBridge bridge = new EventHandlerBridge(object, method, script, annotation.async());
                if (bridge.validate()) {
                    bridges.add(bridge);
                } else {
                    throw new IllegalArgumentException("Invalid EventHandler: " + method.getName());
                }
            }
        }
    }

    /**
     * Submit an object to be handled by the appropriate EventHandler.
     *
     * @param object object to be handled.
     */
    public void submit(Object object) {
        if (object instanceof MessageEvent) {
            if (object.toString().equals("SERVER:Welcome to RuneScape.:null:0")) {
                submit(new LoginEvent(System.currentTimeMillis()));
            }
            if (environment != null && environment.getSession() != null &&
                    environment.getSession().getClient() != null &&
                    environment.getSession().getClient().getLocalPlayer() != null &&
                    environment.getSession().getClient().getLocalPlayer().getName() != null &&
                    ((MessageEvent) object).getMessage().toLowerCase().contains(
                            environment.getSession().getClient().getLocalPlayer()
                                    .getName().toLowerCase()
                    )) {
                submit(new PlayerMentionEvent((MessageEvent) object));
            }
        }
        for (EventHandlerBridge bridge : bridges) {
            if (bridge.accept(object.getClass())) {
                bridge.dispatch(object);
            }
        }
    }

    /**
     * Deregister any EventHandlers of the object.
     *
     * @param object object to remove the listeners of.
     */
    public void deregister(Object object) {
        List<EventHandlerBridge> deprecated = new ArrayList<EventHandlerBridge>();
        for (EventHandlerBridge bridge : bridges) {
            if (object instanceof AbstractScript &&
                    bridge.isScript() || bridge.method.getDeclaringClass().equals(object.getClass())) {
                deprecated.add(bridge);
            }
        }
        bridges.removeAll(deprecated);
    }

    /**
     * Internal wrapper class to help map EventHandler methods to the correct event.
     */
    private static final class EventHandlerBridge {
        private Object object;
        private Method method;
        private boolean script;
        private boolean async;

        public EventHandlerBridge(Object object, Method method, boolean script, boolean async) {
            this.object = object;
            this.method = method;
            this.script = script;
            this.async = async;
        }

        /**
         * Checks if the EventHandler can handle the class type.
         *
         * @param eventType class type to check.
         * @return true if the class type can be handled, else false.
         */
        public boolean accept(Class<?> eventType) {
            Class<?> argType = method.getParameterTypes()[0];
            return argType.equals(eventType);
        }

        /**
         * Checks if the method is a valid EventHandler
         *
         * @return true if it is a valid EventHandler else false.
         */
        public boolean validate() {
            return method.getParameterTypes().length == 1 && !isStatic(method.getModifiers());
        }

        public boolean isScript() {
            return script;
        }

        /**
         * Dispatches the event object to the wrapped event handler.
         * Depending on the async flag it will be done in a new thread or on
         * the thread of the callee.
         *
         * @param arg event arg
         */
        public void dispatch(final Object arg) {
            if (async) {
                Thread dispatchThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        handle(arg);
                    }
                });
                dispatchThread.setDaemon(true);
                dispatchThread.start();
            } else {
                handle(arg);
            }
        }

        /**
         * Calls the EventHandler method and passes the object as the argument.
         *
         * @param arg object to pass into the method.
         */
        private void handle(Object arg) {
            try {
                method.invoke(this.object, arg);
            } catch (Exception e) {
                e.printStackTrace();
                throw new HandlerInvocationException("Handler invocation failed.", e);
            }
        }

        @Override
        public String toString() {
            return "Object: " + object.getClass() + " Method: " + method.toString();
        }
    }
}