export const CATEGORIES = [
  'Best',
  '원피스',
  '상의',
  '하의',
  '아우터',
  '악세잡화',
  '오늘의 할인'
]

// 카테고리별 상품 이미지 시드 (picsum.photos)
const categorySeeds = {
  '원피스': [101,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122],
  '상의':   [201,202,203,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222],
  '하의':   [301,302,303,304,305,306,307,308,309,310,311,312,313,314,315,316,317,318,319,320,321,322],
  '아우터': [401,402,403,404,405,406,407,408,409,410,411,412,413,414,415,416,417,418,419,420,421,422],
  '악세잡화':[501,502,503,504,505,506,507,508,509,510,511,512,513,514,515,516,517,518,519,520,521,522],
  '오늘의 할인':[601,602,603,604,605,606,607,608,609,610,611,612,613,614,615,616,617,618,619,620,621,622],
  'Best':   [701,702,703,704,705,706,707,708,709,710,711,712,713,714,715,716,717,718,719,720,721,722],
}

const categoryProducts = {
  '원피스': [
    { name: '플로럴 플리츠 원피스', price: 69000 },
    { name: '레이스 슬립 원피스', price: 59000 },
    { name: '린넨 셔츠 원피스', price: 55000 },
    { name: '니트 미니 원피스', price: 62000 },
    { name: '랩 플레어 원피스', price: 72000 },
    { name: '체크 트위드 원피스', price: 89000 },
    { name: '쉬폰 맥시 원피스', price: 78000 },
    { name: '리본 벨트 원피스', price: 65000 },
    { name: '스트라이프 셔츠 원피스', price: 58000 },
    { name: '벨벳 오프숄더 원피스', price: 95000 },
    { name: '스모크 미디 원피스', price: 68000 },
    { name: '도트 프린트 원피스', price: 57000 },
    { name: '코듀로이 미디 원피스', price: 75000 },
    { name: '레이어드 슬릿 원피스', price: 82000 },
    { name: '퍼프 슬리브 원피스', price: 70000 },
    { name: 'A라인 포인트 원피스', price: 64000 },
    { name: '스퀘어넥 원피스', price: 67000 },
    { name: '지그재그 니트 원피스', price: 73000 },
    { name: '플리츠 랩 원피스', price: 61000 },
    { name: '빅 플라워 원피스', price: 88000 },
    { name: '체인 스트랩 원피스', price: 79000 },
    { name: '쉬폰 티어드 원피스', price: 66000 },
  ],
  '상의': [
    { name: '베이직 크롭 티셔츠', price: 29000 },
    { name: '레이스 트림 블라우스', price: 47000 },
    { name: '오버핏 린넨 셔츠', price: 52000 },
    { name: '리본 캡 블라우스', price: 43000 },
    { name: '스트라이프 니트 탑', price: 55000 },
    { name: '프릴 넥 블라우스', price: 48000 },
    { name: '크롭 카디건', price: 39000 },
    { name: '퍼프 소매 블라우스', price: 51000 },
    { name: '머슬핏 베이직 티', price: 25000 },
    { name: '빅컬러 오버셔츠', price: 58000 },
    { name: '시폰 랩 블라우스', price: 46000 },
    { name: '롤넥 리브 니트', price: 62000 },
    { name: '데님 오프숄더 탑', price: 42000 },
    { name: '크롭 후디', price: 38000 },
    { name: '브이넥 니트 베스트', price: 45000 },
    { name: '모달 슬리브리스', price: 32000 },
    { name: '캐시미어 블렌드 스웨터', price: 79000 },
    { name: '버튼다운 크롭 셔츠', price: 44000 },
    { name: '프린팅 오버핏 맨투맨', price: 49000 },
    { name: '기모 터틀넥 니트', price: 57000 },
    { name: '쉬어 러플 블라우스', price: 53000 },
    { name: '스트라이프 크롭 니트', price: 41000 },
  ],
  '하의': [
    { name: '슬림 스트레이트 데님', price: 59000 },
    { name: '플리츠 미디 스커트', price: 52000 },
    { name: '와이드 크롭 슬랙스', price: 62000 },
    { name: '체크 미니 스커트', price: 47000 },
    { name: '레더 와이드 팬츠', price: 79000 },
    { name: '롱 플레어 스커트', price: 68000 },
    { name: '하이웨이스트 크롭 팬츠', price: 55000 },
    { name: '벨벳 미니 스커트', price: 57000 },
    { name: '조거 슬랙스', price: 48000 },
    { name: '도트 맥시 스커트', price: 65000 },
    { name: '시어 미디 스커트', price: 58000 },
    { name: '와이드 데님 팬츠', price: 63000 },
    { name: '스웨이드 미디 스커트', price: 72000 },
    { name: '밴딩 배기 팬츠', price: 49000 },
    { name: '레이어드 튤 스커트', price: 75000 },
    { name: '세미 와이드 슬랙스', price: 58000 },
    { name: '플리츠 팬츠', price: 61000 },
    { name: '카고 와이드 팬츠', price: 67000 },
    { name: '새틴 슬립 스커트', price: 53000 },
    { name: '컷아웃 미니 스커트', price: 44000 },
    { name: '와이드 린넨 팬츠', price: 56000 },
    { name: '플레어 벨벳 스커트', price: 71000 },
  ],
  '아우터': [
    { name: '캐시미어 롱 코트', price: 149000 },
    { name: '오버핏 트위드 자켓', price: 98000 },
    { name: '체크 더블 코트', price: 125000 },
    { name: '가죽 라이더 자켓', price: 119000 },
    { name: '퀄팅 패딩 코트', price: 108000 },
    { name: '울 싱글 코트', price: 135000 },
    { name: '데님 블레이저', price: 79000 },
    { name: '베이직 트렌치 코트', price: 112000 },
    { name: '오버핏 봄버 자켓', price: 88000 },
    { name: '퍼 트리밍 코트', price: 145000 },
    { name: '테일러드 자켓', price: 95000 },
    { name: '헤링본 하프 코트', price: 118000 },
    { name: '벨티드 울 코트', price: 139000 },
    { name: '로브 카디건', price: 82000 },
    { name: '패딩 베스트', price: 69000 },
    { name: '앤벨 실루엣 코트', price: 128000 },
    { name: '크롭 자켓', price: 89000 },
    { name: '드레이프 롱 코트', price: 132000 },
    { name: '트위드 크롭 자켓', price: 94000 },
    { name: '오버핏 체크 블레이저', price: 102000 },
    { name: '더블 페이스 코트', price: 142000 },
    { name: '레더 오버핏 자켓', price: 115000 },
  ],
  '악세잡화': [
    { name: '진주 체인 이어링', price: 22000 },
    { name: '미니 버킷백', price: 48000 },
    { name: '체인 크로스백', price: 55000 },
    { name: '실키 스카프', price: 28000 },
    { name: '베이직 뉴스보이캡', price: 32000 },
    { name: '슬라이드 샌들', price: 38000 },
    { name: '레이어드 팔찌 세트', price: 25000 },
    { name: '미니 클러치백', price: 42000 },
    { name: '케이블 니트 비니', price: 19000 },
    { name: '골드 뱅글 세트', price: 29000 },
    { name: '퀼티드 숄더백', price: 68000 },
    { name: '데이지 발찌', price: 18000 },
    { name: '패딩 토트백', price: 52000 },
    { name: '머리핀 세트', price: 15000 },
    { name: '스퀘어 선글라스', price: 35000 },
    { name: '오버니 삭스 세트', price: 12000 },
    { name: '미러 드롭 이어링', price: 23000 },
    { name: '미니 사첼백', price: 58000 },
    { name: '실크 헤어밴드', price: 21000 },
    { name: '리본 머리끈 세트', price: 13000 },
    { name: '체인 미니 숄더백', price: 62000 },
    { name: '골드 레이어드 목걸이', price: 27000 },
  ],
  '오늘의 할인': [
    { name: '[원피스] 플로럴 플리츠 원피스', price: 69000, discountPer: 30 },
    { name: '[상의] 레이스 트림 블라우스', price: 47000, discountPer: 25 },
    { name: '[하의] 플리츠 미디 스커트', price: 52000, discountPer: 35 },
    { name: '[아우터] 데님 블레이저', price: 79000, discountPer: 20 },
    { name: '[악세잡화] 미니 버킷백', price: 48000, discountPer: 30 },
    { name: '[원피스] 린넨 셔츠 원피스', price: 55000, discountPer: 25 },
    { name: '[상의] 오버핏 린넨 셔츠', price: 52000, discountPer: 30 },
    { name: '[하의] 체크 미니 스커트', price: 47000, discountPer: 20 },
    { name: '[아우터] 오버핏 봄버 자켓', price: 88000, discountPer: 25 },
    { name: '[악세잡화] 체인 크로스백', price: 55000, discountPer: 35 },
    { name: '[원피스] 스트라이프 셔츠 원피스', price: 58000, discountPer: 30 },
    { name: '[상의] 크롭 카디건', price: 39000, discountPer: 20 },
    { name: '[하의] 와이드 크롭 슬랙스', price: 62000, discountPer: 25 },
    { name: '[아우터] 로브 카디건', price: 82000, discountPer: 30 },
    { name: '[악세잡화] 퀼티드 숄더백', price: 68000, discountPer: 20 },
    { name: '[원피스] 니트 미니 원피스', price: 62000, discountPer: 35 },
    { name: '[상의] 스트라이프 니트 탑', price: 55000, discountPer: 25 },
    { name: '[하의] 레더 와이드 팬츠', price: 79000, discountPer: 30 },
    { name: '[아우터] 테일러드 자켓', price: 95000, discountPer: 20 },
    { name: '[악세잡화] 미니 사첼백', price: 58000, discountPer: 25 },
    { name: '[원피스] 쉬폰 맥시 원피스', price: 78000, discountPer: 30 },
    { name: '[상의] 캐시미어 블렌드 스웨터', price: 79000, discountPer: 35 },
  ],
}

