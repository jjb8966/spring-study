# 6. 스프링 MVC - 기본 기능

# 6.1 [Jar vs War](https://www.notion.so/JAR-vs-WAR-14f31a49d3bb4979b4c1953e6ccdc8dc)

# 6.2 로깅

- 스프링 부트 **로깅 라이브러리**
    - 스프링 부트를 사용하면 자동으로 포함됨
        - **spring-boot-starter-logging**
    - 사용하는 로깅 라이브러리
        - `SLF4J` → **인터페이스**
        - `Logback` → **구현체**
            - 다양한 라이브러리(구현체)가 있는데 스프링 부트는 기본으로 Logback을 제공함
- 로그 선언
    
    ```java
    private final Logger log = LoggerFactory.getLogger(getClass());
    ```
    
    - `롬복 @Slf4j`를 사용하면 위의 로그 선언 없이도 log를 사용할 수 있음
- 로그 호출
    
    ```java
    String name = "Spring";
    
    log.trace("trace log = {}", name);
    log.debug("debug log = {}", name);
    log.info("info log = {}", name);
    log.warn("warn log = {}", name);
    log.error("error log = {}", name);
    ```
    
    - 해당 매핑 요청이 오면 로그가 출력됨
    - 로그 출력 포맷
        - `시간`—`로그 레벨`—`프로세스 ID`—`쓰레드명`—`클래스명`—`로그 메시지`
        
        ![Untitled](6%20%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20-%20%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%209a03a3aebf094f60a1ccc48bf264d3a1/Untitled.png)
        
- `로그 레벨`
    - **trace > debug > info > warn > error**
        - 앞에 있을 수록 중요도 떨어짐
        - 뒤로 갈 수록 중요도 높아짐
            - trace
                - 보통 개발 서버에서 어떤 값인지 확인할 때 사용
                - 이 로그를 서비스 단계에서 계속 호출하면 성능 저하가 있을 수 있음
            - info
                - default
            - error
                - 가장 중요한 정보를 남기는 로그
                - 에러는 꼭 처리해야 하므로 운영 서버에서 사용
    - `로그 레벨 설정`
        - **application.properties**에서 설정할 수 있음 (defalut = info)
        
        ```java
        # 전체 로그 레벨 설정
        logging.level.root=trace
        
        # hello.springmvc 패키지와 하위 패키지 로그 레벨 설정
        logging.level.hello.springmvc=debug
        ```
        
        - 로그 레벨을 설정하면 **설정한 로그 레벨보다 하위 로그**는 **출력되지 않음**
            
            ex) 로그 레벨 : debug → trace 제외한 나머지 로그 모두 출력
            
            ex) 로그 레벨 : warn → warn, error 로그만 출력
            
        - logging.level.root=trace
            
            → 프로젝트의 모든 라이브러리의 로그 레벨을 설정하기 때문에 엄청나게 많은 로그가 출력됨 (debug만 해도 엄청 많다)
            
- 옳바른 로그 사용법
    - log.info("data = " + data); `(X)`
        - 로그레벨이 warn 이상일 경우
            - info 로그를 출력하지 않아도 String의 더하기 연산이 발생함 
            → 성능 저하 가능성 있음
    - **log.info("data = {}", data);** `(O)`
        - 이렇게 사용하면 **의미없는 연산이 발생하지 않음**
            
            ⇒ 이 방식으로 쓸 것!
            
- `로그의 장점`
    - 무조건 출력되는 콘솔 출력과 달리 **설정한 로그 레벨에 따라 조절해서 로그를 확인할 수 있음**
    - 파일이나 네트워크 등에 **로그를 백업**할 수 있고 이 때 **여러 조건에 따라 로그를 분할해서 백업할 수도 있음**
    - **성능도 System.out 보다 좋음**
        
        ⇒ 로그를 꼭 사용하자!
        

# 6.3 요청 매핑

## 1. @RestController

