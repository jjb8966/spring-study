# 5. 스프링 MVC - 구조 이해

## 5.1 전체 구조

- 직접 만든 프레임워크와 거의 동일
    
    ![Untitled](https://user-images.githubusercontent.com/87421893/170875147-125add2b-2e80-46de-9ba2-52c9ad30acc6.png)

    
    - FrontController → `DispatcherServlet`
        - **DispatcherServlet도 서블릿**으로 동작
            - **HttpServlet의 상속**을 받음
                - DispatcherServlet → FrameworkServlet → HttpServletBean → HttpServlet
        - 스프링 부트가 시작하면서 서블릿으로 DispatcherSevlet 자동 등록
        - 모든 경로(url=”/”)에 대해서 매핑
            
            → 클라이언트가 어떤 경로로 요청하든 실행되는 서블릿
            
        - 이 서블릿의 `service()` 메소드가 호출
            - 정확히는 FrameworkServlet에서 오버라이드한 service() 메소드가 실행
            - service() 메소드를 시작으로 DispatcherServlet.`doDispatch()`가 호출됨
                1. 핸들러 조회
                2. 어댑터 조회
                3. 어댑터 실행 → 핸들러 실행 → ModelAndView 리턴
                4. ModelAndView를 통해 뷰의 논리주소 viewName을 얻음
                5. ViewResolver를 통해 뷰의 논리주소를 물리주소로 바꾸고 View 객체 얻음
                    - JSP에서 인터페이스 구현체
                        - ViewResolver → **InternalResource**ViewResolver
                        - View → **InternalResource**View
                6. View로 렌더링
    - **서블릿을 제외한 나머지**는 `모두 인터페이스`
        - handlerMappingMap → `HandlerMapping`
        - MyHandlerAdapter → `HandlerAdapter`
        - viewResolver() → `ViewResolver`
        - MyView → `View`

### 1. Handler Mapping & Adapter

- 컨트롤러가 호출되는 과정
    1. 핸들러 매핑에서 클라이언트 요청에 맞는 `컨트롤러(핸들러)를 찾음`
        - **핸들러 찾는 방식**
            1. **어노테이션**
            2. 스프링 빈 이름
    2. 해당 핸들러를 지원하는 `어댑터를 찾음`
    3. `어댑터가 핸들러 호출`
- 스프링이 대부분의 필요한 핸들러 매핑과 어댑터를 구현해놨기 때문에 개발자가 직접 만들 일은 거의 없음
    - 자동 등록된 `HandlerMapping(인터페이스)`
        1. `**RequestMapping**HandlerMapping(구현체)`
            - **어노테이션 기반 컨트롤러**를 지원
                - **@RequestMapping** 어노테이션으로 만든 컨트롤러
        2. **BeanNameUrl**HandlerMapping
            - **스프링 빈 이름**으로 컨트롤러 찾음
    - 자동 등록된 `HandlerAdapter(인터페이스)`
        1. `**RequestMapping**HandlerAdapter(구현체)`
            - **어노테이션 기반 컨트롤러**를 지원
        2. HttpRequestHandlerAdapter
            - **HttpRequestHandler 인터페이스를 구현한 컨트롤러**를 지원
        3. **SimpleControllerHandlerAdapter**
            - **Controller 인터페이스를 구현한 컨트롤러**를 지원
        
        ⇒ 자동 등록된 핸들러 매핑과 어댑터는 작성된 순서를 우선순위로 조회됨
        
        ⇒ 실무에서 어노테이션 기반의 컨트롤러가 99%
        

### 옛날 방식의 스프링 컨트롤러

1. **스프링 빈 이름의 컨트롤러**를 등록
    - 스프링 빈 이름은 URL
    - 스프링 빈 클래스는 Controller 인터페이스를 구현함
        - @Controller 아님!
    
    ```java
    @Component("/springmvc/old-controller")
    public class OldController implements Controller {
    
        @Override
        public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
            System.out.println("OldController.handleRequest");
            return new ModelAndView("new-form");
        }
    }
    ```
    
2. HandlerMapping을 순서대로 조회
    1. RequestMappingHandlerMapping
        - @RequestMapping("/springmvc/old-controller") 붙은 컨트롤러 찾음
            
            → 없음
            
    2. **BeanNameUrlHandlerMapping**
        - ‘/springmvc/old-controller’을 이름으로하는 **컨트롤러 찾음**
            
            → 찾음
            
3. **SimpleControllerHandlerAdapter**를 사용해 컨트롤러 실행

### 현재 사용하는 스프링 컨트롤러

1. 어노테이션 기반의 컨트롤러를 등록
    - @Controller, @RequestMapping 어노테이션 사용
    - 스프링 빈의 이름은 따로 있음
    
    ```java
    @Controller
    @RequestMapping("/springmvc/v3/members")
    public class SpringMemberControllerV3 {
    
        private MemberRepository memberRepository = MemberRepository.getInstance();
    
        // HTTP 메소드가 GET 일 때만 동작
        // = @GetMapping
        @RequestMapping(value = "/new-form", method = RequestMethod.GET)
        public String newForm() {
            return "new-form";
        }
    }
    ```
    
2. HandlerMapping을 순서대로 조회
    1. `RequestMappingHandlerMapping`
        - @RequestMapping("/springmvc/v3/members") 붙은 컨트롤러 찾음
            
            → 있음
            
3. `RequestMappingHandlerAdapter`를 사용해 컨트롤러 실행

### 2. View Resolver

- 자동 등록된 `뷰 리졸버`
    1. BeanNameViewResolver
        - 빈 이름으로 뷰를 찾아서 반환
    2. InternalResourceViewResolver
        - jsp를 처리할 수 있는 뷰를 반환

### Controller 인터페이스를 구현한 컨트롤러

- 핸들러 매핑 → BeanNameUrlHandlerMapping
- 핸들러 어댑터 → SimpleControllerHandlerAdapter

```java
@Component("/springmvc/old-controller")
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        return new ModelAndView("new-form");
    }
}
```

- ModelAndView를 뷰의 논리 주소를 사용해 생성하고 리턴
- ViewResolver 호출
    1. BeanNameViewResolver가 **new-form**라는 이름을 갖는 **스프링 빈**을 찾는데 없음
    2. `InternalResourceViewResolver` 호출
        - `InternalResourceView` 리턴
            - JSP처럼 forward()를 호출해서 처리할 수 있는 경우 사용
            - 만약 JSTL 라이브러리가 있으면 InternalResourceView를 상속받은 **JstlView를 리턴**
        - 이 때 application.properties의 설정 정보를 기반으로 등록함
            
            ```java
            spring.mvc.view.prefix=/WEB-INF/views/
            spring.mvc.view.suffix=.jsp
            ```
            

> 템플릿 뷰로 타임리프를 사용하는 경우 ThymeleafViewResolver를 등록해야 함
라이브러리만 추가하면 스프링부트가 자동으로 추가해줌
> 

## 5.2 스프링 MVC

- 실무에서는 **99% 어노테이션 기반의 컨트롤러**를 사용함

### 1. 컨트롤러 V1 - spring mvc 적용

- 컨트롤러 3개 존재
    
    ```java
    @Controller
    public class SpringMemberFormControllerV1 {}
    
    @Controller
    public class SpringMemberListControllerV1 {}
    ```
    
    ```java
    @Controller
    public class SpringMemberSaveControllerV1 {
    
        private MemberRepository memberRepository = MemberRepository.getInstance();
    
        @RequestMapping("/springmvc/v1/members/save")
        public ModelAndView process(HttpServletRequest request, HttpServletResponse response) {
            String userName = request.getParameter("userName");
            int age = Integer.parseInt(request.getParameter("age"));
    
            Member member = new Member(userName, age);
            memberRepository.save(member);
    
            ModelAndView mv = new ModelAndView("save-result");
            mv.addObject("member", member);
    
            return mv;
        }
    }
    ```
    
    - **각각의 컨트롤러가 따로 존재**하고 **스프링 빈으로 등록**됨
    - @RequestMapping 어노테이션을 이용해 컨트롤러에 해당하는 URL 매핑
        - 메소드 이름은 임의로 지어도 무관
    - 메소드 실행 결과 `ModelAndView를 리턴`
    - mv.getModel().put() 대신 **mv.addObject**로 모델에 데이터를 넣을 수 있음

### 2. 컨트롤러 V2 - 컨트롤러 통합

- `@RequestMapping` 어노테이션을 메소드 단위가 아닌 **클래스 단위로 적용**해 **3개의 컨트롤러를 하나의 컨트롤러로 통합함**
    
    ```java
    @Controller
    @RequestMapping("/springmvc/v2/members")
    public class SpringMemberControllerV2 {
    
        private MemberRepository memberRepository = MemberRepository.getInstance();
    
        @RequestMapping("/new-form")
        public ModelAndView newForm() {
            return new ModelAndView("new-form");
        }
    
        @RequestMapping("/save")
        public ModelAndView save(HttpServletRequest request, HttpServletResponse response) {
            String userName = request.getParameter("userName");
            int age = Integer.parseInt(request.getParameter("age"));
    
            Member member = new Member(userName, age);
            memberRepository.save(member);
    
            ModelAndView mv = new ModelAndView("save-result");
            mv.addObject("member", member);
    
            return mv;
        }
    
        @RequestMapping()
        public ModelAndView list() {
            List<Member> members = memberRepository.findAll();
    
            ModelAndView mv = new ModelAndView("members");
            mv.addObject("members", members);
    
            return mv;
        }
    }
    ```
    
    - 클래스의 @RequestMapping
        - 3개의 메소드의 공통적인 부분을 맵핑
    - 메소드의 @RequestMapping
        - 각 요청에 해당하는 세부 URL 맵핑
    
    → @RequestMapping은 메소드 단위로 적용됨
    
    cf) **하나의 컨트롤러로 통합**하고 메소드 레벨에서만 어노테이션을 지정해도 가능하긴 함
    
    → 중복 코드가 존재하므로 권장하진 않음
    
    ```java
    @RequestMapping("/springmvc/v2/members/save")
    public ModelAndView save(){}
    ```
    

