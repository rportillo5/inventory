package com.banderasmusic.rest.inventory.service;

import com.banderasmusic.rest.inventory.dao.InventoryDaoRepository;
import com.banderasmusic.rest.inventory.model.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    final static Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    private InventoryDaoRepository inventoryDao;

    @Override
    public Inventory save(Inventory inventory) {
        if (inventory.getItemNumber() == null) {
            logger.info("Inventory item number is null :");
        }else {
            logger.info("Inventory item  number is not null :"+inventory.getItemNumber());
        }

        return inventoryDao.save(inventory);
    }

    @Override
    public Optional<Inventory> get(long id) {
        return inventoryDao.findById(id);
    }

    @Override
    public Page<Inventory> getInventoryByPage(Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by("itemNumber").descending());
        return inventoryDao.findAll(page);
    }

    @Override
    public void update(long itemNumber, Inventory inventory) {
        inventoryDao.save(inventory);
    }

    @Override
    public void delete(long id) {
        inventoryDao.deleteById(id);
    }
}
