package com.banderasmusic.rest.inventory.events;

import com.banderasmusic.rest.inventory.model.Inventory;
import org.springframework.context.ApplicationEvent;

public class InventoryEvent extends ApplicationEvent {

    private String eventType;
    private Inventory inventory;

    public InventoryEvent(String eventType, Inventory inventory) {
        super(inventory);
        this.eventType = eventType;
        this.inventory = inventory;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
