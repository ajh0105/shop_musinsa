#!/bin/bash
set -e

echo "=== Docker DB 초기화 시작 ==="

# shop_attrangs DB 생성 (shop은 POSTGRES_DB 환경변수로 자동 생성됨)
psql -v ON_ERROR_STOP=1 -U "$POSTGRES_USER" -d "$POSTGRES_DB" <<-EOSQL
    CREATE DATABASE shop_attrangs;
EOSQL

echo "shop_attrangs DB 생성 완료"

# 무신사 DDL 실행 (첫 번째 패스: items 테이블 + 상품 데이터 삽입)
# Spring Boot가 아직 시작 전이므로 members 등 일부 테이블 관련 오류는 정상
echo "무신사 DDL 실행 중..."
psql -U "$POSTGRES_USER" -d shop    -f /musinsa_sql/musinsa_ddl1.sql    2>&1 | grep -v "^ERROR.*does not exist" || true
psql -U "$POSTGRES_USER" -d shop    -f /musinsa_sql/musinsa_ddl2.sql    2>&1 | grep -v "^ERROR.*does not exist" || true

# 아뜨랑스 DDL 실행
echo "아뜨랑스 DDL 실행 중..."
psql -U "$POSTGRES_USER" -d shop_attrangs -f /attrangs_sql/attrangs_ddl1.sql 2>&1 | grep -v "^ERROR.*does not exist" || true
psql -U "$POSTGRES_USER" -d shop_attrangs -f /attrangs_sql/attrangs_ddl2.sql 2>&1 | grep -v "^ERROR.*does not exist" || true

echo "=== Docker DB 초기화 완료 (1차 패스) ==="
