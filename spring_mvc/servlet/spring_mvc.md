# 스프링 MVC

### 목차
### [1. 자바 백엔드 웹 기술 역사](#1-자바-백엔드-웹-기술-역사-1)
### [2. 서블릿](#2-서블릿-2)
### [3. 서블릿, JSP, MVC 패턴](#3-서블릿-jsp-mvc-패턴-1)
### [4. MVC 프레임워크 만들기](#4-mvc-프레임워크-만들기-1)
### [5. 스프링 MVC - 구조 이해](#5-스프링-mvc---구조-이해-1)

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

## 3.4  MVC 패턴으로 만들기

### 1. 개요

- 하나의 서블릿 또는 jsp가 비지니스 로직과 뷰를 모두 포함하고 있는 기존 구조
    - 변경 라이프사이클이 다르기 때문에 유지보수하기 힘듦
- MVC 패턴
    - 모델, 뷰, 컨트롤러 3가지 파트로 나눠서 프로그래밍하는 방식을 말함
        - 모델
            - 뷰에서 출력할 데이터를 담아두는 파트
        - 뷰
            - 모델에 담겨있는 데이터를 화면에 그리는 파트 (여기서는 html을 생성하는 부분)
        - 컨트롤러
            - HTTP 요청을 받아 비지니스 로직을 실행하고, 실행 결과를 모델에 담는 파트
                - 컨트롤러는 비지니스 로직(service)를  호출하여 실행하는 방식
                - 비지니스 로직이 변경되면 컨트롤러도 변경될 수 있음
        
        ![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/0ae40e5d-2583-4165-bde9-769db7aacba9/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220405%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220405T153446Z&X-Amz-Expires=86400&X-Amz-Signature=72a231ad992a2f92f268e6b3cba34a3cb5c0cad5a13131992adea2dc7234859a&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)
        
- 예제
    - 모델
        - request 내부 저장소 (getAttribute)
    - 뷰
        - jsp
    - 컨트롤러
        - 서블릿

### 2. MVC 패턴 적용

- MvcMemberSaveServlet
    
    ```java
    @WebServlet(name = "mvcMemberSaveServlet", urlPatterns = "/servlet-mvc/members/save")
    public class MvcMemberSaveServlet extends HttpServlet {
    
        MemberRepository memberRepository = MemberRepository.getInstance();
    
        @Override
        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // 비지니스 로직 실행
            String userName = request.getParameter("userName");
            int age = Integer.parseInt(request.getParameter("age"));
    
            Member member = new Member(userName, age);
            memberRepository.save(member);
    
            // 모델에 비지니스 로직 실행 결과 저장
            request.setAttribute("member", member);
    
            // 뷰로 모델을 넘겨줌
            String viewPath = "/WEB-INF/views/save-result.jsp";
            RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
            dispatcher.forward(request, response);
        }
    }
    ```
    
    - WEB-INF
        - 이 경로 하위에 jsp 파일이 있으면 외부에서 직접 jsp에 접근할 수 없음
        - 항상 컨트롤러를 통해서만 접근 가능함
    - forward vs redirect
        - forward
            - 서버 내부에서 일어나는 호출
            - response가 나가지 않기 때문에 클라이언트는 전혀 인지하지 못함
        - redirect
            - 실제 클라이언트로 response가 나갔다가 클라이언트가 다시 redirect 경로를 요청하는 방식
- save-result.jsp
    
    ```html
    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <html>
    <head>
        <meta charset="UTF-8">
    </head>
    <body>
    성공
    <ul>
        <li>id=${member.id}</li>
        <li>username=${member.userName}</li>
        <li>age=${member.age}</li>
    </ul>
    <a href="/index.html">메인</a>
    </body>
    </html>
    ```
    

### 3. MVC 패턴의 한계

- 중복되는 코드가 많음
    - viewpath의 경로 (/WEB-INF/views/~~.jsp)
        - 만약 경로가 변경된다면?
            - 모든 컨트롤러의 viewpath 부분을 수정해야 함
    - forward 부분
        - 모든 컨트롤러가 같은 코드를 사용함
- 불필요한 request, response
    - request, response를 사용하지 않을 때에도 전달받음
    - 테스트 코드 작성이 어려워짐
- 기능이 복잡해질수록 `공통으로 처리`해야할 부분이 많아짐
    
    ⇒ `front-controller 패턴`을 도입해 이런 문제를 해결함
    
    ⇒ spring mvc의 핵심!


# 4. MVC 프레임워크 만들기

## 4.1 프론트 컨트롤러 도입 - v1