- 컨트롤러 메소드의 리턴 결과
    - HTTP 메시지 바디에 바로 입력됨
    
    ↔ @Controller - String을 리턴하고 뷰 이름으로 인식
    

## 2. @RequestMapping

- @RequestMapping(”URL”)
    - 해당 URL로 요청이 오면 매핑된 컨트롤러의 메소드가 실행됨
    - 다중 설정이 가능
        - @RequestMapping({”/hello-basic”, “hello-go”})
        - 1개의 컨트롤러 메소드가 /hello-basic, /hello-go 모두 처리할 수 있음

## 3. @GetMapping & @PostMapping

- HTTP 메소드를 축약한 어노테이션
    - 특정 HTTP 메소드로 온 URL 요청을 처리함
    - 같은 URL 요청으로 다른 메소드가 실행되도록 할 수 있음
        
        ex) 회원 등록
        
        - @GetMapping(”/add-member”)
            - 회원 등록 폼을 보여주는 메소드 실행
        - @PostMapping(”/add-member”)
            - 회원 등록 폼에서 넘어온 데이터로 회원을 만드는 메소드 실행

## 4. @PathVariable

- URL 경로 자체에 값이 들어간 경우 사용
- 컨트롤러 메소드의 매개변수로 사용
- 사용 예시
    
    ```java
    @GetMapping("/mapping/{userId}")
    public String findUser(@PathVariable("userId") String data) {
        return "data = " + data;
    } 
    ```
    
    - /mapping/userA
        - userId = userA
    - URL의 경로변수 이름과 메소드 파라미터의 변수 이름이 같으면 @PathVariable에서 이름을 생략해도 됨
        
        ```java
        @GetMapping("/mapping/{userId}")
        public String findUser(@PathVariable String userId) {
            return "get userId = " + userId;
        } 
        ```
        
- 다중 사용도 가능
    
    ```java
    @GetMapping("/mapping/{userId}/{username}")
    public String findUser(@PathVariable String userId, @PathVariable String username) {
    } 
    ```
    

## 5. param

- 특정 파라미터 조건 매핑
    - 특정 파라미터가 들어와야 실행됨 (잘 사용x)
    
    ```java
    // 쿼리 파라미터에 params 가 포함되어야 함
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }
    ```
    

## 6. headers

- 특정 헤더 조건 매핑
    - key=value로 지정
    
    ```java
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }
    ```
    
    - 헤더 이름 - mode
    - 헤더 값 - debug

## 7. consumes & produces

- 미디어 타입 조건 매핑
- consumes
    - 요청 메시지의 Content-Type 헤더를 기반으로 매핑
        
        ```java
        @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
        public String mappingConsumes() {
            log.info("mappingConsumes");
            return "ok";
        }
        ```
        
        - 요청 메시지의 Content-Type 헤더값이 application/json
            - 이 요청을 처리하는 컨트롤러 메소드는 요청 시 보낸 json을 consumes
    - Content-Type과 consumes가 맞지 않는 경우
        - HTTP 415 상태코드 - Unsupported Media Type
- produces
    - 요청 메시지의 Accept 헤더를 기반으로 매핑
        
        ```java
        @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_PLAIN_VALUE)
        public String mappingProduce() {
            log.info("mappingProduce");
            return "ok";
        }
        ```
        
        - 요청 메시지의 Accept 헤더값이 text
            - 이 요청을 처리하는 컨트롤러 메소드는 text 결과를 produces
    - Accept와 produces가 맞지 않는 경우
        - HTTP 406 상태코드 - Not Acceptable

# 6.4 HTTP 요청

## 1. 헤더 정보 조회

```java
@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String header(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMethod httpMethod,
            Locale locale,
            @RequestHeader MultiValueMap<String, String> headerMap,
            @RequestHeader("host") String host,
            @CookieValue(value = "myCookie", required = false) String cookie
            ) {

        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("host={}", host);
        log.info("cookie={}", cookie);

        return "ok";
    }
}
```

