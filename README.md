# tcat-api
개인 토이프로젝트 Tcat의 REST API 입니다.

## 개요
Tcat-api 프로젝트의 소개 및 기술 스택과 목적에 관한 설명입니다.

---

### 소개
Tcat은 주기능으로 영화 예매를 모의적으로 진행할 수 있도록 제작된 프로젝트입니다.
부기능은 보편적으로 사용되는 회원 기능, 공지사항 등 데이터 조회의 기능이 존재합니다.

개인적으로 영화관에서 영화 보는 것을 좋아해서 해당 주제를 선택했습니다.

---

### 기술 스택
+ JDK 17
+ Spring boot 3.3.7
+ JPA (Hibernate 6)
+ QueryDSL
+ Redis 7.2.7
+ PostgreSQL 14.15

---

### 목적
프로젝트를 작업하게 된 목적은 크게 두가지로 나눴습니다.

+ Spring boot에서 동시성 제어
+ 다른 작업간에 사용할 코드 및 패턴 레퍼런스

영화 티켓을 다른 사용자와 동시에 또는 비슷한 시간대에 예매를 하게되는 경우가 있습니다.
이 경우 같은 좌석을 예매하게 되거나, 제한된 좌석 수를 넘어 예매되는 상황이 발생해 내부적으로는 오류가 생길 수 있습니다.

위와 같은 동시성 제어 문제를 해결할 3가지 방법으로
**sychronized, 비관적 락 (Pessimistic Lock), 낙관적 락 (Optimistic Lock)** 이 있습니다.

비관적 락은 성능 저하의 문제, 낙관적 락은 데이터 충돌이 많으면 트랜잭션이 자주 발생 하는 대표적인 이유와 두 방법은 단일 서버보다 다중 서버에서 적합한 방법이라 판단해 배제했습니다.


현재 Tcat은 단일 서버에 배포되어 있으므로 sychronized 방법을 선택했습니다.

그리고 개인적인 공부와 실습, 실무에서 참고 할 수 있도록 프로젝트를 제작했습니다.

---

## 프로젝트 구조
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
![tcat_db_arch1.png](https://github.com/kikimansong/tcat-api/blob/main/tcat_db_arch1.png?raw=true)