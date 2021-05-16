package jpql;

import jpql.dto.MemberDTO;
import jpql.entity.Address;
import jpql.entity.Member;
import jpql.entity.MemberType;
import jpql.entity.Team;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("관리자");
            member.setAge(10);
            member.setType(MemberType.ADMIN);

            member.setTeam(team);

            em.persist(member);

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


            String query = "select m from Member m join m.team t"; //inner join
            String query1 = "select m from Member m left join m.team t"; //left outter join
            String query2 = "select m from Member m, Team t where m.username = t.name"; //cross join
            String query4 = "select m from Member m left join m.team t on t.name = 'teamA'"; //on절을 이용한 left조인
            String query5 = "select m from Member m left join Team t on m.username = t.name"; //회원의 이름과 팀의 이름이 같은 대상 외부조인
            String query6 = "select (select avg(m1.age) From Member m1) as avgAge from Member m join Team t on m.username = t.name"; //select절도 서브쿼리가 가능하다(하이버네이트지원) // from절은 서브쿼리가 불가능하다 조인으로 해결할 수 있으면 해결하고 안된다면 쿼리를 두번 날리는방법으로 해결하자 이 또한 안된다면 native로 넘긴다
            String query7 = "select m.username, 'HELLO', true from Member m where m.type = :userType"; //teamA, HELLO, true
//            List<Member> result = em.createQuery(query7, Member.class)
//                    .getResultList();

//            System.out.println("result.size() = " + result.size());
//            for (Member member1 : result) {
//                System.out.println("member1 = " + member1);
//            }

//            List<Object[]> result = em.createQuery(query7)
//                    .setParameter("userType", MemberType.ADMIN) //파라미터 바인딩
//                    .getResultList();

//            for (Object[] objects : result) {
//                System.out.println("objects[0] = " + objects[0]);
//                System.out.println("objects[0] = " + objects[1]);
//                System.out.println("objects[0] = " + objects[2]);
//            }

            String query8 =
                    "select " +
                        "case when m.age <=10 then '학생요금' " +
                            "when m.age >= 60 then '경로요금' " +
                            "else '일반요금' " +
                            "end  " +
                    "from Member m";
            String query9 = "select coalesce(m.username, '이름없는회원') from Member m"; //username이 없다면 이름없는회원 이라고 나온다
            String query10 = "select nullif(m.username, '관리자') from Member m"; //username == 관리자라면 null반환 아니라면 사용자 이름 반환
            List<String> result = em.createQuery(query10, String.class)
                    .getResultList();

            for (String s : result) {
                System.out.println("s = " + s);
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
