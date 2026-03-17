<template>
  <div class="home">
    <!-- 메인 배너 슬라이더 -->
    <section class="banner-section">
      <div class="banner-slider">
        <div
          class="banner-track"
          :style="{ transform: `translateX(-${currentSlide * 100}%)` }"
        >
          <div
            v-for="(banner, i) in banners"
            :key="i"
            class="banner-slide"
            :style="{ background: banner.bg }"
          >
            <div class="banner-content">
              <div class="banner-badges">
                <span class="banner-badge" :class="`banner-badge--${banner.badge?.toLowerCase()}`">{{ banner.badge }}</span>
                <span class="banner-highlight">{{ banner.highlight }}</span>
              </div>
              <p class="banner-sub">{{ banner.sub }}</p>
              <h2 class="banner-title">{{ banner.title }}</h2>
              <p class="banner-desc">{{ banner.desc }}</p>
              <RouterLink :to="banner.link" class="banner-btn">{{ banner.cta }}</RouterLink>
            </div>
            <div class="banner-image-area">
              <img :src="banner.img" :alt="banner.title" class="banner-img" />
            </div>
          </div>
        </div>

        <!-- 슬라이더 컨트롤 -->
        <button class="slider-btn slider-btn--prev" @click="prevSlide">&#8249;</button>
        <button class="slider-btn slider-btn--next" @click="nextSlide">&#8250;</button>

        <div class="slider-dots">
          <button
            v-for="(_, i) in banners"
            :key="i"
            class="dot"
            :class="{ 'dot--active': i === currentSlide }"
            @click="goToSlide(i)"
          />
        </div>
      </div>
    </section>

    <!-- 카테고리 빠른 메뉴 -->
    <section class="quick-category">
      <div class="container">
        <RouterLink
          v-for="cat in quickCategories"
          :key="cat.name"
          :to="`/category/${cat.name}`"
          class="quick-cat-item"
        >
          <div class="quick-cat-icon">{{ cat.icon }}</div>
          <span class="quick-cat-label">{{ cat.label }}</span>
        </RouterLink>
      </div>
    </section>

    <!-- 신상품 배너 띠 -->
    <section class="promo-banner">
      <div class="container">
        <div class="promo-banner-inner">
          <span class="promo-tag">NEW</span>
          <span class="promo-text">2025 S/S 신상품 대거 입고 — 최대 30% 할인 쿠폰 증정</span>
          <RouterLink to="/category/OUTER" class="promo-link">지금 확인 →</RouterLink>
        </div>
      </div>
    </section>

    <!-- 신상품 -->
    <section class="hit-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">신상품</h2>
          <p class="section-sub">새로 입고된 아이템</p>
        </div>
        <div v-if="newLoading" class="loading-box"><div class="spinner"></div></div>
        <div v-else class="product-grid">
          <RouterLink v-for="item in newItems" :key="item.id" :to="`/product/${item.id}`" class="product-card">
            <div class="product-img-wrap">
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" @error="onImgError" />
              <span class="product-badge product-badge--new">NEW</span>
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
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- 베스트 상품 -->
    <section class="hit-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">베스트</h2>
          <p class="section-sub">지금 가장 인기 있는 아이템</p>
        </div>
        <div v-if="loading" class="loading-box">
          <div class="spinner"></div>
        </div>
        <div v-else class="product-grid">
          <RouterLink
            v-for="item in hitItems"
            :key="item.id"
            :to="`/product/${item.id}`"
            class="product-card"
          >
            <div class="product-img-wrap">
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" @error="onImgError" />
              <span v-if="item.discountPer > 0" class="product-badge">{{ item.discountPer }}%</span>
              <span v-if="item.isSoldOut" class="product-badge product-badge--soldout">품절</span>
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
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- 추천 상품 -->
    <section class="hit-section" style="background:#f8f8f8;">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">추천 상품</h2>
          <p class="section-sub">할인율이 높은 인기 아이템</p>
        </div>
        <div v-if="recLoading" class="loading-box"><div class="spinner"></div></div>
        <div v-else class="product-grid">
          <RouterLink v-for="item in recItems" :key="item.id" :to="`/product/${item.id}`" class="product-card">
            <div class="product-img-wrap">
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" @error="onImgError" />
              <span v-if="item.discountPer > 0" class="product-badge">{{ item.discountPer }}%</span>
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
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- 아우터 추천 섹션 행 -->
    <section class="category-row-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">아우터 추천</h2>
          <RouterLink to="/category/OUTER" class="section-more">더보기 →</RouterLink>
        </div>
        <div v-if="outerLoading" class="loading-box"><div class="spinner"></div></div>
        <div v-else class="product-row">
          <RouterLink
            v-for="item in outerItems"
            :key="item.id"
            :to="`/product/${item.id}`"
            class="product-card product-card--sm"
          >
            <div class="product-img-wrap">
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" @error="onImgError" />
              <span v-if="item.discountPer > 0" class="product-badge">{{ item.discountPer }}%</span>
            </div>
            <div class="product-info">
              <p class="product-brand">{{ item.brand }}</p>
              <p class="product-name">{{ item.name }}</p>
              <div class="product-price-wrap">
                <span class="product-price">{{ formatPrice(item.salePrice) }}</span>
                <span v-if="item.discountPer > 0" class="product-discount">{{ item.discountPer }}%</span>
              </div>
            </div>
          </RouterLink>
        </div>
      </div>
    </section>

    <!-- 할인 배너 -->
    <section class="sale-banner-section">
      <div class="container">
        <RouterLink to="/category/OUTLET" class="sale-banner">
          <div class="sale-banner-left">
            <span class="sale-banner-tag">OUTLET</span>
            <h3 class="sale-banner-title">최대 50% 할인</h3>
            <p class="sale-banner-desc">인기 브랜드 아이템을 특가에 만나보세요</p>
          </div>
          <div class="sale-banner-right">
            <span class="sale-arrow">→</span>
          </div>
        </RouterLink>
      </div>
    </section>

    <!-- 신발 추천 섹션 -->
    <section class="category-row-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">신발 추천</h2>
          <RouterLink to="/category/SHOES" class="section-more">더보기 →</RouterLink>
        </div>
        <div v-if="shoesLoading" class="loading-box"><div class="spinner"></div></div>
        <div v-else class="product-row">
          <RouterLink
            v-for="item in shoesItems"
            :key="item.id"
            :to="`/product/${item.id}`"
            class="product-card product-card--sm"
          >
            <div class="product-img-wrap">
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" @error="onImgError" />
              <span v-if="item.discountPer > 0" class="product-badge">{{ item.discountPer }}%</span>
            </div>
            <div class="product-info">
              <p class="product-brand">{{ item.brand }}</p>
              <p class="product-name">{{ item.name }}</p>
              <div class="product-price-wrap">
                <span class="product-price">{{ formatPrice(item.salePrice) }}</span>
                <span v-if="item.discountPer > 0" class="product-discount">{{ item.discountPer }}%</span>
              </div>
            </div>
          </RouterLink>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useImgFallback } from '../composables/useImgFallback.js'

