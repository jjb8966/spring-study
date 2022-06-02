package hello.upload.domain;

import lombok.Data;

// 클라이언트가 업로드한 파일의 이름과 UUID 만 가지는 클래스 타입
@Data
public class UploadFile {

    // 사용자가 업로드할 때 이름 -> 다른 사용자와 중복될 수 있음
    private String uploadFileName;
    // 서버의 메모리에 저장될 때 이름 -> 고유한 이름 사용 (UUID)
    private String storeFileName;

    public UploadFile(String uploadFileName, String storeFileName) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
    }
}
