package com.banderasmusic.rest.inventory.controllers;

import com.banderasmusic.rest.inventory.events.InventoryEvent;
import com.banderasmusic.rest.inventory.model.Inventory;
import com.banderasmusic.rest.inventory.service.InventoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@EnableAsync
public class InventoryController extends AbstractController{

    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) { this.inventoryService = inventoryService; }

    @GetMapping("/inventory")
    public Page<Inventory> getInventoryByPage(
            @RequestParam(value="pagenumber", required=true, defaultValue="0") Integer pageNumber,
            @RequestParam(value="pagesize", required=true, defaultValue="20") Integer pageSize) {
        Page<Inventory> pagedInventory = inventoryService.getInventoryByPage(pageNumber, pageSize);
        return pagedInventory;
    }

    @GetMapping("/inventory/{id}")
    public ResponseEntity<Inventory> getInventory(@PathVariable("id") Long itemNumber) {
        printCurrentThread();
        Optional<Inventory> inventory = inventoryService.get(itemNumber);
        InventoryEvent inventoryRetrievedEvent = new InventoryEvent("One Inventory item is retrieved", inventory.get());
        eventPublisher.publishEvent(inventoryRetrievedEvent);
        printCurrentThread();

        return ResponseEntity.ok().body(inventory.get());
    }

    @PostMapping("/inventory")
    public ResponseEntity<?> createInventory(@RequestBody Inventory inventory) {
        inventoryService.save(inventory);
        return ResponseEntity.ok().body(inventory.getItemNumber());
    }

    @PutMapping("/inventory/{id}")
    public ResponseEntity<?> updateInventory(@PathVariable("id") long itemNumber, @RequestBody Inventory inventory) {
        inventoryService.update(itemNumber, inventory);
        return ResponseEntity.ok().body("Inventory item: " + inventory.getItemNumber() + " was update successfully");
    }

    @PutMapping("/inventory/{id}/addtocount/{count}")
    public ResponseEntity<?> addItemsToInventory(@PathVariable("id") long itemNumber,
                                                 @PathVariable("count") int count,
                                                 @RequestBody Inventory inventory) {
        Optional<Inventory> inv = inventoryService.get(itemNumber);
        @Positive int startingCount = inv.get().getStartingCount();
        startingCount+= count;
        inventory.setStartingCount(startingCount);
        inventoryService.update(itemNumber, inventory);

        return ResponseEntity.ok().body("Inventory item: " + inventory.getItemNumber() + " was item count updated successfully");
    }

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable("id") long itemNumber) {
        inventoryService.delete(itemNumber);
        return ResponseEntity.ok().body("Inventory item: " + itemNumber + " was deleted successfully");
    }

    public void printCurrentThread() {
        System.out.println("getInventory method. "
                + Thread.currentThread().getName() + " id: " + Thread.currentThread().getId());
    }
}
