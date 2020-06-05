package com.banderasmusic.rest.inventory.listeners;

import com.banderasmusic.rest.inventory.events.InventoryEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventListener {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Async
    @EventListener
    public void onApplicationEvent(InventoryEvent inventoryEvent) { // with @Async this is an asynchronous method
        System.out.println("*** Execute method asynchronously. ***"
                + Thread.currentThread().getName() + " id: " + Thread.currentThread().getId());
        log.info("Received Inventory Event : " + inventoryEvent.getEventType());
        log.info("Received Inventory From Inventory Event :" + inventoryEvent.getInventory().toString());
    }
}
