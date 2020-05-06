package kz.iitu.csse.group34.services.impl;

import kz.iitu.csse.group34.entities.Items;
import kz.iitu.csse.group34.repositories.ItemsRepository;
import kz.iitu.csse.group34.services.ItemService;
import kz.iitu.csse.group34.entities.Items;
import kz.iitu.csse.group34.repositories.ItemsRepository;
import kz.iitu.csse.group34.services.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsRepository itemsRepository;

    @Override
    public Items addItem(Items item) {
        return itemsRepository.save(item);
    }

    @Override
    public List<Items> getAllItems() {
        return itemsRepository.findAllByDeletedAtNullOrderByCreatedAtDesc();
    }

    @Override
    public Items getItemById(Long id) {
        return itemsRepository.findByDeletedAtNullAndId(id);
    }
}

