package spring.corepractice.singleton;

public class StatefulService {

    // 공유되는 필드
    private int price;

    // 이 메소드를 통해 공유되는 필드가 변경될 수 있음 -> 장애가 발생할 수 있음
    public void order(String name, int price) {
        System.out.println("name = " + name + ", price = " + price);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
