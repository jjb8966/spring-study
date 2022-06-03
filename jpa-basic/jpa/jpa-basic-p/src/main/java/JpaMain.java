import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        try {
            Member member = new Member();
            member.setId(2L);
            member.setName("member2");
            em.persist(member);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            em.clear();
        }

        emf.close();
    }
}
