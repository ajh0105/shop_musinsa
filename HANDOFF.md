# 무신사 쇼핑몰 포트폴리오 — 핸드오프 문서

> **작성일**: 2026-06-08  
> **현재 브랜치**: `claude/musinsa-shopping-mall-8yefn`  
> **마지막 커밋**: `1e09bcb` — docker 설정 추가 및 Vue 컴포넌트 수정

---

## 프로젝트 구조

```
c:\jh\shop_musinsa\
├── docker-compose.yml          # 전체 서비스 오케스트레이션 (7개 서비스)
├── docker/
│   └── init/
│       └── 01_init.sh          # PostgreSQL 초기화 스크립트 (LF 줄끝 필수)
├── pure/                       # 무신사 프론트엔드 (Vue 3 + Vite 8)
│   ├── Dockerfile              # node:20-alpine → nginx:alpine
│   ├── nginx.conf              # SPA + /v1 → musinsa-backend:8085 프록시
│   └── src/
│       ├── style.css           # ★ 전면 재작성된 디자인 시스템
│       ├── components/
│       │   ├── Header.vue      # ★ 스크롤 블러 효과 추가
│       │   └── Footer.vue
│       ├── views/
│       │   ├── Home.vue        # ★ 에디토리얼 섹션 번호 + 랭킹 + 배너 카운터
│       │   ├── Category.vue
│       │   ├── ProductDetail.vue
│       │   ├── Cart.vue / CartCheckout.vue / Checkout.vue
│       │   ├── Login.vue / Register.vue / Terms.vue
│       │   ├── MyPage.vue
│       │   ├── Board.vue / FAQ.vue / AllReviews.vue
│       │   └── admin/
│       │       ├── AdminLayout.vue
│       │       ├── Dashboard.vue / Products.vue / Orders.vue
│       │       ├── Members.vue / Reviews.vue / QnA.vue / Faq.vue
│       └── composables/
│           ├── useAuth.js
│           └── useImgFallback.js
├── shop_musinsa/shop/          # 무신사 백엔드 (Spring Boot 3.2 / Java 21)
│   ├── Dockerfile
│   └── src/main/java/com/musinsa/shop/
│       ├── account/            # 로그인/회원가입/세션
│       ├── admin/              # 관리자 CRUD API
│       ├── cart/               # 장바구니
│       ├── item/               # 상품 조회
│       ├── order/              # 주문
│       ├── review/             # 리뷰
│       ├── qna/                # Q&A
│       └── faq/                # FAQ
└── shop_attrangs/              # 아뜨랑스 서브 프로젝트 (별도 쇼핑몰)
    ├── frontend/               # Vue 3 + Vite (node:20-alpine)
    └── shop_attrangs/shop/     # Spring Boot 3.2 / Java 21 (port 8086)
```

---

## Docker 환경

### 서비스 목록

| 서비스 | 포트 | 설명 |
|--------|------|------|
| `postgres` | 5432 | PostgreSQL 14 (DB: `shop`, `shop_attrangs`) |
| `musinsa-backend` | 8085 | 무신사 Spring Boot API |
| `musinsa-frontend` | 3000 | 무신사 Vue SPA (nginx) |
| `attrangs-backend` | 8086 | 아뜨랑스 Spring Boot API |
| `attrangs-frontend` | 3001 | 아뜨랑스 Vue SPA (nginx) |
| `musinsa-db-seeder` | — | DB 시드 컨테이너 (일회성) |
| `attrangs-db-seeder` | — | DB 시드 컨테이너 (일회성) |

### 실행 방법

```bash
# 프로젝트 루트에서 (c:\jh\shop_musinsa)
docker compose up -d --build

# 무신사: http://localhost:3000
# 아뜨랑스: http://localhost:3001
```

### 개별 서비스 재빌드 (프론트 수정 후)

```bash
docker compose build musinsa-frontend
docker compose up -d musinsa-frontend
```

### DB 시딩 구조

- **1단계**: `01_init.sh`가 postgres 기동 시 `shop_attrangs` DB 생성 + 기본 데이터 INSERT
- **2단계**: `musinsa-db-seeder`/`attrangs-db-seeder`가 Spring Boot가 JPA로 `members` 테이블을 생성할 때까지 폴링 후 전체 DDL 시드 실행

---

## 디자인 시스템 (최근 전면 수정)

### style.css 주요 CSS 변수

