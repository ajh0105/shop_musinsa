select * from board;

CREATE TABLE items (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,          -- 브랜드명
    name VARCHAR(100) NOT NULL,         -- 상품명
    category VARCHAR(30),               -- 카테고리 (상의, 하의, 신발 등)
    img_path VARCHAR(255) NOT NULL,     -- 이미지 경로
    price INTEGER NOT NULL,             -- 정가
    discount_per INTEGER DEFAULT 0,     -- 할인율 (%)
    stock_count INTEGER DEFAULT 0,      -- 재고 수량
    view_count INTEGER DEFAULT 0,       -- 조회수 (인기순 정렬용)
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 주석 설정
COMMENT ON TABLE items IS '무신사 판매 상품 정보';
COMMENT ON COLUMN items.brand IS '브랜드명';
COMMENT ON COLUMN items.discount_per IS '할인율(0~100)';

INSERT INTO items (brand, name, category, img_path, price, discount_per, stock_count, view_count)
VALUES 
    ('무신사 스탠다드', '릴렉스 핏 크루 넥 반팔 티셔츠', '상의', '/img/001.jpg', 19900, 10, 500, 12500),
    ('아디다스', '삼바 OG - 화이트:블랙', '신발', '/img/002.jpg', 139000, 0, 50, 45000),
    ('예일', 'UNIVERSITY DAN T-SHIRT WHITE', '상의', '/img/003.jpg', 39000, 30, 200, 8900),
    ('노스페이스', '눕시 하이브리드 다운 자켓', '아우터', '/img/004.jpg', 359000, 5, 30, 15000),
    ('리바이즈', '501 오리지널 셀비지 데님', '하의', '/img/005.jpg', 129000, 15, 120, 7600),
    ('나이키', '에어 포스 1 07 화이트', '신발', '/img/006.jpg', 139000, 0, 100, 62000);



-- 테이블 생성
CREATE TABLE members (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    login_id VARCHAR(50) NOT NULL,
    login_pw VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT members_uk UNIQUE (login_id)
);

-- 테이블 및 컬럼 주석 추가
COMMENT ON TABLE members IS '회원';
COMMENT ON COLUMN members.id IS '아이디';
COMMENT ON COLUMN members.name IS '회원명';
COMMENT ON COLUMN members.login_id IS '로그인 아이디';
COMMENT ON COLUMN members.login_pw IS '로그인 패스워드';
COMMENT ON COLUMN members.created_at IS '생성 일시';

INSERT INTO members (name, login_id, login_pw)
VALUES ('알파', 'alpha@example.com', 'password111');

SELECT * FROM members WHERE login_id = 'alpha@example.com';

-- 테이블 생성
CREATE TABLE carts (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- 관계 설정을 위한 외래 키 추가 (권장)
    CONSTRAINT fk_cart_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

-- 테이블 및 컬럼 주석 추가
COMMENT ON TABLE carts IS '장바구니';
COMMENT ON COLUMN carts.id IS '아이디';
COMMENT ON COLUMN carts.member_id IS '회원 아이디';
COMMENT ON COLUMN carts.item_id IS '상품 아이디';
COMMENT ON COLUMN carts.created_at IS '생성 일시';


-- 테이블 생성
CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(500) NOT NULL,
    payment VARCHAR(10) NOT NULL,
    card_number VARCHAR(16),
    amount BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- 회원 테이블과의 관계 설정 (선택 사항)
    CONSTRAINT fk_order_member FOREIGN KEY (member_id) REFERENCES members(id)
);

-- 테이블 및 컬럼 주석 추가
COMMENT ON TABLE orders IS '주문';
COMMENT ON COLUMN orders.id IS '아이디';
COMMENT ON COLUMN orders.member_id IS '주문자 회원 아이디';
COMMENT ON COLUMN orders.name IS '주문자명';
COMMENT ON COLUMN orders.address IS '배송 주소';
COMMENT ON COLUMN orders.payment IS '결제 수단';
COMMENT ON COLUMN orders.card_number IS '카드 번호';
COMMENT ON COLUMN orders.amount IS '최종 결제 금액';
COMMENT ON COLUMN orders.created_at IS '생성 일시';


-- 테이블 생성
CREATE TABLE order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    
    -- 외래 키 설정: 주문이나 상품이 삭제될 때의 동작 정의
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_item FOREIGN KEY (item_id) REFERENCES items(id)
);

-- 테이블 및 컬럼 주석 추가
COMMENT ON TABLE order_items IS '주문 상품';
COMMENT ON COLUMN order_items.id IS '아이디';
COMMENT ON COLUMN order_items.order_id IS '주문 아이디';
COMMENT ON COLUMN order_items.item_id IS '주문 상품 아이디';
COMMENT ON COLUMN order_items.created_at IS '생성 일시';
