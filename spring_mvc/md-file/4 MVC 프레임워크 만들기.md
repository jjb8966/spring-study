# 4. MVC 프레임워크 만들기

## 4.1 프론트 컨트롤러 도입 - v1

- 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
    - 공통처리가 가능해짐
    - 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아 호출함
        - 나머지 컨트롤러는 서블릿으로 만들 필요 없음
    
    <aside>
    ⭐ 프론트 컨트롤러가 spring mvc의 핵심 개념!
    **spring mvc**의 `DispatcherServlet`이 `프론트 컨트롤러 패턴`으로 구현되어있음
    
    </aside>
    

### 1. 구조

![Untitled](4%20MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%207e6af14513c74eb3b8d7606dbac9ffbf/Untitled.png)

### 2. 컨트롤러 인터페이스

- 서블릿과 비슷한 모양의 컨트롤러 인터페이스
- `매개 변수`
    - request
    - response
- `리턴 타입`
    - void

```java
public interface ControllerV1 {

    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
```

### 3. 컨트롤러

- 이제부터 만들게 될 컨트롤러들은 `서블릿 객체가 아님!`
    - @WebServlet 없음
    - 프론트 컨트롤러에 의해 호출됨
- `서블릿 객체`는 오직 **프론트 컨트롤러 뿐**

```java
public class MemberFormControllerV1 implements ControllerV1;
public class MemberListControllerV1 implements ControllerV1;
```

```java
public class MemberSaveControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(userName, age);
        memberRepository.save(member);

        request.setAttribute("member", member);

        String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```

### 4. 프론트 컨트롤러 - v1

```java
@WebServlet(name = "frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        ControllerV1 controller = controllerMap.get(requestURI);

        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        controller.process(request, response);
    }
}
```

- urlPatterns = `"/front-controller/v1/*"`
    - `/front-controller/v1` 을 포함한 모든 요청에 대해 서블릿이 실행됨
- 생성자
    - controllerMap 초기화
        - key : URL
        - value : 해당 요청에 맞는 컨트롤러
- 클라이언트 요청을 통해 URL을 얻어 해당하는 컨트롤러를 꺼내고, 컨트롤러가 비지니스 로직을 실행함
    - 컨트롤러가 `비지니스 로직` 실행 및 `뷰(forward)` 관련 코드를 모두 포함

## 4.2 view 분리 - v2

### 1. 구조

![Untitled](4%20MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%207e6af14513c74eb3b8d7606dbac9ffbf/Untitled%201.png)

- 뷰로 이동하는 중복되는 코드를 줄임

### MyView 클래스

- 생성될 때 전달받은 viewPath를 이용해 뷰를 렌더링함

```java
public class MyView {

    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```

### 2. 컨트롤러 인터페이스

- `매개 변수`
    - request
    - response
- `리턴 타입`
    - `MyView`
        - 뷰의 물리 주소를 가지고 있고 이를 통해 렌더링함

```java
public interface ControllerV2 {

    MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
```

### 3. 컨트롤러

```java
public class MemberFormControllerV2 implements ControllerV2;
public class MemberListControllerV2 implements ControllerV2;
```

```java
public class MemberSaveControllerV2 implements ControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(userName, age);
        memberRepository.save(member);

        request.setAttribute("member", member);

        return new MyView("/WEB-INF/views/save-result.jsp");
    }
}
```

### 4. 프론트 컨트롤러 - v2

- service() 메소드만 변경

```java
@Override
protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String requestURI = request.getRequestURI();

    ControllerV2 controller = controllerMap.get(requestURI);

    if (controller == null) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return;
    }

    MyView view = controller.process(request, response);
    view.render(request, response);
}
```

- 컨트롤러가 MyView를 리턴하고, MyView가 request에 담긴 정보를 이용해 렌더링함

## 4.3 model 추가 - v3

### 1. 구조

![Untitled](4%20MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%207e6af14513c74eb3b8d7606dbac9ffbf/Untitled%202.png)

### (1) ModelView 클래스

```java
@Getter @Setter
public class ModelView {

    private String viewName;
    private Map<String, Object> model = new HashMap<>();

    public ModelView(String viewName) {
        this.viewName = viewName;
    }
}
```

