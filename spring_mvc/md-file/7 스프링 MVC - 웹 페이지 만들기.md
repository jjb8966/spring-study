# 7. 스프링 MVC - 웹 페이지 만들기

# 1. 요구사항 분석

- 상품 도메인 모델
    - 상품 id
    - 상품명
    - 가격
    - 수량
- 상품 관리 기능
    - 상품 목록
    - 상품 상세
    - 상품 등록
    - 상품 수정
- 서비스 흐름
    
    ![Untitled](https://user-images.githubusercontent.com/87421893/171104852-fb6d8d4a-d60c-49c9-9bec-0ec53c74e0dd.png)
    

# 2. 상품 서비스 HTML

- 부트스트랩
    - HTML에 필요한 css 파일을 다운로드함
    - resources/static/css에 저장

# 3. 상품 목록

- 컨트롤러
    
    ```java
    @Controller
    @RequestMapping("/basic/items")
    @RequiredArgsConstructor
    public class BasicItemController {
    
        private final ItemRepository itemRepository;
    
        @GetMapping
        public String items(Model model) {
            List<Item> items = itemRepository.findAll();
            model.addAttribute("items", items);
            return "basic/items";
        }
    
        // 테스트용 데이터
        @PostConstruct
        public void init() {
            itemRepository.save(new Item("itemA", 10000, 10));
            itemRepository.save(new Item("itemB", 20000, 20));
        }
    }
    ```
    
    - @RequiredArgsConstructor
        - final 붙은 필드를 주입받는 생성자를 자동으로 생성
    - 요청 URL
        - /basic/items
- 뷰
    - basic/items
    
    ```java
    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="utf-8">
        <link href="../css/bootstrap.min.css"
              th:href="@{/css/bootstrap.min.css}"
              rel="stylesheet">
    </head>
    <body>
    <div class="container" style="max-width: 600px">
        <div class="py-5 text-center">
            <h2>상품 목록</h2>
        </div>
        <div class="row">
            <div class="col">
                <button class="btn btn-primary float-end"
                        onclick="location.href='addForm.html'"
                        th:onclick="|location.href='@{/basic/items/add}'|" type="button">
                    상품 등록
                </button>
            </div>
        </div>
        <hr class="my-4">
        <div>
            <table class="table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>상품명</th>
                    <th>가격</th>
                    <th>수량</th>
                </tr>
                </thead>
    
                <tbody>
                <tr th:each="item : ${items}">
                    <td><a href="item.html" th:href="@{/basic/items/{itemId} (itemId=${item.id})}"
                           th:text="${item.id}">회원id</a></td>
                    <td><a href="item.html" th:href="@{|/basic/items/${item.id}|}"
                           th:text="${item.itemName}">상품명</a></td>
                    <td th:text="${item.price}">10000</td>
                    <td th:text="${item.quantity}">10</td>
                </tr>
                </tbody>
            </table>
        </div>
    </div> <!-- /container -->
    </body>
    </html>
    ```
    
    - URL 링크 표현식
        - th:href="`@{/basic/items/{itemId} (itemId=${item.id})}`"
        - th:href="`@{|/basic/items/${item.id}|}`"
            - 둘은 동일한 코드

# 4. 상품 상세

- 컨트롤러
    - 요청 URL
        - /basic/items/{itemId}
        - 상품 id 또는 상품명을 클릭하면 해당 아이템에 맞는 URL 요청
    
    ```java
    @GetMapping("/{itemId}")
    public String item(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }
    ```
    
    - 컨트롤러는 전달받은 itemId를 이용해 상품을 찾고 모델에 넣음
- 뷰
    - basic/item
    
    ```java
    <!DOCTYPE HTML>
    <html xmlns:th="http://www.thymeleaf.org">
    <head>
        <meta charset="utf-8">
        <link href="../css/bootstrap.min.css"
              th:href="@{/css/bootstrap.min.css}"
              rel="stylesheet">
        <style>
            .container {
                max-width: 560px;
            }
        </style>
    </head>
    <body>
    <div class="container">
        <div class="py-5 text-center">
            <h2>상품 상세</h2>
        </div>
    
        <div>
            <label for="itemId">상품 ID</label>
            <input type="text" id="itemId" name="itemId" class="form-control" value="1"
                   th:value="${item.id}" readonly>
        </div>
        <div>
            <label for="itemName">상품명</label>
            <input type="text" id="itemName" name="itemName" class="form-control" value="상품A"
                   th:value="${item.itemName}" readonly></div>
        <div>
            <label for="price">가격</label>
            <input type="text" id="price" name="price" class="form-control" value="10000"
                   th:value="${item.price}" readonly>
        </div>
        <div>
            <label for="quantity">수량</label>
            <input type="text" id="quantity" name="quantity" class="form-control" value="10"
                   th:value="${item.quantity}" readonly>
        </div>
    
        <hr class="my-4">
        <div class="row">
            <div class="col">
                <button class="w-100 btn btn-primary btn-lg"
                        onclick="location.href='editForm.html'"
                        th:onclick="|location.href='@{/basic/items/{itemId}/edit (itemId=${item.id})}'|"
                        type="button">
                    상품 수정
                </button>
            </div>
            <div class="col">
                <button class="w-100 btn btn-secondary btn-lg"
                        onclick="location.href='items.html'"
                        th:onclick="|location.href='@{/basic/items}'|"
                        type="button">
                    목록으로
                </button>
            </div>
        </div>
    </div> <!-- /container -->
    </body>
    </html>
    ```
    
    - 상품 수정 클릭
        - /basic/items/{itemId}/edit
            - itemId=${item.id}
    - 상품 목록 클릭
        - /basic/items

# 5. 상품 등록

- 컨트롤러
    - 요청 URL
        - 상품 등록 폼 → GET /add
        - 상품 등록 로직 실행 → POST /add
            - 하나의 URL로 처리
    
    ```java
    @GetMapping("/add")
    public String addForm() {
        return "basic/addForm";
    }
    ```
    
    ```java
    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item) {
        itemRepository.save(item);
    
        // ModelAttribute 이름 생략 가능
        // 생략 시 ModelAttribute 클래스의 이름 사용(첫 글자 소문자로) -> 모델에 자동 추가
        return "basic/item";
    }
    ```
    
- 뷰
    - 상품 등록 시 → basic/addForm
    
    ```java
    <!DOCTYPE HTML>
    <html xmlns:th="https://www.thymeleaf.org">
    <head>
        <meta charset="utf-8">
        <link href="../css/bootstrap.min.css"
              th:href="@{/css/bootstrap.min.css}"
              rel="stylesheet">
        <style>
            .container {
                max-width: 560px;
            }
        </style>
    </head>
    <body>
    <div class="container">
        <div class="py-5 text-center">
            <h2>상품 등록 폼</h2>
        </div>
    
        <h4 class="mb-3">상품 입력</h4>
        <form action="item.html" th:action method="post">
            <div>
                <label for="itemName">상품명</label>
                <input type="text" id="itemName" name="itemName" class="form-control" placeholder="이름을 입력하세요"></div>
            <div>
                <label for="price">가격</label>
                <input type="text" id="price" name="price" class="form-control" placeholder="가격을 입력하세요">
            </div>
            <div>
                <label for="quantity">수량</label>
                <input type="text" id="quantity" name="quantity" class="form-control" placeholder="수량을 입력하세요">
            </div>
    
            <hr class="my-4">
            <div class="row">
                <div class="col">
                    <button class="w-100 btn btn-primary btn-lg" type="submit">
                        상품 등록
                    </button>
                </div>
                <div class="col">
                    <button class="w-100 btn btn-secondary btn-lg"
                            onclick="location.href='items.html'"
                            th:onclick="|location.href='@{/basic/items}'|"
                            type="button">
                        취소
                    </button>
                </div>
            </div>
        </form>
    </div> <!-- /container -->
    </body>
    </html>
    ```
    
    - 상품 등록 완료 → basic/item

# 6. 상품 수정

- 컨트롤러
    - 요청 URL
        - 상품 수정 폼 → GET /{itemId}/edit
        - 상품 수정 로직 실행 → POST /{itemId}/edit
            - 하나의 URL로 처리
    
    ```java
    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
    
        return "basic/editForm";
    }
    ```
    
    ```java
    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
    
        return "redirect:/basic/items/{itemId}";
    }
    ```
    
- 뷰
    - 상품 수정 시 → basic/editForm
        
        ```java
        <html>
        <head xmlns:th="http://www.thymeleaf.org">
            <meta charset="utf-8">
            <link href="../css/bootstrap.min.css"
                  th:href="@{/css/bootstrap.min.css}"
                  rel="stylesheet">
            <style>
                .container {
                    max-width: 560px;
                }
            </style>
        </head>
        <body>
        <div class="container">
            <div class="py-5 text-center">
                <h2>상품 수정 폼</h2></div>
        
            <form action="item.html" th:action method="post">
                <div>
                    <label for="id">상품 ID</label>
                    <input type="text" id="id" name="id" class="form-control" value="1"
                           th:value="${item.id}" readonly>
                </div>
                <div>
                    <label for="itemName">상품명</label>
                    <input type="text" id="itemName" name="itemName" class="form-control" value="상품A"
                           th:value="${item.itemName}">
                </div>
                <div>
                    <label for="price">가격</label>
                    <input type="text" id="price" name="price" class="form-control" value="10000"
                           th:value="${item.price}">
                </div>
                <div>
                    <label for="quantity">수량</label>
                    <input type="text" id="quantity" name="quantity" class="form-control" value="10"
                           th:value="${item.quantity}">
                </div>
                
                <hr class="my-4">
                <div class="row">
                    <div class="col">
                        <button class="w-100 btn btn-primary btn-lg" type="submit">
                            저장
                        </button>
                    </div>
                    <div class="col">
                        <button class="w-100 btn btn-secondary btn-lg"
                                onclick="location.href='item.html'"
                                th:onclick="|location.href='@{/basic/items/{itemId} (itemId=${item.id})}'|"
                                type="button">취소
                        </button>
                    </div>
                </div>
            </form>
        </div> <!-- /container -->
        </body>
        </html>
        ```
        
    - 상품 수정 완료
        - basic/items/{itemId} —상품 상세 컨트롤러 호출—> basic/item

# 7. PRG 패턴 적용

- 상품 저장 후 리다이렉트 /basic/items/{itemId}
    
    ```java
    @PostMapping("/add")
    public String addItemV5(Item item) {
        itemRepository.save(item);
    
        return "redirect:/basic/items/" + item.getId();
    }
    ```
    
    - get 으로 URL 요청 (마지막 전송)
        - 새로고침 -> get url 요청
            - 데이터 저장x
    - URL + item.getId()
        - URL에 변수를 더하는 방식은 [URL 인코딩](https://www.notion.so/Web-URL-URL-Encoding-Decoding-fe4573a1f4014c60b44d3cefe9c51962)이 안되기 때문에 위험

# 8. RedirectAttributes

- **URL 인코딩 문제를 해결**해줌
- **PathVariable, 쿼리 파라미터를 처리**해줌
    - `PathVariable`
        - **redirectAttributes.addAttribute**(`"itemId"`, `savedItem.getId()`);
        - return "redirect:/basic/items/`{itemId}`";
    - `쿼리 파라미터`
        - **redirectAttributes.addAttribute**(`"status"`, `true`);
        - /basic/items/{itemId}?`status=true`
            - 상품 저장 완료 시 별도의 페이지를 만들지 않고 저장완료 메세지를 띄울 수 있도록 status값을 넘겨줌
    
    ```java
    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes) {
        Item savedItem = itemRepository.save(item);
    
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
    
        return "redirect:/basic/items/{itemId}";
    }
    ```
    
- 뷰에서 저장 완료시 메세지 출력
    
    ```java
    <h2 th:if="${param.status}" th:text="'저장이 완료되었습니다.'"></h2>
    ```
    
    - 파라미터로 status가 true인게 넘어오면 메시지 출력
