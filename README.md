# tcat-api
개인 프로젝트 Tcat의 REST API 입니다.
+ 배포 사이트 https://tcat-front.site/
+ Front Git https://github.com/kikimansong/tcat
+ 데이터 제공 및 참고 https://www.megabox.co.kr/

## 개요
Tcat-api 프로젝트의 소개 및 기술 스택과 목적에 관한 설명입니다.

---

### 소개
Tcat은 영화 예매 사이트를 임의로 제작한 프로젝트 입니다. 주기능은 영화 예매,
부기능은 보편적으로 사용되는 회원 기능, 공지사항 등 데이터 조회의 기능이 존재합니다.

개인적으로 영화관에서 영화 보는 것을 좋아해서 해당 주제를 선택했습니다.

---

### 기술 스택
+ JDK 17
+ Spring boot 3.3.7
+ Gradle
+ JPA (Hibernate 6)
+ QueryDSL
+ Redis 7.2.7
+ PostgreSQL 14.15

---

### 목적
프로젝트를 작업하게 된 목적은 크게 두가지로 나눴습니다.

+ Spring boot 동시성 제어
+ 다른 작업간에 사용할 코드 및 패턴 참고

영화 티켓을 다른 사용자와 동시에 또는 비슷한 시간대에 예매를 하게되는 경우가 있습니다.
이 경우 같은 좌석을 예매하게 되거나, 제한된 좌석 수를 넘어 예매되는 상황이 발생해 내부적으로는 오류가 생길 수 있습니다.

위와 같은 동시성 제어 문제를 해결할 방법으로
**sychronized, 비관적 락 (Pessimistic Lock), 낙관적 락 (Optimistic Lock)** 등이 있습니다.

비관적 락은 성능 저하의 문제, 낙관적 락은 데이터 충돌이 많으면 트랜잭션이 자주 발생 하는 대표적인 이유와 두 방법은 단일 서버보다 분산 서버에서 적합한 방법이라 판단해 배제했습니다.


현재 Tcat은 단일 서버에 배포되어 있으므로 sychronized 방법을 선택했습니다.

그리고 개인적인 공부와 실습, 실무에서 참고 할 수 있도록 프로젝트를 제작했습니다.

---

## 디렉토리 구조
프로젝트의 디렉토리 구조입니다.

* config
* domain
  * controller
  * dto
  * entity
  * repository
  * service
* global
  * entity
  * error
  * jwt
  * redis
  * util

## DB 테이블 구성도
![tcat_db_arch](https://github.com/kikimansong/tcat-api/blob/main/tcat_db_arch1.png?raw=true)

## 로그인
로그인은 JWT 방식을 사용합니다. Access Token은 프론트엔드에 전달해 쿠키에 저장하며, Refresh Token은 Redis에 저장합니다.


![redis_refresh](https://raw.githubusercontent.com/kikimansong/tcat-api/refs/heads/main/redis_refresh.png)