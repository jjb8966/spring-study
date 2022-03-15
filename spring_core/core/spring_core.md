# 스프링 핵심 원리

### 목차
### [1. 객체 지향 설계와 스프링](#1-객체-지향-설계와-스프링-1)
### [2. 객체 지향 원리 적용](#2-객체-지향-원리-적용-1)
### [3. 스프링 컨테이너와 스프링 빈](#3-스프링-컨테이너와-스프링-빈-1)
### [4. 싱글톤 컨테이너](#4-싱글톤-컨테이너-1)
### [5. 컴포넌트 스캔](#5-컴포넌트-스캔-1)
### [6. 의존관계 자동 주입](#6-의존관계-자동-주입-1)
### [7. 빈 생명주기 콜백](#7-빈-생명주기-콜백-1)
### [8. 빈 스코프](#8-빈-스코프-1)

# 1. 객체 지향 설계와 스프링

## 1.1 스프링이란?

> 자바 언어 기반의 `프레임워크`  
참고 - [API & 라이브러리 & 프레임워크](https://www.notion.so/API-983e3cde711849a7bc5842d6f0628868)
>
- 스프링은 자바 언어의 특징인 `객체 지향`을 가장 잘 살려서 애플리케이션을 개발할 수 있도록 도와주는 프레임워크임
- 스프링 의미
    - 스프링 프레임워크
        - 핵심 기술 : 스프링 DI 컨테이너, AOP, 이벤트 등
    - 스프링 DI 컨테이너 기술
    - 스프링 생태계

      → 문맥에 따라 다르게 해석함

- 스프링 부트 → 스프링을 편리하게 사용할 수 있도록 지원하는 것
    - 단독으로 실행할 수 있는 스프링 애플리케이션을 쉽게 생성하도록 도와줌
    - Tomcat 같은 웹 서버를 내장
    - 외부 라이이브러리를 자동으로 구성해줌

## 1.2 객체 지향

- 객체 지향 특징 4가지
    1. 추상화
    2. 캡슐화
    3. 상속
    4. `다형성`
- 객체 지향 프로그래밍
    - 프로그램을 명령어의 모음이 아닌 독립된 단위, 즉 객체들의 모임으로 보는 것
    - 객체는 메시지를 주고 받아 데이터를 처리할 수 있음 (객체 간 협력)
    - 프로그램을 `유연`하고 `변경이 용이`하게 만들기 때문에 대규모 소프트웨어 개발에 많이 사용됨
- 다형성
    - 세상을 `역할`과 `구현`으로 분리하여 프로그램을 유연하고 변경이 용이하게 만들어 줌
    - 예시

        | 역할 | 구현(체) |
        | --- | --- |
        | 자동차 | k3, 아반떼, 제네시스 ... |
        | 로미오 역 | 장동건, 원빈 ... |
        | 줄리엣 역 | 김태희, 송혜교 ... |

        - 어떤 자동차를 타든 운전 면허만 있으면 운전을 할 수 있음
            - 엑셀을 밟으면 앞으로 감
            - 브레이크를 밟으면 멈춤
        - 줄리엣 역할을 누가 맡던 로미오 역할을 바꿀 수 있음
    - 역할 → 인터페이스
    - 구현 → 인터페이스를 구현한 클래스, 구현 객체
        - 인터페이스를 구현한 구현 객체를 실행 시점에 유연하게 변경할 수 있음
            
            → 클라이언트를 변경하지 않고 서버의 구현 기능을 유연하게 변경할 수 있음
            
    - 제어의 역전(IoC), 의존관계 주입(DI)는 다형성을 활용해 역할과 구현을 편리하게 다룰수 있도록 지원함

## 1.3 객체 지향 설계의 5가지 원칙 (SOLID)

1. SRP (Single Responsibility Principle) - 단일 책임 원칙
    - 하나의 클래스는 하나의 책임만 가져야 함
    - 클래스의 변경이 있을 때 파급 효과가 적으면 단일 책임 원칙을 잘 따른 것
2. `OCP` (Open/Closed Principle) - 개방/폐쇄 원칙
    - 소프트웨어 요소가 `확장에는 열려`있으나 `변경에는 닫혀`있어야 함
        - 다형성을 활용
            - 인터페이스를 구현하는 새로운 구현 클래스를 만들 수 있음 → 확장에는 열려있음
            - 새로 구현 클래스를 사용한다고 기존 코드(클라이언트)를 변경할 필요는 없음 → 변경에는 닫혀있음
    - 자바 코드만으로는 기존 코드(클라이언트)의 변경 없이 인터페이스 구현체를 변경할 수 없음

      → OCP 위반

      → 객체를 생성하고 연관 관계를 맺어주는 별도의 설정자가 필요함

      = 스프링 컨테이너

3. LSP (Liskov Substitution Principle) - 리스코프 치환 원칙
    - 다형성을 지원하기 위한 원칙
    - 인터페이스 구현체를 믿고 사용하려면 구현체(하위 클래스)는 인터페이스 규약을 다 지켜야 함

      ex) 자동차 인터페이스 - 엑셀을 밟았는데 뒤로감 → LSP 위반

