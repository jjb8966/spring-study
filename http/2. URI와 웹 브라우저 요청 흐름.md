# 2. URI와 웹 브라우저 요청 흐름

## 2.1 URI (Uniform Resource Identifier)

- `리소스를 식별`하는 `통합된 방법`
- 리소스 : URI로 식별할 수 있는 모든 것
- URL과 URN이 있음
    - `URL` → **리소스의 위치**로 식별
    - URN → 리소스의 이름으로 식별
![Untitled 0](https://user-images.githubusercontent.com/87421893/167386555-62e6f8da-26a4-4d01-82ab-a6029b827446.png)
![Untitled 1](https://user-images.githubusercontent.com/87421893/167386580-e939c150-c4bd-47b4-8c03-87dfd53fcd05.png)

### URL (Locator)

- 문법
    ![Untitled 2](https://user-images.githubusercontent.com/87421893/167386623-de884b12-486d-4d5e-9664-8fdba8c40a3d.png)
    
    - https → `프로토콜`
        - **어떤 방식으로 리소스에 접근할 것인가에 대한 규칙**
        - http, https, ftp etc
    - ~~userinfo@~~ → 거의 사용x
    - [www.google.com](http://www.google.com) → `호스트명`
    - 443 → `포트번호`
        - 일반적으로 생략
        - http:80 / ftp:443
    - /search → `리소스 경로`
    - ?q=hello&hl=ko → `쿼리`
        - ?로 시작하고 &로 추가
        - key : value
        - **쿼리 파라미터**, 쿼리 스트링이라고 부르기도 함
    - ~~#fragment~~ → html 내부 북마크

## 2.2 웹 브라우저 요청 흐름

### 1. URL로 `HTTP 요청 메세지` 생성
![Untitled 3](https://user-images.githubusercontent.com/87421893/167386684-c0d4a019-80be-46e7-92c1-d28ea6791946.png)
![Untitled 4](https://user-images.githubusercontent.com/87421893/167386700-5cb375da-c72d-4415-a234-54ee22d1eb4a.png)

### 2. HTTP 메세지를 `TCP/IP 패킷`에 담아서 서버로 전송
![Untitled 5](https://user-images.githubusercontent.com/87421893/167386728-61322a30-d5ea-4135-93e8-5dee162b9f1e.png)
![Untitled 6](https://user-images.githubusercontent.com/87421893/167386761-5a5d2f3c-e9c5-474f-88a8-ba2bfb220605.png)

### 3. 서버에서 전달받은 `패킷을 해석`하고 `HTTP 응답 메세지`를 보냄
![Untitled 7](https://user-images.githubusercontent.com/87421893/167386782-a8425bd8-d394-45ea-abed-22f5fe158c7b.png)
![Untitled 8](https://user-images.githubusercontent.com/87421893/167386825-0f16d331-959d-4026-806f-4a095d1a3791.png)
![Untitled 9](https://user-images.githubusercontent.com/87421893/167386836-92448417-5b95-4495-8332-3ba6e27d98cc.png)

<aside>
⭐ 정리
  
HTTP 요청 메시지 생성 → TCP/IP 패킷에 담아 서버로 전송 → 서버에서 패킷을 받아 해석하고 HTTP 응답 메시지 생성 → 전송

</aside>
