# 무신사 쇼핑몰 클론 프로젝트

> Vue.js 3 기반 무신사 스타일 쇼핑몰 프론트엔드 포트폴리오 프로젝트

## 라이브 데모

**GitHub Pages:** https://ajh0105.github.io/shop_musinsa/

> 백엔드 서버 없이 **UI/프론트엔드 구성을 확인**할 수 있도록 GitHub Pages에 정적 배포되어 있습니다.
> 실제 데이터 연동(로그인, 주문 등)은 로컬 환경에서 백엔드를 함께 실행해야 합니다.

---

## 프로젝트 소개

무신사 쇼핑몰을 참고하여 제작한 풀스택 쇼핑몰 클론 프로젝트입니다.

- **GitHub Pages 배포 목적:** 프론트엔드(Vue.js) UI/UX 구현 역량을 보여주기 위해 정적 빌드로 배포
- **백엔드 포함 전체 실행:** 로컬 환경에서 Docker Compose로 Spring Boot + MySQL + Vue.js를 함께 실행
- `portfolio` 브랜치의 `/docs` 폴더가 GitHub Pages의 배포 소스입니다

---

## 기술 스택

### Frontend (GitHub Pages에서 확인 가능)
| 기술 | 버전 |
|------|------|
| Vue.js | 3.x |
| Vue Router | 4.x (Hash History — 정적 호스팅 SPA 지원) |
| Vite | 8.x |
| CSS (Vanilla) | - |

### Backend (로컬 실행 시)
| 기술 | 설명 |
|------|------|
| Spring Boot | REST API 서버 (포트 8085) |
| MySQL | 데이터베이스 |
| Docker / Docker Compose | 컨테이너 환경 구성 |

---

## 주요 기능

### 사용자 페이지
- **홈** — 메인 배너 슬라이더, 카테고리 빠른 메뉴, 신상품/추천 상품 목록
- **카테고리** — 카테고리별 상품 목록 및 필터링
- **상품 상세** — 상품 이미지, 설명, 옵션 선택, 리뷰
- **장바구니** — 상품 추가/삭제, 수량 변경, 합계 계산
- **결제** — 장바구니 결제 및 단건 결제 플로우
- **회원가입 / 로그인** — 로컬스토리지 기반 인증 상태 관리
- **마이페이지** — 주문 내역, 회원 정보 조회
- **게시판** — FAQ, Q&A 게시판, 전체 리뷰

### 관리자 페이지 (`/#/admin`)
- **대시보드** — 주요 지표 요약
- **회원 관리** — 회원 목록 및 상태 관리
- **상품 관리** — 상품 등록, 수정, 삭제
- **주문 관리** — 주문 상태 처리
- **리뷰 / Q&A / FAQ 관리**

---

## 폴더 구조

```
shop_musinsa/
├── pure/                   # Vue.js 프론트엔드 소스
│   ├── src/
│   │   ├── views/          # 페이지 컴포넌트
│   │   │   ├── admin/      # 관리자 페이지 (Dashboard, Members, Products, Orders 등)
│   │   │   └── *.vue       # 사용자 페이지 (Home, Category, Cart, Checkout 등)
│   │   ├── components/     # 공통 컴포넌트 (Header, Footer)
│   │   ├── composables/    # 재사용 가능한 로직
│   │   ├── router/         # Vue Router 설정
│   │   └── assets/         # 이미지, 아이콘 등 정적 파일
│   ├── public/             # 공개 정적 파일
│   ├── vite.config.js      # GitHub Pages base URL 설정 포함
│   └── package.json
├── shop_musinsa/           # Spring Boot 백엔드 소스
├── docker/                 # Docker 설정 파일
├── docker-compose.yml      # 전체 스택 실행 설정
├── docs/                   # GitHub Pages 배포 빌드 결과물 (pure/dist 복사본)
└── README.md
```

---

## 로컬 실행 방법

### 프론트엔드만 실행 (UI 확인)

```bash
cd pure
npm install
npm run dev
# http://localhost:5173 접속
```

### 풀스택 실행 (Docker Compose)

```bash
docker-compose up --build
# 프론트엔드: http://localhost:5173
# 백엔드 API: http://localhost:8085
```

> DB 초기화 SQL: `pure/musinsa_ddl1.sql`, `pure/musinsa_ddl2.sql`

---

## GitHub Pages 배포 구조

`portfolio` 브랜치의 `/docs` 폴더를 GitHub Pages 소스로 사용합니다.

- **배포 URL:** `https://ajh0105.github.io/shop_musinsa/`
- **라우터:** Hash History 방식 (`/#/`)으로 정적 서버에서 SPA 지원
- **Vite base:** `/shop_musinsa/` 설정으로 올바른 asset 경로 생성

빌드 결과 갱신 방법:
```bash
cd pure
npm run build
# dist/ 내용을 ../docs/ 로 교체
```

---

## 포트폴리오 목적

이 프로젝트는 다음 역량을 보여주기 위해 제작되었습니다:

- **Vue.js 3** Composition API 및 SFC(`<script setup>`) 활용
- **Vue Router** 를 이용한 SPA 라우팅 및 네비게이션 가드 구현 (일반/관리자 권한 분기)
- **컴포넌트 설계** — Header/Footer 공통 컴포넌트 분리, 재사용 가능한 구조
- **반응형 UI** — 무신사 스타일을 참고한 쇼핑몰 레이아웃 직접 구현
- **REST API 연동** — Spring Boot 백엔드와 fetch API를 통한 통신
- **정적 배포** — Vite 빌드 + GitHub Pages로 프론트엔드 데모 배포

---

## 작성자

**ajh0105** — [GitHub](https://github.com/ajh0105)