- viewName & model
    - `viewName`
        - 뷰의 실제 물리 이름이 아닌 **논리 이름**을 저장
    - `model`
        - **컨트롤러 실행 결과**를 저장

### (2) viewResolver() 메소드

```java
private MyView viewResolver(String viewName) {
    return new MyView("/WEB-INF/views/" + viewName + ".jsp");
}
```

- 뷰의 **논리이름을 물리 이름으로 변환**시켜 `MyView를 리턴`하는 메소드
- 프론트 컨트롤러에 존재

### (3) 수정된 MyView

```java
public class MyView {

    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    public void render(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelToRequest(model, request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void modelToRequest(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((key, value) -> request.setAttribute(key, value));
    }
}
```

- render() 메소드 오버로딩
    - 매개변수로 model을 추가로 받음
- modelToRequest() 메소드
    - jsp는 request에 담긴 데이터를 조회하기 때문에 model의 데이터를 request로 옮김

### 2. 컨트롤러 인터페이스

- `매개변수`
    - 모든 컨트롤러가 request, response를 필요로 하지 않음을 고려
    - `paramMap`
        - request에 담긴 데이터를 paramMap으로 전달받음
        - request를 직접 받지 않으므로 서블릿에 독립적임
            - 컨트롤러 구현이 더 쉬워짐을 전달받아 서블릿 관련 코드를 지움
- `리턴 타입`
    - MyView가 아닌 ModelView를 전달
    - `ModelView`
        - viewName과 model을 포함

```java
public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);
}
```

### 3. 컨트롤러

```java
public class MemberFormControllerV3 implements ControllerV3;
public class MemberListControllerV3 implements ControllerV3;
```

```java
public class MemberSaveControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String userName = paramMap.get("userName");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(userName, age);
        memberRepository.save(member);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);

        return mv;
    }
}
```

- 비지니스 로직을 실행한 결과를 request.setAttribute()가 아닌 ModelView에 담음

### 4. 프론트 컨트롤러 - v3

```java
		@Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);

        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Map<String, String> paramMap = createParamMap(request);

        ModelView mv = controller.process(paramMap);

        String viewName = mv.getViewName();

        MyView myView = viewResolver(viewName);
        myView.render(mv.getModel(), request, response);
    }
```

- 클라이언트가 요청한 데이터(request)를 이용해 paramMap을 만듦
- 컨트롤러의 매개변수로 paramMap 전달
    - 결과로 비지니스 로직을 실행한 결과를 담은 model과 뷰의 논리 이름을 포함하고 있는 ModelView를 리턴
- ModelView에 저장된 뷰의 논리 이름을 viewResolver를 통해 물리 이름으로 바꾸고 그것을 저장한 MyView 객체를 리턴함

```java
public void render(Map<String, Object> model,HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    modelToRequest(model, request);
    RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
    dispatcher.forward(request, response);
}

private void modelToRequest(Map<String, Object> model, HttpServletRequest request) {
    model.forEach((key, value) -> request.setAttribute(key, value));
}
```

## 4.4 단순하고 실용적인 컨트롤러 - v4

### 1. 구조

![Untitled](4%20MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%207e6af14513c74eb3b8d7606dbac9ffbf/Untitled%203.png)

- 컨트롤러가 ModelView가 아닌 viewName, 즉 뷰의 논리 이름만 리턴함
- 데이터를 담을 model은 매개변수로 전달받음

### 2. 컨트롤러 인터페이스

- `매개변수`
    - `paramMap`
    - `model`
        - 컨트롤러의 실행 결과를 저장할 모델을 전달받음
        - 컨트롤러가 받을 때는 비어있는 상태
- `리턴타입`
    - `viewName`
        - 뷰의 논리 이름을 리턴함
        - viewResolver를 통해 물리 이름으로 변환됨
            - 변환된 물리 이름을 MyView 생성하고 렌더링

```java
public interface ControllerV4 {

    String process(Map<String, String> paramMap, Map<String, Object> model);
}
```

### 3. 컨트롤러

```java
public class MemberFormControllerV4 implements ControllerV4;
public class MemberListControllerV4 implements ControllerV4;
```

```java
public class MemberSaveControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String userName = paramMap.get("userName");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(userName, age);
        memberRepository.save(member);

        model.put("member", member);

        return "save-result";
    }
}
```