- 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
    - 공통처리가 가능해짐
    - 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아 호출함
        - 나머지 컨트롤러는 서블릿으로 만들 필요 없음
    
    <aside>
    ⭐ 프론트 컨트롤러가 spring mvc의 핵심 개념!
    spring mvc의 DispatcherServlet이 프론트 컨트롤러 패턴으로 구현되어있음
    
    </aside>
    

### 1. 구조

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/61b5be94-9ec6-4a72-9136-a76cc289509f/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220405%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220405T154116Z&X-Amz-Expires=86400&X-Amz-Signature=05dbbb6eb35ea8a8d0c22a9bbc2dd00c32830a5320073f054d55f2a746129a89&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

### 2. 컨트롤러 인터페이스

- 서블릿과 비슷한 모양의 컨트롤러 인터페이스

```java
public interface ControllerV1 {

    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
```

### 3. 컨트롤러

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

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/48d74e1c-199b-4227-9132-1226493be668/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220405%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220405T154159Z&X-Amz-Expires=86400&X-Amz-Signature=24fe8a7bb096859b4563b19c32416940f2f0c9da4d4ff90e75db8aff82d43c3a&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

- 뷰로 이동하는 중복되는 코드를 줄임
- MyView 클래스
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

- MyView를 리턴함

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

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/16924b41-2c0e-4469-9ac1-890ddb1ba593/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220405%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220405T154238Z&X-Amz-Expires=86400&X-Amz-Signature=b9537b988633ea72a896d708b03f57546e8b3059c254eabd340df992e8cbb3b2&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

- ModelView 클래스
    
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
    
    - viewName → 뷰의 실제 물리 이름이 아닌 `논리 이름을 저장`
- viewResolver(viewName)
    - 뷰의 `논리이름`을 전달받으면 `물리 이름으로 변환`시켜 MyView로 리턴하는 메소드
    - 프론트 컨트롤러에 포함
    
    ```java
    private MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
    ```
    

### 2. 컨트롤러 인터페이스

- 모든 컨트롤러가 request, response를 필요로 하지 않음
    - 매개변수로 request로 만든 paramMap을 전달받아 서블릿 관련 코드를 지움
        
        → 컨트롤러 구현이 쉬워짐
        
- model도 포함하고 있는 ModelView를 리턴함

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
- MyView 클래스에 model을 추가 매개변수로 받는 render 메소드를 오버로딩하여 사용
    - jsp는 request에 담긴 데이터를 조회하기 때문에 model의 데이터를 request에 옮김
    - request, response를 jsp에 전달
    
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

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/23a6aad3-03d8-46b7-b664-4ca589c92af5/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220405%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220405T154303Z&X-Amz-Expires=86400&X-Amz-Signature=110f048fd827362a89c40b60b3ccec5c1cb37d8c43de860ec21c8320f057f2b9&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

- 컨트롤러가 ModelView가 아닌 viewName, 즉 뷰의 논리 이름만 리턴함
- 데이터를 담을 model은 매개변수로 전달받음

### 2. 컨트롤러 인터페이스

- 컨트롤러가 viewName을 리턴함

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

- 매개변수
    - paramMap → 비지니스 로직에 필요한 데이터를 포함
    - model → 비지니스 로직을 실행한 결과를 저장
- 뷰의 논리 이름을 리턴
    - 프론트 컨트롤러에서 viewResolver가 물리 이름으로 변환시켜줌

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

## 4.5 유연한 컨트롤러 - v5

### 1. 구조

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/befe1275-356a-4e8b-a895-107af74892f8/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220405%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220405T154327Z&X-Amz-Expires=86400&X-Amz-Signature=6cab573253c04a4f2c38d418ec3dd59cd002c00cf5722b13f731270fa8cd6984&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

- `어댑터 패턴`
    - 다양한 방식의 컨트롤러를 사용하기 위해 적용하는 패턴
        - ControllerV3, V4 둘 다 사용하고 싶은 경우 어댑터 패턴을 적용
    - 핸들러
        - 컨트롤러를 포함하는 넓은 범위의 개념
        - 어댑터가 지원하기만 한다면(handle 할 수 있으면), 컨트롤러가 아닌 어떤 것이라도 URL 맵핑해서 사용할 수 있음
    - 실행 과정
        1. 사용할 핸들러, (핸들러) 어댑터를 프론트 컨트롤러에 등록
        2. 클라이언트 요청에 해당하는 핸들러 조회
        3. 해당 핸들러를 사용할 수 있는 어댑터 조회
        4. 어댑터를 통해 핸들러 실행 (handle) → ModelView 리턴
        5. ModelView를 통해 실행 결과를 렌더링

### 2. 핸들러 어댑터 인터페이스

```java
public interface MyHandlerAdapter {

    boolean support(Object handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException;
}
```

