package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    // 테스트용 데이터
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

    //path = /basic/items/{itemId}
    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }

    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }

    //@PostMapping("/add")
    public String addItemV1(
            @RequestParam("itemName") String itemName,
            @RequestParam("price") Integer price,
            @RequestParam("quantity") Integer quantity,
            Model model
    ) {
        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute("item") Item item, Model model) {
        itemRepository.save(item);

        //model.addAttribute("item", item); // 자동 추가, 생략 가능

        return "basic/item";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);

        // ModelAttribute 이름 생략 가능
        // 생략 시 ModelAttribute 클래스의 이름 사용(첫 글자 소문자로) -> 모델에 자동 추가
        return "basic/item";
    }

    // post 로 호출된 메소드 -> 상품 저장 후 뷰 템플릿으로 이동
    // 새로고침 -> 마지막에 서버에 전송한 데이터 다시 전송 -> post 호출 반복 -> 이 메소드 계속 실행!! -> 계속 저장
    //@PostMapping("/add")
    public String addItemV4(Item item) {
        itemRepository.save(item);

        //@ModelAttribute 자체도 생략가능
        return "basic/item";
    }

    // 상품 저장 후 리다이렉트 /basic/items/{itemId} -> get 으로 url 요청 (마지막 전송)
    // 새로고침 -> get url 요청 -> 데이터 저장x
    // url + item.getId() -> 인코딩 문제가 있을 수 있음
    //@PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);

        return "redirect:/basic/items/" + item.getId();
    }

    // url 인코딩 문제 해결
    // 저장 완료 시 저장 완료 메시지를 추가하는 화면을 별도의 뷰를 만들지 않고 파라미터로 넘겨 만들 수 있음
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/basic/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);

        return "basic/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);

        return "redirect:/basic/items/{itemId}";
    }
}
