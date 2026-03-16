# VENTALIZE (벙딸리제) 쇼핑몰 포트폴리오

> **스카프, 기성복, 향수, 악세사리, 가방** 전문 온라인 쇼핑몰  
> Spring Boot 3 + Vue 3 + PostgreSQL 기반 풀스택 프로젝트

---

## 📚 프로젝트 구조

```
shop_musinsa/
├── shop_musinsa/
│   ├── shop/                     # Spring Boot 백엔드
│   │   ├── src/main/java/com/ventalize/shop/
│   │   │   ├── account/          # 인증 (로그인/회원가입)
│   │   │   ├── member/           # 회원 관리
│   │   │   ├── item/             # 상품
│   │   │   ├── cart/             # 장바구니
│   │   │   ├── order/            # 주문/결제
│   │   │   ├── review/           # 상품 리뷰
│   │   │   ├── qna/              # 상품 Q&A
│   │   │   ├── board/            # 게시판 (FAQ, 1:1문의)
│   │   │   ├── admin/            # 관리자 API
│   │   │   └── common/           # 공통 설정/유틸
│   │   └── src/main/resources/
│   │       └── application.yml   # 서버 설정
│   └── ventalize_ddl.sql         # DB 초기화 SQL
└── pure/                         # Vue 3 프런트엔드
    ├── src/
    │   ├── views/
    │   │   ├── Home.vue
    │   │   ├── Category.vue
    │   │   ├── ProductDetail.vue
    │   │   ├── admin/            # 관리자 페이지
    │   │   └── board/            # 게시판 (FAQ, 1:1문의)
    │   ├── components/
    │   │   ├── Header.vue
    │   │   └── Footer.vue
    │   └── router/index.js
    ├── package.json
    └── vite.config.js
```

---

## 🛠️ 기술 스택

| 구분 | 기술 |
|------|------|
| Backend | Spring Boot 3.2.0, Spring Security, Spring Data JPA |
| Frontend | Vue 3.5, Vue Router 4, Vite 8 |
| Database | PostgreSQL 15+ |
| Build | Gradle (Java 21), npm |
| Auth | Session 기반 (Spring Security + BCrypt) |

---

## 🗂️ 주요 기능

### 사용자
- 회원가입 / 로그인 / 로그아웃 / 회원 탈퇴
- 마이페이지 (프로필 수정, 주문 내역, 리뷰, Q&A)
- 카테고리별 상품 탐색 (SCARVES / READY TO WEAR / PERFUME / ACC / BAG / SALE)
- 상품 검색, 정렬 (신상 / 베스트 / 추천)
- 장바구니 / 즉시 구매 / 결제
- 배송 현황 추적 (결제대기 → 결제완료 → 배송중 → 배송완료)
- 상품 리뷰 (별점 1~5)
- 상품 Q&A
- FAQ 게시판 조회
- 1:1 문의 (등록 / 조회 / 답변 확인)

### 관리자
- 대시보드 (매출 / 주문 현황 / 재고 부족 알림 / 미답변 문의)
- 회원 관리 (검색 / 등급 변경 / 강퇴)
- 상품 CRUD (이미지 업로드 포함)
- 주문 상태 변경 (원클릭)
- 리뷰 / Q&A 관리
- FAQ 등록 / 수정 / 삭제
- 1:1 문의 답변

---

## ⚙️ 설치 및 실행 가이드 (Step-by-Step)

### Step 1. PostgreSQL DB 생성

#### 1-1. PostgreSQL 설치 확인 및 접속
```bash
# PostgreSQL 버전 확인
psql --version

# postgres 사용자로 접속
psql -U postgres
```

#### 1-2. DB 생성
```sql
-- psql 프롬프트에서 실행
CREATE DATABASE ventalize;
\c ventalize
```

#### 1-3. 초기 데이터 SQL 실행
```bash
# 터미널에서 실행 (프로젝트 루트에서)
psql -U postgres -d ventalize -f shop_musinsa/ventalize_ddl.sql
```

또는 psql 접속 후:
```sql
\i /절대경로/shop_musinsa/ventalize_ddl.sql
```

> **✅ 완료 확인:** `✅ Ventalize DB 초기화 완료!` 메시지가 출력되면 성공

#### 기본 계정 정보
| 계정 | ID | 비밀번호 |
|------|----|----------|
| 관리자 | admin@ventalize.com | admin1234 |
| 테스트 회원1 | user1@ventalize.com | test1234 |
| 테스트 회원2 | user2@ventalize.com | test1234 |

---

### Step 2. IntelliJ 백엔드 실행

