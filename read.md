## *JPA 데이터베이스 방언(Dialect)
> JPA는 특정 RDBMS에 종속되지 않기위해 RDBMS 상관없이 SQL을 실행할 수 있도록 함.
- 방언이란 SQL 표준을 지키지 않는 특정 데이터베이스만의 고유한 기능이며, 이러한 부분으로 인해 문제가 발생한다.
- 위 문제의 해결이 곧 JPA 데이터베이스 방언이며, 하이버네이트를 예제로 들 수 있다.
- Dialect - H2Dialect , MySQLDialect, OracleDialect, H2Dialect

### ANSI SQL
- MS-SQL, Oracle, MySQL, PostgreSQL

### 하이버네이트란?
- 자바 언어를 위한 ORM 프레임워크
- JPA 구현체로 JPA 인터페이스를 구현하며, 내부적으로 JDBC API를 사용
- 실행: \h2\bin h2.bat

## *JPQL 시작
> JPA를 사용하면 엔티티 객체를 중심으로 개발되기 때문에 모든 DB 데이터를 객체로 변환해서 검색하는 것은 불가능 한 이유로 생김.
- 필요한 데이터만 DB에서 불러오려면 결국 검색 조건이 포함된 SQL이 필요하고 JPQL 이 이를 지원함.
- JPQL은 ***엔티티 객체***, SQL은 ***데이터베이스 테이블*** 을 대상으로 쿼리

## JPA에서 가장 중요한 2가지
- 객체와 관계형 데이터베이스 매핑하기
- 영속성 컨텍스트

## 영속성 컨텍스트(Persistence Context)란
- 엔티티를 영구 저장하는 환경
- persist() 시점에는 영속성 컨텍스트에 저장하는 것이다.

```
EntityManager.persist(entity);
```
### 영속성 컨텍스트 이점
1) 1차 캐시
2) 동일성 보정
3) 트랜잭션을 지원하는 쓰기 지연(버퍼링)
4) 변경 감지(Dirty Checking)
5) 지연 로딩(Lazy Loading)

###  1) 1차 캐시
- 영속성 컨텍스트 내부에 있는 첫 번째 캐시,
- 동일한 트랜잭션 내에서 조회 시 1차 캐시에 데이터가 이미 있는지 확인하고, 데이터가 있으면 가져온다.
- 1차 캐시에 데이터가 없다면, 데이터베이스에 데이터를 요청한다.
- 데이터베이스에서 받은 데이터를 다음에 재사용할 수 있도록 1차 캐시에 저장한다.

```
  Member a = em.find(Member.class, 105L);
  Member a1 = em.find(Member.class, 105L);
  Member b = em.find(Member.class, 101L);
  Member b1 = em.find(Member.class, 101L);
  
  System.out.println("a.name = " + a.getName());
  System.out.println("b.name = " + b.getName());
  System.out.println("a1.name = " + a1.getName());
  System.out.println("b1.name = " + b1.getName());

//a1, b1 은 앞서 실행된 a, b가 1차 캐시에 남아서 조회쿼리는 두 번만 실행된다.
```
### 2) 동일성 보장
- Java 컬렉션에서 값을 가져 올때 동일한 주소 값을 가져오듯이, 같은 reference를 불러오면 동일성을 보장해준다.
```
  Member a = em.find(Member.class, "member1");
  Member b = em.find(Member.class, "member1");
  System.out.println(a == b); //결과: true
```

### 3) 트랜잭션을 지원하는 쓰기 지연 SQL 저장소
- commit() 하기 전 persist() 함수 후 실행될 쿼리들을 모아두는 곳
- commit() 후 쓰기 지연 SQL 저장소에 모아둔 쿼리들을 모두 DB에 날린다.

```
  Member memberA = new Member(200L, "memberA");
  Member memberB = new Member(205L, "memberB");

  em.persist(memberA);
  em.persist(memberB);
  
  commit();
```

### 변경 감지(Dirty Checking)
- 1차 캐시
  - @Id, Entity, SnapShot (값을 읽어온 최초의 상태)
  - SnapShot: 영속성 컨텍스트에 최초로 값이 들어왔을 때의 상태값을 저장함.
- 변경 감지 매커니즘
  - transaction.commt() 실행
  - flush() 가 일어나며, Entity와 SnapShot을 일일이 비교한다.
  - 변경사항이 있으면, UPDATE QUERY를 생성
  - 해당 UPDATE QUERY를 쓰기 지연 SQL 저장소에 넣는다
  - UPDATE Query를 DB에 반영 후 commt() 한다.

```
  Member updateMember = em.find(Member.class, 105);
  updateMember.setName("test");
  commit();
```

### flush() 란?
- 영속성 컨텍스트의 변경 내용을 DB에 반영하는 것.
- transaction commit 이 일어날 때 ***flush*** 가 일어나는데, 이 때 쓰기 지연 저장소에 쌓아놨던 INSERT, UPDATE, DELETE SQL 들이 DB에 날아감.
  - 영속성 컨텍스트를 비우는 것이 아님.