### 3. 컨트롤러 V3 - 실용적인 방식

- `리턴 타입`
    - `viewName`
        - String으로 viewName만 받아도 뷰의 논리주소로 인식함
        
        → ModelAndView를 반환하는게 아니므로 **매개변수로 model**을 받아야 함
        
- `매개 변수`
    - `@RequestParam(object) Object object`
        - request, response 대신 request parameter를 받음
    - `Model`
- `@GetMapping` & `@PostMapping`
    - 단순히 URL 매핑만하는 @RequestMapping이 아닌  **HTTP 메소드를 구분하여 동작하는 방식**
        - @GetMapping = @RequestMapping(value = "/new-form", **method = RequestMethod.GET**)
    - **Get, Post, Put, Delete, Patch 모두 지원**

```java
@Controller
@RequestMapping("/springmvc/v3/members")
public class SpringMemberControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    // HTTP 메소드가 GET 일 때만 동작
    // = @GetMapping
    @RequestMapping(value = "/new-form", method = RequestMethod.GET)
    public String newForm() {
        return "new-form";
    }

    // HTTP 메소드가 POST 일 때만 동작
    @PostMapping("/save")
    public String save(
            // =request.getParameter("userName")
            // =Integer.parseInt(request.getParameter("age"))
            @RequestParam("userName") String userName,
            @RequestParam("age") int age,
            Model model
    ) {
        Member member = new Member(userName, age);
        memberRepository.save(member);

        model.addAttribute("member", member);

        return "save-result";
    }

    @GetMapping()
    public String list(Model model) {
        List<Member> members = memberRepository.findAll();

        model.addAttribute("members", members);

        return "members";
    }
}
```
