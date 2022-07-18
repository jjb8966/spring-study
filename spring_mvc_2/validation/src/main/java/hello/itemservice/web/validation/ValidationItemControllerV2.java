
package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    @InitBinder
    public void init(WebDataBinder dataBinder) {
        log.info("init binder = {}", dataBinder);
        dataBinder.addValidators(itemValidator);
    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

    //@PostMapping("/add")
    /**
     * BindingResult -> 인터페이스. Errors(인터페이스) 를 상속받고 있음
     * 구현체 -> BeanPropertyBindingResult
     * ==> Errors 로 받아도 됨 (관례상 BindingResult를 더 많이 사용함)
     * <p>
     * 문제점 : 잘못 입력한 값이 다시 보여지지 않음
     */
    public String addItemV1(@ModelAttribute Item item, BindingResult bindingResult,   // 반드시 검증 대상 뒤에 선언해야 함!
                            RedirectAttributes redirectAttributes, Model model) {
//        Map<String, String> errors = new HashMap<>();

        // 필드 검증
        if (!StringUtils.hasText(item.getItemName())) {
//            errors.put("itemName", "상품 이름은 필수 입니다.");
            bindingResult.addError(new FieldError("item", "itemName", "상품 이름은 필수 입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            errors.put("price", "1000 < 가격 < 1000000");
            bindingResult.addError(new FieldError("item", "price", "1000 < 가격 < 1000000"));
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            errors.put("quantity", "수량 <= 9999");
            bindingResult.addError(new FieldError("item", "quantity", "수량 <= 9999"));
        }

        // 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
//                errors.put("globalError", "최소 주문금액은 10000원");
                bindingResult.addError(new ObjectError("item", "최소 주문금액은 10000원"));
            }
        }

        // 에러 있으면 다시 입력받기
//        if (!errors.isEmpty()) {
//            model.addAttribute("errors", errors);
//            return "validation/v2/addForm";
//        }

        if (bindingResult.hasErrors()) {
            // BindingResult 는 자동으로 model 에 담겨서 넘어감
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult,   // 반드시 검증 대상 뒤에 선언해야 함!
                            RedirectAttributes redirectAttributes, Model model) {
        // 필드 검증
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),
                    false, null, null, "상품 이름은 필수 입니다."));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            errors.put("price", "1000 < 가격 < 1000000");
            bindingResult.addError(new FieldError("item", "price", item.getPrice(),
                    false, null, null, "1000 < 가격 < 1000000"));
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            errors.put("quantity", "수량 <= 9999");
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),
                    false, null, null, "수량 <= 9999"));
        }

        // 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
//                errors.put("globalError", "최소 주문금액은 10000원");
                bindingResult.addError(new ObjectError("item", null, null,
                        "최소 주문금액은 10000원"));
            }
        }

        // 에러 있으면 다시 입력받기
//        if (!errors.isEmpty()) {
//            model.addAttribute("errors", errors);
//            return "validation/v2/addForm";
//        }

        if (bindingResult.hasErrors()) {
            // BindingResult 는 자동으로 model 에 담겨서 넘어감
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult,   // 반드시 검증 대상 뒤에 선언해야 함!
                            RedirectAttributes redirectAttributes, Model model) {
        // 필드 검증
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName", item.getItemName(),
                    false, new String[]{"required.item.itemName"}, null, null));
        }

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
//            errors.put("price", "1000 < 가격 < 1000000");
            bindingResult.addError(new FieldError("item", "price", item.getPrice(),
                    false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
//            errors.put("quantity", "수량 <= 9999");
            bindingResult.addError(new FieldError("item", "quantity", item.getQuantity(),
                    false, new String[]{"max.item.quantity"}, new Object[]{9999}, null));
        }

        // 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
//                errors.put("globalError", "최소 주문금액은 10000원");
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"},
                        new Object[]{10000, resultPrice}, null));
            }
        }

        // 에러 있으면 다시 입력받기
//        if (!errors.isEmpty()) {
//            model.addAttribute("errors", errors);
//            return "validation/v2/addForm";
//        }

        if (bindingResult.hasErrors()) {
            // BindingResult 는 자동으로 model 에 담겨서 넘어감
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult,   // 반드시 검증 대상 뒤에 선언해야 함!
                            RedirectAttributes redirectAttributes, Model model) {
        // 필드 검증
//        if (!StringUtils.hasText(item.getItemName())) {
//            // reject(), rejectValue() -> 내부에서 MessageCodeResolver 가 동작함
//            bindingResult.rejectValue("itemName", "required");
//        }

        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");

        if (item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null);
        }

        if (item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        // 필드가 아닌 복합 룰 검증
        if (item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();

            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin",new Object[]{10000, resultPrice},null);
            }
        }

        // 에러 있으면 다시 입력받기
//        if (!errors.isEmpty()) {
//            model.addAttribute("errors", errors);
//            return "validation/v2/addForm";
//        }

        if (bindingResult.hasErrors()) {
            // BindingResult 는 자동으로 model 에 담겨서 넘어감
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    //@PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult,   // 반드시 검증 대상 뒤에 선언해야 함!
                            RedirectAttributes redirectAttributes, Model model) {

        itemValidator.validate(item, bindingResult);

        if (bindingResult.hasErrors()) {
            // BindingResult 는 자동으로 model 에 담겨서 넘어감
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult,   // 반드시 검증 대상 뒤에 선언해야 함!
                            RedirectAttributes redirectAttributes, Model model) {

//        itemValidator.validate(item, bindingResult);

        if (bindingResult.hasErrors()) {
            // BindingResult 는 자동으로 model 에 담겨서 넘어감
            log.info("errors = {}", bindingResult);
            return "validation/v2/addForm";
        }

        // 성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

}

