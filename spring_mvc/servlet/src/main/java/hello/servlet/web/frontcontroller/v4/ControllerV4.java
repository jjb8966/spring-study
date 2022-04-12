package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

// 컨트롤러가 ModelView 대신 viewName(String) 반환
public interface ControllerV4 {

    /**
     * @param paramMap
     * @param model
     * @return viewName
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
