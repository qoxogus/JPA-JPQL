package jpql;

import jpql.dto.MemberDTO;
import jpql.entity.Address;
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

            for(int i=0;i<100;i++) {
                Member member = new Member();
                member.setUsername("member1"+i);
                member.setAge(i);
                em.persist(member);
            }

            em.flush();
            em.clear();

//            Member result = em.createQuery("select m from Member m where m.username = :username", Member.class) //TypedQuery 타입정보를 받을 수 있을 때(반환 타입이 명확할 때 사용)
//                    .setParameter("username", "member1")
//                    .getSingleResult();
//            System.out.println("result = " + result.getUsername());

//            List<Member> result = em.createQuery("select m from Member m", Member.class) //select되는 대상이 많더라도 다 영속성 컨텍스트에 반영된다
//                    .getResultList();
//
//            Member findMember = result.get(0);
//            findMember.setAge(20); //영속성 컨텍스트에 관리되기 때문에 age가 정상적으로 20으로 바뀌는걸 확인할 수 있다

//            List<Member> resultList = query.getResultList(); //결과가 하나 이상이 일 대 리스트 반환 (결과가 없다면 빈 리스트를 반환한다)
//
//            Member result = query.getSingleResult(); // 결과가 정확히 하나, 단일 객체 반환 (결과가 없거나(NoResult) 둘 이상이면(NonUniqueResult) Exception)
//            System.out.println("result = " + result);

//            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
//            Query query3 = em.createQuery("select m.username, m.age from Member m"); //Query 반환타입이 명확하지 않을 때 사용

//            em.createQuery("select o.address from Order o", Address.class) //임베디드타입 프로젝션
//                    .getResultList();

//            List<Object[]> resultList = em.createQuery("select distinct m.username, m.age from Member m") //distinct : 중복제거  스칼라타입 프로젝션
//                    .getResultList();

//            Object o = resultList.get(0); List<Object[]>를 쓰면 2줄 생략 가능
//           Object[] result = (Object[]) o;
//            Object[] result = resultList.get(0);
//            System.out.println("username = " + result[0]);
//            System.out.println("age = " + result[1]);

//            List<MemberDTO> result = em.createQuery("select new jpql.dto.MemberDTO(m.username, m.age) from Member m", MemberDTO.class)
//                    .getResultList();
//
//            MemberDTO memberDTO = result.get(0);
//            System.out.println("memberDTO.getUsername() = " + memberDTO.getUsername());
//            System.out.println("memberDTO.getAge() = " + memberDTO.getAge());

            List<Member> result = em.createQuery("select m from Member m order by m.age desc", Member.class)
                    .setFirstResult(1)
                    .setMaxResults(10)
                    .getResultList();

            System.out.println("result.size() = " + result.size());
            for (Member member1 : result) {
                System.out.println("member1 = " + member1);
            }

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
