package spring.corepractice.singleton;

public class SingletonService {

    private static final SingletonService instance = new SingletonService();

    // 외부에서 new 를 사용해 객체를 생성하지 못함
    private SingletonService() {
    }

    public static SingletonService getInstance() {
        return instance;
    }

    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
