# tcat-api
개인 토이프로젝트 Tcat의 REST API 입니다.

## 개요
Tcat 프로젝트의 소개 및 목적과 기술 스택에 대한 설명입니다.

---

### 소개
Tcat은 주기능으로 영화 예매를 모의적으로 진행할 수 있도록 제작된 프로젝트입니다.
부기능은 보편적으로 사용되는 회원 기능, 공지사항 등 데이터 조회의 기능이 존재합니다.

개인적으로 영화관에서 영화 보는 것을 좋아해서, 해당 주제로 선택했습니다.

---

### 목적
프로젝트를 작업하게 된 목적은 크게 두가지로 나눴습니다.

+ Spring boot에서 동시성 제어
+ 다른 작업간에 사용할 코드 및 패턴 레퍼런스

영화 티켓을 다른 사용자와 동시에 또는 비슷한 시간대에 예매를 하게되는 경우가 있습니다. 
이 경우 같은 좌석을 예매하게 되거나, 제한된 좌석 수를 넘어 예매되는 상황이 발생해 내부적으로는 오류가 생길 수 있습니다.


