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
    3. **HTML 형식**의 응답 데이터로 화면에 **동적 데이터 출력**
    - 사용자의 요청 데이터를 자바 로직을 통해 가공시키고 가공된 데이터를 html 형식으로 출력함
- **자바 코드로 HTML** 만드는 것은 **매우 비효율적**
- **HTML** 문서에 **동적으로 변하는 데이터만 부분적으로 넣을 수 있다면** 훨씬 편리할 것
    
    → `템플릿 엔진`을 통해 **동적으로 HTML**을 만들 수 있음
    
- 템플릿 엔진
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
- `JSP의 한계`
    - 상위 절반
        - 회원 저장을 위한 비지니스 로직
    - 하위 절반
        - 결과를 html로 보여주기 위한 뷰 영역
    - **JSP가 너무 많은 역할을 함**
        - 프로젝트가 커질수록 하나의 jsp 파일의 크기는 감당할수 없을 정도로 커질 것
- `MVC 패턴의 등장`
    - **비지니스 로직**은 `서블릿`에서 **처리**하고 `JSP`는 목적에 맞게 **화면을 그리는 일에만 집중**하도록 함

## 3.4  MVC 패턴으로 만들기

### 1. 개요

- 기존 구조
    - **하나의 서블릿 또는 jsp**가 **비지니스 로직과 뷰를 모두 포함**하고 있음
    - 변경 라이프사이클이 다르기 때문에 **유지보수하기 힘듦**
- `MVC 패턴`
    - 모델, 뷰, 컨트롤러 3가지 파트로 나눠서 프로그래밍하는 방식을 말함
        - `모델`
            - 뷰에서 **출력할 데이터를 담아두는 파트**
        - `뷰`
            - 모델에 담겨있는 **데이터를 화면에 그리는 파트** (여기서는 html을 생성하는 부분)
        - `컨트롤러`
            - HTTP 요청을 받아 **비지니스 로직을 실행**하고, **실행 결과를 모델에 담는 파트**
                - 컨트롤러는 비지니스 로직(service)를  호출하여 실행하는 방식
                - 비지니스 로직이 변경되면 컨트롤러도 변경될 수 있음
                
                ![Untitled](https://user-images.githubusercontent.com/87421893/170874932-3841cdb0-a298-48ff-8d37-9caf0117a77f.png)

                
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
    
    - `WEB-INF`
        - 이 경로 하위에 jsp 파일이 있으면 **외부에서 직접 jsp에 접근할 수 없음**
        - 항상 컨트롤러를 통해서만 접근 가능함
    - forward() vs redirect()
        - **다른 서블릿이나 jsp로 이동**할 수 있는 기능
        - `forward()`
            - **서버 내부에서 일어나는 호출**
            - response가 나가지 않기 때문에 **클라이언트는 전혀 인지하지 못함**
        - `redirect()`
            - 실제 **클라이언트로 response가 나갔다가** 클라이언트가 **다시 redirect 경로를 요청하는 방식**
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
    
    - <%= request.getAttribute(”member.getId”)%>
        - 너무 번거로움
        - JSP가 제공하는 [EL 문법](https://www.notion.so/EL-JSTL-62c384ef7df049ceafb97a63857a9e1e)으로 간단하게 표현 가능
            - `${member.id}` or `${member.getId}`

### 3. MVC 패턴의 한계

- **중복되는 코드가 많음**
    - viewpath의 경로 (/WEB-INF/views/~~.jsp)
        - 만약 경로가 변경된다면?
            - 모든 컨트롤러의 viewpath 부분을 수정해야 함
    - forward 부분
        - 모든 컨트롤러가 같은 코드를 사용함
- **불필요한 request, response**
    - request, response를 사용하지 않을 때에도 전달받음
    - 테스트 코드 작성이 어려워짐
- 기능이 복잡해질수록 `공통으로 처리`해야할 부분이 많아짐
    
    ⇒ `front-controller 패턴`을 도입해 이런 문제를 해결함
    
    ⇒ `spring mvc의 핵심!`
