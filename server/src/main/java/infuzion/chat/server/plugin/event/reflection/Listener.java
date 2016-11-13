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

package infuzion.chat.server.plugin.event.reflection;

import infuzion.chat.server.plugin.Plugin;
import infuzion.chat.server.plugin.event.Event;
import infuzion.chat.server.plugin.event.IEventListener;

import java.lang.reflect.Method;

public class Listener {
    private final Plugin plugin;
    private final EventPriority priority;
    private final Class<? extends Event> event;
    private final Method listenerMethod;
    private final IEventListener eventListener;

    public Listener(Plugin plugin, EventPriority priority, Class<? extends Event> event, Method listener, IEventListener eventListener) {
        this.plugin = plugin;
        this.priority = priority;
        this.event = event;
        this.listenerMethod = listener;
        this.eventListener = eventListener;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public EventPriority getPriority() {
        return priority;
    }

    public Method getListenerMethod() {
        return listenerMethod;
    }

    public Class<? extends Event> getEvent() {
        return event;
    }

    public IEventListener getEventListener() {
        return eventListener;
    }
}