4. ISP (Interface Segregation Principle) - 인터페이스 분리 원칙
    - 범용 인터페이스 하나보다 인터페이스 여러 개가 나음
        - 자동차 인터페이스 → 운전 인터페이스 + 정비 인터페이스
    - 인터페이스가 명확해지고 대체 가능성이 높아짐
5. `DIP` (Dependency Inversion Principle) - 의존관계 역전 원칙
    - 프로그래머는 추상화에 의존해야지 구체화에 의존하면 안됨
        - 구현체에 의존하지 말고 인터페이스에 의존해야 함
    - 자바 코드만 사용할 경우 인터페이스와 구현체에 동시에 의존함

      → DIP 위반

- 결론
    - 다형성만으로는 OCP, DIP를 지킬 수 없다.
    - 뭔가가 더 필요하다. → 스프링 컨테이너!

## 1.4 다시 스프링으로

- 좋은 객체 지향 프로그램
    - OCP, DIP 원칙을 지켜야 하는데 다형성만으로는 지킬 수 없음
    - OCP, DIP를 지키기 위해 DI 컨테이너가 필요하고 이것을 프로그래머가 직접 만들기엔 할 일이 너무 많아짐
    - 그래서 만들어진 것이 스프링 프레임워크
- 스프링
    - DI 컨테이너를 제공해 다형성 + OCP, DIP를 가능하게 지원함

      → 클라이언트의 코드 변경 없이 서버 기능을 확장할 수 있음

      → 부품을 교체하듯이 쉽게 개발할 수 있음
      
# 2. 객체 지향 원리 적용
# 2. 객체 지향 원리 적용

## 2.1 예제 기존 코드

```java
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository = new MemoryMemberRepository();
}
```

```java
public class OrderServiceImpl implements OrderService {
  //  private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
      private final DiscountPolicy discountPolicy = new RateDiscountPolicy();
}
```

- 인터페이스에 의존함과 동시에 구현 클래스(구현체)에도 의존함
    
    → DIP 위반
    
- 기능을 확장하기 위해 구현체를 바꾸려면 기존 코드를 변경해야 함 = 클라이언트 코드(ServiceImpl)를 변경해야 함
    
    → OCP 위반
    
- 관심사 분리
    - 인터페이스와 구현체를 동시에 의존하는 것은 로미오 역할의 장동건이 줄리엣 역할로 김태희를 직접 초빙하는 것과 같음
    - 배우는 본인의 역할인 배역에만 집중해야 함
    - 별도의 공연 기획자가 존재해야 함

<aside>
❗ 클라이언트 코드가 인터페이스에만 의존하도록 변경해야 함!

</aside>

## 2.2 인터페이스에만 의존하도록 변경한 코드

```java
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
}
```

```java
public class OrderServiceImpl implements OrderService {
      private final DiscountPolicy discountPolicy;
}
```

- DIP 원칙을 따르는 상태
- 이렇게 되면 구현체가 없으므로 실행 시 NPE 예외가 발생함
- 구현체를 주입해줄 누군가가 필요함

## 2.3 AppConfig

- 구현 객체를 생성하고 연결하는 책임을 가지는 설정 클래스

