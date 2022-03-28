package spring.corepractice.beanfind;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import spring.corepractice.AppConfig;

public class ApplicationContextInfoTest {

    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

    @Test
    void findAllBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            Object bean = ac.getBean(beanDefinitionName);

            System.out.println("[beanDefinitionName] : " + beanDefinitionName + " [bean] : " + bean);
        }
    }

    @Test
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();

        for (String beanDefinitionName : beanDefinitionNames) {
            // BeanDefinition : 스프링 빈 메타 정보
            // getBeanDefinition() 메소드를 사용하려면 ApplicationContext 가 아닌 AnnotationConfigApplicationContext을 사용해야 함
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if (beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("[beanDefinitionName] : " + beanDefinitionName + " [bean] : " + bean);
            }
        }
    }
}
