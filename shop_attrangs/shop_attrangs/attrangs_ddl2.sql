-- ==========================================================
-- ATTRANGS 쇼핑몰 DDL 2 (UTF-8)
-- 반드시 attrangs_ddl1.sql 실행 이후에 실행하세요.
-- 기존 스키마에 컬럼 추가 및 샘플 데이터 삽입
-- ==========================================================

-- ── 스키마 보완 (기존 DB 대상 idempotent ALTER) ────────────────────
ALTER TABLE members   ADD COLUMN IF NOT EXISTS role    VARCHAR(20)  NOT NULL DEFAULT 'ROLE_USER';
ALTER TABLE members   ADD COLUMN IF NOT EXISTS grade   VARCHAR(20)  NOT NULL DEFAULT 'BRONZE';
ALTER TABLE members   ADD COLUMN IF NOT EXISTS status  VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE';
ALTER TABLE members   ADD COLUMN IF NOT EXISTS phone   VARCHAR(20);
ALTER TABLE members   ADD COLUMN IF NOT EXISTS address VARCHAR(200);
ALTER TABLE items     ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE orders    ADD COLUMN IF NOT EXISTS status  VARCHAR(20)  NOT NULL DEFAULT 'PAYMENT_DONE';
ALTER TABLE questions ADD COLUMN IF NOT EXISTS qna_password VARCHAR(100);

-- wishlists 테이블이 없을 경우 생성
CREATE TABLE IF NOT EXISTS wishlists (
    id         SERIAL PRIMARY KEY,
    member_id  INTEGER   NOT NULL,
    item_id    INTEGER   NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT fk_wish_member FOREIGN KEY (member_id) REFERENCES members(id) ON DELETE CASCADE,
    CONSTRAINT fk_wish_item   FOREIGN KEY (item_id)   REFERENCES items(id)   ON DELETE CASCADE
);

-- ── 샘플 리뷰 데이터 ───────────────────────────────────────────────
INSERT INTO reviews (member_id, item_id, rating, content)
SELECT m.id, i.id, 5, '핏이 정말 예쁘고 재질도 좋아요. 재구매 의사 있습니다.'
FROM members m
JOIN items i ON i.category IN ('Best', '상의', '원피스')
WHERE m.login_id = 'user1@attrangs.com'
LIMIT 4;

INSERT INTO reviews (member_id, item_id, rating, content)
SELECT m.id, i.id, 4, '배송도 빠르고 품질이 만족스러워요. 추천합니다!'
FROM members m
JOIN items i ON i.category IN ('하의', '아우터')
WHERE m.login_id = 'user2@attrangs.com'
LIMIT 3;

INSERT INTO reviews (member_id, item_id, rating, content)
SELECT m.id, i.id, 5, '사진보다 실물이 훨씬 예뻐요. 선물로도 좋을 것 같아요.'
FROM members m
JOIN items i ON i.category = '악세잡화'
WHERE m.login_id = 'user3@attrangs.com'
LIMIT 2;

-- ── 샘플 Q&A 데이터 ────────────────────────────────────────────────
INSERT INTO questions (member_id, item_id, title, content, is_secret, is_answered, answer_content, answered_at)
SELECT m.id, i.id,
       '사이즈 문의드립니다',
       '평소 M 사이즈 입는데 정사이즈로 주문하면 될까요?',
       FALSE, TRUE,
       '안녕하세요 아뜨랑스입니다. 해당 상품은 정사이즈로 주문하시면 됩니다. 감사합니다.',
       NOW()
FROM members m
JOIN items i ON i.category = '상의'
WHERE m.login_id = 'user1@attrangs.com'
LIMIT 2;

INSERT INTO questions (member_id, item_id, title, content, is_secret, is_answered, answer_content, answered_at)
SELECT m.id, i.id,
       '색상 문의합니다',
       '아이보리 색상 재입고 예정이 있을까요?',
       FALSE, TRUE,
       '안녕하세요! 아이보리 색상은 다음 달 중 재입고 예정입니다. 알림 신청 부탁드립니다.',
       NOW()
FROM members m
JOIN items i ON i.category = '원피스'
WHERE m.login_id = 'user2@attrangs.com'
LIMIT 2;

INSERT INTO questions (member_id, item_id, title, content, is_secret, is_answered, answer_content, answered_at)
SELECT m.id, i.id,
       '배송 기간 문의',
       '주문 후 며칠 정도 소요되나요?',
       FALSE, FALSE, NULL, NULL
FROM members m
JOIN items i ON i.category = '아우터'
WHERE m.login_id = 'user3@attrangs.com'
LIMIT 1;
