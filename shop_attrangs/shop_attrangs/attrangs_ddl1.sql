-- ==========================================================
-- ATTRANGS 쇼핑몰 DDL 1 (UTF-8)
-- DB: shop_attrangs
-- 실행 순서: 1번 먼저 실행 → 2번 실행
-- ==========================================================

-- ── 테이블 생성 ──────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS items (
    id           SERIAL PRIMARY KEY,
    brand        VARCHAR(50)  NOT NULL,
    name         VARCHAR(100) NOT NULL,
    category     VARCHAR(30)  NOT NULL,
    img_path     VARCHAR(255) NOT NULL,
    description  TEXT,
    price        INTEGER      NOT NULL,
    discount_per INTEGER      NOT NULL DEFAULT 0,
    stock_count  INTEGER      NOT NULL DEFAULT 0,
    view_count   INTEGER      NOT NULL DEFAULT 0,
    created_at   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS members (
    id         SERIAL PRIMARY KEY,
    name       VARCHAR(50)  NOT NULL,
    login_id   VARCHAR(100) NOT NULL UNIQUE,
    login_pw   VARCHAR(200) NOT NULL,
    role       VARCHAR(20)  NOT NULL DEFAULT 'ROLE_USER',
    grade      VARCHAR(20)  NOT NULL DEFAULT 'BRONZE',
    status     VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    phone      VARCHAR(20),
    address    VARCHAR(200),
    created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS wishlists (
    id         SERIAL PRIMARY KEY,
    member_id  INTEGER   NOT NULL,
    item_id    INTEGER   NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_wish_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_wish_item   FOREIGN KEY (item_id)   REFERENCES items(id)   ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS carts (
    id         SERIAL PRIMARY KEY,
    member_id  INTEGER   NOT NULL,
    item_id    INTEGER   NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_cart_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item   FOREIGN KEY (item_id)   REFERENCES items(id)   ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    id          SERIAL PRIMARY KEY,
    member_id   INTEGER      NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    address     VARCHAR(500) NOT NULL,
    payment     VARCHAR(10)  NOT NULL,
    card_number VARCHAR(16),
    amount      BIGINT       NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'PAYMENT_DONE',
    created_at  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_member FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE TABLE IF NOT EXISTS order_items (
    id         SERIAL PRIMARY KEY,
    order_id   INTEGER   NOT NULL,
    item_id    INTEGER   NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_item  FOREIGN KEY (item_id)  REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id         SERIAL PRIMARY KEY,
    member_id  INTEGER  NOT NULL,
    item_id    INTEGER  NOT NULL,
    order_id   INTEGER,
    rating     INTEGER  NOT NULL CHECK (rating BETWEEN 1 AND 5),
    content    TEXT     NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_item   FOREIGN KEY (item_id)   REFERENCES items(id)   ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS questions (
    id               SERIAL PRIMARY KEY,
    member_id        INTEGER      NOT NULL,
    item_id          INTEGER,
    title            VARCHAR(200) NOT NULL,
    content          TEXT         NOT NULL,
    is_secret        BOOLEAN      NOT NULL DEFAULT FALSE,
    is_answered      BOOLEAN      NOT NULL DEFAULT FALSE,
    qna_password     VARCHAR(100),
    answer_content   TEXT,
    answer_member_id INTEGER,
    answered_at      TIMESTAMP,
    created_at       TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_question_item   FOREIGN KEY (item_id)   REFERENCES items(id)   ON DELETE SET NULL
);

-- ── 기존 데이터 초기화 ────────────────────────────────────────
TRUNCATE TABLE order_items  RESTART IDENTITY CASCADE;
TRUNCATE TABLE wishlists    RESTART IDENTITY CASCADE;
TRUNCATE TABLE carts        RESTART IDENTITY CASCADE;
TRUNCATE TABLE reviews      RESTART IDENTITY CASCADE;
TRUNCATE TABLE questions    RESTART IDENTITY CASCADE;
TRUNCATE TABLE orders       RESTART IDENTITY CASCADE;
TRUNCATE TABLE members      RESTART IDENTITY CASCADE;
TRUNCATE TABLE items        RESTART IDENTITY CASCADE;

-- ── 상품 데이터 (카테고리별 16개, 총 112개) ──────────────────────
-- 카테고리: Best | 원피스 | 상의 | 하의 | 아우터 | 악세잡화 | 오늘의 할인
-- 페이지 사이즈 8 → 2페이지 분량

INSERT INTO items (brand, name, category, img_path, description, price, discount_per, stock_count, view_count) VALUES

-- ── Best (16) ──────────────────────────────────────────────────
('아뜨랑스', '시그니처 플레어 원피스', 'Best', 'https://picsum.photos/seed/att_b01/420/560', '아뜨랑스 대표 베스트셀러 플레어 원피스', 89000, 0, 24, 1240),
('아뜨랑스', '클래식 트위드 자켓', 'Best', 'https://picsum.photos/seed/att_b02/420/560', '매 시즌 완판되는 트위드 자켓', 129000, 0, 18, 1180),
('아뜨랑스', '데일리 크롭 블라우스', 'Best', 'https://picsum.photos/seed/att_b03/420/560', '여리핏 크롭 블라우스 데일리 필수템', 52000, 0, 38, 1050),
('아뜨랑스', '하이웨이스트 와이드 슬랙스', 'Best', 'https://picsum.photos/seed/att_b04/420/560', '롱 레그라인 완성 와이드 슬랙스', 69000, 0, 32, 980),
('아뜨랑스', '벨벳 미디 스커트', 'Best', 'https://picsum.photos/seed/att_b05/420/560', '고급스러운 벨벳 소재 미디 스커트', 75000, 0, 27, 920),
('아뜨랑스', '오버핏 캐시미어 코트', 'Best', 'https://picsum.photos/seed/att_b06/420/560', '캐시미어 블렌딩 오버핏 롱코트', 189000, 0, 14, 870),
('아뜨랑스', '리본 넥 미니 원피스', 'Best', 'https://picsum.photos/seed/att_b07/420/560', '리본 포인트 러블리 미니원피스', 65000, 0, 29, 810),
('아뜨랑스', '진주 레이어드 이어링', 'Best', 'https://picsum.photos/seed/att_b08/420/560', '베스트 악세서리 진주 이어링', 22000, 0, 65, 790),
('아뜨랑스', '스트라이프 크롭 니트', 'Best', 'https://picsum.photos/seed/att_b09/420/560', '베스트셀러 스트라이프 크롭 니트', 48000, 0, 42, 760),
('아뜨랑스', '린넨 셔츠 원피스', 'Best', 'https://picsum.photos/seed/att_b10/420/560', '봄여름 필수 린넨 셔츠 원피스', 79000, 0, 22, 730),
('아뜨랑스', '레이스 슬립 원피스', 'Best', 'https://picsum.photos/seed/att_b11/420/560', '우아한 레이스 슬립 원피스', 95000, 0, 19, 700),
('아뜨랑스', '코튼 오버핏 맨투맨', 'Best', 'https://picsum.photos/seed/att_b12/420/560', '편안한 착용감 오버핏 맨투맨', 45000, 0, 48, 680),
('아뜨랑스', '플리츠 미디 스커트', 'Best', 'https://picsum.photos/seed/att_b13/420/560', '우아한 실루엣 플리츠 미디 스커트', 68000, 0, 31, 650),
('아뜨랑스', '테일러드 블레이저', 'Best', 'https://picsum.photos/seed/att_b14/420/560', '모던 페미닌 테일러드 블레이저', 115000, 0, 17, 620),
('아뜨랑스', '새틴 슬립 스커트', 'Best', 'https://picsum.photos/seed/att_b15/420/560', '고급 새틴 소재 슬립 스커트', 58000, 0, 33, 600),
('아뜨랑스', '체크 하프 코트', 'Best', 'https://picsum.photos/seed/att_b16/420/560', '클래식 체크 패턴 하프 코트', 145000, 0, 12, 580),

-- ── 원피스 (16) ─────────────────────────────────────────────────
('아뜨랑스', '플로럴 플리츠 원피스', '원피스', 'https://picsum.photos/seed/att_op01/420/560', '봄 감성 가득한 플로럴 플리츠 원피스', 79000, 0, 28, 540),
('아뜨랑스', '랩 플레어 원피스', '원피스', 'https://picsum.photos/seed/att_op02/420/560', '날씬해 보이는 랩 플레어 원피스', 72000, 0, 24, 510),
('아뜨랑스', '체크 트위드 원피스', '원피스', 'https://picsum.photos/seed/att_op03/420/560', '클래식 체크 트위드 미디 원피스', 89000, 0, 20, 490),
('아뜨랑스', '쉬폰 맥시 원피스', '원피스', 'https://picsum.photos/seed/att_op04/420/560', '로맨틱 쉬폰 맥시 원피스', 98000, 0, 18, 470),
('아뜨랑스', '리본 벨트 미디 원피스', '원피스', 'https://picsum.photos/seed/att_op05/420/560', '리본 벨트 포인트 미디 원피스', 85000, 0, 22, 450),
('아뜨랑스', '스트라이프 셔츠 원피스', '원피스', 'https://picsum.photos/seed/att_op06/420/560', '캐주얼 스트라이프 셔츠 원피스', 68000, 0, 30, 430),
('아뜨랑스', '벨벳 오프숄더 원피스', '원피스', 'https://picsum.photos/seed/att_op07/420/560', '특별한 날을 위한 벨벳 오프숄더', 115000, 0, 14, 410),
('아뜨랑스', '스모크 미디 원피스', '원피스', 'https://picsum.photos/seed/att_op08/420/560', '편안하고 여성스러운 스모크 원피스', 75000, 0, 26, 390),
('아뜨랑스', '도트 프린트 미니 원피스', '원피스', 'https://picsum.photos/seed/att_op09/420/560', '사랑스러운 도트 프린트 미니 원피스', 65000, 0, 32, 370),
('아뜨랑스', '퍼프 슬리브 원피스', '원피스', 'https://picsum.photos/seed/att_op10/420/560', '트렌디한 퍼프 슬리브 원피스', 82000, 0, 20, 350),
('아뜨랑스', '스퀘어넥 미디 원피스', '원피스', 'https://picsum.photos/seed/att_op11/420/560', '모던한 스퀘어넥 미디 원피스', 78000, 0, 24, 340),
('아뜨랑스', '지그재그 니트 원피스', '원피스', 'https://picsum.photos/seed/att_op12/420/560', '유니크한 지그재그 패턴 니트 원피스', 88000, 0, 18, 330),
('아뜨랑스', '미디 컷아웃 원피스', '원피스', 'https://picsum.photos/seed/att_op13/420/560', '트렌디 컷아웃 디테일 미디 원피스', 92000, 0, 16, 320),
('아뜨랑스', '레이어드 슬릿 원피스', '원피스', 'https://picsum.photos/seed/att_op14/420/560', '시크한 레이어드 슬릿 원피스', 95000, 0, 15, 310),
('아뜨랑스', '코듀로이 미디 원피스', '원피스', 'https://picsum.photos/seed/att_op15/420/560', '따뜻한 코듀로이 소재 미디 원피스', 85000, 0, 21, 300),
('아뜨랑스', 'A라인 포인트 원피스', '원피스', 'https://picsum.photos/seed/att_op16/420/560', '날씬한 A라인 실루엣 포인트 원피스', 72000, 0, 27, 290),

-- ── 상의 (16) ───────────────────────────────────────────────────
('아뜨랑스', '베이직 크롭 티셔츠', '상의', 'https://picsum.photos/seed/att_tp01/420/560', '매일 입기 좋은 베이직 크롭 티', 29000, 0, 55, 520),
('아뜨랑스', '레이스 트림 블라우스', '상의', 'https://picsum.photos/seed/att_tp02/420/560', '여성스러운 레이스 트림 블라우스', 52000, 0, 38, 490),
('아뜨랑스', '오버핏 린넨 셔츠', '상의', 'https://picsum.photos/seed/att_tp03/420/560', '시원한 여름 오버핏 린넨 셔츠', 58000, 0, 32, 460),
('아뜨랑스', '프릴 넥 블라우스', '상의', 'https://picsum.photos/seed/att_tp04/420/560', '사랑스러운 프릴 넥 블라우스', 48000, 0, 40, 440),
('아뜨랑스', '크롭 카디건', '상의', 'https://picsum.photos/seed/att_tp05/420/560', '코디하기 편한 크롭 카디건', 42000, 0, 45, 420),
('아뜨랑스', '퍼프 소매 블라우스', '상의', 'https://picsum.photos/seed/att_tp06/420/560', '트렌디한 퍼프 소매 블라우스', 55000, 0, 35, 400),
('아뜨랑스', '롤넥 리브 니트', '상의', 'https://picsum.photos/seed/att_tp07/420/560', '슬림핏 롤넥 리브 니트', 62000, 0, 28, 380),
('아뜨랑스', '시폰 랩 블라우스', '상의', 'https://picsum.photos/seed/att_tp08/420/560', '우아한 시폰 랩 블라우스', 49000, 0, 38, 360),
('아뜨랑스', '캐시미어 블렌드 스웨터', '상의', 'https://picsum.photos/seed/att_tp09/420/560', '고급 캐시미어 블렌드 스웨터', 89000, 0, 22, 340),
('아뜨랑스', '버튼다운 크롭 셔츠', '상의', 'https://picsum.photos/seed/att_tp10/420/560', '깔끔한 버튼다운 크롭 셔츠', 45000, 0, 42, 320),
('아뜨랑스', '쉬어 러플 블라우스', '상의', 'https://picsum.photos/seed/att_tp11/420/560', '로맨틱한 쉬어 러플 블라우스', 58000, 0, 30, 300),
('아뜨랑스', '기모 터틀넥 니트', '상의', 'https://picsum.photos/seed/att_tp12/420/560', '따뜻한 기모 터틀넥 니트', 65000, 0, 26, 290),
('아뜨랑스', '데님 오프숄더 탑', '상의', 'https://picsum.photos/seed/att_tp13/420/560', '캐주얼 데님 오프숄더 탑', 42000, 0, 36, 280),
('아뜨랑스', '프린팅 오버핏 맨투맨', '상의', 'https://picsum.photos/seed/att_tp14/420/560', '유니크한 프린팅 오버핏 맨투맨', 52000, 0, 40, 270),
('아뜨랑스', '레이스 백리본 블라우스', '상의', 'https://picsum.photos/seed/att_tp15/420/560', '백리본 포인트 레이스 블라우스', 56000, 0, 32, 260),
('아뜨랑스', '브이넥 니트 베스트', '상의', 'https://picsum.photos/seed/att_tp16/420/560', '활용도 높은 브이넥 니트 베스트', 48000, 0, 38, 250),

-- ── 하의 (16) ───────────────────────────────────────────────────
('아뜨랑스', '슬림 스트레이트 데님', '하의', 'https://picsum.photos/seed/att_bt01/420/560', '깔끔한 슬림 스트레이트 데님 팬츠', 65000, 0, 35, 500),
('아뜨랑스', '플리츠 미디 스커트', '하의', 'https://picsum.photos/seed/att_bt02/420/560', '우아한 플리츠 미디 스커트', 58000, 0, 30, 470),
('아뜨랑스', '와이드 크롭 슬랙스', '하의', 'https://picsum.photos/seed/att_bt03/420/560', '트렌디 와이드 크롭 슬랙스', 68000, 0, 28, 450),
('아뜨랑스', '체크 미니 스커트', '하의', 'https://picsum.photos/seed/att_bt04/420/560', '큐트한 체크 패턴 미니 스커트', 52000, 0, 36, 430),
('아뜨랑스', '레더 와이드 팬츠', '하의', 'https://picsum.photos/seed/att_bt05/420/560', '시크한 레더 와이드 팬츠', 85000, 0, 22, 410),
('아뜨랑스', '롱 플레어 스커트', '하의', 'https://picsum.photos/seed/att_bt06/420/560', '로맨틱 롱 플레어 스커트', 72000, 0, 26, 390),
('아뜨랑스', '하이웨이스트 크롭 팬츠', '하의', 'https://picsum.photos/seed/att_bt07/420/560', '하이웨이스트 크롭 팬츠', 62000, 0, 32, 370),
('아뜨랑스', '벨벳 미니 스커트', '하의', 'https://picsum.photos/seed/att_bt08/420/560', '고급스러운 벨벳 미니 스커트', 65000, 0, 28, 350),
('아뜨랑스', '도트 맥시 스커트', '하의', 'https://picsum.photos/seed/att_bt09/420/560', '사랑스러운 도트 패턴 맥시 스커트', 78000, 0, 20, 330),
('아뜨랑스', '시어 미디 스커트', '하의', 'https://picsum.photos/seed/att_bt10/420/560', '여성스러운 시어 소재 미디 스커트', 68000, 0, 24, 310),
('아뜨랑스', '와이드 데님 팬츠', '하의', 'https://picsum.photos/seed/att_bt11/420/560', '캐주얼 와이드 데님 팬츠', 72000, 0, 30, 300),
('아뜨랑스', '밴딩 배기 팬츠', '하의', 'https://picsum.photos/seed/att_bt12/420/560', '편안한 밴딩 배기 팬츠', 55000, 0, 38, 290),
('아뜨랑스', '레이어드 튤 스커트', '하의', 'https://picsum.photos/seed/att_bt13/420/560', '화려한 레이어드 튤 스커트', 88000, 0, 16, 280),
('아뜨랑스', '세미 와이드 슬랙스', '하의', 'https://picsum.photos/seed/att_bt14/420/560', '깔끔한 세미 와이드 슬랙스', 65000, 0, 26, 270),
('아뜨랑스', '새틴 슬립 스커트', '하의', 'https://picsum.photos/seed/att_bt15/420/560', '섹시한 새틴 슬립 스커트', 62000, 0, 24, 260),
('아뜨랑스', '카고 와이드 팬츠', '하의', 'https://picsum.photos/seed/att_bt16/420/560', '트렌디한 카고 와이드 팬츠', 75000, 0, 28, 250),

-- ── 아우터 (16) ─────────────────────────────────────────────────
('아뜨랑스', '캐시미어 롱 코트', '아우터', 'https://picsum.photos/seed/att_ot01/420/560', '고급 캐시미어 소재 롱 코트', 189000, 0, 12, 480),
('아뜨랑스', '오버핏 트위드 자켓', '아우터', 'https://picsum.photos/seed/att_ot02/420/560', '스타일리시 오버핏 트위드 자켓', 125000, 0, 18, 460),
('아뜨랑스', '체크 더블 코트', '아우터', 'https://picsum.photos/seed/att_ot03/420/560', '클래식 체크 더블 브레스트 코트', 155000, 0, 14, 440),
('아뜨랑스', '가죽 라이더 자켓', '아우터', 'https://picsum.photos/seed/att_ot04/420/560', '시크한 가죽 라이더 자켓', 135000, 0, 16, 420),
('아뜨랑스', '울 싱글 코트', '아우터', 'https://picsum.photos/seed/att_ot05/420/560', '깔끔한 울 싱글 버튼 코트', 145000, 0, 13, 400),
('아뜨랑스', '데님 블레이저', '아우터', 'https://picsum.photos/seed/att_ot06/420/560', '캐주얼 데님 블레이저', 89000, 0, 22, 380),
('아뜨랑스', '베이직 트렌치 코트', '아우터', 'https://picsum.photos/seed/att_ot07/420/560', '베이직 트렌치 코트', 125000, 0, 15, 360),
('아뜨랑스', '오버핏 봄버 자켓', '아우터', 'https://picsum.photos/seed/att_ot08/420/560', '스트릿 오버핏 봄버 자켓', 95000, 0, 20, 340),
('아뜨랑스', '테일러드 자켓', '아우터', 'https://picsum.photos/seed/att_ot09/420/560', '모던 테일러드 자켓', 105000, 0, 18, 320),
('아뜨랑스', '헤링본 하프 코트', '아우터', 'https://picsum.photos/seed/att_ot10/420/560', '클래식 헤링본 하프 코트', 135000, 0, 14, 310),
('아뜨랑스', '벨티드 울 코트', '아우터', 'https://picsum.photos/seed/att_ot11/420/560', '허리 벨트 포인트 울 코트', 165000, 0, 11, 300),
('아뜨랑스', '로브 카디건', '아우터', 'https://picsum.photos/seed/att_ot12/420/560', '편안한 오버사이즈 로브 카디건', 85000, 0, 24, 290),
('아뜨랑스', '패딩 베스트', '아우터', 'https://picsum.photos/seed/att_ot13/420/560', '가볍고 따뜻한 패딩 베스트', 72000, 0, 28, 280),
('아뜨랑스', '크롭 자켓', '아우터', 'https://picsum.photos/seed/att_ot14/420/560', '트렌디한 크롭 길이 자켓', 98000, 0, 20, 270),
('아뜨랑스', '더블 페이스 코트', '아우터', 'https://picsum.photos/seed/att_ot15/420/560', '고급 더블 페이스 코트', 175000, 0, 10, 260),
('아뜨랑스', '무스탕 쇼트 자켓', '아우터', 'https://picsum.photos/seed/att_ot16/420/560', '아늑한 무스탕 쇼트 자켓', 115000, 0, 16, 250),

-- ── 악세잡화 (16) ───────────────────────────────────────────────
('아뜨랑스', '진주 체인 이어링', '악세잡화', 'https://picsum.photos/seed/att_ac01/420/560', '클래식 진주 체인 이어링', 22000, 0, 68, 530),
('아뜨랑스', '미니 버킷백', '악세잡화', 'https://picsum.photos/seed/att_ac02/420/560', '데일리 미니 버킷백', 52000, 0, 45, 500),
('아뜨랑스', '체인 크로스백', '악세잡화', 'https://picsum.photos/seed/att_ac03/420/560', '골드 체인 미니 크로스백', 65000, 0, 38, 470),
('아뜨랑스', '실키 스카프', '악세잡화', 'https://picsum.photos/seed/att_ac04/420/560', '부드러운 실키 스카프', 32000, 0, 55, 450),
('아뜨랑스', '레이어드 팔찌 세트', '악세잡화', 'https://picsum.photos/seed/att_ac05/420/560', '골드 레이어드 팔찌 3종 세트', 28000, 0, 60, 430),
('아뜨랑스', '미니 클러치백', '악세잡화', 'https://picsum.photos/seed/att_ac06/420/560', '파티룩에 딱인 미니 클러치백', 48000, 0, 42, 410),
('아뜨랑스', '케이블 니트 비니', '악세잡화', 'https://picsum.photos/seed/att_ac07/420/560', '따뜻한 케이블 니트 비니', 19000, 0, 72, 390),
('아뜨랑스', '퀄팅 숄더백', '악세잡화', 'https://picsum.photos/seed/att_ac08/420/560', '고급스러운 퀄팅 숄더백', 78000, 0, 32, 370),
('아뜨랑스', '스퀘어 선글라스', '악세잡화', 'https://picsum.photos/seed/att_ac09/420/560', '트렌디 스퀘어 프레임 선글라스', 38000, 0, 48, 350),
('아뜨랑스', '골드 뱅글 세트', '악세잡화', 'https://picsum.photos/seed/att_ac10/420/560', '깔끔한 골드 뱅글 2종 세트', 32000, 0, 55, 330),
('아뜨랑스', '미러 드롭 이어링', '악세잡화', 'https://picsum.photos/seed/att_ac11/420/560', '빛나는 미러 드롭 이어링', 25000, 0, 62, 310),
('아뜨랑스', '실크 헤어밴드', '악세잡화', 'https://picsum.photos/seed/att_ac12/420/560', '고급 실크 소재 헤어밴드', 24000, 0, 58, 300),
('아뜨랑스', '체인 미니 숄더백', '악세잡화', 'https://picsum.photos/seed/att_ac13/420/560', '미니 사이즈 체인 숄더백', 68000, 0, 35, 290),
('아뜨랑스', '골드 레이어드 목걸이', '악세잡화', 'https://picsum.photos/seed/att_ac14/420/560', '우아한 골드 레이어드 목걸이', 29000, 0, 58, 280),
('아뜨랑스', '새틴 리본 헤어핀', '악세잡화', 'https://picsum.photos/seed/att_ac15/420/560', '사랑스러운 새틴 리본 헤어핀 세트', 15000, 0, 80, 270),
('아뜨랑스', '패딩 토트백', '악세잡화', 'https://picsum.photos/seed/att_ac16/420/560', '가볍고 실용적인 패딩 토트백', 58000, 0, 38, 260),

-- ── 오늘의 할인 (16) — discount_per > 0 ─────────────────────────
('아뜨랑스', '[원피스] 플로럴 플리츠 원피스 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl01/420/560', '오늘만 특가! 플로럴 플리츠 원피스', 79000, 30, 20, 950),
('아뜨랑스', '[상의] 레이스 트림 블라우스 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl02/420/560', '오늘만 특가! 레이스 트림 블라우스', 52000, 25, 24, 920),
('아뜨랑스', '[하의] 플리츠 미디 스커트 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl03/420/560', '오늘만 특가! 플리츠 미디 스커트', 58000, 20, 28, 890),
('아뜨랑스', '[아우터] 데님 블레이저 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl04/420/560', '오늘만 특가! 데님 블레이저', 89000, 35, 16, 870),
('아뜨랑스', '[악세잡화] 미니 버킷백 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl05/420/560', '오늘만 특가! 미니 버킷백', 52000, 20, 22, 840),
('아뜨랑스', '[원피스] 린넨 셔츠 원피스 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl06/420/560', '오늘만 특가! 린넨 셔츠 원피스', 79000, 25, 18, 820),
('아뜨랑스', '[상의] 오버핏 린넨 셔츠 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl07/420/560', '오늘만 특가! 오버핏 린넨 셔츠', 58000, 30, 26, 800),
('아뜨랑스', '[하의] 체크 미니 스커트 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl08/420/560', '오늘만 특가! 체크 미니 스커트', 52000, 20, 30, 780),
('아뜨랑스', '[아우터] 오버핏 봄버 자켓 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl09/420/560', '오늘만 특가! 봄버 자켓', 95000, 35, 14, 760),
('아뜨랑스', '[악세잡화] 체인 크로스백 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl10/420/560', '오늘만 특가! 체인 크로스백', 65000, 25, 20, 740),
('아뜨랑스', '[원피스] 니트 미니 원피스 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl11/420/560', '오늘만 특가! 니트 미니 원피스', 72000, 30, 18, 720),
('아뜨랑스', '[상의] 캐시미어 블렌드 스웨터 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl12/420/560', '오늘만 특가! 캐시미어 스웨터', 89000, 40, 14, 700),
('아뜨랑스', '[하의] 레더 와이드 팬츠 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl13/420/560', '오늘만 특가! 레더 와이드 팬츠', 85000, 25, 18, 680),
('아뜨랑스', '[아우터] 테일러드 자켓 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl14/420/560', '오늘만 특가! 테일러드 자켓', 105000, 35, 12, 660),
('아뜨랑스', '[악세잡화] 퀄팅 숄더백 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl15/420/560', '오늘만 특가! 퀄팅 숄더백', 78000, 20, 20, 640),
('아뜨랑스', '[원피스] 쉬폰 맥시 원피스 SALE', '오늘의 할인', 'https://picsum.photos/seed/att_sl16/420/560', '오늘만 특가! 쉬폰 맥시 원피스', 98000, 40, 14, 620);

-- ── 회원 더미 데이터 ──────────────────────────────────────────────
-- 비밀번호는 AdminInitializer가 실행 시 자동으로 BCrypt 인코딩합니다.
INSERT INTO members (name, login_id, login_pw, role, grade, status, phone, address) VALUES
('테스트회원1', 'user1@attrangs.com', 'test1234', 'ROLE_USER', 'BRONZE', 'ACTIVE', '010-1111-1111', '서울특별시 강남구 테헤란로 123'),
('테스트회원2', 'user2@attrangs.com', 'test1234', 'ROLE_USER', 'SILVER', 'ACTIVE', '010-2222-2222', '서울특별시 서초구 반포대로 456'),
('테스트회원3', 'user3@attrangs.com', 'test1234', 'ROLE_USER', 'GOLD',   'ACTIVE', '010-3333-3333', '경기도 성남시 분당구 판교로 789')
ON CONFLICT (login_id) DO NOTHING;
