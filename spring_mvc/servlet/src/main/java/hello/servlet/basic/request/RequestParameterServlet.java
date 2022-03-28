package hello.servlet.basic.request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "requestParameterServlet", urlPatterns = "/request-param")
public class RequestParameterServlet extends HttpServlet {

    /**
     * 1. 파라미터 전송 기능
     * http://localhost:8080/request-param?username=hello&age=20
     * <p>
     * 2. 동일한 파라미터 전송 가능
     * http://localhost:8080/request-param?username=hello&username=kim&age=20
     */

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("[전체 파라미터 조회]");
        request.getParameterNames().asIterator()
                .forEachRemaining(parameterName -> System.out.println(parameterName + " : " + request.getParameter(parameterName)));
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");
        System.out.println("username = " + username);
        System.out.println("age = " + age);

        System.out.println("[이름이 같은 복수 파라미터 조회]");
        System.out.println("getParameterValues(username)");
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("username : " + name);
        }

        response.getWriter().write("ok");
    }
}

