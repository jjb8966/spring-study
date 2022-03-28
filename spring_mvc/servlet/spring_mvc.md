# 스프링 MVC

### 목차
### [1. 자바 백엔드 웹 기술 역사](#1-자바-백엔드-웹-기술-역사-1)
### [2. 서블릿](#2-서블릿-2)
### [3. 서블릿, JSP, MVC 패턴](#3-서블릿-jsp-mvc-패턴-1)

# 1. 자바 백엔드 웹 기술 역사

## 1.1 웹 서버 & 웹 애플리케이션 서버

### 1. 웹 서버

- HTTP 기반
- `정적 리소스` 제공
- ex) APACHE, NGINX

### 2. 웹 애플리케이션 서버

- HTTP 기반
- 웹 서버 기능 + `애플리케이션 로직 수행`
- ex) Tomcat, Jetty, Undertow

### 3. 웹 시스템 구성

1. WAS, DB
- WAS가 정적 리소스, 애플리케이션 로직을 모두 제공할 수 있기 때문에 2가지만으로도 웹 시스템 구성할 수 있음
- 단점
    1. WAS가 너무 많은 역할을 담당
    2. 애플리케이션 로직이 정적 리소스때문에 수행이 어려울 수 있음
    3. WAS 장애 시 오류 화면도 띄울 수 없음
1. WEB, WAS, DB
- 웹 서버를 사용해 정적 리소스를 따로 제공
- 동적인 처리가 필요하면 WAS가 애플리케이션 로직 작동
- 장점
    1. WAS가 중요한 애플리케이션 로직만 처리함
    2. 리소스를 효율적으로 관리할 수 있음
        - 정적 리소스가 많으면 웹 서버를 증설하고, 애플리케이션 리소스가 많이 사용되면 WAS를 증설함
    3. 웹 서버는 잘 죽지 않고 WAS는 잘 죽음

       → WAS 장애 시 웹 서버가 오류화면을 제공할 수 있음


## 1.2 서블릿

### 1. 서버에서 처리하는 업무

- 비지니스 로직 외에 처리해야할 일이 매우 많음

  ex) 서버 TCP/IP 연결 대기, HTTP 요청 메시지 파싱, 응답 메시지 생성 등

- 서블릿을 지원하는 WAS 사용 시 비지니스 로직을 제외한 나머지 작업을 서블릿이 대신 해줌
    - 개발자는 비지니스 로직만 짜면 됨

### 2. 서블릿

```java
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

		@Override
		protected void service(HttpServletRequest request, 
													HttpServletResponse response){
		}
}
```

