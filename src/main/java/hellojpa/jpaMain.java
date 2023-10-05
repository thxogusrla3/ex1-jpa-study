package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class jpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            /* 영속성 컨텍스트 예시 */
            //객체를 생성한 상태(비영속) (commit 주석 풀것)*/
            Member member = new Member();
            member.setId(106L);
            member.setName("ㅇㅇㅇ");
            //객체를 생성한 상태(비영속)

            //객체를 영속성 컨텍스트에 저장한 상태(영속)
//            em.persist(member);
            //객체를 영속성 컨텍스트에 저장한 상태(영속)
            /* 영속성 컨텍스트 예시 */
            tx.commit();
            
            /* 1차 캐시 예제 */
            // find 는 총 4번이지만, select 쿼리는 1차 캐시로 인해 2개만 생성된다.
            Member a = em.find(Member.class, 105L);
            Member a1 = em.find(Member.class, 105L);
            Member b = em.find(Member.class, 101L);
            Member b1 = em.find(Member.class, 101L);

            System.out.println("a.name = " + a.getName());
            System.out.println("b.name = " + b.getName());
            System.out.println("a1.name = " + a1.getName());
            System.out.println("b1.name = " + b1.getName());
            /* 1차 캐시 예제 */
            
            /* 쓰기 지연 예시(commit 주석 풀것) */
            //영속성 컨텍스트에 저장해도 DB에 바로 insert 쿼리를 날리지 않는다.
            tx.begin();
            Member memberA = new Member(200L, "memberA");
            Member memberB = new Member(205L, "memberB");

            em.persist(memberA);
            em.persist(memberB);

//            tx.commit();
            /* 쓰기 지연 예시 */
            
            /* 변경 감지 예시 */
            Member updateMember = em.find(Member.class, 105);
            System.out.println("before " + updateMember.getName());
            updateMember.setName("test");
            System.out.println("after " + updateMember.getName());
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            //무조건 닫아줘야함.
            em.clear();
        }
    }
}
