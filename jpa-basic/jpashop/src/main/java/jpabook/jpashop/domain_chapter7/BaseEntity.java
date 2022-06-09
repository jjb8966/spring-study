package jpabook.jpashop.domain_chapter7;

import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class BaseEntity {

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