- /hello URL이 호출되면 서블릿 코드가 실행됨
- HTTP 요청과 응답을 편리하게 사용할 수 있게 해주는 HttpServletRequest과 HttpServletResponse
- 개발자는 편리하게 HTTP를 사용할 수 있음
- HTTP 요청 & 응답 과정

  ![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/b8cbe7a8-17c2-4f1a-b3d9-38b293487176/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220328%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220328T084118Z&X-Amz-Expires=86400&X-Amz-Signature=ef22f5c382ad0bffd668d1f0271f2bd9c2e47f80693448d154e03cd88ab11409&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

    1. HTTP 요청
    2. WAS가 request, response 객체를 만들어서 서블릿 객체를 호출
    3. 개발자가 request 객체에 HTTP 요청 정보를 편리하게 꺼내서 사용
    4. 개발자가 response 객체에 HTTP 응답 정보를 편리하게 입력
    5. WAS는 response 객체에 담겨있는 내용으로 HTTP 응답 메시지를 생성

### 3. 서블릿 컨테이너

- 톰캣처럼 `서블릿을 지원하는 WAS`를 서블릿 컨테이너라고 함
- 서블릿 객체의 생명주기를 관리함
- 서블릿 객체는 `싱글톤`으로 관리됨
    - 최초 로딩 시 1개의 서블릿 객체를 만들어 놓음
    - 여러 고객이 요청 시 동일한 서블릿 객체에 접근
    - `공유 변수` 사용에 주의해야 함
- JSP도 서블릿으로 변환되어 사용됨
- 동시 요청을 위해 `멀티 쓰레드 지원`

## 1.3 동시 요청 - 멀티 쓰레드

### 1. 쓰레드

- main 메소드 실행 → main이라는 이름의 쓰레드가 실행됨
- 동시 처리가 필요한 경우 쓰레드를 추가로 생성해야 함

### 2. 단일 쓰레드

- 고객의 요청으로 하나의 쓰레드가 사용되고 있을 경우
    - 쓰레드가 서블릿 객체를 사용해 응답을 받고 쓰레드를 반환 함
- 먼저 들어온 요청의 처리가 끝나지 않은 상태에서 추가 요청이 들어오는 경우
    - 다른 고객은 먼저 들어온 고객의 요청이 끝나서 쓰레드가 반환될 때까지 대기해야 함 → 처리 지연
    - 이 때 먼저 들어온 요청에 오류가 생기면?
        - 이 후에 들어오는 요청들도 처리되지 못하기 무한 대기하게 됨

### 3. 요청마다 쓰레드 생성

- 장점
    - 동시 요청을 처리할 수 있음
    - 하나의 쓰레드가 지연되어도 나머지 쓰레드는 정상 작동함
- 단점
    - 쓰레드 생성 비용이 비쌈
    - 쓰레드 컨텍스트 스위칭 비용이 들어감
    - 쓰레드 생성 제한이 없음 → 요청이 매우 많은 경우 서버가 죽을 수 있음

  ⇒ 이런 단점들을 해결하기 위해 `쓰레드 풀` 사용


### 4. 쓰레드 풀

- 생성 가능한 `쓰레드의 최대치`만큼 미리 생성해 쓰레드 풀에 보관하고 관리함
    - 톰캣은 최대 200개
    - `최대 쓰레드`를 적절하게 설정하는게 `WAS의 주요 튜닝 포인트`

      → 성능 테스트를 통해 적절한 최대 쓰레드를 정해야 함

        - 너무 낮게 설정하면?
            - 서버 리소스는 여유롭지만 요청이 조금만 많아져도 금방 응답이 지연됨
        - 너무 높게 설정하면?
            - 동시 요청이 너무 많아 리소스 임계점 초과 시 서버가 다운될 수 있음
                - 클라우드면 장애 발생 시 일단 서버를 늘리고 이후에 튜닝함
- 쓰레드가 필요한 경우 쓰레드 풀에서 꺼내 사용하고 종료되면 쓰레드를 다시 반납함
- 쓰레드 풀의 모든 쓰레드가 사용 중이라면 `요청을 거절`하거나 `대기`하도록 설정할 수 있음
- 장점
    - 쓰레드를 미리 생성해서 보관하기 때문에 쓰레드 생성, 종료 비용을 절약할 수 있고 응답 시간이 빠름
    - 생성 가능한 쓰레드의 최대치가 있으므로 여러 요청이 들어와도 안전하게 처리할 수 있음

### 5. WAS → 멀티 쓰레드 지원

- 개발자는 복잡한 멀티 쓰레드 관련 코드를 신경 쓰지 않아도 됨. WAS가 처리해 줌
- 개발자는 싱글 쓰레드 프로그래밍을 하듯이 편하게 개발하면 됨
    - 싱글톤 객체의 공유 변수는 주의해야 함

## 1.4 HTML, HTTP API, CSR, SSR

### 1. 정적 리소스

- 고정 HTML 파일, CSS, JS, 이미지 등

### 2. HTML

- WAS는 동적으로 필요한 HTML 파일을 생성해서 전달해 줄 수 있음

### 3. HTTP API

- HTML이 아니라 데이터만 주고 받음 (주로 JSON)
- UI 화면이 필요하면 클라이언트가 별도로 처리
- 웹 뿐만 아니라 다양한 시스템에서 호출함
    1. 웹 ↔ 서버
    2. 서버 ↔ 서버
    3. 앱 ↔ 서버

### 4. SSR & CSR

- SSR (Server Side Rendering)
    - HTML 최종 결과를 서버에서 만들어 웹 브라우저에 전달함
    - 주로 `정적인 화면`에 사용
        - JSP, 타임리프 → 백엔드
- CSR (Client Side Rendering)
    - HTML 결과를 JS를 사용해 웹 브라우저에서 동적으로 생성해 적용
    - 주로 `동적인 화면`에 사용
        - React, Vue.js → 프론트

## 1.5 자바 웹 기술 역사

### 1. 과거 기술

> 서블릿 → JSP → 서블릿 & JSP 조합 MVC → 다양한 MVC
>

### 2. 현재 사용 기술

- 어노테이션 기반의 `스프링 MVC`
- 스프링 부트
    - 빌드 결과(Jar)에 WAS 포함 → 빌드 배포 단순화

### 3. 최신 기술

- Web Servlet → Spring MVC
- Web Reactive → Spring WebFlux
    - 비동기 넌 블러킹 처리
    - 최소 쓰레드로 최대 성능
        - ex) 4코어 CPU → 4~5 쓰레드 → 쓰레드 컨텍스트 스위칭 비용 최소화
    - 함수형 스타일로 개발
    - 서블릿 기술 사용 x
    - 단점
        - 난이도 높음, RDB 지원 부족, 아직 많이 사용 x

      ⇒ 아직까진 Spring MVC도 충분히 빠르다.


