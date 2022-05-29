package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ItemRepository {

    // 실무에서는 멀티쓰레드 환경이기 때문에 hash map 을 쓰지 않음
    // ConcurrentHashMap 사용
    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;      // atomic long

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);

        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);

        // updateParam 이 3개의 필드만 사용하고 id는 사용하지 않기 때문에
        // 이런 경우 updateParam 을 별도의 클래스로 만들어서 사용하는게 명확함
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }
}
