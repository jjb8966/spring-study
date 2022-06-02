package hello.upload.controller;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

// item 입력 폼
// 실제 데이터(MultipartFile)을 가지고 있는 클래스 타입
// -> FileStore 에 저장 후 이름만 가지는 UploadFile 객체를 만들고 그것으로 Item 객체를 만들어 ItemRepository 에 저장
@Data
public class ItemForm {

    private Long itemId;
    private String itemName;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles;
}
