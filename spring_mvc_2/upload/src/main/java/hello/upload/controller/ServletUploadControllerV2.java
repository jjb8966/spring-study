package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    // spring 라이브러리의 Value 써야 함
    // application.properties 의 file.dir 을 주입해줌
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFileV2(HttpServletRequest request) throws ServletException, IOException {

        // HTML Form으로 넘어온 파라미터 데이터
        String itemName = request.getParameter("itemName");
        log.info("itemName = {}", itemName);

        // form 데이터가 아닌 파일은 parameter 로 얻을 수 없음
        String file = request.getParameter("file");
        log.info("file(string) = {}", file);    // null

        // 요청 메세지의 각 part를 얻음
        Collection<Part> parts = request.getParts();
        log.info("parts = {}", parts);

        for (Part part : parts) {
            log.info("=====PART=====");
            log.info("name={}", part.getName());

            // 각 part 별 헤더를 얻음
            Collection<String> headerNames = part.getHeaderNames();

            for (String headerName : headerNames) {
                log.info("header {} = {}", headerName, part.getHeader(headerName));
            }

            // 파일이라면 파일의 이름을 얻음
            log.info("SubmittedFileName = {}",part.getSubmittedFileName());
            // 파트의 바디 부분 사이즈
            log.info("size = {}", part.getSize());

            // 바디 -> string
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
            log.info("body = {}", body);

            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 full path = {}", fullPath);

                // 요청 메시지로 온 파일 데이터를 저장
                part.write(fullPath);
            }
        }

        return "upload-form";
    }
}
