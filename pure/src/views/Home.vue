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
          <RouterLink to="/category/SCARVES" class="promo-link">지금 확인 →</RouterLink>
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
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" />
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
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" />
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
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" />
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

    <!-- 스카프 추천 섹션 행 -->
    <section class="category-row-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">스카프 추천</h2>
          <RouterLink to="/category/SCARVES" class="section-more">더보기 →</RouterLink>
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
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" />
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
        <RouterLink to="/category/SALE" class="sale-banner">
          <div class="sale-banner-left">
            <span class="sale-banner-tag">SALE</span>
            <h3 class="sale-banner-title">최대 50% 할인</h3>
            <p class="sale-banner-desc">인기 브랜드 아이템을 특가에 만나보세요</p>
          </div>
          <div class="sale-banner-right">
            <span class="sale-arrow">→</span>
          </div>
        </RouterLink>
      </div>
    </section>

    <!-- 가방 추천 섹션 -->
    <section class="category-row-section">
      <div class="container">
        <div class="section-header">
          <h2 class="section-title">가방 추천</h2>
          <RouterLink to="/category/BAG" class="section-more">더보기 →</RouterLink>
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
              <img :src="item.imgPath" :alt="item.name" class="product-img" loading="lazy" />
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

const currentSlide = ref(0)
let autoSlide = null

const banners = [
  {
    bg: 'linear-gradient(135deg, #1a0e1f 0%, #2d1b38 60%, #3d2550 100%)',
    sub: '2025 S/S NEW COLLECTION',
    title: '봄을 감싸다,\nVENTALIZE 스카프',
    desc: '순수 실크부터 캐시미어까지\n계절을 아름답게 완성하는 스카프',
    cta: 'SCARVES 보러가기',
    link: '/category/SCARVES',
    badge: 'NEW',
    highlight: '최대 20% 할인',
    img: 'https://images.unsplash.com/photo-1590736704728-f4730bb30770?w=600'
  },
  {
    bg: 'linear-gradient(135deg, #0f1a1a 0%, #1a2d2d 60%, #253d3d 100%)',
    sub: '2025 S/S READY TO WEAR',
    title: '우아한 일상을\n입다',
    desc: '실크 블라우스, 울 재킷, 플리츠 스커트\n벙딸리제의 기성복 컬렉션',
    cta: 'READY TO WEAR 보러가기',
    link: '/category/READY_TO_WEAR',
    badge: 'HOT',
    highlight: '시즌 신상 입고',
    img: 'https://images.unsplash.com/photo-1594938298603-c8148c4b4cde?w=600'
  },
  {
    bg: 'linear-gradient(135deg, #1a1205 0%, #2d1e08 60%, #3d2d0e 100%)',
    sub: 'VENTALIZE SPECIAL SALE',
    title: '최대 50% OFF\n시즌 오프 특가',
    desc: '지난 시즌 인기 아이템을 특별한 가격에\n한정 수량 특가 판매',
    cta: 'SALE 전체보기',
    link: '/category/SALE',
    badge: 'SALE',
    highlight: '최대 50% 할인',
    img: 'https://images.unsplash.com/photo-1512411600720-d26919c6a9da?w=600'
  }
]

const quickCategories = [
  { name: 'SCARVES',       label: '스카프',     icon: '🧣' },
  { name: 'READY_TO_WEAR', label: '기성복',     icon: '👗' },
  { name: 'PERFUME',       label: '향수',       icon: '🌸' },
  { name: 'ACC',           label: '악세사리',   icon: '💍' },
  { name: 'BAG',           label: '가방',       icon: '👜' },
  { name: 'SALE',          label: '세일',       icon: '🏷️' },
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
    const res = await fetch('/v1/api/items?category=SCARVES', { credentials: 'include' })
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
    const res = await fetch('/v1/api/items?category=BAG', { credentials: 'include' })
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
