<template>
  <div class="mypage">
    <div class="mypage-container">
      <aside class="mypage-sidebar">
        <div class="mypage-user-info">
          <div class="mypage-avatar">{{ (profile?.name || 'U')[0] }}</div>
          <p class="mypage-username">{{ profile?.name || '로딩 중...' }}</p>
          <p class="mypage-email">{{ profile?.loginId }}</p>
          <span class="grade-badge" :class="`grade-${profile?.grade?.toLowerCase()}`">{{ profile?.grade }}</span>
        </div>
        <nav class="mypage-nav">
          <button v-for="tab in tabs" :key="tab.key" class="mypage-nav-btn"
            :class="{ active: activeTab === tab.key }" @click="activeTab = tab.key">
            {{ tab.label }}
          </button>
          <button class="mypage-nav-btn logout-btn" @click="handleLogout">로그아웃</button>
        </nav>
      </aside>

      <main class="mypage-main">
        <!-- 주문 내역 -->
        <section v-if="activeTab === 'orders'">
          <h2 class="mypage-section-title">주문 내역</h2>
          <div v-if="ordersLoading" class="loading-box"><div class="spinner"></div></div>
          <div v-else-if="orders.length === 0" class="empty-list">주문 내역이 없습니다.</div>
          <div v-else class="order-list">
            <div v-for="order in orders" :key="order.id" class="order-card">
              <div class="order-card-header">
                <span class="order-id">#{{ order.id }}</span>
                <span class="order-date">{{ formatDate(order.createdAt) }}</span>
                <span class="order-status" :class="`status-${order.status?.toLowerCase()}`">
                  {{ order.status === 'PAID' ? '결제완료' : order.status === 'CANCELLED' ? '취소됨' : order.status }}
                </span>
              </div>
              <div class="order-card-body">
                <p>수령인: {{ order.name }}</p>
                <p>결제금액: <strong>{{ order.amount?.toLocaleString() }}원</strong></p>
                <p>결제수단: {{ order.payment }}</p>
              </div>
              <div class="order-card-footer">
                <button v-if="order.status === 'PAID'" class="btn-cancel-order" @click="cancelOrder(order.id)">주문 취소</button>
              </div>
            </div>
          </div>
        </section>

        <!-- 내 리뷰 -->
        <section v-if="activeTab === 'reviews'">
          <h2 class="mypage-section-title">내 리뷰</h2>
          <div v-if="reviews.length === 0" class="empty-list">작성한 리뷰가 없습니다.</div>
          <div v-else class="review-list">
            <div v-for="r in reviews" :key="r.id" class="review-item">
              <div class="review-header">
                <span class="review-rating">{{ '★'.repeat(r.rating) }}{{ '☆'.repeat(5 - r.rating) }}</span>
                <span class="review-date">{{ formatDate(r.createdAt) }}</span>
                <button class="btn-del" @click="deleteReview(r.id)">삭제</button>
              </div>
              <p class="review-content">{{ r.content }}</p>
            </div>
          </div>
        </section>

        <!-- 내 Q&A -->
        <section v-if="activeTab === 'qna'">
          <h2 class="mypage-section-title">내 Q&A</h2>
          <div v-if="questions.length === 0" class="empty-list">작성한 문의가 없습니다.</div>
          <div v-else class="qna-list">
            <div v-for="q in questions" :key="q.id" class="qna-item">
              <div class="qna-header">
                <span class="qna-title">{{ q.title }}</span>
                <span class="qna-status" :class="q.isAnswered ? 'answered' : 'pending'">
                  {{ q.isAnswered ? '답변완료' : '답변대기' }}
                </span>
                <span class="qna-date">{{ formatDate(q.createdAt) }}</span>
                <button class="btn-del" @click="deleteQna(q.id)">삭제</button>
              </div>
              <p class="qna-content">{{ q.content }}</p>
              <div v-if="q.isAnswered && q.answerContent" class="qna-answer">
                <strong>답변:</strong> {{ q.answerContent }}
              </div>
            </div>
          </div>
        </section>

        <!-- 내 정보 수정 -->
        <section v-if="activeTab === 'profile'">
          <h2 class="mypage-section-title">내 정보 수정</h2>
          <form class="profile-form" @submit.prevent="updateProfile">
            <div class="form-group">
              <label class="form-label">이름</label>
              <input v-model="profileForm.name" class="form-input" type="text" />
            </div>
            <div class="form-group">
              <label class="form-label">연락처</label>
              <input v-model="profileForm.phone" class="form-input" type="tel" placeholder="010-0000-0000" />
            </div>
            <div class="form-group">
              <label class="form-label">주소</label>
              <input v-model="profileForm.address" class="form-input" type="text" />
            </div>
            <hr class="form-divider" />
            <h4>비밀번호 변경</h4>
            <div class="form-group">
              <label class="form-label">현재 비밀번호</label>
              <input v-model="profileForm.currentPw" class="form-input" type="password" />
            </div>
            <div class="form-group">
              <label class="form-label">새 비밀번호</label>
              <input v-model="profileForm.newPw" class="form-input" type="password" />
            </div>
            <p v-if="profileMsg" class="form-msg" :class="profileMsgType">{{ profileMsg }}</p>
            <button type="submit" class="btn-submit">저장</button>
          </form>
        </section>

        <!-- 회원 탈퇴 -->
        <section v-if="activeTab === 'withdraw'">
          <h2 class="mypage-section-title">회원 탈퇴</h2>
          <div class="withdraw-box">
            <p class="withdraw-warning">⚠️ 탈퇴 후에는 계정 복구가 불가능합니다.</p>
            <div class="form-group">
              <label class="form-label">비밀번호 확인</label>
              <input v-model="withdrawPw" class="form-input" type="password" placeholder="현재 비밀번호 입력" />
            </div>
            <p v-if="withdrawMsg" class="form-msg error">{{ withdrawMsg }}</p>
            <button class="btn-withdraw" @click="handleWithdraw">탈퇴하기</button>
          </div>
        </section>
      </main>
    </div>
    <div v-if="toast" class="toast-msg">{{ toast }}</div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth.js'

