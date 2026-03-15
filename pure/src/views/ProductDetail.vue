<template>
  <div class="product-detail-page" v-if="item">
    <div class="product-detail-container">
      <!-- 이미지 영역 -->
      <div class="product-detail-image">
        <img :src="item.imgPath" :alt="item.name" />
        <div v-if="item.isSoldOut" class="soldout-badge">SOLD OUT</div>
        <div v-if="item.discountPer > 0" class="discount-badge">{{ item.discountPer }}% OFF</div>
      </div>

      <!-- 상품 정보 -->
      <div class="product-detail-info">
        <p class="product-brand">{{ item.brand }}</p>
        <h1 class="product-name">{{ item.name }}</h1>
        <div class="product-price-area">
          <span v-if="item.discountPer > 0" class="price-original">{{ item.price?.toLocaleString() }}원</span>
          <span class="price-sale">{{ item.salePrice?.toLocaleString() }}원</span>
          <span v-if="item.discountPer > 0" class="price-discount-badge">{{ item.discountPer }}%</span>
        </div>
        <p class="product-stock" :class="{ 'stock-low': item.stockCount <= 5 }">
          재고: {{ item.stockCount > 0 ? item.stockCount + '개' : '품절' }}
        </p>

        <!-- 수량 선택 -->
        <div class="quantity-selector">
          <button @click="qty = Math.max(1, qty - 1)">-</button>
          <span>{{ qty }}</span>
          <button @click="qty = Math.min(item.stockCount || 1, qty + 1)">+</button>
        </div>

        <!-- 버튼 -->
        <div class="product-actions">
          <button class="btn-cart" @click="addToCart" :disabled="item.isSoldOut">장바구니 담기</button>
          <button class="btn-order" @click="openOrderModal" :disabled="item.isSoldOut">바로 구매</button>
        </div>

        <div class="product-meta">
          <p v-if="item.description" class="product-description">{{ item.description }}</p>
        </div>
      </div>
    </div>

    <!-- 탭 (상품설명 / 리뷰 / Q&A) -->
    <div class="product-tabs">
      <div class="tab-header">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          class="tab-btn"
          :class="{ active: activeTab === tab.key }"
          @click="activeTab = tab.key"
        >
          {{ tab.label }}
          <span v-if="tab.key === 'review'" class="tab-count">({{ reviews.length }})</span>
          <span v-if="tab.key === 'qna'" class="tab-count">({{ questions.length }})</span>
        </button>
      </div>

      <!-- 상품 설명 -->
      <div v-if="activeTab === 'desc'" class="tab-content">
        <div class="product-desc-full">
          <img :src="item.imgPath" :alt="item.name" style="width:100%;max-width:600px;display:block;margin:0 auto 20px;" />
          <p>{{ item.description || '상품 설명이 없습니다.' }}</p>
          <ul class="product-spec-list">
            <li><strong>브랜드</strong> {{ item.brand }}</li>
            <li><strong>카테고리</strong> {{ item.category }}</li>
            <li><strong>정가</strong> {{ item.price?.toLocaleString() }}원</li>
            <li><strong>할인율</strong> {{ item.discountPer }}%</li>
          </ul>
        </div>
      </div>

      <!-- 리뷰 -->
      <div v-if="activeTab === 'review'" class="tab-content">
        <!-- 리뷰 작성 폼 -->
        <div v-if="isLoggedIn" class="review-write-box">
          <h4>리뷰 작성</h4>
          <div class="star-input">
            <button
              v-for="n in 5"
              :key="n"
              class="star-btn"
              :class="{ filled: n <= reviewForm.rating }"
              @click="reviewForm.rating = n"
            >★</button>
          </div>
          <textarea v-model="reviewForm.content" class="review-textarea" placeholder="솔직한 리뷰를 작성해주세요..." rows="3" />
          <button class="btn-submit-sm" @click="submitReview">등록</button>
        </div>

        <div v-if="reviews.length === 0" class="empty-list">아직 리뷰가 없습니다.</div>
        <div v-for="r in reviews" :key="r.id" class="review-item">
          <div class="review-header">
            <span class="review-author">{{ r.memberName }}</span>
            <span class="review-rating">{{ '★'.repeat(r.rating) }}{{ '☆'.repeat(5 - r.rating) }}</span>
            <span class="review-date">{{ formatDate(r.createdAt) }}</span>
            <button v-if="r.memberId === currentMemberId" class="btn-del" @click="deleteReview(r.id)">삭제</button>
          </div>
          <p class="review-content">{{ r.content }}</p>
        </div>
      </div>

      <!-- Q&A -->
      <div v-if="activeTab === 'qna'" class="tab-content">
        <div v-if="isLoggedIn" class="qna-write-box">
          <h4>문의 작성</h4>
          <input v-model="qnaForm.title" class="form-input" placeholder="문의 제목" />
          <textarea v-model="qnaForm.content" class="review-textarea" placeholder="문의 내용을 입력해주세요..." rows="3" />
          <label class="check-label">
            <input type="checkbox" v-model="qnaForm.isSecret" /> 비밀글
          </label>
          <button class="btn-submit-sm" @click="submitQna">등록</button>
        </div>

        <div v-if="questions.length === 0" class="empty-list">아직 문의가 없습니다.</div>
        <div v-for="q in questions" :key="q.id" class="qna-item">
          <div class="qna-header">
            <span class="qna-secret" v-if="q.isSecret">🔒</span>
            <span class="qna-title">{{ q.title }}</span>
            <span class="qna-author">{{ q.memberName }}</span>
            <span class="qna-date">{{ formatDate(q.createdAt) }}</span>
            <span class="qna-status" :class="q.isAnswered ? 'answered' : 'pending'">
              {{ q.isAnswered ? '답변완료' : '답변대기' }}
            </span>
            <button v-if="q.memberId === currentMemberId" class="btn-del" @click="deleteQna(q.id)">삭제</button>
          </div>
          <p class="qna-content">{{ q.content }}</p>
          <div v-if="q.isAnswered && q.answerContent" class="qna-answer">
            <strong>답변:</strong> {{ q.answerContent }}
          </div>
        </div>
      </div>
    </div>

    <!-- 연관 상품 -->
    <div class="related-section" v-if="related.length > 0">
      <h3 class="section-title">연관 상품</h3>
      <div class="product-grid-4">
        <div
          v-for="p in related"
          :key="p.id"
          class="product-card"
          @click="$router.push('/product/' + p.id)"
        >
          <div class="product-card-img">
            <img :src="p.imgPath" :alt="p.name" />
          </div>
          <div class="product-card-body">
            <p class="card-brand">{{ p.brand }}</p>
            <p class="card-name">{{ p.name }}</p>
            <p class="card-price">{{ p.salePrice?.toLocaleString() }}원</p>
          </div>
        </div>
      </div>
    </div>

    <!-- 주문 모달 -->
    <div v-if="showOrderModal" class="modal-overlay" @click.self="showOrderModal = false">
      <div class="modal-box">
        <h3>주문하기</h3>
        <div class="form-group">
          <label class="form-label">받는 분 이름</label>
          <input v-model="orderForm.name" class="form-input" placeholder="이름" />
        </div>
        <div class="form-group">
          <label class="form-label">배송 주소</label>
          <input v-model="orderForm.address" class="form-input" placeholder="주소" />
        </div>
        <div class="form-group">
          <label class="form-label">결제 수단</label>
          <select v-model="orderForm.payment" class="form-input">
            <option value="CARD">신용카드</option>
            <option value="KAKAO">카카오페이</option>
            <option value="NAVER">네이버페이</option>
            <option value="TOSS">토스페이</option>
          </select>
        </div>
        <div v-if="orderForm.payment === 'CARD'" class="form-group">
          <label class="form-label">카드 번호</label>
          <input v-model="orderForm.cardNumber" class="form-input" placeholder="0000-0000-0000-0000" maxlength="19" />
        </div>
        <div class="order-summary">
          <p>상품: {{ item.name }}</p>
          <p>수량: {{ qty }}개</p>
          <p class="order-total">결제금액: <strong>{{ (item.salePrice * qty).toLocaleString() }}원</strong></p>
        </div>
        <div class="modal-actions">
          <button class="btn-cancel" @click="showOrderModal = false">취소</button>
          <button class="btn-confirm" @click="submitOrder">결제하기</button>
        </div>
      </div>
    </div>

    <!-- 토스트 -->
    <div v-if="toast" class="toast-msg">{{ toast }}</div>
  </div>

  <div v-else class="loading-page">
    <p v-if="notFound">상품을 찾을 수 없습니다.</p>
    <p v-else>로딩 중...</p>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { useAuth } from '../composables/useAuth.js'