![Untitled](6%20%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20-%20%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%209a03a3aebf094f60a1ccc48bf264d3a1/Untitled%201.png)

- MultiValueMap
    - 하나의 키로 여러 개의 값을 받을 수 있음
    - accept=[text/html,application/xhtml+xml,….]

## 2. 요청 파라미터 처리

- GET, POST 메소드로 요청이 올 시 사용

### 1. request.getParameter(”파라미터 이름”)

- request로 직접 조회
    
    ```java
    // 옛날 방식
    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
    
        log.info("username={}, age={}", username, age);
    
        response.getWriter().write("ok");
    }
    ```
    

### 2. @RequestParam

- 매개변수로 바로 파라미터를 받음
    
    ```java
    // @RequestParam 사용
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge
    ) {
        log.info("username={}, age={}", memberName, memberAge);
    
        return "ok";
    }
    ```
    
- 파라미터 이름과 매개변수 이름이 같은 경우
    - @RequestParam에 파라미터 이름을 생략해도 됨 → 권장
    
    ```java
    // 요청 파라미터와 같은 이름 사용 시 -> @RequestParam 괄호 내부 생략
    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(@RequestParam String username, @RequestParam int age) {
        log.info("username={}, age={}", username, age);
    
        return "ok";
    }
    ```
    
- required 옵션
    - true (default)
        - 요청 시 해당 파라미터를 필수로 보내야 함
        - 없으면 400 에러 발생
        - 주의!
            - 파라미터 이름만 있고 값을 없는 경우(빈 문자)
                - 에러가 나지 않고 파라미터를 전달받은 것으로 인식함
    - false
        - 해당 파라미터가 없어도 됨
        - 주의!
            - 파라미터가 넘어오지 않는 경우 매개변수에 null이 입력됨
            - 이 때 primitive type이면 null을 넣을 수 없음
                
                → 500 에러 발생
                
            - 해결 방법
                - 매개변수 타입을 Integer로 변경
                - defaultValue 설정
    - defaultValue
        - 파라미터 값이 넘어오지 않을 경우 매개변수에 들어갈 기본값 설정
        - 이미 기본값이 설정되어 있으므로 required는 의미 x
        - 파라미터 값이 빈 문자인 경우에도 defaultValue로 설정한 값 적용됨
    
    ```java
    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true, defaultValue = "no") String username,
            @RequestParam(required = false, defaultValue = "0") int age
    ) {
        log.info("username={}, age={}", username, age);
    
        return "ok";
    }
    ```
    

### 3. 파라미터를 매개변수로 바로 받기 (@RequestParam 생략)

- 너무 많이 생략된 형태 → 권장 x

```java
@ResponseBody
@RequestMapping("/request-param-v4")
public String requestParamV4(String username, int age) {
    log.info("username={}, age={}", username, age);

    return "ok";
}
```

### 4. @ModelAttribute

- `파라미터를 바인딩한 객체`를 **매개변수**로 받음

```java
@ResponseBody
@RequestMapping("/model-attribute-v1")
public String modelAttributeV1(@ModelAttribute HelloData helloData) {
    log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

    return "ok";
}
```

- 작동 원리
    1. HelloData 객체 생성
    2. 요청 파라미터 이름으로 HelloData 객체의 프로퍼티를 찾음
        - 파라미터 - username
            - setUsername() 메소드를 찾음
                - 이 메소드가 있다면 객체는 username이라는 프로퍼티를 가지고 있는 것
    3. 프로퍼티를 찾으면 setter를 호출해서 값을 바인딩함
- 바인딩 오류 (BindException)
    - 객체의 프로퍼티와 파라미터의 데이터 타입이 맞지 않는 경우 발생
