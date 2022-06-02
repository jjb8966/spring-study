package hello.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/spring")
public class SpringUploadController {

    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }

    //@PostMapping("/upload")
    public String saveFile(
            @RequestParam String itemName,
            @ModelAttribute MultipartFile file,   // @RequestParam 으로 파일 자체를 받음
            HttpServletRequest request) throws IOException {
        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("file={}", file);

        if (!file.isEmpty()) {
            //fileName
            String fullPath = fileDir + file.getOriginalFilename();
            log.info("파일 저장 fullPath={}", fullPath);
            file.transferTo(new File(fullPath));
        }

        return "upload-form";
    }

    @PostMapping("/upload")
    public String saveFiles(
            @RequestParam String itemName,
            @RequestParam List<MultipartFile> files,
            HttpServletRequest request) throws IOException {
        log.info("request={}", request);
        log.info("itemName={}", itemName);
        log.info("files={}", files);

        for (MultipartFile multiFile : files) {
            log.info("file={}", multiFile);

            String fullPath = fileDir + multiFile.getOriginalFilename();
            log.info("파일 저장 fullPath={}", fullPath);
            multiFile.transferTo(new File(fullPath));
        }

        return "upload-form";
    }
}
