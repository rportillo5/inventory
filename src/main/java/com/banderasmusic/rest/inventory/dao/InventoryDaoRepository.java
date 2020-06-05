package com.banderasmusic.rest.inventory.dao;

import com.banderasmusic.rest.inventory.model.Inventory;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryDaoRepository extends PagingAndSortingRepository<Inventory, Long> {
    Optional<Inventory> findById(Long itemNumber);

    @Override
    List<Inventory> findAll();
}
