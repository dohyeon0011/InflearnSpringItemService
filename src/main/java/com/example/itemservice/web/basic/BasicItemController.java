package com.example.itemservice.web.basic;


import com.example.itemservice.domain.Item;
import com.example.itemservice.domain.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     * ("")가 디폴트일 때는 클래스명의 첫 앞글자만 소문자로 바꿔줌(Item -> item)
     * 웹 브라우저의 새로고침은 마지막에 서버에 전송한 데이터를 다시 전송
     * -> 상품 등록 폼에서 데이터를 입력하고 저장한 후, 이 상태에서 새로고침을 하게 되면
     * 마지막에 전송한 POST /add + 상품 데이터를 다시 서버로 전송해서 ID만 다른 상품 데이터가 쌓임
     * 그래서 redirect를 사용해야 함
     * prg(post-redirect-get)
     */
   /* @PostMapping("/add")
    public String saveItem(@ModelAttribute("item") Item item, Model model) {
//        Item newItem = new Item(item);

//        newItem.setItemName(item.getItemName());
//        newItem.setPrice(item.getPrice());
//        newItem.setQuantity(item.getQuantity());

        itemRepository.save(item);

//        model.addAttribute("item", newItem);

        return "redirect:/basic/items/" + item.getId(); // 한글이나 띄어쓰기가 있는 경우 따로 인코딩을 해준 후 리다이렉트를 해줘야 함
    }*/

    // RedirectAttribute : URL 인코딩도 해주고, pathVarible 쿼리 파라미터까지 처리해주면서 리다이렉트 해줌
    @PostMapping("/add")
    public String saveItemV2(@ModelAttribute("item") Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);    // true이면 저장해서 넘어왔다고? 인식

        // uri에 /4?status=true 이런 식으로 쿼리 파라미터가 넘어가서 리다이렉트 경로인 {itemId}로 가서 폼을 수정해줘야함
        return "redirect:/basic/items/{itemId}";    // redirectAttribute에 넣은 itemId 값이 치환돼서 들어옴, 치환되지 않은 값을 쿼리 파라미터로 넘어감
    }


    @GetMapping("/{itemId}/edit")
    public String updateForm(@PathVariable Long itemId, Model model) {
        Item findItem = itemRepository.findById(itemId);
        model.addAttribute("item", findItem);

        return "basic/editForm";
    }

    // html form 전송은 http 스펙상 put, patch를 지원하지 않아서 post로 수정(get, post만 지원, http api 통신)
    // Rest API 통신 시 put, patch 사용
    @PostMapping("/{itemId}/edit")
    public String updateItem(@PathVariable Long itemId,
                             @RequestParam String itemName,
                             @RequestParam Integer price,
                             @RequestParam Integer quantity,
                             Model model) {
        Item updateItem = new Item(itemName, price, quantity);

        itemRepository.update(itemId, updateItem);

        return "redirect:/basic/items/{itemId}";
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
