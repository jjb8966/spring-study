package hello.itemservice.domain.item;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Item {

    // groups 지정해서 저장과 수정의 입력 조건을 다르게 할 수 있음 (추천 x)
    // save, update 시 별도의 폼 객체로 받고 거기서 검증하는게 더 좋은 방법
//    @NotNull(groups = UpdateCheck.class)
    private Long id;

//    @NotBlank(groups = SaveCheck.class)
    private String itemName;

//    @NotNull
    @Range(min = 1000, max = 1000000)
    private Integer price;

//    @NotNull
    @Max(9999)
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