### 4. 자바 뷰 템플릿 (템플릿 엔진) 역사

1. JSP
    - 속도 느림
    - 기능 부족
2. 프리마커, 벨로시티
    - 속도 빠름
3. `타임리프(Thymleaf)`
    - HTML 모양 유지하면서 뷰 템플릿 적용
    - `스프링 MVC와 강력한 기능 통합`

# 2. 서블릿

## 2.1 서블릿 실행 환경 설정

- 스프링 부트 환경에서 서블릿 등록
    - 원래는 톰캣같은 WAS를 직접 설치하고 그 위에 서블릿 코드를 클래스로 빌드해서 올린 다음 톰캣을 실행해야 함
    - 스프링 부트는 톰캣 서버를 내장하고 있으기 때문에 별도로 톰캣을 설치하지 않아도 됨
    - Packaging : War (Jar 아님) → JSP를 실행하려면 War로 해야함
- 스프링 부트는 서블릿을 직접 등록해서 사용할 수 있도록 `@ServletComponentScan`을 지원함
- 서블릿 어노테이션
    - @WebServlet
        - name - 서블릿 이름
        - urlPatterns : URL 매핑

          → 해당 url이 호출되면 서블릿 컨테이너가 service 메소드를 실행함

- HTTP 요청 메시지 로그 확인
    - application.properties에 다음을 추가
        - logging.level.org.apache.coyote.http11=debug
        - 모든 요청 정보를 남김 → 성능 저하를 유발할 수 있으므로 개발 단계에서만 적용

## 2.2 HttpServletRequest

### 1. 개요

- 역할
    - `HTTP 요청 메시지를 파싱`해서 HttpServletRequest 객체에 담아서 제공
    - HTTP 요청 메시지
        1. start line
            - HTTP 메소드
            - URL
            - 쿼리 스트링
            - 스키마, 프로토콜
        2. header
            - 헤더 조회
        3. message body
            - form 파라미터 형식 조회 → getParameter()
            - 데이터 직접 조회 → JSON 형식 데이터
- 기능
    1. 임시 저장소 기능
        - HTTP 요청의 시작부터 끝까지 유지됨
            - 저장 : request.setAttribute(name, value)
            - 조회 : request.getAttribute(name)
    2. 세션 관리 기능
        - request.getSession(create:true)

### 2. 메소드

- start line 정보
    1. getMethod()
        - HTTP 메소드를 조회
        - GET, POST..
    2. getProtocol()
        - HTTP 버전 조회
        - HTTP/1.1
    3. getSchema()
        - http, https, ftp와 같은 프로토콜
    4. getRequestURL()
        - 전체 URL 리턴
        - http://localhost:8080/request-header
    5. getRequestURI()
        - URL에서 스키마, 서버이름, 포트번호를 제외한 나머지 주소 리턴
        - /request-header
    6. getQueryString()
    7. isSecure()
        - https와 같은 보안 채널의 사용 여부
- header 정보
    - getHeader(headerName)
    - header 편리하게 조회
        1. getServerName()
            - 서버 이름
            - localhost
        2. getServerPort()
            - 서버 포트
            - 8080
        3. getLocale()
            - 지역 정보
            - getLocal : ko_KR
        4. getCookies()
            - 모든 쿠키값
        5. getContentType()
        6. getContentLength()
        7. getContentEncoding()
- 기타 정보 (HTTP 메시지 정보 x)
    - 클라이언트 정보
        1. getRemoteHost()
        2. getRemoteAddr()
        3. getRemotePort()
    - 서버 정보
        1. getLocalName()
        2. getLocalAddr()
        3. getLocalPort()

### 3. 요청 데이터

- content-type
    - HTTP 메시지 바디의 데이터 형식을 지정
    - GET 쿼리 파라미터 형식은 메시지 바디를 사용하지 않으므로 content-type 없음
- HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 3가지 방법
1. GET 쿼리 파라미터
    - 메시지 바디 없이 `쿼리 파라미터`에 데이터를 포함해서 전달
        - 검색, 필터 등에서 많이 사용하는 방식
    - ?로 시작하고 파라미터는 &로 구분
    - 쿼리 파라미터 조회 메소드
        1. getParameter()
            - 단일 파라미터 조회
        2. getParameterNames()
            - 파라미터 이름들 모두 조회
        3. getParameterMap()
            - 파라미터를 Map으로 조회
        4. getParameterValues()
            - 복수 파라미터 조회
        - 복수 파라미터에서 단일 파라미터 조회를 하는 경우
            - getParameterValues()의 첫 번째 값을 반환
2. POST HTML Form
    - content-type : application/x-www-form-urlencoded
    - 메시지 바디에 `쿼리 파라미터 형식`으로 전달
        - 쿼리 파라미터 조회 메소드를 사용할 수 있음
    - 웹 브라우저에서 HTTP 요청 메시지 생성해서 서버로 전달
3. API 메시지 바디
    - 메시지 바디에 데이터를 직접 담아서 요청
    - HTTP API에서 주로 사용하는 방식
    - POST, PUT, PATCH
    1. 단순 텍스트
        - 텍스트를 메시지 바디에 넣어서 보내는 경우
        - content-type : text/plain
        - InputStream을 사용해 읽음

            ```java
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            ```

    2. JSON
        - JSON 형식의 데이터를 메시지 바디에 넣어서 보내는 경우
        - content-type : application/json
        - JSON 형식의 데이터를 파싱해서 자바 객체로 변환하여 읽음
            - Spring MVC는 기본으로 JSON 변환 라이브러리로 Jackson 라이브러리 (ObjectMapper)를 제공

            ```java
            private ObjectMapper objectMapper = new ObjectMapper();
            
            ServletInputStream inputStream = request.getInputStream();
            String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            
            HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
            
            System.out.println("helloData.userName : " + helloData.getUserName());
            System.out.println("helloData.age : " + helloData.getAge());
            ```


## 2.3 HttpServletResponse

### 1. 개요

- 역할
    1. HTTP 응답 메시지 생성
        1. HTTP 응답 코드 지정
        2. 헤더 생성
        3. 바디 생성
    2. 편의 기능 제공
        - content-type
        - 쿠키
        - redirect

### 2. 메소드

- start line 지정
    - setStatus(`HttpServletResponse.*SC_OK*`) → 다양한 상수 존재
- 헤더 생성
    - setHeader(헤더이름, 헤더내용)

        ```java
        response.setHeader(”Content-Type”, “text/plain;charset=utf-8”);
        ```

- 바디 생성

    ```java
    PrintWriter writer = response.getWriter();
    writer.write("ok");
    ```

- 편의 기능
    - content

        ```java
        // = response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        ```

    - cookie

        ```java
        // = response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); //600초
        response.addCookie(cookie);
        ```

    - redirect

        ```java
        //response.setStatus(HttpServletResponse.SC_FOUND); //302
        // = response.setHeader("Location", "/basic/hello-form.html");
        response.sendRedirect("/basic/hello-form.html");
        ```


### 3.응답 데이터

1. 단순 텍스트

    ```java
    response.setContentType("text/plain");
    response.setCharacterEncoding("utf-8");
    
    PrintWriter writer = response.getWriter();
    writer.write("ok");
    ```

2. HTML

    ```java
    response.setContentType("text/html");
    response.setCharacterEncoding("utf-8");
    
    PrintWriter writer = response.getWriter();
    
    writer.println("<html>");
    writer.println("<body>");
    writer.println(" <div>안녕?</div>");
    writer.println("</body>");
    writer.println("</html>");
    ```

3. API JSON

    ```java
    response.setContentType("application/json");
    response.setCharacterEncoding("utf-8");
    
    HelloData helloData = new HelloData();
    helloData.setUserName("Kim");
    helloData.setAge(20);
    
    String result = objectMapper.writeValueAsString(helloData);
    
    response.getWriter().write(result);
    ```

# 3. 서블릿, JSP, MVC 패턴

## 3.1 회원관리 웹 애플리케이션

- Member class

    ```java
    @Getter
    @Setter
    public class Member {
    
        private Long id;
        private String userName;
        private int age;
    
        public Member() {
        }
    
        public Member(String userName, int age) {
            this.userName = userName;
            this.age = age;
        }
    }
    ```

