package com.example.itemservice.web.basic;


import com.example.itemservice.domain.Item;
import com.example.itemservice.domain.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/basic/items") //기본 경로
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "basic/items";
    }

    @GetMapping("/{itemId}")
    public String findItem(@PathVariable Long itemId, Model model) {
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);

        log.info("itemI={}", itemId);

        return "basic/item";
    }

    @GetMapping("/add")
    public String saveItemForm() {
        return "basic/addForm";
    }

    /*@PostMapping("/add")
    public String saveItem(@RequestParam String itemName,   // html form data가 그대로 쿼리 파라미터로 넘어옴
                           @RequestParam Integer price,
                           @RequestParam Integer quantity,
                           Model model) {

        Item newItem = new Item(itemName, price, quantity);
        itemRepository.save(newItem);

        model.addAttribute("item", newItem);

        return "basic/item";
    }*/

    /**
     * @ModelAttribute는 요청 파라미터의 객체도 만들어주고, View에도 ("")로 지정한 이름으로 모델 객체를 넣어줌
     */
    @PostMapping("/add")
    public String saveItem(@ModelAttribute("item") Item item, Model model) {
//        Item newItem = new Item(item);

//        newItem.setItemName(item.getItemName());
//        newItem.setPrice(item.getPrice());
//        newItem.setQuantity(item.getQuantity());

        itemRepository.save(item);

//        model.addAttribute("item", newItem);

        return "basic/item";
    }

    /**
     * 테스트용 데이터 추가
     */
    @PostConstruct
    public void init() {
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 25000, 5));
    }

}
