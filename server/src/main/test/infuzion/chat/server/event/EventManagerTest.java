package infuzion.chat.server.event;

import infuzion.chat.server.event.reflection.EventHandler;
import infuzion.chat.server.event.reflection.EventPriority;
import infuzion.chat.server.permission.FakeServer;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Sri on 11/22/2016.
 */
public class EventManagerTest implements IEventListener {
    private boolean[] orderCalled = {false, false, false, false, false};

    @Test
    public void registerEvents() throws Exception {
        orderCalled = new boolean[]{false, false, false, false, false};
        IEventManager eventManager = new EventManager(new FakeServer());
        eventManager.registerEvent(FakeEvent.class);
        eventManager.registerListener(this, null);
        eventManager.fireEvent(new FakeEvent());
        eventManager.reset();
        assertTrue(orderCalled[0]);
    }

    @Test
    public void reset() throws Exception {
        IEventManager eventManager = new EventManager(new FakeServer());
        eventManager.registerEvent(FakeEvent.class);
        eventManager.registerListener(this, null);
        eventManager.reset();
        eventManager.fireEvent(new FakeEvent());
        eventManager.reset();
    }

    @Test
    public void fireEventOrder() throws Exception {
        IEventManager eventManager = new EventManager(new FakeServer());
        eventManager.registerEvent(FakeEvent.class);
        eventManager.registerListener(this, null);
        eventManager.fireEvent(new FakeEvent());
        eventManager.reset();
    }

    @Test
    public void checkAllTestsFired() throws Exception {
        IEventManager eventManager = new EventManager(new FakeServer());
        eventManager.registerEvent(FakeEvent.class);
        eventManager.registerListener(this, null);
        eventManager.fireEvent(new FakeEvent());
        for (boolean b : orderCalled) {
            assertTrue(b);
        }
        eventManager.reset();
    }

    @EventHandler // Defaults to normal
    public void onFakeEvent(FakeEvent event) {
        assertTrue(orderCalled[0]);
        assertFalse(orderCalled[1]);
        assertFalse(orderCalled[2]);
        assertFalse(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[1] = true;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onFakeEventLow(FakeEvent event) {
        assertFalse(orderCalled[0]);
        assertFalse(orderCalled[1]);
        assertFalse(orderCalled[2]);
        assertFalse(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[0] = true;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onFakeEventHigh(FakeEvent event) {
        assertTrue(orderCalled[0]);
        assertTrue(orderCalled[1]);
        assertFalse(orderCalled[2]);
        assertFalse(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[2] = true;
    }

    @EventHandler(priority = EventPriority.VERY_HIGH)
    public void onFakeEventVeryHigh(FakeEvent event) {
        assertTrue(orderCalled[0]);
        assertTrue(orderCalled[1]);
        assertTrue(orderCalled[2]);
        assertFalse(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[3] = true;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFakeEventMonitor(FakeEvent event) {
        assertTrue(orderCalled[0]);
        assertTrue(orderCalled[1]);
        assertTrue(orderCalled[2]);
        assertTrue(orderCalled[3]);
        assertFalse(orderCalled[4]);
        orderCalled[4] = true;
    }


}