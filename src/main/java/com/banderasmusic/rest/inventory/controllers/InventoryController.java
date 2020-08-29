package com.banderasmusic.rest.inventory.controllers;

import com.banderasmusic.rest.inventory.events.InventoryEvent;
import com.banderasmusic.rest.inventory.model.Inventory;
import com.banderasmusic.rest.inventory.service.InventoryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@EnableAsync
@RequestMapping("/api")
public class InventoryController extends AbstractController{

    private InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) { this.inventoryService = inventoryService; }

    @GetMapping("/inventory")
    @ApiOperation(value = "Finds all Inventory", notes = "Paging is provided", response = Page.class)
    public Page<Inventory> getInventoryByPage(
            @RequestParam(value="pagenumber", required=true, defaultValue="0") Integer pageNumber,
            @RequestParam(value="pagesize", required=true, defaultValue="20") Integer pageSize) {
        Page<Inventory> pagedInventory = inventoryService.getInventoryByPage(pageNumber, pageSize);
        return pagedInventory;
    }

    @GetMapping("/inventory/{id}")
    @ApiOperation(value = "Finds Inventory by ID", notes = "Only one Inventory is returned", response = ResponseEntity.class)
    public ResponseEntity<Inventory> getInventory(@PathVariable("id") Long itemNumber) {
        printCurrentThread();
        Optional<Inventory> inventory = inventoryService.get(itemNumber);
        InventoryEvent inventoryRetrievedEvent = new InventoryEvent("One Inventory item is retrieved", inventory.get());
        eventPublisher.publishEvent(inventoryRetrievedEvent);
        printCurrentThread();

        return ResponseEntity.ok().body(inventory.get());
    }

    @PostMapping("/inventory")
    @ApiOperation(value = "Add new Inventory", notes = "Returns OK 201", response = ResponseEntity.class)
    public ResponseEntity<?> createInventory(@RequestBody Inventory inventory) {
        inventoryService.save(inventory);
        return ResponseEntity.ok().body(inventory.getItemNumber());
    }

    @PutMapping("/inventory/{id}")
    @ApiOperation(value = "Updates Inventory by ID", notes = "Only one Inventory is updated", response = ResponseEntity.class)
    public ResponseEntity<?> updateInventory(@PathVariable("id") long itemNumber, @RequestBody Inventory inventory) {
        printCurrentThread();
        checkResourceFound(inventoryService.get(itemNumber));
        inventoryService.update(itemNumber, inventory);
        InventoryEvent inventoryRetrievedEvent = new InventoryEvent("One Inventory item was updated", inventory);
        eventPublisher.publishEvent(inventoryRetrievedEvent);
        printCurrentThread();

        return ResponseEntity.ok().body("Inventory item: " + inventory.getItemNumber() + " was updated successfully");
    }

    @PutMapping("/inventory/{id}/addtocount/{count}")
    @ApiOperation(value = "Update item count of Inventory by ID", notes = "Add to items counts", response = ResponseEntity.class)
    public ResponseEntity<?> addItemsToInventory(@PathVariable("id") long itemNumber,
                                                 @PathVariable("count") int count,
                                                 @RequestBody Inventory inventory) {
        checkResourceFound(inventoryService.get(itemNumber));
        Optional<Inventory> inv = inventoryService.get(itemNumber);
        @Positive int startingCount = inv.get().getStartingCount();
        startingCount+= count;
        inventory.setStartingCount(startingCount);
        inventoryService.update(itemNumber, inventory);

        return ResponseEntity.ok().body("Inventory item: " + inventory.getItemNumber() + " item count updated successfully");
    }

    @PutMapping("/inventory/{id}/removefromcount/{count}")
    @ApiOperation(value = "Update item count of Inventory by ID", notes = "Remove to items counts", response = ResponseEntity.class)
    public ResponseEntity<?> removeItemsFromInventory(@PathVariable("id") long itemNumber,
                                                 @PathVariable("count") int count,
                                                 @RequestBody Inventory inventory) {
        String message = "";
        checkResourceFound(inventoryService.get(itemNumber));
        Optional<Inventory> inv = inventoryService.get(itemNumber);
        @Positive int startingCount = inv.get().getStartingCount();
        startingCount-= count;
        if (startingCount <= inventory.getReorderPoint()) {
            message = "Time to reorder! " + " current count is: " + startingCount + ". Reorder point is: " +
                    inventory.getReorderPoint();
        } else {
            message = "Inventor item: " + inventory.getItemNumber() + " item count updated successfully ";
        }
        inventory.setStartingCount(startingCount);
        inventoryService.update(itemNumber, inventory);

        return ResponseEntity.ok().body(message);
    }

    @DeleteMapping("/inventory/{id}")
    @ApiOperation(value = "Delete Inventory by ID", notes = "Delete inventory", response = ResponseEntity.class)
    public ResponseEntity<?> deleteInventory(@PathVariable("id") long itemNumber) {
        checkResourceFound(inventoryService.get(itemNumber));
        inventoryService.delete(itemNumber);
        return ResponseEntity.ok().body("Inventory item: " + itemNumber + " was deleted successfully");
    }

    public void printCurrentThread() {
        System.out.println("getInventory method. "
                + Thread.currentThread().getName() + " id: " + Thread.currentThread().getId());
    }
}
