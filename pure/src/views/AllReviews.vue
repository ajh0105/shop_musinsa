<template>
  <div class="board-page">
    <div class="board-hero">
      <div class="container">
        <h1 class="board-hero-title">전체 리뷰</h1>
        <p class="board-hero-desc">고객님들의 생생한 리뷰를 확인하세요.</p>
      </div>
    </div>

    <div class="container board-container">
      <!-- 정렬 / 필터 -->
      <div class="board-toolbar">
        <div class="review-stats" v-if="reviews.length > 0">
          총 <strong>{{ reviews.length }}</strong>개의 리뷰
        </div>
        <div class="board-sort">
          <select v-model="sortBy" class="sort-select">
            <option value="latest">최신순</option>
            <option value="rating_high">별점 높은순</option>
            <option value="rating_low">별점 낮은순</option>
          </select>
          <select v-model="filterRating" class="sort-select">
            <option value="0">전체 별점</option>
            <option value="5">★★★★★</option>
            <option value="4">★★★★</option>
            <option value="3">★★★</option>
            <option value="2">★★</option>
            <option value="1">★</option>
          </select>
        </div>
      </div>

      <div v-if="loading" class="loading-box"><div class="spinner"></div></div>

      <div v-else-if="sortedReviews.length === 0" class="empty-box">
        <p>{{ filterRating !== '0' ? '해당 별점의 리뷰가 없습니다.' : '등록된 리뷰가 없습니다.' }}</p>
      </div>

      <div v-else class="review-board-list">
        <div v-for="r in sortedReviews" :key="r.id" class="review-board-card">
          <div class="review-board-header">
            <div class="review-rating">{{ '★'.repeat(r.rating) }}<span class="rating-empty">{{ '★'.repeat(5 - r.rating) }}</span></div>
            <div class="review-meta">
              <span class="review-author">{{ r.memberName }}</span>
              <span class="review-date">{{ formatDate(r.createdAt) }}</span>
            </div>
          </div>
          <p class="review-content">{{ r.content }}</p>
          <div v-if="r.itemId" class="review-item-link">
            <RouterLink :to="`/product/${r.itemId}`" class="review-product-link">상품 보기 →</RouterLink>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const reviews = ref([])
const loading = ref(true)
const sortBy = ref('latest')
const filterRating = ref('0')

const sortedReviews = computed(() => {
  let list = [...reviews.value]
  if (filterRating.value !== '0') {
    list = list.filter(r => r.rating === Number(filterRating.value))
  }
  if (sortBy.value === 'rating_high') list.sort((a, b) => b.rating - a.rating)
  else if (sortBy.value === 'rating_low') list.sort((a, b) => a.rating - b.rating)
  else list.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
  return list
})

function formatDate(dt) {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('ko-KR')
}

async function loadReviews() {
  try {
    const res = await fetch('/v1/api/reviews')
    if (res.ok) reviews.value = await res.json()
  } catch {} finally {
    loading.value = false
  }
}

onMounted(loadReviews)
</script>

<style scoped>
.board-page { min-height: 60vh; }

.board-hero {
  background: var(--color-primary);
  color: #fff;
  padding: 48px 0 40px;
  margin-bottom: 40px;
}
.board-hero-title { font-size: 2rem; font-weight: 800; margin-bottom: 8px; }
.board-hero-desc { font-size: 0.95rem; opacity: 0.8; }

.board-container { padding-bottom: 80px; }

.board-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-wrap: wrap;
  gap: 12px;
}
.review-stats { font-size: 0.9rem; color: var(--color-text-muted); }
.review-stats strong { color: var(--color-text); }

.board-sort { display: flex; gap: 8px; }
.sort-select {
  padding: 7px 12px;
  border: 1px solid var(--color-border-dark);
  border-radius: var(--radius-sm);
  font-size: 0.85rem;
  background: #fff;
  cursor: pointer;
}

.review-board-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

.review-board-card {
  border: 1px solid var(--color-border);
  border-radius: var(--radius-md);
  padding: 20px;
  background: #fff;
  transition: box-shadow var(--transition);
}
.review-board-card:hover { box-shadow: var(--shadow-md); }

.review-board-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.review-rating {
  font-size: 1rem;
  color: #f59e0b;
}
.rating-empty { color: var(--color-border-dark); }

.review-meta {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 2px;
}
.review-author { font-size: 0.85rem; font-weight: 600; }
.review-date { font-size: 0.78rem; color: var(--color-text-muted); }

.review-content {
  font-size: 0.9rem;
  line-height: 1.6;
  color: var(--color-text);
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 4;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.review-item-link { margin-top: 8px; border-top: 1px solid var(--color-border); padding-top: 10px; }
.review-product-link { font-size: 0.82rem; color: #4f8ef7; }
.review-product-link:hover { text-decoration: underline; }
</style>