```java
public class AppConfig {

    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
		
		// MemberRepository 인터페이스의 구현체로 MemoryMemberRepository 사용됨
    private MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

		// DiscountPolicy 인터페이스의 구현체로 RateDiscountPolicy 사용됨
    private DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
```

- MemberServiceImpl, OrderServiceImpl은 생성자를 통해 외부에서 구현체(인스턴스 참조)를 주입받음
    
    → 의존관계 주입 (DI)
    
- 만약 DiscountPolicy 인터페이스 구현체를 바꾸고 싶다면?
    - discountPolicy() 메소드만 수정해주면 됨 → 클라이언트 코드는 변경하지 않음
    - OCP 원칙을 따르는 상태

## 2.4 IoC & DI & 컨테이너

### IoC (Inversion of Control) - 제어의 역전

> `프로그램의 흐름 제어`를 구현 객체가 직접 제어하는게 아니라 `외부에서 제어`하는 것
> 
- 기존 프로그램은 구현 객체가 프로그램의 제어 흐름을 스스로 조종했음
- AppConfig 등장 이후
    - 구현 객체는 자신의 로직을 실행하는 역할만 담당
        - OrderServiceImpl은 인터페이스를 호출하지만 어떤 구현 객체가 들어올지 모름
    - 프로그램의 흐름 제어는 AppConfig가 담당
        - MemberServiceImpl, OrderServiceImpl도 AppConfig가 생성함
        - MemberService 인터페이스의 다른 구현체를 사용할 수 도 있음
- 프레임워크 vs 라이브러리
    - 프레임워크
        - 프레임워크가 내가 작성한 코드를 제어하고 대신 실행해줌 (JUnit)
    - 라이브러리
        - 내가 작성한 코드가 제어의 흐름을 담당함

### DI(Dependency Injection) - 의존 관계 주입

> `실행 시점(런타임)`에 `외부에서 구현 객체를 생성`하고 `클라이언트에 전달`해서 클라이언트와 서버의 실제 의존 관계가 연결되는 것
> 
- 의존 관계
    - 클래스 의존 관계
        - 정적 → 애플리케이션 실행 전 결정되는 의존 관계
        - import 코드만 보고 의존 관계를 파악할 수 있음
        - 인터페이스에 어떤 구현체가 들어올지는 알 수 없음
    - 객체 의존 관계
        - 동적 → 애플리케이션 실행 시점에 결정되는 의존 관계
        - 외부에서 객체 인스턴스를 생성하고 그 참조값을 전달하여 연결됨
        - DI는 객체 의존 관계 주입을 의미하는 것
- DI를 사용하는 이유
    - 정적인 클래스 의존 관계를 변경하지 않고 동적인 `객체 의존 관계를 쉽게 변경`할 수 있음

### IoC 컨테이너, DI 컨테이너

> `객체를 생성하고 관리`하면서 `의존 관계를 연결`해 주는 것
> 
- 주로 DI 컨테이너라고 함
- AppConfig가 DI 컨테이너의 역할을 함
    - 이 후 배울 spring이 DI 컨테이너 역할을 함
- 어셈블러, 오브젝트 팩토리 등으로 불리기도 함

## 2.5 AppConfig를 spring 기반으로 변경

```java
@Configuration      // 스프링 컨테이너가 설정 정보로 사용하는 클래스
public class AppConfig {

    // 메서드 명 : key -> 스프링 빈 이름
    // 리턴 객체 : value

    @Bean
    public MemberService memberService() {
        return new MemberServiceImpl(memberRepository());
    }

    @Bean
    public OrderService orderService() {
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }

    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }

    @Bean
    public DiscountPolicy discountPolicy() {
        //return new FixDiscountPolicy();
        return new RateDiscountPolicy();
    }
}
```

```java
class MemberServiceTest {

    MemberService memberService;

    @BeforeEach
    void setup() {
        // spring 사용 전
        // AppConfig appConfig = new AppConfig();
        // memberService = appConfig.memberService();

        // spring 사용 후
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        memberService = ac.getBean("memberService", MemberService.class);
    }

    @Test
    void 회원가입_회원조회() {
        //given
        Member member = new Member(1L, "A", Grade.VIP);

        //when
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        //then
        assertThat(member).isEqualTo(findMember);
    }
}
```

