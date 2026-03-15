export const CATEGORIES = [
  'Best',
  '원피스',
  '상의',
  '하의',
  '아우터',
  '악세잡화',
  '오늘의 할인'
]

const baseNames = {
  Best: '베스트 라인',
  '원피스': '로맨틱 원피스',
  '상의': '여리핏 상의',
  '하의': '슬림 하의',
  '아우터': '시즌 아우터',
  '악세잡화': '포인트 악세잡화',
  '오늘의 할인': '오늘의 특가'
}

function createItemsForCategory(category, startId) {
  const items = []
  for (let i = 1; i <= 16; i += 1) {
    const price = 29000 + i * 3000 + category.length * 500
    const discount = category === '오늘의 할인'
      ? 30 + (i % 4) * 5
      : category === 'Best'
        ? 10 + (i % 3) * 5
        : (i % 4) * 5

    items.push({
      id: startId + i,
      brand: '아뜨랑스',
      name: `${baseNames[category]} ${i}`,
      category,
      imgPath: `https://picsum.photos/seed/${encodeURIComponent(category)}${i}/420/560`,
      description: `${category} 카테고리 샘플 상품 ${i}번입니다.`,
      price,
      discountPer: discount,
      salePrice: Math.floor(price * (100 - discount) / 100),
      stockCount: 30 - (i % 7),
      viewCount: 300 + i * 17
    })
  }
  return items
}

let nextId = 1000
export const MOCK_ITEMS = CATEGORIES.flatMap((category) => {
  const group = createItemsForCategory(category, nextId)
  nextId += 100
  return group
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
  return [...MOCK_ITEMS].sort((a, b) => b.discountPer - a.discountPer).slice(0, 4)
}
