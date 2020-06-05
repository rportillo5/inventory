package com.banderasmusic.rest.inventory.controllers;

import com.banderasmusic.rest.inventory.model.Inventory;
import com.banderasmusic.rest.inventory.dao.InventoryDaoRepository;
import com.banderasmusic.rest.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class InventoryController {

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
        Optional<Inventory> inventory = inventoryService.get(itemNumber);
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

    @DeleteMapping("/inventory/{id}")
    public ResponseEntity<?> deleteInventory(@PathVariable("id") long itemNumber) {
        inventoryService.delete(itemNumber);
        return ResponseEntity.ok().body("Inventory item: " + itemNumber + " was deleted successfully");
    }

}
