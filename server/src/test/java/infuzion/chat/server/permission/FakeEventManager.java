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

package infuzion.chat.server.permission;

import infuzion.chat.server.event.Event;
import infuzion.chat.server.event.IEventListener;
import infuzion.chat.server.event.IEventManager;
import infuzion.chat.server.plugin.Plugin;

public class FakeEventManager implements IEventManager {
    @Override
    public void registerEvent(Class<? extends Event> events) {

    }

    @Override
    public void registerListener(IEventListener listener, Plugin plugin) {

    }

    @Override
    public void fireEvent(Event event) {

    }

    @Override
    public void reset() {

    }
}
