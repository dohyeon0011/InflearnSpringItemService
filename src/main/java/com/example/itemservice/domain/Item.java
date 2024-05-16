package com.example.itemservice.domain;


import lombok.Data;

@Data // 얘는 메인 도메인에 쓰기엔 위험함 / DTO와 같은 데이터 이동용은 써도 되지만 상황을 봐야함
//@Getter @Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price;      // 가격이 없을 수도 있으니 null을 넣기 위해
    private Integer quantity;   // 수량이 없을 수도 있으니 null을 넣기 위해

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
