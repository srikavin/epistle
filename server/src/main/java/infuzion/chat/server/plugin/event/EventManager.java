/*
 *  Copyright 2016 Infuzion
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package infuzion.chat.server.plugin.event;

import infuzion.chat.server.Server;
import infuzion.chat.server.plugin.Plugin;
import infuzion.chat.server.plugin.event.chat.ChatEvent;
import infuzion.chat.server.plugin.event.chat.MessageEvent;
import infuzion.chat.server.plugin.event.command.CommandEvent;
import infuzion.chat.server.plugin.event.command.PreCommandEvent;
import infuzion.chat.server.plugin.event.reflection.EventHandler;
import infuzion.chat.server.plugin.event.reflection.EventPriority;
import infuzion.chat.server.plugin.event.reflection.Listener;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class EventManager implements IEventManager {
    private Server server;
    private List<Class<? extends Event>> eventTypes = new ArrayList<>();

    public EventManager(Server server) {
        this.server = server;
        registerEvents(Event.class);
        registerEvents(ChatEvent.class);
        registerEvents(MessageEvent.class);
        registerEvents(CommandEvent.class);
        registerEvents(PreCommandEvent.class);
    }

    public void registerEvents(Class event) {
        if (Event.class.isAssignableFrom(event) && !eventTypes.contains(event)) {
            eventTypes.add(event);
        }
    }

    public void registerEvents(IEventListener listener, Plugin plugin) {
        Method[] methods = listener.getClass().getMethods();
        for (Method method : methods) {
            Annotation annotation = method.getAnnotation(EventHandler.class);
            if (annotation == null) {
                continue;
            }
            EventPriority priority = ((EventHandler) annotation).priority();
            if (method.getParameterCount() != 1) {
                continue;
            }
            Class listenerMethodClass = method.getParameterTypes()[0];

            if (Event.class.isAssignableFrom(listenerMethodClass)) {
                eventTypes.stream().filter(eventType -> eventType.isAssignableFrom(listenerMethodClass)).forEach(eventType ->
                        Event.getAllHandlers().add(new Listener(plugin, priority, eventType, method, listener)));
            }
        }

    }

    public void fireEvent(Event event) {
        try {
            fireEvent(event, EventPriority.LOW);
            fireEvent(event, EventPriority.NORMAL);
            fireEvent(event, EventPriority.HIGH);
            fireEvent(event, EventPriority.VERY_HIGH);
            fireEvent(event, EventPriority.MONITOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fireEvent(Event event, EventPriority priority) throws Exception {
        for (Listener e : Event.getAllHandlers()) {
            if (e.getEvent().equals(event.getClass()) && e.getPriority().equals(priority)) {
                e.getListenerMethod().invoke(e.getEventListener(), event);
            }
        }
    }

    public void reset() {
        Event.removeAllHandlers();
    }
}