- `support(handler)`
    - 해당 핸들러를 사용할 수 있는(지원하는) 어댑터인지 확인하는 메소드
- `handle(requset, response, handler)`
    - 해당 핸들러를 사용해 컨트롤러를 실행
        
        → 비지니스 로직을 실행하고 뷰 정보를 리턴함 (ModelView 리턴)
        
    - 실제 컨트롤러가 ModelView를 리턴하지 못하면 `어댑터가 ModelView를 생성해서라도 리턴`해야함

### 3. 핸들러 어댑터

- V3 핸들러 어댑터
    
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
    
    - handle 메소드를 실행하는 경우는 이미 support 메소드를 통해 ControllerV3임을 보장받은 후 이므로 핸들러를 v3 컨트롤러로 변환하면 됨
    - v3 컨트롤러를 실행하기 위해 필요한 매개변수 paramMap을 생성 후 컨트롤러에 전달
        
        → ModelView 리턴
        
- V4 핸들러 어댑터
    
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
    
            String viewName = controller.process(paramMap, model);
    
            ModelView mv = new ModelView(viewName);
            mv.setModel(model);
    
            return mv;
        }
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

		private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter handlerAdapter : handlerAdapters) {
            if (handlerAdapter.support(handler)) {
                return handlerAdapter;
            }
        }

        throw new IllegalArgumentException("해당 핸들러의 어댑터가 없습니다.");
    }
}
```

- 생성자
    - initHandlerMappingMap()
        - handlerMappingMap 초기화
            - key : URL
            - value : 핸들러
    - initHandlerAdapter();
        - handlerAdapters 초기화
            - 어댑터를 저장
    - 이 두 메소드만 외부에서 주입받는다면 완벽한 OCP를 지킬 수 있음
        
        → service 메소드를 수정하지 않고 다양한 핸들러(컨트롤러)를 사용할 수 있음
        
- service()
    1. handlerMappingMap에서 클라이언트 요청에 해당하는 핸들러를 조회
    2. handlerAdapters에서 해당 핸들러를 사용할 수 있는(support) 어댑터가 있으면 조회해서 리턴
    3. 어댑터의 handle 메소드를 통해 핸들러 실행
        
        → ModelView 리턴
        
    4. ModelView로 실행결과 뷰에 렌더링

## 4.6 정리

1. v1
    - spring mvc의 핵심개념인 `프론트 컨트롤러` 적용
2. v2
    - 중복되는 `뷰 로직`을 MyView 클래스를 사용해 `분리`
3. v3
    - 컨트롤러의 매개변수로 request, response가 아닌 paramMap을 사용
        
        → 서블릿 종속성 제거
        
    - 컨트롤러 실행 결과 ModelView를 리턴
        - 비지니스 로직의 실행결과를 저장하는 `model`을 포함
        - 뷰의 논리 이름을 포함
            
            → 뷰 이름의 중복성 제거
            
    - ModelView에 저장된 뷰의 논리 이름을 viewResolver를 통해 뷰의 물리 이름을 저장한 MyView 객체를 얻음
    - MyView가 model에 담긴 데이터와 뷰의 물리 이름을 사용해 결과를 렌더링
4. v4
    - 컨트롤러가 바로 viewName을 리턴
5. v5
    - 다양한 컨트롤러(핸들러)를 사용할 수 있는 `어댑터 패턴`을 적용함
    - 핸들러를 지원하는 어댑터만 있다면 어떤 핸들러든지 URL 맵핑하여 사용할 수 있음
        
        ex) 어노테이션 핸들러 → 어노테이션 핸들러 어댑터 추가해서 사용
        

<aside>
⭐ 버전이 높아질 수록 프론트 컨트롤러가 복잡해지고 컨트롤러는 간단해진다.
spring MVC 프레임워크는 위와 같은 과정으로 발전해왔다.

</aside>

# 5. 스프링 MVC - 구조 이해
## 5.1 전체 구조

- 직접 만든 프레임워크와 거의 동일
    
    ![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/bf5fffa9-e355-429a-879a-e9d8049a573e/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220412%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220412T111835Z&X-Amz-Expires=86400&X-Amz-Signature=b4bfdb95ae3d9f2d77ec0c011b85876d0da8c881e31e5462ce181be713e034db&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)
    
    - FrontController → `DispatcherServlet (서블릿)`
        - 모든 경로(url=”/”)에 대해서 매핑
            
            → 클라이언트가 어떤 경로로 요청하든 실행되는 서블릿
            
        - 이 서블릿의 `service()` 메소드가 호출
            - DispatcherServlet.`doDispatch()` 호출
                1. 핸들러 조회
                2. 어댑터 조회
                3. 어댑터 실행 → 핸들러 실행 → ModelAndView 리턴
                4. ModelAndView를 통해 viewName 얻음
                5. ViewResolver를 통해 View 얻음
                    - jsp
                        - ViewResolver → InternalResourceViewResolver
                        - View → InternalResourceView
                6. view로 렌더링
    - 서블릿을 제외한 나머지는 모두 인터페이스
        - handlerMappingMap → `HandlerMapping`
        - MyHandlerAdapter → `HandlerAdapter`
        - viewResolver → `ViewResolver`
        - MyView → `View`

### 1. Handler Mapping & Adapter

- 컨트롤러가 호출되는 과정
    1. 핸들러 매핑에서 클라이언트 요청에 맞는 컨트롤러(핸들러)를 찾음
        - 핸들러 찾는 방식
            1. 어노테이션
            2. 스프링 빈 이름
    2. 해당 핸들러를 지원하는 어댑터를 찾음
    3. 어댑터가 핸들러 호출
- 스프링이 대부분의 필요한 핸들러 매핑과 어댑터를 구현해놨기 때문에 개발자가 직접 만들 일은 거의 없음
    - 자동 등록된 `핸들러 매핑`
        1. `RequestMappingHandlerMapping`
            - 어노테이션 기반 컨트롤러(@Controller, @RequestMapping) 찾음
        2. BeanNameUrlHandlerMapping
            - 스프링 빈 이름으로 컨트롤러 찾음
    - 자동 등록된 `핸들러 어댑터`
        1. `RequestMappingHandlerAdapter`
            - 어노테이션 기반 컨트롤러를 지원
        2. HttpRequestHandlerAdapter
            - HttpRequestHandler를 지원
        3. SimpleControllerHandlerAdapter
            - Controller 인터페이스를 구현한 컨트롤러를 지원
        
        ⇒ 자동 등록된 핸들러 매핑과 어댑터는 작성된 순서를 우선순위로 조회됨
        
        ⇒ 실무에서 어노테이션 기반의 컨트롤러가 99%
        

### 2. View Resolver

- 자동 등록된 `뷰 리졸버`
    1. BeanNameViewResolver
        - 빈 이름으로 뷰를 찾아서 반환
    2. InternalResourceViewResolver
        - jsp를 처리할 수 있는 뷰를 반환
- Controller 인터페이스를 구현한 컨트롤러
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
    
    - ModelAndView을 뷰의 논리 주소를 사용해 생성하고 리턴
    - ViewResolver 호출
        1. BeanNameViewResolver가 new-form라는 이름을 갖는 스프링 빈을 찾는데 없음
        2. `InternalResourceViewResolver` 호출
            - 이 때 application.properties의 설정 정보를 기반으로 등록함
                
                ```java
                spring.mvc.view.prefix=/WEB-INF/views/
                spring.mvc.view.suffix=.jsp
                ```
                
    - View 리턴
        - InternalResourceViewResolver가 `InternalResourceView` 리턴
        - forward()를 호출해 렌더링함

## 5.2 스프링 MVC

- 실무에서는 99% 어노테이션 기반의 컨트롤러를 사용함

### 1. V1 - spring mvc 적용

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
    
    - 각각의 컨트롤러가 따로 존재하고 스프링 빈으로 등록됨
    - @RequestMapping 어노테이션을 이용해 컨트롤러에 해당하는 URL 매핑
        - 메소드 이름은 임의로 지어도 무관
    - 메소드 실행 결과 ModelAndView를 리턴함
    - mv.getModel().put() 대신 mv.addObject로 모델에 데이터를 넣을 수 있음

### 2. V2 - 컨트롤러 통합

- @RequestMapping 어노테이션을 메소드 단위가 아닌 클래스 단위로 적용해 3개의 컨트롤러를 하나의 컨트롤러로 통합함
    
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
    
    cf) 하나의 컨트롤러로 통합하고 메소드 레벨에서만 어노테이션을 지정해도 가능하긴 함
    
    → 중복 코드가 존재하므로 권장하진 않음
    
    ```java
    @RequestMapping("/springmvc/v2/members/save")
    public ModelAndView save(){}
    ```
    

### 3. V3 - 실용적인 방식

1. 각 컨트롤러(메소드)가 ModelAndView가 아닌 viewName만 리턴
2. 매개변수로 request, response가 아닌 parameter를 받음
    - 실행 결과 String(viewName)을 리턴하므로 model을 매개변수로 받아 실행 결과를 직접 저장
3. 단순히 URL 매핑만하는 @RequestMapping 대신 HTTP 메소드를 구분하여 동작할 수 있는 @GetMapping, @PostMapping 어노테이션 사용
    - Get, Post, Put, Delete, Patch 모두 지원

```java
package hello.servlet.web.springmvc.v3;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
