package hello.itemservice.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.jpa.JpaRepositoryV2;
import hello.itemservice.repository.jpa.JpaRepositoryV3;
import hello.itemservice.repository.jpa.SpringDataJpaItemRepository;
import hello.itemservice.service.ItemService;
import hello.itemservice.service.ItemServiceV1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@RequiredArgsConstructor
public class QuerydslConfig {

    private final EntityManager em;

    @Bean
    public ItemService itemService() {
        return new ItemServiceV1(itemRepository());
    }

    @Bean
    public ItemRepository itemRepository() {
        return new JpaRepositoryV3(em);
    }
}