const route = useRoute()
const { isLoggedIn } = useAuth()

const item = ref(null)
const related = ref([])
const reviews = ref([])
const questions = ref([])
const activeTab = ref('desc')
const qty = ref(1)
const notFound = ref(false)
const toast = ref('')
const showOrderModal = ref(false)
const currentMemberId = ref(null)

const tabs = [
  { key: 'desc', label: '상품설명' },
  { key: 'review', label: '리뷰' },
  { key: 'qna', label: 'Q&A' },
]

const reviewForm = ref({ rating: 5, content: '' })
const qnaForm = ref({ title: '', content: '', isSecret: false })
const orderForm = ref({ name: '', address: '', payment: 'CARD', cardNumber: '' })

async function loadItem(id) {
  try {
    const res = await fetch(`/v1/api/items/${id}`)
    if (!res.ok) { notFound.value = true; return }
    item.value = await res.json()
    loadRelated(id)
    loadReviews(id)
    loadQna(id)
  } catch { notFound.value = true }
}

async function loadRelated(id) {
  try {
    const res = await fetch(`/v1/api/items/${id}/related`)
    if (res.ok) related.value = await res.json()
  } catch {}
}

async function loadReviews(id) {
  try {
    const res = await fetch(`/v1/api/reviews/item/${id}`)
    if (res.ok) reviews.value = await res.json()
  } catch {}
}