### 4. 프론트 컨트롤러 - v4

```java
@Override
protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String requestURI = request.getRequestURI();

    ControllerV4 controller = controllerMap.get(requestURI);

    Map<String, String> paramMap = createParamMap(request);
    Map<String, Object> model = new HashMap<>();

    String viewName = controller.process(paramMap, model);

    MyView myView = viewResolver(viewName);
    myView.render(model, request, response);
}
```

- paramMap과 model을 생성해 컨트롤러에게 넘겨줌
- 컨트롤러가 뷰의 논리 이름을 리턴하고 viewResolver가 물리 이름으로 변환시킴
- 모델과 뷰의 물리 이름을 이용해 뷰 렌더링

## 4.5 유연한 컨트롤러 - v5 ⭐

### 1. 구조

![Untitled](4%20MVC%20%E1%84%91%E1%85%B3%E1%84%85%E1%85%A6%E1%84%8B%E1%85%B5%E1%86%B7%E1%84%8B%E1%85%AF%E1%84%8F%E1%85%B3%20%E1%84%86%E1%85%A1%E1%86%AB%E1%84%83%E1%85%B3%E1%86%AF%E1%84%80%E1%85%B5%207e6af14513c74eb3b8d7606dbac9ffbf/Untitled%204.png)

- `어댑터 패턴`
    - **다양한 방식의 컨트롤러를 사용**하기 위해 적용하는 패턴
        - **ControllerV3, V4 둘 다 사용하고 싶은 경우** 어댑터 패턴을 적용
    - 핸들러
        - 컨트롤러를 포함하는 넓은 범위의 개념
        - 어댑터가 지원하기만 한다면(handle 할 수 있으면), 컨트롤러가 아닌 어떤 것이라도 URL 맵핑해서 사용할 수 있음
    - 실행 과정
        1. 사용할 `핸들러`, (핸들러) `어댑터`를 **프론트 컨트롤러에 등록**
        2. 클라이언트 요청에 해당하는 `핸들러 조회`
        3. 해당 핸들러를 사용할 수 있는 `어댑터 조회`
        4. 어댑터를 통해 `핸들러 실행` (handle) → ModelView 리턴
        5. **ModelView**를 통해 실행 결과를 **렌더링**

### 2. 핸들러 어댑터 인터페이스

```java
public interface MyHandlerAdapter {

    boolean support(Object handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
```

- `support`(handler)
    - **해당 핸들러를 사용할 수 있는(지원하는) 어댑터인지 확인**하는 메소드
- `handle`(requset, response, handler)
    - 해당 핸들러를 사용해 **컨트롤러를 실행**
        
        → 비지니스 로직을 실행하고 뷰 정보를 리턴함 (**ModelView 리턴**)
        
    - **실제 컨트롤러가 ModelView를 리턴하지 못하면** `어댑터가 ModelView를 생성해서라도 리턴`해야함

### 3. 핸들러 어댑터

- `V3` 핸들러 어댑터
    
    ```java
    public class ControllerV3HandlerAdapter implements MyHandlerAdapter{
    
        @Override
        public boolean support(Object handler) {
            return (handler instanceof ControllerV3);
        }
    
        @Override
        public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
            ControllerV3 controller = (ControllerV3) handler;
    
            Map<String, String> paramMap = createParamMap(request);
    
            return controller.process(paramMap);
        }
    }
    ```
    
    ```java
    public interface ControllerV3 {
    
        ModelView process(Map<String, String> paramMap);
    }
    ```
    
    - handle 메소드를 실행하는 경우는 이미 support 메소드를 통해 ControllerV3임을 보장받은 후 이므로 핸들러를 v3 컨트롤러로 변환하면 됨
    - v3 컨트롤러를 실행하기 위해 필요한 매개변수 paramMap을 생성 후 컨트롤러에 전달
        
        → ModelView 리턴
        
