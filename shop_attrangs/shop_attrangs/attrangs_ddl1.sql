-- ==========================================================
-- ATTRANGS 쇼핑몰 DDL 1 (UTF-8)
-- DB: shop_attrangs
-- ==========================================================

CREATE TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(30) NOT NULL,
    img_path VARCHAR(255) NOT NULL,
    description TEXT,
    price INTEGER NOT NULL,
    discount_per INTEGER NOT NULL DEFAULT 0,
    stock_count INTEGER NOT NULL DEFAULT 0,
    view_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS members (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    login_id VARCHAR(100) NOT NULL UNIQUE,
    login_pw VARCHAR(200) NOT NULL,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    grade VARCHAR(20) NOT NULL DEFAULT 'BRONZE',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    phone VARCHAR(20),
    address VARCHAR(200),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS carts (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_cart_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(500) NOT NULL,
    payment VARCHAR(10) NOT NULL,
    card_number VARCHAR(16),
    amount BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PAID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_member FOREIGN KEY (member_id) REFERENCES members(id)
);

CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_item FOREIGN KEY (item_id) REFERENCES items(id)
);

CREATE TABLE IF NOT EXISTS reviews (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    order_id INTEGER,
    rating INTEGER NOT NULL CHECK (rating BETWEEN 1 AND 5),
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_review_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_review_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS questions (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    item_id INTEGER,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    is_secret BOOLEAN NOT NULL DEFAULT FALSE,
    is_answered BOOLEAN NOT NULL DEFAULT FALSE,
    answer_content TEXT,
    answer_member_id INTEGER,
    answered_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_question_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_question_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE SET NULL
);

TRUNCATE TABLE order_items RESTART IDENTITY CASCADE;
TRUNCATE TABLE carts RESTART IDENTITY CASCADE;
TRUNCATE TABLE reviews RESTART IDENTITY CASCADE;
TRUNCATE TABLE questions RESTART IDENTITY CASCADE;
TRUNCATE TABLE orders RESTART IDENTITY CASCADE;
TRUNCATE TABLE members RESTART IDENTITY CASCADE;
TRUNCATE TABLE items RESTART IDENTITY CASCADE;

-- 카테고리: Best, 원피스, 상의, 하의, 아우터, 악세잡화, 오늘의 할인
INSERT INTO items (brand, name, category, img_path, description, price, discount_per, stock_count, view_count) VALUES
('아뜨랑스', '베스트 실루엣 원피스', 'Best', 'https://picsum.photos/seed/best01/400/500', '베스트 라인 대표 원피스', 89000, 10, 24, 820),
('아뜨랑스', '베스트 트위드 자켓', 'Best', 'https://picsum.photos/seed/best02/400/500', '인기 코디 베스트 아우터', 129000, 15, 18, 760),

('아뜨랑스', '로맨틱 플레어 원피스', '원피스', 'https://picsum.photos/seed/onepiece01/400/500', '봄 시즌 데일리 원피스', 79000, 5, 30, 510),
('아뜨랑스', '하객룩 레이스 원피스', '원피스', 'https://picsum.photos/seed/onepiece02/400/500', '하객룩 추천 레이스 원피스', 99000, 10, 21, 640),

('아뜨랑스', '리본 블라우스', '상의', 'https://picsum.photos/seed/top01/400/500', '여리핏 리본 블라우스', 49000, 0, 42, 530),
('아뜨랑스', '보트넥 니트', '상의', 'https://picsum.photos/seed/top02/400/500', '데일리 보트넥 니트', 39000, 5, 38, 610),

('아뜨랑스', '와이드 슬랙스', '하의', 'https://picsum.photos/seed/bottom01/400/500', '롱 레그라인 슬랙스', 59000, 10, 32, 470),
('아뜨랑스', '세미부츠컷 데님', '하의', 'https://picsum.photos/seed/bottom02/400/500', '활용도 높은 데님 팬츠', 55000, 0, 28, 430),

('아뜨랑스', '핸드메이드 코트', '아우터', 'https://picsum.photos/seed/outer01/400/500', '울 블렌딩 핸드메이드 코트', 189000, 15, 14, 390),
('아뜨랑스', '트위드 숏자켓', '아우터', 'https://picsum.photos/seed/outer02/400/500', '간절기 숏자켓', 119000, 10, 20, 450),

('아뜨랑스', '진주 이어링', '악세잡화', 'https://picsum.photos/seed/acc01/400/500', '페미닌 무드 진주 이어링', 19000, 0, 65, 540),
('아뜨랑스', '클래식 벨트', '악세잡화', 'https://picsum.photos/seed/acc02/400/500', '슬림핏 레더 벨트', 25000, 5, 58, 420),

('아뜨랑스', '오늘만 트렌치 특가', '오늘의 할인', 'https://picsum.photos/seed/today01/400/500', '오늘의 할인 한정 특가', 79000, 45, 12, 900),
('아뜨랑스', '오늘만 원피스 특가', '오늘의 할인', 'https://picsum.photos/seed/today02/400/500', '오늘의 할인 원피스', 49000, 50, 16, 860);

INSERT INTO members (name, login_id, login_pw, role, grade, status, phone, address) VALUES
('테스트회원1', 'user1@attrangs.com', 'test1234', 'ROLE_USER', 'BRONZE', 'ACTIVE', '010-1111-1111', '서울 강남구'),
('테스트회원2', 'user2@attrangs.com', 'test1234', 'ROLE_USER', 'SILVER', 'ACTIVE', '010-2222-2222', '서울 서초구'),
('테스트회원3', 'user3@attrangs.com', 'test1234', 'ROLE_USER', 'GOLD', 'ACTIVE', '010-3333-3333', '경기 성남시')
ON CONFLICT (login_id) DO NOTHING;
