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