- @ModelAttribute(”helloData”)
    - model.addAttribute(”helloData”, helloData)
    - helloData라는 속성으로 helloData 객체를 넣음
    - @ModelAttribute 괄호가 생략되면 뒤에 오는 클래스 이름의 앞글자만 소문자로 바꿔서 모델에 넣음

### 5. 객체를 매개변수로 바로 받기 (@ModelAttribute 생략)

- 너무 많이 생략된 형태 → 권장 x

### @RequestParam, @ModelAttribute 둘 다 생략된 경우

- 1. 단순 타입 (String, int, Integer) → @RequestParam
- 2. 나머지 → @ModelAttribute
    
    cf) argument resolver로 지정된 타입은 반드시 @ModelAttribute를 써줘야 함 (뒤에서 자세히)
    

## 2. 메시지 바디 처리 - String

- HTTP API에서 주로 사용
    - json, xml, text…
    - 보통 데이터 형식은 json
- POST, PUT, PATCH 메소드 사용
- 메시지 바디의 데이터는 @RequestParam, @ModelAttribute를 사용할 수 없음

### 1. request.getInputStream()

- request에서 직접 InputStream 조회
    
    ```java
    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    
        log.info("message body = {}", messageBody);
    
        response.getWriter().write("ok");
    }
    ```
    
    1. inputStream 객체 얻기
    2. inputStream → String

### 2. 매개변수로 직접 InputStream 받기

- 매개변수로 Writer도 직접 받을 수 있음
    
    ```java
    @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
    
        log.info("message body = {}", messageBody);
    
        responseWriter.write("ok");
    }
    ```
    

### 3. HttpEntity<String>

- `HTTP 헤더와 바디`를 **간편하게 조회**할 수 있는 클래스
- HttpEntity에 데이터를 넣어서 리턴할 수도 있음
    
    ```java
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) {
        String messageBody = httpEntity.getBody();
        HttpHeaders headers = httpEntity.getHeaders();
    
        log.info("message body = {}", messageBody);
        log.info("headers = {}", headers);
    
        return new HttpEntity<>("ok");
    }
    ```
    
- RequestEntity, ResponseEntity
    - HttpEntity를 상속받음
    - `RequestEntity`
        - 요청 메시지 entity
        - **HTTP 메소드**, **URL 정보** 추가로 얻을 수 있음
    - `ResponseEntity`
        - 응답 메시지 entity
        - **HTTP 상태 코드를 설정**을 추가로 할 수 있음

> 메시지 컨버터
HTTP 메시지 바디 → String, 객체 변환시켜줌 (뒤에서 자세히)
> 

### 4. @RequestBody

- `메시지 바디만` **편리하게 조회**하는 어노테이션
    - 헤더 정보가 필요하다면 @RequestHeader 또는 HttpEntity를 사용해 조회할 수 있음
- 메시지 바디를 조회하는 것이므로 파라미터와는 아무런 상관이 없음 (@RequestParam, @ModelAttribute)
- **@ResponseBody**를 메소드 앞에 붙이면 메소드 **리턴 결과를 바로 응답 메시지 바디에 넣음**
    
    ```java
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public String requestBodyStringV4(@RequestBody String messageBody) {
        log.info("message body = {}", messageBody);
    
        return "ok";
    }
    ```
    

## 3. 메시지 바디 처리 - JSON

### 1. request.getInputStream()

- 문자로 된 JSON 데이터를 Jackson 라이브러리인 objectMapper를 사용해 자바 객체로 변환
- `JSON → 객체` 변환 과정
    - InputStream → String → Object
    
    ```java
    @RequestMapping("/request-body-json-v1")
    public void requestBodyJsonV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
    
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("String Data : {}", messageBody);
    
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("Object Data : {}", helloData);
    
        response.getWriter().write("ok");
    }
    ```
    

### 2. @RequestBody로 String 받기