const { onImgError } = useImgFallback()

const currentSlide = ref(0)
let autoSlide = null

const banners = [
  {
    bg: 'linear-gradient(135deg, #0a0a0a 0%, #1c1c2e 60%, #252545 100%)',
    sub: '2025 S/S NEW COLLECTION',
    title: '봄을 입다,\n무신사 스탠다드',
    desc: '헤비 코튼부터 린넨 블렌드까지\n합리적인 가격의 일상 필수 아이템',
    cta: '상의 컬렉션 보러가기',
    link: '/category/TOP',
    badge: 'NEW',
    highlight: '최대 30% 쿠폰',
    img: 'https://picsum.photos/seed/bnr001/500/620'
  },
  {
    bg: 'linear-gradient(135deg, #0f0f1a 0%, #1a1035 60%, #2a1a4a 100%)',
    sub: '2025 SNEAKER DROP',
    title: '이번 시즌\n필수 스니커즈',
    desc: '나이키 덩크, 아디다스 삼바, 뉴발란스 990\n지금 바로 만나보세요',
    cta: '신발 전체보기',
    link: '/category/SHOES',
    badge: 'HOT',
    highlight: '인기 스니커즈 총집합',
    img: 'https://picsum.photos/seed/bnr002/500/620'
  },
  {
    bg: 'linear-gradient(135deg, #1a0a0a 0%, #2d1010 60%, #3d1515 100%)',
    sub: 'OUTLET SPECIAL SALE',
    title: '최대 50% OFF\n아울렛 특가',
    desc: '노스페이스·나이키·아디다스 시즌 오프\n이월 상품 한정 수량 특가 판매',
    cta: '아울렛 전체보기',
    link: '/category/OUTLET',
    badge: 'SALE',
    highlight: '최대 50% 할인',
    img: 'https://picsum.photos/seed/bnr003/500/620'
  },
  {
    bg: 'linear-gradient(135deg, #0a1a0f 0%, #102a18 60%, #1a3a24 100%)',
    sub: 'STREET BRAND PICK',
    title: '지금 뜨는\n스트리트 브랜드',
    desc: '커버낫, 아더에러, 칼하트, 브레인데드\n국내외 스트리트 씬의 핵심 아이템',
    cta: '아우터 보러가기',
    link: '/category/OUTER',
    badge: 'PICK',
    highlight: '에디터 큐레이션',
    img: 'https://picsum.photos/seed/bnr004/500/620'
  },
  {
    bg: 'linear-gradient(135deg, #1a1205 0%, #2a1e08 60%, #3a2a0e 100%)',
    sub: 'ACCESSORY SEASON',
    title: '완성하는 한 가지\n악세사리 컬렉션',
    desc: 'MLB 볼캡, 뉴에라 피티드, 무신사 스탠다드 삭스\n데일리 룩의 마무리',
    cta: '악세사리 보러가기',
    link: '/category/ACC',
    badge: 'NEW',
    highlight: '시즌 신상 대거 입고',
    img: 'https://picsum.photos/seed/bnr005/500/620'
  }
]

