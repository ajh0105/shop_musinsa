package com.musinsa.shop.common.config;

import com.musinsa.shop.item.entity.Item;
import com.musinsa.shop.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Order(2)
@RequiredArgsConstructor
public class ItemDataInitializer implements CommandLineRunner {

    private final ItemRepository itemRepository;

    private static final String BRAND = "아뜨랑스";
    private static final String IMG_BASE = "https://picsum.photos/seed/";
    private static final String IMG_SUFFIX = "/420/560";

    private static final String[][] CATEGORIES = {
            {"원피스", "101", "false"},
            {"상의",   "201", "false"},
            {"하의",   "301", "false"},
            {"아우터", "401", "false"},
            {"악세잡화", "501", "false"},
            {"오늘의 할인", "601", "true"},
    };

    @Override
    public void run(String... args) {
        List<Item> items = new ArrayList<>();

        if (itemRepository.countByCategory("원피스") == 0)
            items.addAll(createCategory("원피스", 101, DRESS_NAMES, DRESS_PRICES, false));
        if (itemRepository.countByCategory("상의") == 0)
            items.addAll(createCategory("상의", 201, TOP_NAMES, TOP_PRICES, false));
        if (itemRepository.countByCategory("하의") == 0)
            items.addAll(createCategory("하의", 301, BOTTOM_NAMES, BOTTOM_PRICES, false));
        if (itemRepository.countByCategory("아우터") == 0)
            items.addAll(createCategory("아우터", 401, OUTER_NAMES, OUTER_PRICES, false));
        if (itemRepository.countByCategory("악세잡화") == 0)
            items.addAll(createCategory("악세잡화", 501, ACC_NAMES, ACC_PRICES, false));
        if (itemRepository.countByCategory("오늘의 할인") == 0)
            items.addAll(createCategory("오늘의 할인", 601, SALE_NAMES, SALE_PRICES, true));

        if (items.isEmpty()) {
            log.info("모든 카테고리에 상품 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        itemRepository.saveAll(items);
        log.info("상품 데이터 초기화 완료: {}개 등록", items.size());
    }

    private List<Item> createCategory(String category, int seedStart,
                                      String[] names, int[] prices, boolean isSale) {
        List<Item> result = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            int discountPer = 0;
            if (isSale) {
                int[] saleRates = {20, 25, 30, 35, 20, 25, 30, 35, 20, 25, 30, 35,
                        20, 25, 30, 35, 20, 25, 30, 35, 20, 25, 30, 35};
                discountPer = saleRates[i % saleRates.length];
            }
            result.add(Item.builder()
                    .brand(BRAND)
                    .name(names[i])
                    .category(category)
                    .imgPath(IMG_BASE + (seedStart + i) + IMG_SUFFIX)
                    .description(category + " 카테고리의 " + names[i] + "입니다. 여성스럽고 세련된 디자인을 만나보세요.")
                    .price(prices[i % prices.length])
                    .discountPer(discountPer)
                    .stockCount(30 + (i % 10) * 2)
                    .viewCount(50 + i * 15)
                    .build());
        }
        return result;
    }

    /* ── 원피스 (24개) ── */
    private static final String[] DRESS_NAMES = {
            "플로럴 플리츠 원피스", "레이스 슬립 원피스", "린넨 셔츠 원피스", "니트 미니 원피스",
            "랩 플레어 원피스", "체크 트위드 원피스", "쉬폰 맥시 원피스", "리본 벨트 원피스",
            "스트라이프 셔츠 원피스", "벨벳 오프숄더 원피스", "스모크 미디 원피스", "도트 프린트 원피스",
            "코듀로이 미디 원피스", "레이어드 슬릿 원피스", "퍼프 슬리브 원피스", "A라인 포인트 원피스",
            "스퀘어넥 원피스", "지그재그 니트 원피스", "플리츠 랩 원피스", "빅 플라워 원피스",
            "체인 스트랩 원피스", "쉬폰 티어드 원피스", "미디 컷아웃 원피스", "로맨틱 볼레로 원피스"
    };
    private static final int[] DRESS_PRICES = {
            69000, 59000, 55000, 62000, 72000, 89000, 78000, 65000,
            58000, 95000, 68000, 57000, 75000, 82000, 70000, 64000,
            67000, 73000, 61000, 88000, 79000, 66000, 84000, 71000
    };

    /* ── 상의 (24개) ── */
    private static final String[] TOP_NAMES = {
            "베이직 크롭 티셔츠", "레이스 트림 블라우스", "오버핏 린넨 셔츠", "리본 캡 블라우스",
            "스트라이프 니트 탑", "프릴 넥 블라우스", "크롭 카디건", "퍼프 소매 블라우스",
            "머슬핏 베이직 티", "빅컬러 오버셔츠", "시폰 랩 블라우스", "롤넥 리브 니트",
            "데님 오프숄더 탑", "크롭 후디", "브이넥 니트 베스트", "모달 슬리브리스",
            "캐시미어 블렌드 스웨터", "버튼다운 크롭 셔츠", "프린팅 오버핏 맨투맨", "기모 터틀넥 니트",
            "쉬어 러플 블라우스", "스트라이프 크롭 니트", "레이스 백리본 블라우스", "코튼 루즈핏 셔츠"
    };
    private static final int[] TOP_PRICES = {
            29000, 47000, 52000, 43000, 55000, 48000, 39000, 51000,
            25000, 58000, 46000, 62000, 42000, 38000, 45000, 32000,
            79000, 44000, 49000, 57000, 53000, 41000, 50000, 36000
    };

    /* ── 하의 (24개) ── */
    private static final String[] BOTTOM_NAMES = {
            "슬림 스트레이트 데님", "플리츠 미디 스커트", "와이드 크롭 슬랙스", "체크 미니 스커트",
            "레더 와이드 팬츠", "롱 플레어 스커트", "하이웨이스트 크롭 팬츠", "벨벳 미니 스커트",
            "조거 슬랙스", "도트 맥시 스커트", "시어 미디 스커트", "와이드 데님 팬츠",
            "스웨이드 미디 스커트", "밴딩 배기 팬츠", "레이어드 튤 스커트", "세미 와이드 슬랙스",
            "플리츠 팬츠", "카고 와이드 팬츠", "새틴 슬립 스커트", "컷아웃 미니 스커트",
            "와이드 린넨 팬츠", "플레어 벨벳 스커트", "테이퍼드 슬랙스", "크롭 조거 팬츠"
    };
    private static final int[] BOTTOM_PRICES = {
            59000, 52000, 62000, 47000, 79000, 68000, 55000, 57000,
            48000, 65000, 58000, 63000, 72000, 49000, 75000, 58000,
            61000, 67000, 53000, 44000, 56000, 71000, 64000, 42000
    };

    /* ── 아우터 (24개) ── */
    private static final String[] OUTER_NAMES = {
            "캐시미어 롱 코트", "오버핏 트위드 자켓", "체크 더블 코트", "가죽 라이더 자켓",
            "퀄팅 패딩 코트", "울 싱글 코트", "데님 블레이저", "베이직 트렌치 코트",
            "오버핏 봄버 자켓", "퍼 트리밍 코트", "테일러드 자켓", "헤링본 하프 코트",
            "벨티드 울 코트", "로브 카디건", "패딩 베스트", "앤벨 실루엣 코트",
            "크롭 자켓", "드레이프 롱 코트", "트위드 크롭 자켓", "오버핏 체크 블레이저",
            "더블 페이스 코트", "레더 오버핏 자켓", "무스탕 쇼트 자켓", "클래식 피코트"
    };
    private static final int[] OUTER_PRICES = {
            149000, 98000, 125000, 119000, 108000, 135000, 79000, 112000,
            88000, 145000, 95000, 118000, 139000, 82000, 69000, 128000,
            89000, 132000, 94000, 102000, 142000, 115000, 106000, 125000
    };

    /* ── 악세잡화 (24개) ── */
    private static final String[] ACC_NAMES = {
            "진주 체인 이어링", "미니 버킷백", "체인 크로스백", "실키 스카프",
            "베이직 뉴스보이캡", "슬라이드 샌들", "레이어드 팔찌 세트", "미니 클러치백",
            "케이블 니트 비니", "골드 뱅글 세트", "퀄티드 숄더백", "데이지 발찌",
            "패딩 토트백", "머리핀 세트", "스퀘어 선글라스", "오버니 삭스 세트",
            "미러 드롭 이어링", "미니 사첼백", "실크 헤어밴드", "리본 머리끈 세트",
            "체인 미니 숄더백", "골드 레이어드 목걸이", "빈티지 링 세트", "새틴 리본 헤어핀"
    };
    private static final int[] ACC_PRICES = {
            22000, 48000, 55000, 28000, 32000, 38000, 25000, 42000,
            19000, 29000, 68000, 18000, 52000, 15000, 35000, 12000,
            23000, 58000, 21000, 13000, 62000, 27000, 24000, 16000
    };

    /* ── 오늘의 할인 (24개) ── */
    private static final String[] SALE_NAMES = {
            "[원피스] 플로럴 플리츠 원피스 SALE", "[상의] 레이스 트림 블라우스 SALE",
            "[하의] 플리츠 미디 스커트 SALE",     "[아우터] 데님 블레이저 SALE",
            "[악세잡화] 미니 버킷백 SALE",         "[원피스] 린넨 셔츠 원피스 SALE",
            "[상의] 오버핏 린넨 셔츠 SALE",        "[하의] 체크 미니 스커트 SALE",
            "[아우터] 오버핏 봄버 자켓 SALE",      "[악세잡화] 체인 크로스백 SALE",
            "[원피스] 스트라이프 셔츠 원피스 SALE","[상의] 크롭 카디건 SALE",
            "[하의] 와이드 크롭 슬랙스 SALE",     "[아우터] 로브 카디건 SALE",
            "[악세잡화] 퀄티드 숄더백 SALE",       "[원피스] 니트 미니 원피스 SALE",
            "[상의] 스트라이프 니트 탑 SALE",      "[하의] 레더 와이드 팬츠 SALE",
            "[아우터] 테일러드 자켓 SALE",          "[악세잡화] 미니 사첼백 SALE",
            "[원피스] 쉬폰 맥시 원피스 SALE",      "[상의] 캐시미어 블렌드 스웨터 SALE",
            "[하의] 롱 플레어 스커트 SALE",        "[아우터] 체크 더블 코트 SALE"
    };
    private static final int[] SALE_PRICES = {
            69000, 47000, 52000, 79000, 48000, 55000, 52000, 47000,
            88000, 55000, 58000, 39000, 62000, 82000, 68000, 62000,
            55000, 79000, 95000, 58000, 78000, 79000, 68000, 125000
    };
}
