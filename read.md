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

## 영속성 컨텍스트란
- 엔티티를 영구 저장하는 환경

### 1차 캐시
- 영속성 컨텍스트 내부에 있는 첫 번째 캐시,
- 조회 시 1차 캐시에 데이터가 이미 있는지 확인하고, 데이터가 있으면 가져온다.
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

### 변경 감지(Dirty Checking)
- commit() 후에 flush()가 실행되고, 1차 캐시에는 ID, Entity, SnapShot 이 생김
- 그 후 엔티티와 스냅샷을 비교하고 쓰기지연SQL 저장소에 UPDATE 쿼리를 날림
- 즉 객체를 변경하게 되면 자동으로 UPDATE 쿼리가 실행된다고 생각하면 됨.
```
Member updateMember = em.find(Member.class, 105);
updateMember.setName("test");
commit();
```
### flush() 란?
- 