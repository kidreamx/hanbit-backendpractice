package kr.co.hanbit.product.management.domain;

public class Product {
    private Long id;
    private String name;
    private int price;
    private int amount;

    public void setId(Long id) {
        this.id = id;
    }
    public Boolean sameId(Long id){
        return this.id.equals(id);
    }

}