- ApplicationContext : 스프링 컨테이너
- @Configuration : 스프링 컨테이너가 설정 정보로 사용할 클래스에 붙이는 어노테이션
- @Bean : 스프링 컨테이너에 등록할 스프링 빈이 되는 메소드에 붙이는 어노테이션
    - 메소드 이름 : key → 스프링 빈 이름
    - 리턴 객체 : value
- getBean() 메소드를 사용해 스프링 컨테이너에서 스프링 빈을 찾아서 사용해야 함

<aside>
❓ 그냥 자바 코드로 DI를 하는게 더 간단해 보이는데 왜 스프링 컨테이너를 사용하는가?

</aside>

# 3. 스프링 컨테이너와 스프링 빈

## 3.1 스프링 컨테이너 생성 과정

1. 스프링 컨테이너 생성
2. 스프링 빈 등록
3. 스프링 빈 의존관계 설정

## 3.2 스프링 빈 조회

- 스프링 컨테이너에는 사용자가 만든 애플리케이션 빈 뿐만 아니라 스프링 내부에서 사용하는 빈들도 존재함
    - beanDefinition.getRole() == BeanDefinition.*ROLE_APPLICATION*
        - 사용자가 만든 빈
    - beanDefinition.getRole() == BeanDefinition.*ROLE_INFRASTRUCTURE*
        - 스프링 내부에서 사용하는 빈
- 스프링 컨테이너에 같은 타입의 빈이 2개 이상인 경우
    - 해당 타입으로만 조회하면 예외가 발생함 (NoUniqueBeanDefinitionException)
    - 빈 이름, 타입을 모두 사용해서 조회하면 정상적으로 조회할 수 있음
- 스프링 컨테이너에 부모가 같은 타입이 2개 이상인 경우
    - 스프링은 부모 타입으로 조회하면 자식 타입도 함께 조회하기 때문에 부모 타입으로 조회하면 예외가 발생함 (NoUniqueBeanDefinitionException)
    - 빈 이름 + 부모 타입을 사용해서 조회하면 정상적으로 조회할 수 있음
    - 자식 타입으로 조회하면 정상적으로 조회할 수 있음 → 권장하진 않음

## 3.3 BeanFactory & ApplicationContext

- BeanFactory
    - 스프링 컨테이너의 최상위 인터페이스
    - 스프링 `빈을 관리`하고 `조회`하는 역할
    - getBean() 제공
    - 직접 사용할 일은 거의 없음
- ApplicationContext
    - BeanFactory를 상속받은 인터페이스
    - 빈을 관리하고 조회하는 기능 외 `다양한 부가기능`을 제공함
        
        ![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/a47ee424-a382-41d6-bb8b-39cf3f29257b/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220315%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220315T170250Z&X-Amz-Expires=86400&X-Amz-Signature=de61c86a7fb6db674dd979d41d6f0a397baf1fa69a2d82737e81c98779d2fa2e&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)
        

## 3.4 스프링 컨테이너 설정

- 스프링 빈은 `다양한 형식의 설정 정보`를 이용해 만들 수 있음
    - ex) java, XML, Groovy 등
    - AppConfig 클래스를 이용해 설정 정보를 등록한게 java를 이용한 것
- 스프링 컨테이너는 `BeanDefinition`을  기반으로 스프링 빈을 생성함
    - BeanDefinition
        - 빈 설정 메타 정보
        - @Bean 당 하나씩 메타 정보가 생성됨
- 어떤 형식인지 상관 없이 스프링 컨테이너는 BeanDefinition만 알면 된다!
    - BeanDefinition 추상화
        
        → 역할 & 구현 분리
        
        ![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/e7a1aeab-c9cb-4453-bb68-fd01d86c9356/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220315%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220315T170313Z&X-Amz-Expires=86400&X-Amz-Signature=1fd8963693c2ae35793a24c2d0613555676f1c85a05becd275bcbb01c20f9e39&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

# 4. 싱글톤 컨테이너

# 5. 컴포넌트 스캔

# 6. 의존관계 자동 주입

# 7. 빈 생명주기 콜백

# 8. 빈 스코프
