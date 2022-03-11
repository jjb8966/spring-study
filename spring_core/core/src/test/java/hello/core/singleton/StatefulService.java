package hello.core.singleton;

/**
 * 공유 필드를 갖는 클래스 (stateful)
 * 특정 클라이언트가 공유 필드를 변경할 수 있음
 * -> 싱글톤을 적용하면 안된다!!! (매우 중요)
 */
public class StatefulService {

    private int price;      // 공유 필드

    public void order(String name, int price) {
        System.out.println("name = " + name + " price = " + price);
        this.price = price;
    }

    public int getPrice() {
        return price;
    }
}