- String → Object
    
    ```java
    @ResponseBody
    @RequestMapping("/request-body-json-v2")
    public String requestBodyJsonV2(@RequestBody String messageBody) throws IOException {
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("Object Data : {}", helloData);
    
        return "ok";
    }
    ```
    

### 3. @RequestBody로 바로 Object 받기 → 많이 사용

- 매개변수로 **@RequestBody Object object** 형식으로 바로 전환
    - 이렇게 사용할 수 있는 이유는 `메시지 컨버터`가 **바디의 내용을 문자 또는 객체로 변환**해주기 때문임
    - `HTTP 요청` 시 `Content-Type`이 **application/json**인지 꼭 확인해야 함
        - 그래야 **JSON을 처리할 수 있는 메시지 컨버터가 실행됨**
- **@RequestBody는 생략 불가**
    - 매개변수로 객체를 받는데 어노테이션을 생략하면 **@ModelAttribute가 적용됨**
    
    ```java
    @ResponseBody
    @RequestMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) throws IOException {
        log.info("Object Data : {}", helloData);
    
        return "ok";
    }
    ```
    
- `@ResponseBody`를 사용해 **객체를 메시지 바디에 넣어서** 보낼 수도 있음
    
    ```java
    @ResponseBody
    @RequestMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData helloData) throws IOException {
        helloData.setAge(100);
        log.info("Object Data : {}", helloData);
    
        return helloData;
    }
    ```
    

### 4. HttpEntity<Object>

- HttpEntity의 getBody()로 객체를 얻을 수도 있음
    
    ```java
    @ResponseBody
    @RequestMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) throws IOException {
        HelloData helloData = httpEntity.getBody();
        log.info("Object Data : {}", helloData);
    
        return "ok";
    }
    ```
    
- `@ResponseBody`를 사용해 HttpEntity를 메시지 바디에 넣어서 보낼 수도 있음
    - 객체를 넣는것과 똑같은 결과
    - 객체를 넣는게 더 편해서 굳이 쓸 필요는 없을듯

# 6.5 HTTP 응답

> 스프링 서버에서 응답 데이터를 만드는 3가지 방법
> 

## 1. 정적 리소스

- 정적이 HTML, CSS, JS 등의 리소스
- 스프링 부트는 class path (`resources`)에  다음 **4가지 디렉토리에 저장된 정적 리소스를 제공함**
    - 직접 URL로 접근할 수 있음
    1. `/static`
    2. /public
    3. /resources
    4. /META_INF/resources
    
    ex) src/main/`resources`/`static`**/basic/hello.html**
    
    → localhost:8080**/basic/hello.html**
    

## 2. 뷰 템플릿 사용

- 뷰 템플릿을 거쳐 **HTML을 생성**하고 **뷰가 응답을 만들어서 전달함**
- 뷰 템플릿 경로
    - src/main/`resources`/`templates`
    - 여기에 있는 html 파일을 컨트롤러가 호출하면 컨트롤러 실행 결과 데이터를 뷰가 동적으로 렌더링 해줌
    
    ex) 컨트롤러 메소드 return “/hi/hello”; (@ResponseBody 없는 그냥 컨트롤러)
    
    → src/main/`resources`/`templates`**/hi/hello.html**
    
    해당 경로에 hello.html 파일 있으면 렌더링하여 보여줌
    
- 컨트롤러 메소드 리턴 타입
    1. String
        - 뷰 리졸버가 실행되어 뷰를 찾고 렌더링함
    2. void - 권장x
        - 요청 URL을 사용해 논리 뷰 이름으로 사용
            
            ex) **/response/hello**
            
            → templates**/response/hello**
            
    3. HTTP 메시지 바디에 직접 입력
        - @ResponseBody, HttpEntity 등을 이용해 메시지 바디에 직접 데이터를 넣음

## 3. HTTP 메시지 사용

> `HTTP API` 같은 경우 **메시지 바디에** HTML이 아니라 **JSON 형식의 데이터**를 넘겨줘야 함
> 