const quickCategories = [
  { name: 'OUTER', label: '아우터', icon: '🧥' },
  { name: 'TOP', label: '상의', icon: '👕' },
  { name: 'PANTS', label: '하의', icon: '👖' },
  { name: 'SHOES', label: '신발', icon: '👟' },
  { name: 'BAG', label: '가방', icon: '👜' },
  { name: 'ACC', label: '악세사리', icon: '🕶️' },
  { name: 'OUTLET', label: '아울렛', icon: '🏷️' },
]

const loading = ref(false)
const newLoading = ref(false)
const recLoading = ref(false)
const outerLoading = ref(false)
const shoesLoading = ref(false)
const hitItems = ref([])
const newItems = ref([])
const recItems = ref([])
const outerItems = ref([])
const shoesItems = ref([])

function formatPrice(price) {
  return price?.toLocaleString('ko-KR') + '원'
}

function nextSlide() {
  currentSlide.value = (currentSlide.value + 1) % banners.length
}
function prevSlide() {
  currentSlide.value = (currentSlide.value - 1 + banners.length) % banners.length
}
function goToSlide(i) {
  currentSlide.value = i
}

async function fetchItems() {
  loading.value = true
  try {
    const res = await fetch('/v1/api/items?sort=best')
    const data = await res.json()
    hitItems.value = data.slice(0, 4)
  } catch (e) {
    console.error('상품 조회 실패', e)
  } finally {
    loading.value = false
  }
}

async function fetchNew() {
  newLoading.value = true
  try {
    const res = await fetch('/v1/api/items?sort=new')
    newItems.value = await res.json()
  } catch (e) {
    console.error(e)
  } finally {
    newLoading.value = false
  }
}

async function fetchRecommend() {
  recLoading.value = true
  try {
    const res = await fetch('/v1/api/items?sort=recommend')
    recItems.value = await res.json()
  } catch (e) {
    console.error(e)
  } finally {
    recLoading.value = false
  }
}

async function fetchOuter() {
  outerLoading.value = true
  try {
    const res = await fetch('/v1/api/items?category=OUTER', { credentials: 'include' })
    const data = await res.json()
    outerItems.value = data.slice(0, 5)
  } catch (e) {
    console.error(e)
  } finally {
    outerLoading.value = false
  }
}

async function fetchShoes() {
  shoesLoading.value = true
  try {
    const res = await fetch('/v1/api/items?category=SHOES', { credentials: 'include' })
    const data = await res.json()
    shoesItems.value = data.slice(0, 5)
  } catch (e) {
    console.error(e)
  } finally {
    shoesLoading.value = false
  }
}

onMounted(() => {
  fetchItems()
  fetchNew()
  fetchRecommend()
  fetchOuter()
  fetchShoes()
  autoSlide = setInterval(nextSlide, 4000)
})

onUnmounted(() => {
  if (autoSlide) clearInterval(autoSlide)
})
</script>
