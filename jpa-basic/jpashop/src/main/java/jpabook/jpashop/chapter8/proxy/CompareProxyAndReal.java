package jpabook.jpashop.chapter8.proxy;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CompareProxyAndReal {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tr = em.getTransaction();

        tr.begin();

        try {
            Member member1 = new Member();
            member1.setUsername("member1");
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("member2");
            em.persist(member2);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member1.getId());
            Member refMember = em.getReference(Member.class, member2.getId());

            isSameClassWrong(findMember, refMember);
            isSameClassRight(findMember, refMember);

            em.clear();

            // **jpa -> 한 트랜잭션 내에서 같은 pk로 조회한 객체는 무조건 같도록(==) 보장해줌**

            // member1에 대한 실제 객체를 영속성 컨텍스트에 저장
            Member findMember2 = em.find(Member.class, member1.getId());
            System.out.println("findMember2.getClass() = " + findMember2.getClass());   // class jpabook.jpashop.chapter8.proxy.Member

            // getReference -> 가짜 프록시 객체가 아니라 실제 객체를 저장하고 있음
            Member refMember2 = em.getReference(Member.class, member1.getId());

            System.out.println("findMember2 == refMember2 : " + (findMember2 == refMember2));   //true

            em.clear();

            // 프록시 객체 저장
            Member refMember3 = em.getReference(Member.class, member1.getId());
            System.out.println("refMember3.getClass() = " + refMember3.getClass()); // class jpabook.jpashop.chapter8.proxy.Member$HibernateProxy$RTAtuAa4

            // find 했는데 프록시 객체가 반환됨
            Member findMember3 = em.find(Member.class, member1.getId());

            System.out.println("findMember3 == refMember3 : " + (findMember3 == refMember3));   //true

            tr.commit();
        } catch (Exception e) {
            tr.rollback();
        } finally {
            em.close();
        }

        emf.close();
    }

    private static void isSameClassRight(Member member1, Member member2) {
        System.out.println("member1 instanceof Member : " + (member1 instanceof Member));
        System.out.println("member2 instanceof Member : " + (member2 instanceof Member));
    }

    private static void isSameClassWrong(Member member1, Member member2) {
        System.out.println("member1 == member2 : " + (member1.getClass() == member2.getClass()));
    }
}
