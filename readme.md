내일배움캠프 최종프로젝트 코딩테스트 사이트(2025.05.27~2025.07.08)

# 📢 Ezcode - 개발자 중심의 코딩테스트 플랫폼

> **Ezcode**는 채점 그 이상의 가치를 제공하는 개발자 중심 코딩테스트 플랫폼입니다.  
> 단순히 문제를 푸는 것을 넘어서, 지속적인 성장과 동기부여를 설계했습니다.

---
## 🚀 주요 특징

### ✅ 매일 한 문제씩, 꾸준히 실력 향상
- 실시간 채점 및 AI 코드 리뷰 제공
- GitHub 자동 연동으로 코드 관리 용이
- 캐릭터 성장과 랭킹 시스템을 통한 학습 동기 부여

---

## 🛠️ 주요 기능

### 👥 유저 서비스
- **회원가입/로그인**
    - 이메일 및 비밀번호, 소셜 로그인(Google, GitHub)
- **마이페이지**
    - 개인정보, 랭킹, 문제 현황, 프로필 이미지 관리
- **메일 인증**
    - 회원가입 시 인증 및 비밀번호 재설정 링크 제공
- **회원 탈퇴**
    - Soft Delete 방식으로 탈퇴 처리

---

### 📓 문제 서비스
- 관리자 전용 문제 등록/수정/삭제
- 카테고리 및 난이도별 필터링, 이미지 포함 문제 제공
- 문제별 점수 및 검색 기능

---

### 🖍️ 채점 및 리뷰
- 병렬 채점 시스템으로 빠른 피드백
- 제출별 테스트 케이스별 결과 확인
- AI 기반 코드 리뷰 제공 (시간 복잡도 분석 등)
- **매주 토큰 지급**: 매일 한 문제를 풀면 보상 증가
- GitHub 레포지토리 자동 푸시 기능

---

### 🎮 게임화 시스템
- 문제 풀이 시 캐릭터 성장 (레벨/포인트)
- 캐릭터 능력치, 아이템, 스킬 확인 가능
- **PVP, 어드벤처 모드**, 아이템/스킬 뽑기 시스템

---

### 💬 커뮤니티 기능
- 실시간 채팅 및 질문/토론 공유
- 문제 관련 토론 게시판 (정렬 기능 포함)
- 댓글/대댓글 소통 기능

---

### 📮 알림 시스템
- 토론글 추천, 댓글, 대댓글 등 알림 전송

---


---

## 📂 아키텍처

### ⚙️ 4-Layer Architecture + Port & Adapter Pattern

> 자세한 논의 내용은 아래 링크를 참고하세요:
- [ver1 아키텍처](https://www.notion.so/2012dc3ef51480e88b11fd1d69e9fc40?pvs=21)
- [ver2 아키텍처](https://www.notion.so/ver2-2012dc3ef514801f8e80dab143a11088?pvs=21)
- [ver3 아키텍처](https://www.notion.so/ver3-2022dc3ef51480ecb369f608ae0c3dc6?pvs=21)

---

## 💡 기술 스택

### 🖥 Language
- Java 17

### 🧑🏻‍💻 Backend
- Spring Boot, Spring Data JPA, QueryDSL, Spring Batch

### 🔐 Security
- Spring Security, JWT

### 💾 DB
- MySQL, Redis, MongoDB, ElasticCache

### 📜 Message
- ActiveMQ, Redis Stream

### 🖥 IDE
- IntelliJ IDEA

### 📲 External API
- Gmail SMTP, OPEN AI, Judge0

### 🧩 Test Tool
- Postman, JUnit5, nGrinder

### 📝 API Docs
- Swagger

### 🌐 Cloud Service
- AWS EC2, RDS, S3, LightSail, Cloudflare

### 🔨 Deployment Tools
- Jenkins, Git Actions, Docker, Nginx

### 📈 Monitoring
- Spring Actuator, Prometheus, Grafana

---