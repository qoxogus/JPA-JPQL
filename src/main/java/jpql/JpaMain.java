package jpql;

import jpql.entity.Member;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class) //TypedQuery 타입정보를 받을 수 있을 때(반환 타입이 명확할 때 사용)
                    .setParameter("username", "member1")
                    .getSingleResult();
            System.out.println("result = " + result.getUsername());


//            List<Member> resultList = query.getResultList(); //결과가 하나 이상이 일 대 리스트 반환 (결과가 없다면 빈 리스트를 반환한다)
//
//            Member result = query.getSingleResult(); // 결과가 정확히 하나, 단일 객체 반환 (결과가 없거나(NoResult) 둘 이상이면(NonUniqueResult) Exception)
//            System.out.println("result = " + result);

//            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
//            Query query3 = em.createQuery("select m.username, m.age from Member m"); //Query 반환타입이 명확하지 않을 때 사용

            tx.commit(); //트랜젝션 커밋시점에 쿼리가 나가게 된다
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
