package hello.springmvc.basic;

import lombok.Data;

//@Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
@Data
public class HelloData {
    private String username;
    private int age;
}