```css
:root {
  --color-primary:    #0a0a0a;
  --color-accent:     #E8282B;   /* 무신사 레드 */
  --color-sale:       #E8282B;
  --color-text:       #111111;
  --color-text-muted: #767676;
  --color-text-light: #b0b0b0;
  --color-bg:         #ffffff;
  --color-bg-gray:    #f5f5f5;
  --color-border:     #ebebeb;
  --radius-sm: 2px;
  --radius-md: 4px;
  --radius-lg: 8px;
  --ease:       cubic-bezier(0.4, 0, 0.2, 1);
  --transition: 0.2s var(--ease);
  --container-max: 1280px;
}
```

### 주요 새 클래스

| 클래스 | 설명 |
|--------|------|
| `.site-header--scrolled` | 스크롤 시 헤더 backdrop-filter blur(12px) 반투명 |
| `.section-eyebrow` | `— 01 NEW ARRIVAL` 스타일 섹션 번호 라벨 |
| `.section-title-group` | eyebrow + title 묶음 flex 컨테이너 |
| `.section-more` | `ALL VIEW →` 스타일 대문자 링크 |
| `.banner-counter` | `01 / 05` 슬라이드 카운터 (배너 우상단) |
| `.product-rank` | 베스트 상품 이미지에 오버레이되는 이탤릭 랭킹 숫자 |

---

## 백엔드 API 구조 (무신사)

베이스 URL: `/v1/api/`

| 경로 | 설명 |
|------|------|
| `POST /account/login` | 로그인 (세션 쿠키) |
| `POST /account/logout` | 로그아웃 |
| `POST /account/join` | 회원가입 |
| `GET /account/check` | 현재 세션 확인 |
| `GET /items` | 상품 목록 (`?sort=best\|new\|recommend`, `?category=OUTER` 등) |
| `GET /items/{id}` | 상품 상세 |
| `GET /cart` | 장바구니 조회 |
| `POST /cart` | 장바구니 추가 |
| `POST /order` | 주문 생성 |
| `GET /mypage/orders` | 내 주문 목록 |
| `GET /reviews` | 전체 리뷰 |
| `POST /reviews` | 리뷰 작성 |
| `GET /qna` | Q&A 목록 |
| `GET /faq` | FAQ 목록 |
| `GET /board` | 게시판 |
| `/admin/*` | 관리자 API (세션 권한 필요) |

카테고리: `OUTER`, `TOP`, `PANTS`, `SHOES`, `BAG`, `ACC`, `OUTLET`

---

## 기술 스택

| 영역 | 스택 |
|------|------|
| 프론트엔드 | Vue 3, Vite 8, Vue Router 4 |
| 백엔드 | Spring Boot 3.2, Java 21, Spring Data JPA |
| DB | PostgreSQL 14 |
| 인증 | Session 기반 (서버 세션 + 쿠키) |
| 빌드 | Gradle (백엔드), npm (프론트엔드) |
| 컨테이너 | Docker, Docker Compose |
| 웹서버 | nginx:alpine (프론트엔드 SPA + 역프록시) |

---

## 알려진 사항 / 주의점

1. **Node 버전**: Vite 8은 Node 20+ 필요. `node:18-alpine`으로 변경 시 `CustomEvent` 에러 발생.
2. **Shell 스크립트 줄끝**: `docker/init/01_init.sh`는 반드시 LF. `.gitattributes`에 `*.sh text eol=lf` 설정됨.
3. **Spring Boot DB URL**: Docker 내부에서 `localhost:5432`가 아닌 `postgres:5432` 필요. `docker-compose.yml`에서 `SPRING_DATASOURCE_URL` 환경변수로 주입.
4. **업로드 이미지**: 백엔드 컨테이너의 `/app/uploads/` 경로. 현재 컨테이너 재시작 시 초기화됨 (볼륨 미설정).
5. **관리자 계정**: DB 시딩 SQL 참조 (`docker/` 하위 DDL 파일).

---

## 남은 개선 가능 작업

- [ ] 상품 상세 페이지(`ProductDetail.vue`) 디자인 개선
- [ ] 카테고리 페이지(`Category.vue`) 필터/정렬 UI 고도화
- [ ] 마이페이지(`MyPage.vue`) 주문 내역 UI 개선
- [ ] 이미지 업로드 볼륨 마운트 추가 (docker-compose.yml)
- [ ] 아뜨랑스 프론트엔드 동일 수준 디자인 개선
- [ ] 반응형(모바일) CSS 보완

---

## Claude에게 작업 인계 시 참고 사항

- 디자인 토큰은 모두 `pure/src/style.css` 최상단 `:root` 블록에 있음
- Vue 컴포넌트는 `<script setup>` Composition API 사용
- 인증 상태는 `useAuth.js` composable로 관리 (localStorage 기반)
- 백엔드는 `@RestController` + 세션 인증, CORS는 Spring Security에서 설정
- DB 스키마는 `ddl-auto: update` — 엔티티 수정 시 테이블 자동 변경