#### 2-1. 프로젝트 열기
1. IntelliJ IDEA 실행
2. `File → Open` → `shop_musinsa/shop/` 폴더 선택
3. **Gradle 프로젝트**로 인식될 때까지 대기 (우측 하단 진행바)

#### 2-2. 라이브러리 로드
- IntelliJ 우측 **Gradle 패널** → `🔄 새로고침(Reload)` 클릭
- 또는 터미널에서:
```bash
cd shop_musinsa/shop
./gradlew dependencies
```

#### 2-3. DB 연결 정보 확인
`src/main/resources/application.yml` 파일:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/ventalize
    username: postgres
    password: 1004        # ← 본인 PostgreSQL 비밀번호로 변경
```

#### 2-4. 서버 실행
- IntelliJ: `ShopApplication.java` 파일 우클릭 → `Run 'ShopApplication'`
- 또는 터미널:
```bash
./gradlew bootRun
```

> **✅ 완료 확인:** 콘솔에 `Started ShopApplication` + 포트 8085 표시

---

### Step 3. Vue.js 프런트엔드 실행

#### 3-1. Node.js 설치 확인
```bash
node -v   # v18 이상 권장
npm -v
```

#### 3-2. 의존성 설치
```bash
cd pure
npm install
```

#### 3-3. 개발 서버 실행
```bash
npm run dev
```

> **✅ 완료 확인:** `http://localhost:5173` 에서 벙딸리제 쇼핑몰 접속 가능

#### 3-4. 프록시 설정 확인
`pure/vite.config.js` 파일:
```js
server: {
  proxy: {
    '/v1': {
      target: 'http://localhost:8085',  // 백엔드 포트
      changeOrigin: true
    }
  }
}
```

---

## 🌐 API 포트 정보

| 서비스 | 포트 | URL |
|--------|------|-----|
| Vue 프런트엔드 | 5173 | http://localhost:5173 |
| Spring Boot API | 8085 | http://localhost:8085 |
| 관리자 패널 | 5173 | http://localhost:5173/admin |

---

## 📊 배송 상태 흐름

```
결제대기 (PENDING_PAYMENT)
    ↓
결제완료 (PAID)
    ↓
배송중 (SHIPPING)
    ↓
배송완료 (DELIVERED)

※ 어느 단계에서든 → 취소 (CANCELLED) 가능
```

---

## 🗃️ 주요 API 엔드포인트

### 공개 API
| Method | URL | 설명 |
|--------|-----|------|
| POST | /v1/api/account/join | 회원가입 |
| POST | /v1/api/account/login | 로그인 |
| POST | /v1/api/account/logout | 로그아웃 |
| GET | /v1/api/items | 상품 목록 (검색/필터/정렬) |
| GET | /v1/api/items/{id} | 상품 상세 |
| GET | /v1/api/board/faq | FAQ 목록 |

### 인증 필요
| Method | URL | 설명 |
|--------|-----|------|
| GET | /v1/api/orders | 내 주문 목록 |
| POST | /v1/api/orders | 주문 생성 |
| GET | /v1/api/board/inquiry/my | 내 1:1문의 |
| POST | /v1/api/board/inquiry | 1:1문의 등록 |

### 관리자 전용
| Method | URL | 설명 |
|--------|-----|------|
| GET | /v1/api/admin/dashboard | 대시보드 통계 |
| PATCH | /v1/api/admin/orders/{id}/status | 배송 상태 변경 |
| POST | /v1/api/admin/board/faq | FAQ 등록 |
| POST | /v1/api/admin/board/inquiry/{id}/answer | 문의 답변 |

---

## 🐛 트러블슈팅

### DB 연결 오류
```
application.yml의 username/password를 본인 PostgreSQL 설정에 맞게 수정하세요.
```

### 포트 충돌
```bash
# 8085 포트 사용 중인 프로세스 확인 (Mac/Linux)
lsof -i :8085
# Windows
netstat -ano | findstr :8085
```

### 이미지 업로드 경로
업로드된 이미지는 `shop_musinsa/shop/uploads/` 폴더에 저장됩니다.

---

## 📁 카테고리 코드표

| 카테고리 | URL 코드 | 설명 |
|----------|---------|------|
| 스카프 | SCARVES | 실크/울/캐시미어 스카프, 머플러 |
| 기성복 | READY_TO_WEAR | 블라우스, 재킷, 드레스, 팬츠 |
| 향수 | PERFUME | 오드퍼퓸, 오드뚜왈렛, 퍼퓸 오일 |
| 악세사리 | ACC | 목걸이, 이어링, 반지, 벨트, 헤어 |
| 가방 | BAG | 토트백, 크로스백, 클러치, 백팩 |
| 세일 | SALE | 시즌 오프 특가 상품 |

---

*Made with ❤️ by Ventalize Team*
