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

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

public class HandlerListTest {
    @Before
    public void resetStatic() throws Exception {
        Field field = HandlerList.class.getDeclaredField("allListeners");
        field.setAccessible(true);
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(null, new ArrayList<Listener>());
    }

    @Test
    public void getHandlersAndRegister() throws Exception {
        HandlerList handlerList = new HandlerList();
        assertEquals("Should be empty", 0, handlerList.getHandlers().size());
        handlerList.register(new Listener(null, null, null, null, null));
        assertEquals("Should be one", 1, handlerList.getHandlers().size());
        handlerList.register(new Listener(null, null, null, null, null));
        handlerList.register(new Listener(null, null, null, null, null));
        handlerList.register(new Listener(null, null, null, null, null));
        handlerList.register(new Listener(null, null, null, null, null));
        assertEquals("Should be five", 5, handlerList.getHandlers().size());
    }
}