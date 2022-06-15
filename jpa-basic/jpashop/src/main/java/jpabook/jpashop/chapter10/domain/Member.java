package jpabook.jpashop.chapter10.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private Integer age;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