async function loadQna(id) {
  try {
    const res = await fetch(`/v1/api/qna/item/${id}`, { credentials: 'include' })
    if (res.ok) questions.value = await res.json()
  } catch {}
}

async function loadCurrentMember() {
  try {
    const res = await fetch('/v1/api/account/profile', { credentials: 'include' })
    if (res.ok) {
      const data = await res.json()
      currentMemberId.value = data.id
    }
  } catch {}
}

async function addToCart() {
  if (!isLoggedIn.value) { showToast('로그인이 필요합니다.'); return }
  const res = await fetch('/v1/api/carts', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ itemId: item.value.id })
  })
  if (res.ok) showToast('장바구니에 추가되었습니다.')
  else if (res.status === 409) showToast('이미 장바구니에 있습니다.')
  else showToast('오류가 발생했습니다.')
}

function openOrderModal() {
  if (!isLoggedIn.value) { showToast('로그인이 필요합니다.'); return }
  showOrderModal.value = true
}

async function submitOrder() {
  if (!orderForm.value.name || !orderForm.value.address) {
    showToast('이름과 주소를 입력해주세요.'); return
  }
  const res = await fetch('/v1/api/orders', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({
      itemIds: [item.value.id],
      name: orderForm.value.name,
      address: orderForm.value.address,
      payment: orderForm.value.payment,
      cardNumber: orderForm.value.cardNumber
    })
  })
  if (res.ok) {
    showOrderModal.value = false
    showToast('주문이 완료되었습니다!')
  } else showToast('주문 처리 중 오류가 발생했습니다.')
}

async function submitReview() {
  if (!reviewForm.value.content) { showToast('리뷰 내용을 입력해주세요.'); return }
  const res = await fetch('/v1/api/reviews', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ itemId: item.value.id, ...reviewForm.value })
  })
  if (res.ok) {
    reviewForm.value = { rating: 5, content: '' }
    loadReviews(route.params.id)
    showToast('리뷰가 등록되었습니다!')
  } else showToast('리뷰 등록에 실패했습니다.')
}

async function deleteReview(id) {
  if (!confirm('리뷰를 삭제하시겠습니까?')) return
  const res = await fetch(`/v1/api/reviews/${id}`, { method: 'DELETE', credentials: 'include' })
  if (res.ok) { loadReviews(route.params.id); showToast('삭제되었습니다.') }
}

async function submitQna() {
  if (!qnaForm.value.title || !qnaForm.value.content) { showToast('제목과 내용을 입력해주세요.'); return }
  const res = await fetch('/v1/api/qna', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify({ itemId: item.value.id, ...qnaForm.value })
  })
  if (res.ok) {
    qnaForm.value = { title: '', content: '', isSecret: false }
    loadQna(route.params.id)
    showToast('문의가 등록되었습니다!')
  } else showToast('문의 등록에 실패했습니다.')
}

async function deleteQna(id) {
  if (!confirm('문의를 삭제하시겠습니까?')) return
  const res = await fetch(`/v1/api/qna/${id}`, { method: 'DELETE', credentials: 'include' })
  if (res.ok) { loadQna(route.params.id); showToast('삭제되었습니다.') }
}

function showToast(msg) {
  toast.value = msg
  setTimeout(() => { toast.value = '' }, 2500)
}

function formatDate(dt) {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('ko-KR')
}

onMounted(() => {
  loadItem(route.params.id)
  if (isLoggedIn.value) loadCurrentMember()
})

watch(() => route.params.id, (id) => {
  if (id) loadItem(id)
})
</script>
