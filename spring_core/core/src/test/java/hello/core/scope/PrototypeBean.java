package hello.core.scope;

import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Scope("prototype")
public class PrototypeBean {

    private int count = 0;

    public void addCount() {
        count++;
    }

    public int getCount() {
        return count;
    }

    @PostConstruct
    public void init() {
        System.out.println("PrototypeBean.init " + this);
    }

    @PreDestroy
    public void destroy() {
        System.out.println("PrototypeBean.destroy");
    }
}