### 1. response.getWriter.write()

- response 객체를 사용해 메시지 바디 입력 → 옛날 방식
    
    ```java
    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }
    ```
    

### 2. @ResponseBody

- string 리턴
    
    ```java
    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() throws IOException {
        return "ok";
    }
    ```
    
- object 리턴
    - @ResponseStatus(상태 상수)로 HTTP 상태코드 지정할 수 있음
    
    ```java
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2() throws IOException {
        HelloData helloData = new HelloData();
        helloData.setAge(10);
        helloData.setUsername("joo");
        return helloData;
    }
    ```
    

### 3. [ResponseEntity](6%20%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20-%20%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%209a03a3aebf094f60a1ccc48bf264d3a1.md) (HttpEntity)

- ResponseEntity 생성하여 리턴
    
    ```java
    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() throws IOException {
        return new ResponseEntity<>("hi", HttpStatus.OK);
    }
    ```
    
    ```java
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() throws IOException {
        HelloData helloData = new HelloData();
        helloData.setAge(10);
        helloData.setUsername("joo");
        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }
    ```
    

# 6.6 HTTP 메시지 컨버터

> `메시지 바디에 있는 데이터` ↔ `자바 코드` 변환해주는 역할
> 

## 1. 요청 & 응답

- 요청 & 응답 시 **HTTP 메시지 바디와 자바 코드를 변환**할 때 **메시지 컨버터가 동작함**
    
    ![Untitled](6%20%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20-%20%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%209a03a3aebf094f60a1ccc48bf264d3a1/Untitled%202.png)
    
    - 요청
        - `@RequestBody` & `HttpEntity (ResponseEntity)`
        - HTTP 메시지 바디 → 컨트롤러 메소드의 매개변수
    - 응답
        - `@ResponseBody` & `HttpEntity (RequestEntity)`
        - 컨트롤러 메소드의 리턴 결과 → HTTP 메시지 바디

## 2. HTTP 메시지 컨버터 인터페이스

```java

public interface HttpMessageConverter<T> {

	boolean canRead(Class<?> clazz, @Nullable MediaType mediaType);

	boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType);

	T read(Class<? extends T> clazz, HttpInputMessage inputMessage)
			throws IOException, HttpMessageNotReadableException;

	void write(T t, @Nullable MediaType contentType, HttpOutputMessage outputMessage)
			throws IOException, HttpMessageNotWritableException;

}
```

- `canRead()` & `canWrite()`
    - 해당 클래스 타입, 미디어 타입을 지원하는지 체크하는 메소드
        - **canRead()** → 메시지 바디를 자바 코드로 읽을 수 있음
        - **canWrite()** → 자바 코드를 메시지 바디로 쓸 수 있음
- `read()` & `write()`
    - 위 두 메소드를 통해 메시지 컨버터가 작동할때 실행되는 메소드
        - **read()** → 메시지 바디를 자바 코드로 읽음
        - **write()** → 자바 코드를 메시지 바디로

## 3. 스프링 부트 기본 메시지 컨버터

> 스프링 부트에 기본으로 등록되는 `HttpMessageConverter 구현체`
> 
- `우선순위`에 따라 순차적으로 조회 (중요한것 3개)
    - 만족하지 않을 시 다음 메시지 컨버터로 조회함
    1. `ByteArray`HttpMessageConverter
        - 클래스 타입
            - **byte[]**
        - 미디어 타입
            - ***/***
        
        ex) byte[] → application/json
        
        ex) text/plain → byte[] 
        
    2. `String`HttpMessageConverter
        - 클래스 타입
            - **String**
        - 미디어 타입
            - ***/***
        
        ex) String → text
        
        ex) application/json → String
        
    3. `MappingJackson2`HttpMessageConverter
        - 클래스 타입
            - 객체 또는 HashMap
            - **byte[], string을 제외한 나머지**
        - 미디어 타입
            - **application/json**
            
            ⇒ `byte[], string을 제외한 나머지 타입`은 **application/json으로만 변환**될 수 있다
            
            ⇒ ~~application/json은 byte[], string을 제외한 나머지 타입으로만 변환될 수 있다.~~
            
        
        ex) HelloData → application/json (O)
        
        ex) HelloData → text/plain (X)
        
        ex) application/json → HelloData (O)
        
        ex) application/json → String (O)
        

