package com.example.itemservice.domain.item;

import com.example.itemservice.domain.Item;
import com.example.itemservice.domain.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ItemRepositoryTest {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void save() {
        //given
        Item item = new Item("itemA", 10000, 10);

        //when
        Item savedItem = itemRepository.save(item);

        //then
        Item findItem = itemRepository.findById(item.getId());
        assertThat(findItem).isEqualTo(savedItem);
    }

    @Test
    void findAll() {
        //given
        Item item1 = new Item("itemA", 10000, 10);
        Item item2 = new Item("itemB", 25000, 5);

        itemRepository.save(item1);
        itemRepository.save(item2);

        //when
        List<Item> savedItem = itemRepository.findAll();

        //then
        assertThat(savedItem.size()).isEqualTo(2);
        assertThat(savedItem).contains(item1, item2);
    }

    @Test
    void updateItem() {
        //given
        Item item = new Item("itemA", 10000, 10);

        itemRepository.save(item);

        //when
        Item updateItem = new Item("itemB", 25000, 5);
        itemRepository.update(item.getId(), updateItem);

        //then
        Item findItem = itemRepository.findById(item.getId());

        assertThat(findItem.getItemName()).isEqualTo(updateItem.getItemName());
        assertThat(findItem.getPrice()).isEqualTo(updateItem.getPrice());
        assertThat(findItem.getQuantity()).isEqualTo(updateItem.getQuantity());
    }

}