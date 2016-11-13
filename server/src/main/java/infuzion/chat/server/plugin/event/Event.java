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

import infuzion.chat.server.plugin.event.reflection.HandlerList;
import infuzion.chat.server.plugin.event.reflection.Listener;

import java.util.List;

public abstract class Event {
    private String name;

    public Event() {
    }

    public static List<Listener> getAllHandlers() {
        return new HandlerList().getAllListeners();
    }

    public static void removeAllHandlers() {
        new HandlerList().reset();
    }

//    abstract HandlerList getHandlers();

    public String getName() {
        if (this.name == null) {
            return getClass().getSimpleName();
        }
        return name;
    }
}