### HTTP 요청 → 자바 코드

1. HTTP 요청 & 컨트롤러에서 @RequestBody, HttpEntity 사용
2. ByteArray 메시지 컨버터의 canRead() 호출하여 읽을 수 있는지 판단
    - 클래스 타입 & 미디어 타입을 보고 지원할 수 있는지 판단
    1. 지원한다면
        - read() 메소드를 호출해 메시지 바디를 자바 코드로 변환
    2. 지원하지 않으면
        - 다음 메시지 컨버터의 canRead() 호출
            - String → MappingJackson2→…

### 자바 코드 → HTTP 응답

1. 컨트롤러에서 @ResponseBody, HttpEntity 사용하여 리턴
2. ByteArray 메시지 컨버터가 canWrite() 호출하여 메시지 바디에 쓸 수 있는지 판단
    - 클래스 타입 & 미디어 타입을 보고 지원할 수 있는지 판단
    1. 지원한다면
        - write() 메소드를 호출해 자바 코드를 메시지 바디로 변환
    2. 지원하지 않으면
        - 다음 메시지 컨버터의 canWrite() 호출
            - String → MappingJackson2→…

# 6.7 핸들러 어댑터 구조

## 1. 동작 방식

- 스프링의 어노테이션 기반 컨트롤러
    - `다양한 매개변수`를 받을 수 있음
        
        → by `ArgumentResolver`
        
    - `다양한 리턴타입`을 가질 수 있음
        
        → by `ReturnValueHandler`
        
- `스프링 서버 동작 방식 (총 정리)`
    
    ![Untitled](6%20%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20-%20%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%209a03a3aebf094f60a1ccc48bf264d3a1/Untitled%203.png)
    
    ![Untitled](6%20%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20-%20%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%209a03a3aebf094f60a1ccc48bf264d3a1/Untitled%204.png)
    
    ![Untitled](6%20%E1%84%89%E1%85%B3%E1%84%91%E1%85%B3%E1%84%85%E1%85%B5%E1%86%BC%20MVC%20-%20%E1%84%80%E1%85%B5%E1%84%87%E1%85%A9%E1%86%AB%20%E1%84%80%E1%85%B5%E1%84%82%E1%85%B3%E1%86%BC%209a03a3aebf094f60a1ccc48bf264d3a1/Untitled%205.png)
    
    1. 클라이언트로부터 HTTP 메세지 요청이 들어옴
    2. DispatcherServlet(Front Controller) 호출
    3. 해당 URL에 맞는 핸들러(컨트롤러) 조회
    4. 찾은 핸들러를 지원하는 핸들러 어댑터 조회
    5. 핸들러 어댑터가 핸들러 실행을 위해 필요한 매개변수를 생성해주는 `ArgumentResolver` 호출
        - ArgumentResolver가 HTTP 요청으로부터 매개변수 생성
        - 컨트롤러에서 **@RequestBody, HttpEntity을 사용하는 경우** `HTTP 메시지 컨버터를 호출`하여 매개변수 생성
    6. 생성한 매개변수를 핸들러에게 넘겨주고 핸들러 실행
    7. 핸들러 실행이 완료되며 핸들러는 다양한 리턴 타입의 결과를 `ReturnValueHandler`에게 넘겨줌
        - ReturnValueHandler는 넘겨받은 자바 코드를 적절하게 처리함
        - 컨트롤러에서 **@ResponseBody, HttpEntity를 사용하는 경우** `HTTP 메시지 컨버터를 호출`하여 메시지 바디에 들어갈 데이터를 처리
    8. ReturnValueHandler 생성한 결과물을 핸들러 어댑터가 넘겨받고 DispatcherServlet에게 ModelAndView를 반환
    9. DispatcherServlet이 viewResolver를 호출해 View를 얻음
    10. view.render(model) 호출해 결과물을 렌더링해서 HTTP 응답 메시지를 보냄

