package hello.upload.domain;

import lombok.Data;

import java.util.List;

// DB에 저장될 객체
// MultipartFile 이 아닌 UploadFile 타입의 파일 가짐
// -> 파일의 이름 & 고유 이름만 저장하고 있음
@Data
public class Item {

    private Long id;
    private String itemName;
    private UploadFile attachFile;
    private List<UploadFile> imageFiles;
}