- `V4` 핸들러 어댑터
    
    ```java
    public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
    
    		@Override
        public boolean support(Object handler) {
            return (handler instanceof ControllerV4);
        }
    
        @Override
        public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
            ControllerV4 controller = (ControllerV4) handler;
    
            Map<String, String> paramMap = createParamMap(request);
            Map<String, Object> model = new HashMap<>();
    
            "V4 컨트롤러는 ModelView를 리턴해주지 않음!!"
            String viewName = controller.process(paramMap, model);
            ModelView mv = new ModelView(viewName);
            mv.setModel(model);
    
            return mv;
        }
    }
    ```
    
    ```java
    public interface ControllerV4 {
    
        String process(Map<String, String> paramMap, Map<String, Object> model);
    }
    ```
    
    - v4 컨트롤러를 실행하기 위해 필요한 paramMap, model을 생성 후 컨트롤러에 전달
        
        → viewName(논리 이름) 리턴
        
        → `viewName을 이용해 ModelView를 만들어 리턴`해야함
        
        → 다양한 컨트롤러를 지원하는 `어댑터 패턴의 핵심!`
        

### 4. 프론트 컨트롤러 - v5

```java
@WebServlet(name = "frontControllerServletV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerServletV5 extends HttpServlet {

    private Map<String, Object> handlerMappingMap = new HashMap<>();
    private List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServletV5() {
        initHandlerMappingMap();
        initHandlerAdapter();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    private void initHandlerAdapter() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object handler = getHandler(request);
        MyHandlerAdapter myHandlerAdapter = getHandlerAdapter(handler);

        ModelView mv = myHandlerAdapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        MyView myView = viewResolver(viewName);

        myView.render(mv.getModel(), request, response);
    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Object handler = handlerMappingMap.get(requestURI);
        return handler;
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.support(handler)) {
                return handlerAdapter;
            }
        }
        throw new IllegalArgumentException("해당 핸들러의 어댑터가 없습니다.");
    }

    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
```

- 프론트 컨트롤러 V5
    - handlerMappingMap
        - URL에 해당하는 컨트롤러를 저장
    - handlerAdapters
        - 어댑터를 저장
        
        > 이 두 필드만 외부에서 주입받는다면 완벽한 OCP를 지킬 수 있음
        → service 메소드를 수정하지 않고 다양한 핸들러(컨트롤러)를 사용할 수 있음
        > 
    - service() 동작 과정
        - handlerMappingMap에서 URL에 해당하는 컨트롤러를 찾음
        - 해당 컨트롤러를 지원하는 어댑터를 찾음
        - 찾은 어댑터로 컨트롤러를 실행함 (handle)
            
            → ModelView 리턴
            
        - ModelView 리턴으로 컨트롤러 실행 결과를 렌더링
            - viewName을 얻어 MyView 생성
            - MyView에 model을 넘겨 뷰 렌더링

## 4.6 정리

1. v1
    - spring mvc의 핵심개념인 `프론트 컨트롤러` 적용
2. v2
    - 중복되는 `뷰 로직`을 `MyView` 클래스를 사용해 `분리`
3. v3
    - 컨트롤러의 매개변수로 request, response가 아닌 paramMap을 사용
        
        → 서블릿 종속성 제거
        
    - 컨트롤러 실행 결과 `ModelView`를 리턴
        - 비지니스 로직의 실행결과를 저장하는 **model을 포함**
        - **뷰의 논리 이름을 포함**
            
            → 뷰 이름의 중복성 제거
            
    - ModelView에 저장된 뷰의 논리 이름을 `viewResolver`를 통해 **뷰의 물리 이름을 저장한 MyView 객체를 얻음**
    - (수정된)`MyView`가 model에 담긴 데이터와 뷰의 물리 이름을 사용해 **결과를 렌더링**
4. v4
    - **컨트롤러**가 바로 **viewName을 리턴**
5. v5
    - **다양한 컨트롤러(핸들러)를 사용**할 수 있는 `어댑터 패턴`을 적용함
    - 핸들러를 지원하는 어댑터만 있다면 어떤 핸들러든지 URL 맵핑하여 사용할 수 있음
        
        ex) 어노테이션 핸들러 → 어노테이션 핸들러 어댑터 추가해서 사용
        

<aside>
⭐ 버전이 높아질 수록 **프론트 컨트롤러가 복잡**해지고 **컨트롤러는 간단**해진다.
spring MVC 프레임워크는 위와 같은 과정으로 발전해왔다.

</aside>