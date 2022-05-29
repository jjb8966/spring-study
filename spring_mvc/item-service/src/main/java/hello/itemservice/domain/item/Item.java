package hello.itemservice.domain.item;

import lombok.Data;

// 예측하지 못하게 동작하는 경우가 있음. 주의해서 사용해야 함
@Data
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
