package jpql;

import jpql.dto.MemberDTO;
import jpql.entity.Address;
import jpql.entity.Member;
import jpql.entity.MemberType;
import jpql.entity.Team;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.SplittableRandom;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("teamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("teamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

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
            String query11 = "select 'a' || 'b' from Member m"; //concat concat('a', 'b')
            String query12 = "select substring(m.username, 2, 3) From Member m"; //substring  잘라내기
            String query13 = "select locate('de','abcdefg') from Member m "; //locate 4출력
            String query14 = "select size(t.members) from Team t"; //1

//            @OrderColumn
            String query15 = "select index(t.members) from Team t"; //쓰지마라 그냥 ㅋㅋ
            String query16 = "select m.username from Member m"; //상태필드 m.username에서 더 뻗어나갈 수 없음, 더이상 탐색X
            String query17 = "select m.team from Member m"; //단일값 연관경로 : 묵시적 내부조인(inner join) 발생, 탐색 O(ex m.team.name)  묵시적 내부조인이 일어나게 짜면 안된다 SQL과 JPQL을 최대한 맞추도록 코드를 짜보자
            String query18 = "select t.members.size from Team t"; //컬렉션값 연관경로 : 묵시적 내부조인(inner join) 발생, 탐색 X (FROM절에서 명시적 조인을 통해 별칭을 얻으면 별칭을 통해 탐색가능)  팀에있는 멤버 수
            String query19 = "select m.username from Team t  join t.members m"; //컬렉션값 연관경로 : (FROM절에서 "명시적 조인"을 통해 별칭을 얻으면 별칭을 통해 탐색가능)
            //명시적 조인을 사용하자 묵시적 내부조인 사용하면 쿼리 튜닝도 힘들다;;


//            List<String> result = em.createQuery(query12, String.class)
//                    .getResultList();
//            List<Integer> result = em.createQuery(query16, Integer.class)
//                    .getResultList();
//            List<Team> result = em.createQuery(query17, Team.class)
//                    .getResultList();
//            List result = em.createQuery(query18, Collection.class)
//                    .getResultList();
//            Integer result = em.createQuery(query18, Integer.class)
//                    .getSingleResult();
//            System.out.println("result = " + result);
//            List<Collection> result = em.createQuery(query19, Collection.class)
//                    .getResultList();
//            List<String> stringList = em.createQuery(query19, String.class)
//                    .getResultList();
//            System.out.println("stringList = " + stringList);

//            for (Integer  s : result) {
//                System.out.println("s = " + s);
//            }
//            for (Team  s : result) {
//                System.out.println("s = " + s);
//            }
//            for (Object o : result) {
//                System.out.println("o = " + o);
//            }
            String query20 = "select m from Member m";
            String query21 = "select m from Member m join fetch m.team";
            String query22 = "select t from Team t join fetch t.members";
            String query23 = "select distinct t from Team t join fetch t.members";
            String query24 = "select t from Team t";
            String query25 = "select m from Member m where m = :member"; //엔티티를 파라미터로 전달
            String query26 = "select m from Member m where m.id = :memberId"; //식별자(PK)를 파라미터로 전달
            String query27 = "select m from Member m where m.team = :team"; //외래키

//            List<Member> result = em.createQuery(query21, Member.class) //지연로딩보다 fetch조인이 항상 우선이라 result에 값들이 들어올때는 모두 프록시가 아닌 진짜엔티티가 들어온다  (다대일 관계)
//                    .getResultList();

//            List<Team> result = em.createQuery(query24, Team.class)
//                    .setFirstResult(0)
//                    .setMaxResults(2)
//                    .getResultList();

            Member findMember = em.createQuery(query25, Member.class)
                    .setParameter("member", member1)
                    .getSingleResult();

            Member findMemberId = em.createQuery(query26, Member.class)
                    .setParameter("memberId", member1.getId())
                    .getSingleResult();

            List<Member> members = em.createQuery(query27, Member.class)
                    .setParameter("team", teamA)
                    .getResultList();

//            System.out.println("result.size() = " + result.size());

            System.out.println("findMember = " + findMember);
            //엔티티를 사용하여 조회를 하든 아이디를 사용해서 조회를 하든 값은 똑같다
            System.out.println("findMemberId = " + findMemberId);

            for (Member member : members) {
                System.out.println("member = " + member); //팀에 있는 members두명 조회된 값 출력
            }

//            for (Member member : result) {
//                System.out.println("member = " + member.getUsername() + "," + member.getTeam().getName());
//                //회원1, teamA(SQL)
//                //회원2, teamA(1차캐시)
//                //회원3, teamB(SQL)
//                //회원 100명(모두 팀 소속이 다름) -> 100방쿼리.. N + 1   (페치조인으로 해결하자..!)
//            }

//            for (Team team : result) {
//                System.out.println("team = " + team.getName() + ", members=" + team.getMembers().size()); //일대 다 관계 (컬렉션 페치조인) 데이터 뻥튀기가 될 수 있음 (중복출력등)
//                for ( Member member : team.getMembers() ) {
//                    System.out.println("-> member = " + member); //중복회원이라는걸 보여주는 코드
//                }
//            }

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
