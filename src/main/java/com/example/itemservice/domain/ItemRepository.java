package com.example.itemservice.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    // 실무에서 멀티쓰레드(동시에 여러 쓰레드가 접근할 때) 환경에서 해시맵 사용x(할거면 ConcurrentHashMap<>())
    // Long도 값이 꼬일 수 있어서 AtomicLong
    private static final Map<Long, Item> store = new HashMap<>();   // static
    private static long sequence = 0L;  // static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);

        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        // store.values()로 그대로 반환해도 되지만 Collection(ArrayList)로 감싸서 반환하면
        // ArrayList에 값을 넣어도 실제 store 값에는 함이 없기 때문에 안전하게 감싸기
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }

}
