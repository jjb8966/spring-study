package jpabook.jpashop.domain_chapter6.one_to_many;

import javax.persistence.*;

//@Entity
public class MemberV2 {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String username;
}
