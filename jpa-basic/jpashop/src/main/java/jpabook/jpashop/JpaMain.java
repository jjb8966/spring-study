package jpabook.jpashop;

import jpabook.jpashop.domain_chapter7.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("jpashop");
        EntityManager em = factory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        transaction.begin();

        try {
//            OrderItem orderItem = new OrderItem();
//            OrderItem orderItem2 = new OrderItem();
//            em.persist(orderItem);
//            em.persist(orderItem2);
//
//            Order order = new Order();
//            List<OrderItem> orderItems = order.getOrderItems();
//            orderItems.add(orderItem);
//            orderItems.add(orderItem2);
//            em.persist(order);

            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
        } finally {
            em.close();
        }

        factory.close();
    }

//    public static void main(String[] args) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction transaction = em.getTransaction();
//
//        transaction.begin();
//        try {
//
//        } catch (Exception e) {
//            transaction.rollback();
//        } finally {
//            em.close();
//        }
//
//        emf.close();
//    }
}
