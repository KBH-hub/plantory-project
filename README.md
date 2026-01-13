 🌱 Plantory

식물 관리 & 나눔 커뮤니티 플랫폼

<img width="804" height="715" alt="스크린샷 2025-12-15 223946" src="https://github.com/user-attachments/assets/e93366c3-f550-4b07-904d-1723dded12c2" />
<img width="1390" height="672" alt="스크린샷 2025-12-15 223951" src="https://github.com/user-attachments/assets/5025e086-0fd8-4818-965a-ad5c41f7062a" />

🌟 20대 자취생을 위한
🌟 식물 관리 자동화 + 식물 나눔 커뮤니티 서비스

<img width="1432" height="804" alt="스크린샷 2025-12-15 223525" src="https://github.com/user-attachments/assets/26d6e35a-5327-436d-ad71-3df2600a4681" />
아키텍처
 🌿프로젝트 소개

Plantory는 식물을 키우는 데 어려움을 느끼는 자취생·초보 식집사를 위해
식물 관리 일정, 관찰 기록, 식물 나눔 커뮤니티를
하나의 플랫폼에서 제공하는 웹 서비스입니다.

 🌿 식물 관리 캘린더
 💧 물주기 알림 (SMS)
 📝 관찰일지 관리
 🤝 식물 나눔 & 질문 커뮤니티
 🔔 실시간 알림 시스템



 🎥 시연 영상

📺 서비스 시연 영상 (YouTube)
👉 [https://www.youtube.com/watch?v=nkgHNMzrm_M](https://www.youtube.com/watch?v=nkgHNMzrm_M)



 🖼️ 시스템 아키텍처

🌟 Spring Boot 3 기반의 MVC 구조 + Security + 외부 API 연동
<img width="1743" height="750" alt="image" src="https://github.com/user-attachments/assets/7fd78463-b07a-4b01-a8e4-06d5170e5730" />


주요 흐름

 Client (Axios) → Spring Security FilterChain
 DispatcherServlet → Controller → Service → MyBatis Mapper
 MariaDB / Google Cloud Storage / 외부 API 연동
 AWS EC2 (Ubuntu) 배포



 ⚙️ 기술 스택

 Backend

 Java 21
 Spring Boot 3 (Tomcat 10)
 Spring MVC
 Spring Security (JWT)
 MyBatis
 JUnit 5

 Frontend

 HTML5 / CSS3
 JavaScript (ES6)
 Axios
 Thymeleaf

 Database

 MariaDB

 Infra & DevOps

 AWS EC2 (Ubuntu 24.04)
 Google Cloud Storage (이미지 업로드)
 Solapi (SMS 알림)
 농사로 식물 정보 API

 Collaboration

 Git / GitHub (PR 기반 협업)
 Jira
 Confluence



 🧩 주요 기능

 🔐 계정 & 보안

 회원가입 / 로그인 / 로그아웃
 JWT 기반 인증
 Spring Security FilterChain 적용
 비밀번호 BCrypt 암호화
 사용자 정지 시 세션 강제 만료 처리



 📊 대시보드

 내 식물 수 확인
 오늘 물 줄 식물 수
 관심 필요 식물 수
 추천 나눔 게시글 확인
 오늘의 식물 관리 일정 요약



 🌿 내 식물 관리

 내 식물 등록 / 수정 / 삭제
 식물 이름 검색
 식물 캘린더

   물주기 등록 / 수정 / 삭제
   관찰일지 등록 / 삭제
   월 단위 전체 조회
   일 단위 상세 조회
 물주기 SMS 알림 발송



 📖 식물 사전

 실내 식물 / 건조 식물 분류
 필터 기반 검색
 식물 상세 정보 제공 (외부 API 연동)


<img width="1431" height="764" alt="스크린샷 2025-12-15 223552" src="https://github.com/user-attachments/assets/5aad4f24-cd57-4d1b-b7db-650a53d5827c" />
나눔 게시판 우선순위 알고리즘 로직
 🤝 나눔 커뮤니티

 나눔 게시글 목록 / 검색
 주소 기반 나눔 게시글 조회
 나눔 게시글 등록 / 수정 / 삭제
 관심 나눔 설정
 댓글 CRUD
 나눔 완료 처리
 나눔 후기 작성



 ❓ 질문 커뮤니티

 질문 게시글 등록 / 검색
 질문 상세 조회
 답글(댓글) CRUD
 질문 게시글 수정 / 삭제



 ✉️ 쪽지 & 알림

 쪽지함 (수신 / 발신 관리)
 쪽지 읽음 처리
 제목 기반 검색
 알림 생성 / 조회 / 읽음 처리
 알림 소프트 삭제



 👤 프로필

 내 프로필 / 타인 프로필 조회
 나눔 지수 확인
 관심 나눔 글 수 / 나눔 내역 수
 내가 쓴 글 / 댓글 단 글 검색
 글 삭제 플래그 처리
 회원 정보 수정 / 비밀번호 변경
 회원 탈퇴



 🚨 신고 & 관리자 기능

 사용자 신고
 신고 관리
 회원 관리
 부적절 게시글 삭제
 가중치 관리 시스템



 🗂️ 프로젝트 구조 (Backend)

```text
src
 └─ main
    ├─ java
    │   └─ com.plantory
    │       ├─ config
    │       ├─ security
    │ 
    │       (각 기능 마다)
    │       ├─ controller
    │       ├─ service
    │       ├─ mapper
    │       ├─ domain
    │       └─ util
    └─ resources
        ├─ mapper
        ├─ templates
        └─ application.yml
```



 🚀 배포 환경

 AWS EC2 (Ubuntu 24.04 LTS)
 Spring Boot JAR 배포
 MariaDB 설치 및 연동
 GCS 서비스 계정 기반 이미지 업로드
 환경변수 기반 설정 관리



 🧪 테스트

 JUnit 5 기반 단위 테스트
 Service / Mapper 레이어 테스트

<img width="776" height="505" alt="image" src="https://github.com/user-attachments/assets/7318a342-8ee2-4e5d-91af-8b84905023c6" />

<img width="1498" height="858" alt="image" src="https://github.com/user-attachments/assets/15b1355c-dc51-4aab-98d4-1fb7dc77a608" />

 👥 팀 협업

 GitHub PR 기반 코드 리뷰
 기능 단위 브랜치 전략
 Jira 이슈 관리
 Confluence 문서화



 🌿프로젝트 의의

 실사용자를 가정한 현실적인 기능 설계
 Spring Security + JWT 기반 인증 흐름 이해
 외부 API(SMS, 공공데이터, GCS) 연동 경험
 EC2 배포 및 서버 운영 경험
 PR 기반 협업 프로세스 경험