- 호출 방법
  - em.flush() 를 직접 호출
  - transaction.commit() 호출
  - JPQL 쿼리 작성 시 자동 호출

## 준영속
> 영속 상태의 엔티티를 영속성 컨텍스트에서 분리(detached)하는 것
- em.detach(entity) //특정 엔티티만 준영속으로
- em.clear() //영속성 컨텍스트를 완전히 초기화
- em.close() //영속성 컨텍스트 종료

## 엔티티 매핑
### 1) 객체와 테이블 매핑
- **@Entity**
  - 엔티티라하며, JPA가 관리함.
  - JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 필수
  - 기본 생성자 필수(파라미터가 없는 public or protected)
  - final, enum, interface, inner Class 사용 X
  - 속성: name
- **@Table**
  - 엔티티와 매핑할 테이블 지정
  - 속성
    - **name**: DB와 매핑할 테이블 이름, 기본값으로 entity 이름 사용
    - **catalog**: DB catalog 매핑
    - **schema**: DB schema 매핑
    - **uniqueConstraints(DDL)**: DDL 생성 시에 유니크 제약 조건 생성

2) 데이터베이스 스키마 자동 생성
- persistence.xml에 코드 추가 
  - \<property name="hibernate.hbm2ddl.auto" value="create" />
- value 종류
  - create: DROP + CREATE
  - create-drop : create와 같으나 종료시점에 테이블 DROP
  - update: 변경분만 반영
  - validate: 엔티티와 테이블이 정상 매핑되었는지만 확인
  - none: 사용하지 않음
3) 필드 매핑 어노테이션 정리
- @Column: 데이터베이스 컬럼 매핑
  - 속성
    - name: 필드와 매핑할 테이블의 컬럼 이름
    - insertable, updatetable: 등록, 변경 가능 여부
    - nullable(DDL): null 값의 허용 여부를 설정한다. false로 설정하면 DDL 생성 시에 not null 제약조건이 붙는다.
    - unique(DDL): @Table의 uniqueConstraints와 같지만 한 컬럼에 간단히 유니크 제약조건을 걸 때 사용한다.
    - columnDefinition(DDL): 데이터베이스 커럼 정보를 직접 줄 수 있다.
    - length: 문자 길이 제약, String 에만 사용
    - percision, scale(DDL): BigDecimal(BigInteger) 타입에서 사용, percision은 소수점을 포함한 전체 자릿수를, scale은 소수의 자릿수
- @Temporal: 날짜 타입 매핑
- @Enumerated: enum 타입 매핑
- @Lob: BLOG, CLOB 매핑
- @Transient: 특정 필드를 컬럼에 매핑하지 않음(매핑 무시)

## 기본 키 매핑
> 기본키 매핑하는 방법은 2가지로 ***직접 할당*** 과 ***자동 생성***이 있다.
```java
import javax.persistence.GeneratedValue;

class Member {
    /*자동 생성*/
    @Id //직접 할당
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /*자동 생성*/
    private Long id;
}
```
- 직접 할당: 기본키를 애플리케이션에서 직접 할당한다.
```java
class JpaMain {
    Member member = new Member();
    member.setId("1"); //애플리케이션에서 키 직접 할당
}
```
- 자동 생성 전략: 대리키 사용 방식
  - IDENTITY: 기본키 생성을 DB에 위임한다.
  - SEQUENCE: DB 시퀀스를 사용해서 기본키를 할당한다. em.persist() 할 때 기본키 때문에 이 시점에 insert 쿼리를 보낸다.
```java
@Entity
@SequenceGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        sequenceName = "MEMBER_SEQ", //매핑할 DB 시퀀스 이름
        initialValue = 1, allocationSize = 1
) //MEMBER_SEQ_GENERATOR 시퀀스 생성기 등록
public class Member {
  @Id
  @GeneratedValue( //Sequence 자동생성 전략을 타입으로 정함
          strategy = GenerationType.SEQUENCE,
          generator ="MEMBER_SEQ_GENERATOR"
  )
  private Long id;
}
/** 위 자바코드의 DDL
 * CREATE SEQUENCE [sequenceName]
 * start with [initailValue] increment by [allocationSize]
 * */
```
    
  - TABLE: 키 생성 테이블을 사용한다.

```java
@Entity
@TableGenerator(
        name = "MEMBER_SEQ_GENERATOR",
        table = "MY_SEQUENCES", //매핑할 DB 시퀀스 이름
        pkColumnValue = 1, allocationSize = 1
) //MEMBER_SEQ_GENERATOR 시퀀스 생성기 등록
public class Member {
  @Id
  @GeneratedValue( //Sequence 자동생성 전략을 타입으로 정함
          strategy = GenerationType.TABLE,
          generator = "MEMBER_SEQ_GENERATOR"
  )
  private Long id;
}
```
  - AUTO: 선택한 DB에 따라 위 전략 IDENTITY, SEQUENCD, TABLE 중 하나를 자동으로 선택한다.(default)

