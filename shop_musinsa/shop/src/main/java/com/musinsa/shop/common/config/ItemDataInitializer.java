package com.musinsa.shop.common.config;

import com.musinsa.shop.item.entity.Item;
import com.musinsa.shop.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 샘플 패션 상품 데이터를 자동으로 생성합니다.
 * DB에 상품이 없을 때만 실행됩니다.
 * 이미지 출처: Unsplash (https://unsplash.com) — 무료 상업 이용 가능
 */
@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class ItemDataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;

    @Override
    public void run(String... args) {
        if (itemRepository.count() > 0) return;

        List<Item> items = List.of(
            /* ============================================================
               OUTER
               ============================================================ */
            Item.builder()
                .brand("MUSINSA STANDARD")
                .name("오버핏 울 싱글 코트")
                .category("OUTER")
                .imgPath("https://images.unsplash.com/photo-1551028719-00167b16eac5?w=400&h=500&fit=crop&auto=format")
                .description("고급 울 소재의 클래식 싱글 코트. 오버핏 실루엣으로 세련된 스타일을 연출합니다.")
                .price(159000)
                .discountPer(20)
                .stockCount(30)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("COVERNAT")
                .name("레트로 항공 점퍼")
                .category("OUTER")
                .imgPath("https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=400&h=500&fit=crop&auto=format")
                .description("빈티지 감성의 항공 점퍼. 클래식한 디자인에 현대적 핏을 더했습니다.")
                .price(129000)
                .discountPer(15)
                .stockCount(25)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("THISISNEVERTHAT")
                .name("클래식 트렌치코트")
                .category("OUTER")
                .imgPath("https://images.unsplash.com/photo-1548126032-079a0fb0099d?w=400&h=500&fit=crop&auto=format")
                .description("시즌리스로 입기 좋은 트렌치코트. 고급스러운 원단과 세밀한 마감이 특징입니다.")
                .price(189000)
                .discountPer(0)
                .stockCount(20)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("POLO RALPH LAUREN")
                .name("캐주얼 후드 짚업 점퍼")
                .category("OUTER")
                .imgPath("https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=400&h=500&fit=crop&auto=format")
                .description("편안한 후드 짚업 점퍼. 일상부터 아웃도어까지 활용 가능한 베이직 아이템.")
                .price(98000)
                .discountPer(10)
                .stockCount(40)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("STONE ISLAND")
                .name("나일론 패딩 다운 재킷")
                .category("OUTER")
                .imgPath("https://images.unsplash.com/photo-1608063615781-e2ef8c73d114?w=400&h=500&fit=crop&auto=format")
                .description("경량 구스다운 패딩. 보온성과 활동성을 동시에 갖춘 겨울 필수 아이템.")
                .price(278000)
                .discountPer(5)
                .stockCount(15)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("MUSINSA STANDARD")
                .name("데님 트러커 재킷")
                .category("OUTER")
                .imgPath("https://images.unsplash.com/photo-1551537482-f2075a1d41f2?w=400&h=500&fit=crop&auto=format")
                .description("클래식 데님 재킷. 어떤 스타일에도 잘 어울리는 만능 아우터.")
                .price(79000)
                .discountPer(0)
                .stockCount(35)
                .viewCount(0)
                .build(),

            /* ============================================================
               TOP
               ============================================================ */
            Item.builder()
                .brand("MUSINSA STANDARD")
                .name("오버핏 베이직 티셔츠")
                .category("TOP")
                .imgPath("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=400&h=500&fit=crop&auto=format")
                .description("데일리로 입기 좋은 오버핏 반팔 티셔츠. 무게감 있는 230g 원단 사용.")
                .price(29000)
                .discountPer(0)
                .stockCount(100)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("GRAFLEX")
                .name("스트라이프 옥스포드 셔츠")
                .category("TOP")
                .imgPath("https://images.unsplash.com/photo-1503341455253-b2e723bb3dbb?w=400&h=500&fit=crop&auto=format")
                .description("깔끔한 스트라이프 패턴의 옥스포드 셔츠. 비즈니스 캐주얼에 적합.")
                .price(69000)
                .discountPer(20)
                .stockCount(45)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("ADIDAS")
                .name("크루넥 맨투맨 스웨트셔츠")
                .category("TOP")
                .imgPath("https://images.unsplash.com/photo-1620799139834-6b8f844fbe61?w=400&h=500&fit=crop&auto=format")
                .description("두꺼운 기모 안감으로 따뜻한 크루넥 맨투맨. 스트릿 감성의 그래픽 프린트.")
                .price(59000)
                .discountPer(15)
                .stockCount(60)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("POLO RALPH LAUREN")
                .name("피케 폴로 반팔 티셔츠")
                .category("TOP")
                .imgPath("https://images.unsplash.com/photo-1559163499-413811fb2344?w=400&h=500&fit=crop&auto=format")
                .description("프리미엄 피케 소재의 폴로 셔츠. 클래식 로고 자수 포인트.")
                .price(89000)
                .discountPer(0)
                .stockCount(30)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("MUSINSA STANDARD")
                .name("린넨 반팔 셔츠")
                .category("TOP")
                .imgPath("https://images.unsplash.com/photo-1516478177764-9fe5bd7e9717?w=400&h=500&fit=crop&auto=format")
                .description("시원한 린넨 소재 반팔 셔츠. 여름 필수 아이템으로 다양하게 활용 가능.")
                .price(49000)
                .discountPer(10)
                .stockCount(70)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("CHAMPION")
                .name("로고 후드 티셔츠")
                .category("TOP")
                .imgPath("https://images.unsplash.com/photo-1556821840-3a63f15732ce?w=400&h=500&fit=crop&auto=format")
                .description("챔피언 클래식 로고 후드 티셔츠. 베이직하면서도 브랜드 정체성이 느껴지는 아이템.")
                .price(79000)
                .discountPer(25)
                .stockCount(50)
                .viewCount(0)
                .build(),

            /* ============================================================
               PANTS
               ============================================================ */
            Item.builder()
                .brand("MUSINSA STANDARD")
                .name("슬림 스트레이트 데님 팬츠")
                .category("PANTS")
                .imgPath("https://images.unsplash.com/photo-1542272604-787c3835535d?w=400&h=500&fit=crop&auto=format")
                .description("슬림한 실루엣의 스트레이트 데님. 편안한 신축성 소재로 일상 착용에 최적화.")
                .price(69000)
                .discountPer(0)
                .stockCount(55)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("GRAFLEX")
                .name("치노 테이퍼드 팬츠")
                .category("PANTS")
                .imgPath("https://images.unsplash.com/photo-1594938298603-c8148c4b4d17?w=400&h=500&fit=crop&auto=format")
                .description("깔끔한 치노 소재의 테이퍼드 팬츠. 캐주얼과 세미포멀 모두 소화 가능.")
                .price(59000)
                .discountPer(10)
                .stockCount(40)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("CARHARTT WIP")
                .name("카고 와이드 팬츠")
                .category("PANTS")
                .imgPath("https://images.unsplash.com/photo-1600717535275-0b18ede2f7fc?w=400&h=500&fit=crop&auto=format")
                .description("넉넉한 실루엣의 카고 팬츠. 다용도 포켓이 특징인 스트릿 스타일 아이템.")
                .price(109000)
                .discountPer(0)
                .stockCount(25)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("MUSINSA STANDARD")
                .name("린넨 와이드 하이웨이스트 팬츠")
                .category("PANTS")
                .imgPath("https://images.unsplash.com/photo-1547257965-83e0b749c4b4?w=400&h=500&fit=crop&auto=format")
                .description("시원한 린넨 소재의 와이드 팬츠. 하이웨이스트 디자인으로 다리가 길어 보이는 효과.")
                .price(49000)
                .discountPer(5)
                .stockCount(60)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("LEVIS")
                .name("501 오리지널 데님 청바지")
                .category("PANTS")
                .imgPath("https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=400&h=500&fit=crop&auto=format")
                .description("리바이스 오리지널 501 스트레이트 진. 100년 이상의 전통을 자랑하는 클래식 데님.")
                .price(129000)
                .discountPer(20)
                .stockCount(35)
                .viewCount(0)
                .build(),

            /* ============================================================
               SHOES
               ============================================================ */
            Item.builder()
                .brand("NEW BALANCE")
                .name("993 클래식 러닝화")
                .category("SHOES")
                .imgPath("https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=500&fit=crop&auto=format")
                .description("미국산 프리미엄 러닝화. 탁월한 쿠셔닝과 내구성으로 매일 신기 좋은 스니커즈.")
                .price(219000)
                .discountPer(0)
                .stockCount(20)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("NIKE")
                .name("에어 포스 1 '07")
                .category("SHOES")
                .imgPath("https://images.unsplash.com/photo-1549298916-b41d501d3772?w=400&h=500&fit=crop&auto=format")
                .description("나이키의 아이코닉 에어 포스 1. 클래식 화이트 컬러로 어떤 코디에도 잘 어울리는 스니커즈.")
                .price(109000)
                .discountPer(0)
                .stockCount(30)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("ADIDAS")
                .name("삼바 OG 스니커즈")
                .category("SHOES")
                .imgPath("https://images.unsplash.com/photo-1608231387042-66d1773070a5?w=400&h=500&fit=crop&auto=format")
                .description("아디다스 삼바 오리지널. 스포츠와 패션을 넘나드는 레전더리 실루엣.")
                .price(129000)
                .discountPer(10)
                .stockCount(25)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("DR. MARTENS")
                .name("1460 8홀 레이스업 부츠")
                .category("SHOES")
                .imgPath("https://images.unsplash.com/photo-1539185441755-769473a23570?w=400&h=500&fit=crop&auto=format")
                .description("닥터마틴 아이코닉 8홀 부츠. 견고한 가죽 소재와 옐로 스티칭이 특징.")
                .price(229000)
                .discountPer(15)
                .stockCount(15)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("CONVERSE")
                .name("척 테일러 올스타 캔버스")
                .category("SHOES")
                .imgPath("https://images.unsplash.com/photo-1465453869711-7e174808ace9?w=400&h=500&fit=crop&auto=format")
                .description("컨버스 척 테일러 클래식 하이. 100년의 역사를 지닌 캔버스 스니커즈의 원조.")
                .price(89000)
                .discountPer(0)
                .stockCount(50)
                .viewCount(0)
                .build(),

            /* ============================================================
               BAG
               ============================================================ */
            Item.builder()
                .brand("MUSINSA STANDARD")
                .name("레더 토트백")
                .category("BAG")
                .imgPath("https://images.unsplash.com/photo-1548036328-c9fa89d128fa?w=400&h=500&fit=crop&auto=format")
                .description("고급 PU 레더 소재 토트백. 노트북 수납 가능한 넉넉한 용량과 미니멀 디자인.")
                .price(79000)
                .discountPer(0)
                .stockCount(25)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("COACH")
                .name("시그니처 캔버스 숄더백")
                .category("BAG")
                .imgPath("https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=400&h=500&fit=crop&auto=format")
                .description("코치 클래식 시그니처 패턴 숄더백. 크로스백으로도 활용 가능한 다기능 가방.")
                .price(298000)
                .discountPer(20)
                .stockCount(10)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("THE NORTH FACE")
                .name("베이스캠프 더플 백팩")
                .category("BAG")
                .imgPath("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=500&fit=crop&auto=format")
                .description("더 노스페이스 베이스캠프 시리즈 백팩. 방수 소재로 야외 활동에 최적.")
                .price(159000)
                .discountPer(10)
                .stockCount(20)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("MUSINSA STANDARD")
                .name("미니 크로스백")
                .category("BAG")
                .imgPath("https://images.unsplash.com/photo-1622560480605-d83c853bc5c3?w=400&h=500&fit=crop&auto=format")
                .description("미니멀 디자인의 크로스백. 필수품만 넣기에 딱 좋은 사이즈로 데일리 사용에 적합.")
                .price(39000)
                .discountPer(0)
                .stockCount(45)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("HERSCHEL")
                .name("리틀 아메리카 백팩")
                .category("BAG")
                .imgPath("https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=400&h=500&fit=crop&auto=format")
                .description("허쉘 클래식 스트라이프 라이닝 백팩. 24L 용량으로 노트북 수납 가능.")
                .price(89000)
                .discountPer(5)
                .stockCount(30)
                .viewCount(0)
                .build(),

            /* ============================================================
               ACC
               ============================================================ */
            Item.builder()
                .brand("GUCCI")
                .name("인터로킹 GG 레더 벨트")
                .category("ACC")
                .imgPath("https://images.unsplash.com/photo-1611591437281-460bfbe1220a?w=400&h=500&fit=crop&auto=format")
                .description("구찌 클래식 인터로킹 버클 레더 벨트. 정품 소가죽과 메탈 버클로 고급스러운 마감.")
                .price(498000)
                .discountPer(0)
                .stockCount(8)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("RAY-BAN")
                .name("웨이파러 클래식 선글라스")
                .category("ACC")
                .imgPath("https://images.unsplash.com/photo-1509695507497-903c140c43b0?w=400&h=500&fit=crop&auto=format")
                .description("레이밴 아이코닉 웨이파러 선글라스. UV400 렌즈로 자외선 완벽 차단.")
                .price(198000)
                .discountPer(10)
                .stockCount(15)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("DANIEL WELLINGTON")
                .name("클래식 쿼츠 가죽 스트랩 워치")
                .category("ACC")
                .imgPath("https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=500&fit=crop&auto=format")
                .description("다니엘 웰링턴 클래식 시계. 미니멀 디자인과 교체 가능한 스트랩으로 다양한 연출 가능.")
                .price(178000)
                .discountPer(15)
                .stockCount(12)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("MUSINSA STANDARD")
                .name("울 버킷 햇")
                .category("ACC")
                .imgPath("https://images.unsplash.com/photo-1568702846914-96b305d2aaeb?w=400&h=500&fit=crop&auto=format")
                .description("부드러운 울 소재 버킷 햇. 베이직한 색상으로 다양한 코디에 활용 가능.")
                .price(35000)
                .discountPer(0)
                .stockCount(50)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("ACNE STUDIOS")
                .name("울 로고 패치 비니")
                .category("ACC")
                .imgPath("https://images.unsplash.com/photo-1576871337622-98d48d1cf531?w=400&h=500&fit=crop&auto=format")
                .description("아크네 스튜디오 시그니처 로고 패치 비니. 고급 울 소재로 따뜻하면서도 스타일리시.")
                .price(129000)
                .discountPer(20)
                .stockCount(20)
                .viewCount(0)
                .build(),

            /* ============================================================
               OUTLET (할인 상품)
               ============================================================ */
            Item.builder()
                .brand("POLO RALPH LAUREN")
                .name("[OUTLET] 클래식 핏 버튼다운 셔츠")
                .category("OUTLET")
                .imgPath("https://images.unsplash.com/photo-1594938298603-c8148c4b4d17?w=400&h=500&fit=crop&auto=format")
                .description("폴로 랄프로렌 클래식 버튼다운 셔츠. 시즌 오프 특가로 최대 50% 할인.")
                .price(189000)
                .discountPer(50)
                .stockCount(20)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("COLUMBIA")
                .name("[OUTLET] 옴니히트 플리스 재킷")
                .category("OUTLET")
                .imgPath("https://images.unsplash.com/photo-1576871337622-98d48d1cf531?w=400&h=500&fit=crop&auto=format")
                .description("컬럼비아 옴니히트 보온 플리스 재킷. 경량·보온·통기성 삼박자를 갖춘 기능성 아우터. 시즌오프 세일.")
                .price(149000)
                .discountPer(40)
                .stockCount(18)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("LEVI'S")
                .name("[OUTLET] 스탠다드 테이퍼 진")
                .category("OUTLET")
                .imgPath("https://images.unsplash.com/photo-1542272604-787c3835535d?w=400&h=500&fit=crop&auto=format")
                .description("리바이스 스탠다드 테이퍼 데님. 편안한 핏과 스트레치 소재로 활동성 극대화. 아울렛 특가.")
                .price(119000)
                .discountPer(45)
                .stockCount(22)
                .viewCount(0)
                .build(),
            Item.builder()
                .brand("REEBOK")
                .name("[OUTLET] 클래식 레더 스니커즈")
                .category("OUTLET")
                .imgPath("https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=500&fit=crop&auto=format")
                .description("리복 클래식 레더 스니커즈. 1983년 오리지널 디자인 그대로. 시즌 마감 50% 특가.")
                .price(99000)
                .discountPer(50)
                .stockCount(15)
                .viewCount(0)
                .build()
        );

        itemRepository.saveAll(items);
        log.info("✅ 샘플 상품 {}개 등록 완료 (Unsplash 패션 이미지 사용)", items.size());
    }
}