let nextId = 1000
export const MOCK_ITEMS = CATEGORIES.flatMap((category) => {
  const products = categoryProducts[category] || []
  const seeds = categorySeeds[category] || []
  return products.map((p, i) => {
    const id = ++nextId
    const discountPer = p.discountPer ?? 0
    return {
      id,
      brand: '아뜨랑스',
      name: p.name,
      category,
      imgPath: `https://picsum.photos/seed/${seeds[i] ?? (id)}/420/560`,
      description: `아뜨랑스 ${p.name}입니다. 여성스럽고 세련된 디자인으로 다양한 스타일링에 활용하세요.`,
      price: p.price,
      discountPer,
      salePrice: discountPer > 0 ? Math.floor(p.price * (100 - discountPer) / 100) : null,
      stockCount: 25 + (i % 8) * 3,
      viewCount: 200 + i * 23 + category.length * 10
    }
  })
})

export function getCategoryItems(category) {
  return MOCK_ITEMS.filter((item) => item.category === category)
}

export function getSectionItems(type) {
  if (type === 'new') {
    return [...MOCK_ITEMS].sort((a, b) => b.id - a.id).slice(0, 4)
  }
  if (type === 'best') {
    return [...MOCK_ITEMS].sort((a, b) => b.viewCount - a.viewCount).slice(0, 4)
  }
  // recommend: 오늘의 할인 상품 중 할인율 높은 순
  return MOCK_ITEMS
    .filter(item => item.discountPer > 0)
    .sort((a, b) => b.discountPer - a.discountPer)
    .slice(0, 4)
}
