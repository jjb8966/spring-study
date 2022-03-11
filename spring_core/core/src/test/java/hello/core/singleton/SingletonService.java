package hello.core.singleton;

/**
 * 싱글톤 패턴을 적용한 클래스
 * 단점 : 구현해야 할 코드 자체가 많다.
 * DIP, OCP 원칙을 위반할 가능성이 높아짐, 테스트 어려움, 유연성 떨어짐 등..
 */
public class SingletonService {

    // 필수
    private static final SingletonService instance = new SingletonService();

    // 필수
    private SingletonService() {
    }

    // 필수
    public static SingletonService getInstance() {
        return instance;
    }
    public void logic() {
        System.out.println("싱글톤 객체 로직 출력");
    }
}