## 2. ArgumentResolver

> 컨트롤러가 필요로 하는 매개변수를 생성해주는 것
> 
- ArgumentResolver 인터페이스
    - 원래 이름은 HandlerMethodArgumentResolver
    
    ```java
    public interface HandlerMethodArgumentResolver {
    
    	boolean supportsParameter(MethodParameter parameter);
    
    	@Nullable
    	Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
    			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception;
    
    }
    ```
    
    - supportsParameter()
        - 해당 매개변수를 만들 수 있는지 판단하는 메소드
    - resolveArgument()
        - 매개변수를 만드는 메소드
- 핸들러 어댑터의 요청에 따라 다양한 매개변수들을 만들어 줌
    - 스프링은 30개가 넘는 ArgumentResolver 구현체를 제공함
        - HttpServletRequest, Model, @RequestParam….
- 특히 그 중 **@RequestBody, HttpEntity을 사용하는 경우**
    - `HTTP 메시지 컨버터를 호출`하여 객체를 생성함
- 매개변수가 다 만들어지면 컨트롤러 호출 시 생성된 객체가 핸들러로 넘어감

> 원한다면 직접 인터페이스를 구현하여 원하는 ArgumentResolver를 만들 수 있음 (MVC 2편 로그인에서 진행)
> 

## 3. ReturnValueHandler

> 핸들러가 넘겨준 다양한 리턴 타입의 결과를 처리해서 메시지 바디에 들어갈 데이터로 만들어주는 것
> 
- ReturnValueHandler 인터페이스
    - 원래 이름은 HandlerMethodReturnValueHandler
    
    ```java
    public interface HandlerMethodReturnValueHandler {
    
    	boolean supportsReturnType(MethodParameter returnType);
    
    	void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
    			ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception;
    
    }
    ```
    
    - supportsReturnType()
        - 컨트롤러로부터 넘겨받은 데이터를 처리할 수 있는지 판단하는 메소드
    - handleReturnValue()
        - 컨트롤러로부터 넘겨받은 데이터를 처리하는 메소드
- 리턴 타입에 따라 적절한 처리를 함
    - return string → 뷰 이름으로 인식하고 처리
    - return 객체 → json으로 변환해줌
    - 스프링은 10개가 넘는 ReturnValueHandler를 제공함
        - ModelAndView, String, @ResponseBody, HttpEntity…
- 특히 그 중 **@ResponseBody, HttpEntity를 사용하는 경우**
    - `HTTP 메시지 컨버터를 호출`하여 메시지 바디에 들어갈 데이터를 처리
- 모든 처리가 끝나면 데이터를 핸들러 어댑터에게 넘겨줌

### RequestResponseBodyMethodProcessor & HttpEntityProcessor

- 둘 다 **ArgumentResolver 이면서 ReturnValueHandler** (둘 다 구현함)
- RequestResponseBodyMethodProcessor
    
    → @RequestBody, @ResponseBody 사용 시
    
- HttpEntityProcessor
    
    → HttpEntity 사용 시
    

## 4. 확장

- ArgumentResolver, ReturnValueHandler, HttpMessageConverter 모두 인터페이스
    - 필요하다면 언제든 구현하여 구현체를 추가할 수 있음
    - 강력한 다형성!
- 확장하고 싶을 시
    - WebMvcConfigurer를 상속받아 스프링 빈으로 등록하면 됨

> 사실 확장할 일은 많지 않음..
>