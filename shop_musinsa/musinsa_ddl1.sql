-- ==========================================
-- MUSINSA SHOPPING MALL DDL
-- ==========================================

-- 상품 테이블
CREATE TABLE IF NOT EXISTS items (
    id SERIAL PRIMARY KEY,
    brand VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(30),
    img_path VARCHAR(255) NOT NULL,
    price INTEGER NOT NULL,
    discount_per INTEGER DEFAULT 0,
    stock_count INTEGER DEFAULT 0,
    view_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE items IS '무신사 판매 상품 정보';
COMMENT ON COLUMN items.brand IS '브랜드명';
COMMENT ON COLUMN items.discount_per IS '할인율(0~100)';

-- 기존 데이터 삭제 후 재삽입
TRUNCATE TABLE items RESTART IDENTITY CASCADE;

INSERT INTO items (brand, name, category, img_path, price, discount_per, stock_count, view_count)
VALUES

-- ========================
-- OUTER (아우터) - 12개
-- ========================
('노스페이스', '눕시 하이브리드 다운 자켓 블랙', 'OUTER', 'https://picsum.photos/seed/outer01/300/400', 359000, 10, 50, 25000),
('K2', '알파인 고어텍스 등산 자켓 네이비', 'OUTER', 'https://picsum.photos/seed/outer02/300/400', 289000, 15, 40, 18000),
('네파', '플리스 집업 자켓 그린', 'OUTER', 'https://picsum.photos/seed/outer03/300/400', 159000, 20, 80, 14000),
('디스커버리', '윈드브레이커 베이직 자켓 카키', 'OUTER', 'https://picsum.photos/seed/outer04/300/400', 179000, 5, 60, 21000),
('무신사 스탠다드', '오버핏 코튼 코트 아이보리', 'OUTER', 'https://picsum.photos/seed/outer05/300/400', 129000, 30, 100, 32000),
('아디다스', '아디다스 클래식 3-스트라입 자켓', 'OUTER', 'https://picsum.photos/seed/outer06/300/400', 129000, 10, 70, 17000),
('나이키', '나이키 테크 플리스 자켓 다크그레이', 'OUTER', 'https://picsum.photos/seed/outer07/300/400', 169000, 0, 45, 28000),
('MLB', 'MLB 빅볼 척 롱 패딩 크림', 'OUTER', 'https://picsum.photos/seed/outer08/300/400', 249000, 20, 35, 19000),
('마운틴하드웨어', '스트레치 다운 후드 자켓 블루', 'OUTER', 'https://picsum.photos/seed/outer09/300/400', 399000, 5, 25, 12000),
('뉴발란스', 'NB 에센셜 패딩 자켓 블랙', 'OUTER', 'https://picsum.photos/seed/outer10/300/400', 199000, 15, 55, 16000),
('커버낫', '울 오버 코트 베이지', 'OUTER', 'https://picsum.photos/seed/outer11/300/400', 219000, 10, 30, 22000),
('오아이오아이', '쉐르파 플리스 자켓 브라운', 'OUTER', 'https://picsum.photos/seed/outer12/300/400', 149000, 25, 65, 13000),

-- ========================
-- TOP (상의) - 12개
-- ========================
('무신사 스탠다드', '릴렉스 핏 크루 넥 반팔 티셔츠 화이트', 'TOP', 'https://picsum.photos/seed/top01/300/400', 19900, 10, 500, 42000),
('커버낫', '스몰 로고 반팔 티셔츠 블랙', 'TOP', 'https://picsum.photos/seed/top02/300/400', 35000, 0, 200, 31000),
('챔피온', '리버스 위브 후드 스웨트셔츠 그레이', 'TOP', 'https://picsum.photos/seed/top03/300/400', 89000, 20, 150, 24000),
('폴로 랄프로렌', '클래식 핏 폴로 셔츠 네이비', 'TOP', 'https://picsum.photos/seed/top04/300/400', 129000, 0, 80, 35000),
('예일', '아치 로고 크루넥 스웨트셔츠 블루', 'TOP', 'https://picsum.photos/seed/top05/300/400', 59000, 30, 180, 27000),
('라코스테', '레귤러 핏 피케 폴로 그린', 'TOP', 'https://picsum.photos/seed/top06/300/400', 109000, 5, 90, 19000),
('아더에러', '오버사이즈 그래픽 후드 블랙', 'TOP', 'https://picsum.photos/seed/top07/300/400', 179000, 0, 40, 38000),
('에스피오나지', '드롭숄더 롱슬리브 티셔츠 화이트', 'TOP', 'https://picsum.photos/seed/top08/300/400', 49000, 15, 120, 21000),
('나이키', '나이키 스우시 반팔 티셔츠 블랙', 'TOP', 'https://picsum.photos/seed/top09/300/400', 45000, 10, 300, 44000),
('아디다스', '에센셜 3-스트라입 후드 그레이', 'TOP', 'https://picsum.photos/seed/top10/300/400', 75000, 15, 200, 29000),
('무신사 스탠다드', '헤비 코튼 루즈핏 긴팔 티셔츠 베이지', 'TOP', 'https://picsum.photos/seed/top11/300/400', 29000, 0, 400, 36000),
('칸코', '빈티지 워싱 스트라이프 셔츠 블루', 'TOP', 'https://picsum.photos/seed/top12/300/400', 65000, 20, 100, 17000),

-- ========================
-- PANTS (하의) - 12개
-- ========================
('리바이즈', '501 오리지널 셀비지 데님 인디고', 'PANTS', 'https://picsum.photos/seed/pants01/300/400', 129000, 15, 120, 38000),
('무신사 스탠다드', '워크 카고 팬츠 베이지', 'PANTS', 'https://picsum.photos/seed/pants02/300/400', 59000, 20, 200, 26000),
('칼하트', 'WIP 더블 니 팬츠 블랙', 'PANTS', 'https://picsum.photos/seed/pants03/300/400', 149000, 0, 80, 19000),
('게스', '빈티지 슬림 스트레이트 데님 라이트블루', 'PANTS', 'https://picsum.photos/seed/pants04/300/400', 119000, 10, 90, 17000),
('커버낫', '코튼 트위드 팬츠 브라운', 'PANTS', 'https://picsum.photos/seed/pants05/300/400', 89000, 0, 100, 22000),
('아더에러', '와이드 코듀로이 팬츠 그린', 'PANTS', 'https://picsum.photos/seed/pants06/300/400', 159000, 10, 50, 31000),
('폴로 랄프로렌', '슬림 핏 치노 팬츠 카키', 'PANTS', 'https://picsum.photos/seed/pants07/300/400', 139000, 0, 70, 24000),
('나이키', '나이키 테크 플리스 조거 팬츠 그레이', 'PANTS', 'https://picsum.photos/seed/pants08/300/400', 99000, 0, 150, 41000),
('아디다스', '아디다스 트랙 팬츠 블랙', 'PANTS', 'https://picsum.photos/seed/pants09/300/400', 79000, 20, 130, 28000),
('브레인데드', '멀티 포켓 카고 팬츠 올리브', 'PANTS', 'https://picsum.photos/seed/pants10/300/400', 199000, 0, 30, 16000),
('무신사 스탠다드', '와이드 핏 린넨 팬츠 아이보리', 'PANTS', 'https://picsum.photos/seed/pants11/300/400', 49000, 25, 180, 34000),
('룩아스', '스트레치 슬랙스 차콜', 'PANTS', 'https://picsum.photos/seed/pants12/300/400', 69000, 0, 110, 21000),

-- ========================
-- SHOES (신발) - 12개
-- ========================
('나이키', '에어 포스 1 07 화이트', 'SHOES', 'https://picsum.photos/seed/shoes01/300/400', 139000, 0, 100, 62000),
('아디다스', '삼바 OG 화이트/블랙', 'SHOES', 'https://picsum.photos/seed/shoes02/300/400', 139000, 0, 50, 58000),
('뉴발란스', '990v6 그레이', 'SHOES', 'https://picsum.photos/seed/shoes03/300/400', 259000, 0, 30, 45000),
('반스', '올드스쿨 블랙/화이트', 'SHOES', 'https://picsum.photos/seed/shoes04/300/400', 99000, 10, 80, 32000),
('컨버스', '척 테일러 올스타 하이 블랙', 'SHOES', 'https://picsum.photos/seed/shoes05/300/400', 89000, 0, 90, 28000),
('아식스', '젤-카야노 14 크림/실버', 'SHOES', 'https://picsum.photos/seed/shoes06/300/400', 169000, 5, 40, 39000),
('살로몬', 'XT-6 GTX 블랙/올리브', 'SHOES', 'https://picsum.photos/seed/shoes07/300/400', 229000, 0, 25, 27000),
('호카', '클리프톤 9 화이트/블루', 'SHOES', 'https://picsum.photos/seed/shoes08/300/400', 189000, 10, 35, 33000),
('온러닝', '클라우드 5 올화이트', 'SHOES', 'https://picsum.photos/seed/shoes09/300/400', 209000, 0, 20, 24000),
('리복', '클럽 C 85 화이트/그린', 'SHOES', 'https://picsum.photos/seed/shoes10/300/400', 119000, 15, 60, 21000),
('나이키', '덩크 로우 레트로 블랙', 'SHOES', 'https://picsum.photos/seed/shoes11/300/400', 139000, 0, 45, 51000),
('팀버랜드', '6인치 프리미엄 부츠 웨이트', 'SHOES', 'https://picsum.photos/seed/shoes12/300/400', 269000, 10, 55, 19000),

-- ========================
-- BAG (가방) - 12개
-- ========================
('무신사 스탠다드', '미니 크로스백 블랙', 'BAG', 'https://picsum.photos/seed/bag01/300/400', 39000, 10, 150, 18000),
('나이키', 'NK 헤리티지 백팩 올리브', 'BAG', 'https://picsum.photos/seed/bag02/300/400', 69000, 0, 80, 22000),
('MLB', 'MLB 모노그램 토트백 베이지', 'BAG', 'https://picsum.photos/seed/bag03/300/400', 89000, 15, 60, 29000),
('아디다스', '에센셜 린 백팩 블랙', 'BAG', 'https://picsum.photos/seed/bag04/300/400', 79000, 20, 70, 16000),
('살로몬', 'XT 10L 런닝 베스트팩 블랙', 'BAG', 'https://picsum.photos/seed/bag05/300/400', 159000, 0, 25, 14000),
('커버낫', '레더 미니 쇼퍼백 화이트', 'BAG', 'https://picsum.photos/seed/bag06/300/400', 69000, 10, 40, 19000),
('피스마이너스원', 'PMO 미니 메신저백 블랙', 'BAG', 'https://picsum.photos/seed/bag07/300/400', 189000, 0, 20, 35000),
('디스커버리', '멀티 포켓 힙색 블랙', 'BAG', 'https://picsum.photos/seed/bag08/300/400', 49000, 0, 100, 21000),
('칼하트', 'WIP 델트로이트 캐리어 브라운', 'BAG', 'https://picsum.photos/seed/bag09/300/400', 129000, 0, 35, 17000),
('뉴발란스', 'NB 에센셜 힙팩 그레이', 'BAG', 'https://picsum.photos/seed/bag10/300/400', 49000, 10, 90, 23000),
('아더에러', '오버사이즈 쇼퍼백 그린', 'BAG', 'https://picsum.photos/seed/bag11/300/400', 159000, 0, 15, 26000),
('무신사 스탠다드', '캔버스 토트백 내추럴', 'BAG', 'https://picsum.photos/seed/bag12/300/400', 29000, 0, 200, 31000),

-- ========================
-- ACC (악세사리) - 12개
-- ========================
('MLB', 'MLB 뉴욕 양키스 볼캡 블랙', 'ACC', 'https://picsum.photos/seed/acc01/300/400', 49000, 0, 200, 45000),
('뉴에라', '59FIFTY 로스앤젤레스 다저스 블루', 'ACC', 'https://picsum.photos/seed/acc02/300/400', 55000, 0, 150, 38000),
('커버낫', '리사이클 버킷햇 베이지', 'ACC', 'https://picsum.photos/seed/acc03/300/400', 35000, 15, 120, 24000),
('나이키', '나이키 스우시 비니 블랙', 'ACC', 'https://picsum.photos/seed/acc04/300/400', 35000, 0, 180, 19000),
('무신사 스탠다드', '울 크루 삭스 3팩 화이트', 'ACC', 'https://picsum.photos/seed/acc05/300/400', 19900, 0, 500, 52000),
('폴로 랄프로렌', '클래식 레더 벨트 브라운', 'ACC', 'https://picsum.photos/seed/acc06/300/400', 89000, 0, 60, 17000),
('아디다스', '아디다스 크로스바디 파우치 블랙', 'ACC', 'https://picsum.photos/seed/acc07/300/400', 45000, 10, 90, 21000),
('칼하트', 'WIP 와치 햇 그레이', 'ACC', 'https://picsum.photos/seed/acc08/300/400', 59000, 0, 70, 28000),
('반스', '반스 옐드 선글라스 블랙', 'ACC', 'https://picsum.photos/seed/acc09/300/400', 49000, 20, 80, 15000),
('커버낫', '리사이클 캔버스 키링 그린', 'ACC', 'https://picsum.photos/seed/acc10/300/400', 19900, 0, 300, 33000),
('MLB', 'MLB 모노그램 스카프 베이지', 'ACC', 'https://picsum.photos/seed/acc11/300/400', 59000, 10, 100, 22000),
('무신사 스탠다드', '헤비 코튼 양말 5팩 블랙', 'ACC', 'https://picsum.photos/seed/acc12/300/400', 24900, 0, 400, 41000),

-- ========================
-- OUTLET (할인 품목) - 12개
-- ========================
('노스페이스', '에코 플리스 자켓 블루 - 시즌 오프', 'OUTLET', 'https://picsum.photos/seed/outlet01/300/400', 199000, 40, 30, 21000),
('나이키', '나이키 에어맥스 90 화이트 - 재고 세일', 'OUTLET', 'https://picsum.photos/seed/outlet02/300/400', 179000, 35, 20, 18000),
('아디다스', '울트라부스트 22 블랙 - 이월 상품', 'OUTLET', 'https://picsum.photos/seed/outlet03/300/400', 229000, 45, 15, 16000),
('무신사 스탠다드', '코듀로이 셔츠 자켓 카멜 - 시즌오프', 'OUTLET', 'https://picsum.photos/seed/outlet04/300/400', 79000, 50, 40, 29000),
('리바이즈', '550 배기 데님 스톤워시 - 이월', 'OUTLET', 'https://picsum.photos/seed/outlet05/300/400', 149000, 30, 25, 14000),
('MLB', 'MLB 레더 백팩 블랙 - 재고 세일', 'OUTLET', 'https://picsum.photos/seed/outlet06/300/400', 129000, 40, 18, 12000),
('커버낫', '스프링 블루종 민트 - 시즌 오프', 'OUTLET', 'https://picsum.photos/seed/outlet07/300/400', 119000, 35, 22, 11000),
('뉴발란스', '327 테리 화이트/그레이 - 이월', 'OUTLET', 'https://picsum.photos/seed/outlet08/300/400', 139000, 30, 35, 19000),
('챔피온', '리버스 위브 후드 블랙 - 재고 세일', 'OUTLET', 'https://picsum.photos/seed/outlet09/300/400', 89000, 40, 45, 24000),
('K2', '트레킹 팬츠 그레이 - 이월 상품', 'OUTLET', 'https://picsum.photos/seed/outlet10/300/400', 99000, 45, 30, 17000),
('폴로 랄프로렌', '클래식 체크 플란넬 셔츠 레드 - 이월', 'OUTLET', 'https://picsum.photos/seed/outlet11/300/400', 119000, 30, 28, 13000),
('아더에러', '스트링 파우치 백 블랙 - 시즌오프', 'OUTLET', 'https://picsum.photos/seed/outlet12/300/400', 99000, 35, 12, 31000);


-- 회원 테이블
CREATE TABLE IF NOT EXISTS members (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    login_id VARCHAR(50) NOT NULL,
    login_pw VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT members_uk UNIQUE (login_id)
);

COMMENT ON TABLE members IS '회원';
COMMENT ON COLUMN members.id IS '아이디';
COMMENT ON COLUMN members.name IS '회원명';
COMMENT ON COLUMN members.login_id IS '로그인 아이디';
COMMENT ON COLUMN members.login_pw IS '로그인 패스워드';
COMMENT ON COLUMN members.created_at IS '생성 일시';

INSERT INTO members (name, login_id, login_pw)
VALUES ('알파', 'alpha@example.com', 'password111')
ON CONFLICT (login_id) DO NOTHING;


-- 장바구니 테이블
CREATE TABLE IF NOT EXISTS carts (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_cart_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE
);

COMMENT ON TABLE carts IS '장바구니';
COMMENT ON COLUMN carts.id IS '아이디';
COMMENT ON COLUMN carts.member_id IS '회원 아이디';
COMMENT ON COLUMN carts.item_id IS '상품 아이디';
COMMENT ON COLUMN carts.created_at IS '생성 일시';


-- 주문 테이블
CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    member_id INTEGER NOT NULL,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(500) NOT NULL,
    payment VARCHAR(10) NOT NULL,
    card_number VARCHAR(16),
    amount BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_member FOREIGN KEY (member_id) REFERENCES members(id)
);

COMMENT ON TABLE orders IS '주문';
COMMENT ON COLUMN orders.id IS '아이디';
COMMENT ON COLUMN orders.member_id IS '주문자 회원 아이디';
COMMENT ON COLUMN orders.name IS '주문자명';
COMMENT ON COLUMN orders.address IS '배송 주소';
COMMENT ON COLUMN orders.payment IS '결제 수단';
COMMENT ON COLUMN orders.card_number IS '카드 번호';
COMMENT ON COLUMN orders.amount IS '최종 결제 금액';
COMMENT ON COLUMN orders.created_at IS '생성 일시';


-- 주문 상품 테이블
CREATE TABLE IF NOT EXISTS order_items (
    id SERIAL PRIMARY KEY,
    order_id INTEGER NOT NULL,
    item_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_item FOREIGN KEY (item_id) REFERENCES items(id)
);

COMMENT ON TABLE order_items IS '주문 상품';
COMMENT ON COLUMN order_items.id IS '아이디';
COMMENT ON COLUMN order_items.order_id IS '주문 아이디';
COMMENT ON COLUMN order_items.item_id IS '주문 상품 아이디';
COMMENT ON COLUMN order_items.created_at IS '생성 일시';