const router = useRouter()
const { clearLogin } = useAuth()

const activeTab = ref('orders')
const tabs = [
  { key: 'orders', label: '주문 내역' },
  { key: 'reviews', label: '내 리뷰' },
  { key: 'qna', label: '내 Q&A' },
  { key: 'profile', label: '내 정보 수정' },
  { key: 'withdraw', label: '회원 탈퇴' },
]

const profile = ref(null)
const orders = ref([])
const reviews = ref([])
const questions = ref([])
const ordersLoading = ref(false)
const profileForm = ref({ name: '', phone: '', address: '', currentPw: '', newPw: '' })
const profileMsg = ref('')
const profileMsgType = ref('success')
const withdrawPw = ref('')
const withdrawMsg = ref('')
const toast = ref('')

function showToast(msg) {
  toast.value = msg
  setTimeout(() => { toast.value = '' }, 2500)
}
function formatDate(dt) {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('ko-KR')
}

async function loadProfile() {
  try {
    const res = await fetch('/v1/api/account/profile', { credentials: 'include' })
    if (res.ok) {
      profile.value = await res.json()
      profileForm.value.name = profile.value.name || ''
      profileForm.value.phone = profile.value.phone || ''
      profileForm.value.address = profile.value.address || ''
    }
  } catch {}
}

async function loadOrders() {
  ordersLoading.value = true
  try {
    const res = await fetch('/v1/api/orders', { credentials: 'include' })
    if (res.ok) orders.value = await res.json()
  } catch {} finally { ordersLoading.value = false }
}

async function loadReviews() {
  try {
    const res = await fetch('/v1/api/reviews/my', { credentials: 'include' })
    if (res.ok) reviews.value = await res.json()
  } catch {}
}

async function loadQna() {
  try {
    const res = await fetch('/v1/api/qna/my', { credentials: 'include' })
    if (res.ok) questions.value = await res.json()
  } catch {}
}

async function cancelOrder(id) {
  if (!confirm('주문을 취소하시겠습니까?')) return
  const res = await fetch(`/v1/api/orders/${id}/cancel`, { method: 'PATCH', credentials: 'include' })
  if (res.ok) { loadOrders(); showToast('주문이 취소되었습니다.') }
  else showToast('취소 처리 중 오류가 발생했습니다.')
}

async function deleteReview(id) {
  if (!confirm('리뷰를 삭제하시겠습니까?')) return
  const res = await fetch(`/v1/api/reviews/${id}`, { method: 'DELETE', credentials: 'include' })
  if (res.ok) { loadReviews(); showToast('삭제되었습니다.') }
}

async function deleteQna(id) {
  if (!confirm('문의를 삭제하시겠습니까?')) return
  const res = await fetch(`/v1/api/qna/${id}`, { method: 'DELETE', credentials: 'include' })
  if (res.ok) { loadQna(); showToast('삭제되었습니다.') }
}

async function updateProfile() {
  profileMsg.value = ''
  const body = {}
  if (profileForm.value.name) body.name = profileForm.value.name
  if (profileForm.value.phone) body.phone = profileForm.value.phone
  if (profileForm.value.address) body.address = profileForm.value.address
  if (profileForm.value.newPw) {
    body.currentPw = profileForm.value.currentPw
    body.newPw = profileForm.value.newPw
  }
  const res = await fetch('/v1/api/account/profile', {
    method: 'PUT',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(body)
  })
  if (res.ok) {
    profileMsg.value = '저장되었습니다.'
    profileMsgType.value = 'success'
    loadProfile()
    profileForm.value.currentPw = ''
    profileForm.value.newPw = ''
  } else {
    profileMsg.value = (await res.text()) || '저장에 실패했습니다.'
    profileMsgType.value = 'error'
  }
}

async function handleWithdraw() {
  withdrawMsg.value = ''
  if (!withdrawPw.value) { withdrawMsg.value = '비밀번호를 입력해주세요.'; return }
  if (!confirm('정말 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) return
  const res = await fetch(`/v1/api/account/withdraw?password=${encodeURIComponent(withdrawPw.value)}`, {
    method: 'DELETE',
    credentials: 'include'
  })
  if (res.ok) {
    clearLogin()
    router.push('/')
  } else {
    withdrawMsg.value = (await res.text()) || '탈퇴 처리 중 오류가 발생했습니다.'
  }
}

async function handleLogout() {
  try {
    await fetch('/v1/api/account/logout', { method: 'POST', credentials: 'include' })
  } catch {}
  clearLogin()
  router.push('/')
}

onMounted(() => {
  loadProfile()
  loadOrders()
  loadReviews()
  loadQna()
})
</script>
