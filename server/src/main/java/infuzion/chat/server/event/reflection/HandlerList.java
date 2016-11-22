/*
 *
 *  *  Copyright 2016 Infuzion
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package infuzion.chat.server.event.reflection;

import java.util.ArrayList;
import java.util.List;

public class HandlerList {
    private static List<Listener> allListeners = new ArrayList<>();
    private volatile List<Listener> handlers = new ArrayList<>();

    public List<Listener> getHandlers() {
        return handlers;
    }

    public void register(Listener listener) {
        handlers.add(listener);
    }

    public List<Listener> getAllListeners() {
        return allListeners;
    }

    public void reset() {
        allListeners = new ArrayList<>();
        handlers = new ArrayList<>();
    }
}
