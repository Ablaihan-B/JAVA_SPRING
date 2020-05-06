package kz.iitu.csse.group34.services;



import kz.iitu.csse.group34.entities.Items;

import java.util.List;

public interface ItemService {

    Items addItem(Items items);
    List<Items> getAllItems();
    Items getItemById(Long id);

}