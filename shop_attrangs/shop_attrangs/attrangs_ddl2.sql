-- ==========================================================
-- ATTRANGS 쇼핑몰 DDL 2 (UTF-8)
-- attrangs_ddl1.sql 실행 후 사용
-- ==========================================================

ALTER TABLE members ADD COLUMN IF NOT EXISTS role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER';
ALTER TABLE members ADD COLUMN IF NOT EXISTS grade VARCHAR(20) NOT NULL DEFAULT 'BRONZE';
ALTER TABLE members ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE';
ALTER TABLE members ADD COLUMN IF NOT EXISTS phone VARCHAR(20);
ALTER TABLE members ADD COLUMN IF NOT EXISTS address VARCHAR(200);
ALTER TABLE items ADD COLUMN IF NOT EXISTS description TEXT;
ALTER TABLE orders ADD COLUMN IF NOT EXISTS status VARCHAR(20) NOT NULL DEFAULT 'PAID';

INSERT INTO reviews (member_id, item_id, rating, content)
SELECT m.id, i.id, 5, '핏이 정말 예쁘고 재질도 좋아요. 재구매 의사 있습니다.'
FROM members m
JOIN items i ON i.category IN ('Best', '상의', '하의')
WHERE m.login_id = 'user1@attrangs.com'
LIMIT 3;

INSERT INTO questions (member_id, item_id, title, content, is_secret, is_answered, answer_content, answered_at)
SELECT m.id,
       i.id,
       '사이즈 문의드립니다',
       '정사이즈로 주문하면 될까요?',
       FALSE,
       TRUE,
       '네, 정사이즈를 추천드립니다.',
       NOW()
FROM members m
JOIN items i ON i.category = '상의'
WHERE m.login_id = 'user2@attrangs.com'
LIMIT 2;
