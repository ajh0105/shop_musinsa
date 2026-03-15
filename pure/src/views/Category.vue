<template>
  <div class="category-page">
    <!-- 카테고리 헤더 -->
    <div class="category-header">
      <div class="container">
        <div class="breadcrumb">
          <RouterLink to="/" class="breadcrumb-link">홈</RouterLink>
          <span class="breadcrumb-sep">›</span>
          <span class="breadcrumb-current">{{ categoryLabel }}</span>
        </div>
        <h1 class="category-title">{{ categoryLabel }}</h1>
        <p class="category-count">총 {{ items.length }}개 상품</p>
      </div>
    </div>

    <div class="container">
      <!-- 정렬 & 필터 바 -->
      <div class="filter-bar">
        <div class="filter-sort">
          <button
            v-for="opt in sortOptions"
            :key="opt.value"
            class="sort-btn"
            :class="{ 'sort-btn--active': sortBy === opt.value }"
            @click="sortBy = opt.value"
          >
            {{ opt.label }}
          </button>
        </div>
        <div class="filter-info">
          <span>{{ currentPage }} / {{ totalPages }} 페이지</span>
        </div>
      </div>

      <!-- 로딩 -->
      <div v-if="loading" class="loading-box">
        <div class="spinner"></div>
        <p>상품을 불러오는 중...</p>
      </div>

      <!-- 상품 없음 -->
      <div v-else-if="pagedItems.length === 0" class="empty-box">
        <p>상품이 없습니다.</p>
      </div>

      <!-- 상품 그리드 -->
      <div v-else class="product-grid product-grid--category">
        <div
          v-for="item in pagedItems"
          :key="item.id"
          class="product-card"
          @click="$router.push('/product/' + item.id)"
        >
          <div class="product-img-wrap">
            <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" />
            <span v-if="item.discountPer > 0" class="product-badge">{{ item.discountPer }}%</span>
            <span v-if="item.isSoldOut" class="product-badge product-badge--soldout">품절</span>
            <div class="product-overlay">
              <button class="btn-cart-quick" @click.stop="addToCart(item)">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/>
                  <line x1="3" y1="6" x2="21" y2="6"/>
                  <path d="M16 10a4 4 0 0 1-8 0"/>
                </svg>
                장바구니 담기
              </button>
            </div>
          </div>
          <div class="product-info">
            <p class="product-brand">{{ item.brand }}</p>
            <p class="product-name">{{ item.name }}</p>
            <div class="product-price-wrap">
              <span v-if="item.discountPer > 0" class="product-price-original">{{ formatPrice(item.price) }}</span>
              <span class="product-price">{{ formatPrice(item.salePrice) }}</span>
              <span v-if="item.discountPer > 0" class="product-discount">{{ item.discountPer }}%</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 페이지네이션 -->
      <div v-if="totalPages > 1" class="pagination">
        <button
          class="page-btn"
          :disabled="currentPage === 1"
          @click="currentPage--"
        >
          ‹ 이전
        </button>
        <button
          v-for="p in totalPages"
          :key="p"
          class="page-btn page-num"
          :class="{ 'page-btn--active': p === currentPage }"
          @click="currentPage = p"
        >
          {{ p }}
        </button>
        <button
          class="page-btn"
          :disabled="currentPage === totalPages"
          @click="currentPage++"
        >
          다음 ›
        </button>
      </div>
    </div>

    <!-- 토스트 메시지 -->
    <Transition name="toast">
      <div v-if="toastMsg" class="toast">{{ toastMsg }}</div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuth } from '../composables/useAuth.js'

const route = useRoute()
const { isLoggedIn } = useAuth()

const ITEMS_PER_PAGE = 6
const loading = ref(false)
const items = ref([])
const currentPage = ref(1)
const sortBy = ref('popular')
const toastMsg = ref('')

const categoryLabels = {
  OUTER: '아우터',
  TOP: '상의',
  PANTS: '하의',
  SHOES: '신발',
  BAG: '가방',
  ACC: '악세사리',
  OUTLET: '아울렛',
}

const sortOptions = [
  { value: 'popular', label: '인기순' },
  { value: 'new', label: '신상품순' },
  { value: 'low', label: '낮은 가격순' },
  { value: 'high', label: '높은 가격순' },
  { value: 'discount', label: '할인율순' },
]

const categoryLabel = computed(() => {
  if (route.query.keyword) return `"${route.query.keyword}" 검색 결과`
  return categoryLabels[route.params.name] || route.params.name
})

const sortedItems = computed(() => {
  const arr = [...items.value]
  switch (sortBy.value) {
    case 'popular': return arr.sort((a, b) => b.viewCount - a.viewCount)
    case 'new': return arr.sort((a, b) => b.id - a.id)
    case 'low': return arr.sort((a, b) => a.salePrice - b.salePrice)
    case 'high': return arr.sort((a, b) => b.salePrice - a.salePrice)
    case 'discount': return arr.sort((a, b) => b.discountPer - a.discountPer)
    default: return arr
  }
})

const totalPages = computed(() => Math.max(1, Math.ceil(sortedItems.value.length / ITEMS_PER_PAGE)))

const pagedItems = computed(() => {
  const start = (currentPage.value - 1) * ITEMS_PER_PAGE
  return sortedItems.value.slice(start, start + ITEMS_PER_PAGE)
})

function formatPrice(price) {
  return price?.toLocaleString('ko-KR') + '원'
}

function showToast(msg) {
  toastMsg.value = msg
  setTimeout(() => { toastMsg.value = '' }, 2500)
}

async function addToCart(item) {
  if (!isLoggedIn.value) {
    showToast('로그인 후 이용하실 수 있습니다.')
    return
  }
  if (item.isSoldOut) {
    showToast('품절된 상품입니다.')
    return
  }
  try {
    const res = await fetch('/v1/api/carts', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ itemId: item.id })
    })
    if (res.ok) {
      showToast('장바구니에 추가되었습니다.')
    } else if (res.status === 409) {
      showToast('이미 장바구니에 담긴 상품입니다.')
    } else {
      showToast('오류가 발생했습니다.')
    }
  } catch {
    showToast('네트워크 오류가 발생했습니다.')
  }
}

async function fetchItems() {
  loading.value = true
  currentPage.value = 1
  items.value = []
  const category = route.params.name
  const keyword = route.query.keyword
  try {
    let url
    if (keyword) {
      url = `/v1/api/items?keyword=${encodeURIComponent(keyword)}`
    } else if (category && category !== 'ALL') {
      url = `/v1/api/items?category=${category}`
    } else {
      url = '/v1/api/items'
    }
    const res = await fetch(url)
    items.value = await res.json()
  } catch (e) {
    console.error('상품 조회 실패', e)
  } finally {
    loading.value = false
  }
}

watch([() => route.params.name, () => route.query.keyword], () => {
  fetchItems()
}, { immediate: true })
</script>
