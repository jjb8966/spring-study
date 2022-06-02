package hello.upload.controller;

import hello.upload.domain.Item;
import hello.upload.domain.ItemRepository;
import hello.upload.domain.UploadFile;
import hello.upload.file.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemRepository itemRepository;
    private final FileStore fileStore;

    @GetMapping("/items/new")
    public String newItem(@ModelAttribute ItemForm form) {
        return "item-form";
    }

    @PostMapping("/items/new")
    public String saveItem(@ModelAttribute ItemForm form, RedirectAttributes redirectAttributes) throws IOException {
        // form 으로 전달받은 실제 파일을 fileStore(서버 메모리)에 저장하고 파일의 이름과 UUID 를 가진 UploadFile 타입을 리턴
        UploadFile attachFile = fileStore.storeFile(form.getAttachFile());
        List<UploadFile> storeImageFiles = fileStore.storeFiles(form.getImageFiles());

        // UploadFile 을 이용해 Item 객체 생성
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setAttachFile(attachFile);
        item.setImageFiles(storeImageFiles);

        // Item 을 itemRepository 에 저장
        itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", item.getId());

        return "redirect:/items/{itemId}";
    }

    // item 이 itemRepository 에 저장되고 호출되는 메소드
    @GetMapping("/items/{id}")
    public String items(@PathVariable Long id, Model model) {
        // itemId 로 item 조회
        Item item = itemRepository.findById(id);

        // 뷰에게 item 을 넘김
        // -> 뷰에서 파일의 이름과 UUID 를 사용해 서버에 저장된 파일들을 조회해서 보여줌
        model.addAttribute("item", item);
        return "/item-view";
    }

    // fileStore 에 저장된 (서버에 저장된) 이미지를 읽는 메소드
    @ResponseBody
    @GetMapping("/images/{filename}")
    // Resource 타입을 메시지 바디에 넣어서 보여줄 수 있음
    public Resource downloadImage(@PathVariable String filename) throws MalformedURLException {
        log.info("fileStore.getFullPath(filename)={}", fileStore.getFullPath(filename));
        /* --> /Users/joojongbum/Desktop/down-file/9f44aa92-2f22-4195-9fe9-b2efea503b89.png */

        // Resource 의 구현체로 UrlResource 를 사용함
        // UrlResource 는 (서버에 저장된 파일의 경로 + 이름)으로 만들 수 있음
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    // 첨부파일 링크를 클릭하면 클라이언트가 파일을 다운받도록 하는 메소드
    // Resource 타입을 메시지 바디에 넣어주고 Content-Disposition 헤더값을 설정해주면 서버가 응답 시 다운로드됨
    // 헤더값 설정을 위해 ResponseEntity 사용
    @GetMapping("/attach/{itemId}")
    public ResponseEntity<Resource> downloadAttach(@PathVariable Long itemId) throws MalformedURLException {
        Item item = itemRepository.findById(itemId);
        String storeFileName = item.getAttachFile().getStoreFileName();
        String uploadFileName = item.getAttachFile().getUploadFileName();

        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));

        log.info("uploadFileName={}", uploadFileName);

        String encodedUploadFileName = UriUtils.encode(uploadFileName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
}
