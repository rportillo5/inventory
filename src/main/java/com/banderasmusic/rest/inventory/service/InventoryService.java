package com.banderasmusic.rest.inventory.service;

import com.banderasmusic.rest.inventory.model.Inventory;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface InventoryService {
    Inventory save(Inventory inventory);
    Optional<Inventory> get(long itemNumber);
    Page<Inventory> getInventoryByPage(Integer pageNumber, Integer pageSize);
    void update(long itemNumber, Inventory inventory);
    void delete(long itemNumber);
}