- MemberRepository

    ```java
    public class MemberRepository {
    
        private static Map<Long, Member> store = new HashMap<>();
        private static Long sequence = 0L;
    
        private static final MemberRepository instance = new MemberRepository();
    
        private MemberRepository() {
        }
    
        public static MemberRepository getInstance() {
            return instance;
        }
    
        public Member save(Member member) {
            member.setId(++sequence);
            store.put(member.getId(), member);
            return member;
        }
    
        public Member findById(Long id) {
            return store.get(id);
        }
    
        public List<Member> findAll() {
            return new ArrayList<>(store.values());
        }
    
        public void clearStore() {
            store.clear();
        }
    }
    ```


## 3.2 서블릿으로 만들기

- MemberSaveServlet.class

    ```java
    @WebServlet(name = "memberSaveServlet", urlPatterns = "/servlet/members/save")
    public class MemberSaveServlet extends HttpServlet {
    
        MemberRepository memberRepository = MemberRepository.getInstance();
    
        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            response.setContentType("text/html");
            response.setCharacterEncoding("utf-8");
    
            String userName = request.getParameter("userName");
            int age = Integer.parseInt(request.getParameter("age"));
    
            Member member = new Member(userName, age);
            System.out.println("member = " + member);
            memberRepository.save(member);
    
            PrintWriter w = response.getWriter();
            w.write("<html>\n" +
                    "<head>\n" +
                    " <meta charset=\"UTF-8\">\n" + "</head>\n" +
                    "<body>\n" +
                    "성공\n" +
                    "<ul>\n" +
                    "    <li>id="+member.getId()+"</li>\n" +
                    "    <li>username="+member.getUserName()+"</li>\n" +
                    " <li>age="+member.getAge()+"</li>\n" + "</ul>\n" +
                    "<a href=\"/index.html\">메인</a>\n" + "</body>\n" +
                    "</html>");
        }
    }
    ```

    1. request로 넘겨받은 값으로 Member 객체 생성
    2. MemberRepository에 저장
    3. HTML 형식의 응답 데이터로 화면에 데이터 출력 (동적 데이터)
    - 사용자의 요청 데이터를 자바 로직을 통해 가공시키고 가공된 데이터를 html 형식으로 출력함
- 템플릿 엔진 사용
    - 서블릿을 통해 동적으로 HTML을 만들 수 있음
    - 하지만 자바 코드로 HTML을 만드는 것은 매우 비효율적임
    - HTML 문서에 동적으로 변하는 데이터만 부분적으로 넣을 수 있다면 훨씬 편리할 것

      → 템플릿 엔진을 통해 가능

    - JSP, Thymeleaf, Velocity 등이 있음
        - JSP는 잘 사용하지 않는 추세
        - Spring과 가장 잘 통합되는 Thymeleaf를 사용하는게 좋음

## 3.3 JSP로 만들기

- save.jsp

    ```java
    <%@ page import="hello.servlet.domain.member.MemberRepository" %>
    <%@ page import="hello.servlet.domain.member.Member" %>
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    
    <%
        MemberRepository memberRepository = MemberRepository.getInstance();
    
        System.out.println("MemberSaveServlet.service");
        String userName = request.getParameter("userName");
        int age = Integer.parseInt(request.getParameter("age"));
    
        Member member = new Member(userName, age);
        System.out.println("member = " + member);
        memberRepository.save(member);
    %>
    
    <html>
    <head>
        <meta charset="UTF-8"/>
        <title>Title</title>
    </head>
    <body>
    <ul>
        <li>id=<%=member.getId()%></li>
        <li>username=<%=member.getUserName()%></li>
        <li>age=<%=member.getAge()%></li>
    </ul>
    <a href="/index.html">메인</a>
    </body>
    </html>
    ```

    - html에 자바 코드를 부분적으로 넣는 방식
        - 정적인 html에서 동적인 자바 코드를 사용할 수 있음
    - <% %> : 자바 코드를 입력
    - <%= %> : 자바 코드를 출력
- JSP의 한계
    - 상위 절반
        - 회원 저장을 위한 비지니스 로직
    - 하위 절반
        - 결과를 html로 보여주기 위한 뷰 영역
    - JSP가 너무 많은 역할을 함
        - 프로젝트가 커질수록 하나의 jsp 파일의 크기는 감당할수 없을 정도로 커질 것
- MVC 패턴의 등장
    - 비지니스 로직은 서블릿에서 처리하고 JSP는 목적에 맞게 화면을 그리는 일에만 집중하도록 함
