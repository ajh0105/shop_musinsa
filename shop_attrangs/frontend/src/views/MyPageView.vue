<template>
  <main class="page-wrap">
    <div class="section-head">
      <h2>마이페이지</h2>
    </div>

    <div v-if="!isLoggedIn" class="empty">
      로그인이 필요합니다. <router-link to="/login">로그인</router-link>
    </div>

    <div v-else>
      <div class="mypage-welcome">
        <span class="welcome-name">{{ userName }}</span> 님, 환영합니다.
        <span v-if="profile" class="welcome-grade grade-{{ profile.grade?.toLowerCase() }}">{{ gradeLabel(profile.grade) }}</span>
      </div>

      <!-- 탭 -->
      <div class="mypage-tabs">
        <button v-for="tab in tabs" :key="tab.key"
          class="mypage-tab" :class="{ active: activeTab === tab.key }"
          @click="switchTab(tab.key)">
          {{ tab.label }}
        </button>
      </div>

      <!-- ── 주문 내역 ── -->
      <div v-if="activeTab === 'orders'">
        <div v-if="orders.length === 0" class="empty">주문 내역이 없습니다.</div>
        <div v-else class="order-table-wrap">
          <!-- 리스트 헤더 -->
          <div class="order-table-head">
            <span class="ot-id">주문번호</span>
            <span class="ot-date">주문일</span>
            <span class="ot-summary">대표 상품</span>
            <span class="ot-amount">결제금액</span>
            <span class="ot-status">상태</span>
            <span class="ot-arrow"></span>
          </div>

          <!-- 리스트 행 + 클릭 시 상세 카드 펼침 -->
          <div v-for="order in orders" :key="order.id" class="order-row-wrap">
            <!-- 리스트 행 -->
            <div class="order-table-row" @click="toggleOrder(order.id)"
              :class="{ expanded: openOrderId === order.id }">
              <span class="ot-id">#{{ order.id }}</span>
              <span class="ot-date">{{ formatDate(order.createdAt) }}</span>
              <span class="ot-summary">
                {{ order.items?.[0]?.name || '-' }}
                <em v-if="order.items?.length > 1"> 외 {{ order.items.length - 1 }}건</em>
              </span>
              <span class="ot-amount">{{ Number(order.amount).toLocaleString() }}원</span>
              <span class="ot-status">
                <span class="order-status-badge" :class="statusClass(order.status)">{{ statusLabel(order.status) }}</span>
              </span>
              <span class="ot-arrow">{{ openOrderId === order.id ? '▲' : '▼' }}</span>
            </div>

            <!-- 상세 카드 (아코디언) -->
            <Transition name="slide-down">
              <div v-if="openOrderId === order.id" class="order-detail-card">
                <!-- 배송 단계 진행바 -->
                <div class="order-progress">
                  <div v-for="(step, idx) in ORDER_STEPS" :key="step.key"
                    class="order-step"
                    :class="{ active: isStepActive(order.status, idx), done: isStepDone(order.status, idx) }">
                    <div class="step-dot"></div>
                    <span class="step-label">{{ step.label }}</span>
                  </div>
                </div>
                <!-- 주문 상품 목록 -->
                <div class="order-items">
                  <div v-for="item in order.items" :key="item.id" class="order-item-row">
                    <img :src="item.imgPath" :alt="item.name" class="order-thumb" />
                    <div>
                      <p class="order-item-name">{{ item.name }}</p>
                      <p class="order-item-price">{{ Number(item.salePrice ?? item.price).toLocaleString() }}원</p>
                    </div>
                  </div>
                </div>
                <!-- 결제 정보 -->
                <div class="order-footer">
                  <span class="order-payment">{{ paymentLabel(order.payment) }}</span>
                  <span class="order-amount">총 {{ Number(order.amount).toLocaleString() }}원</span>
                </div>
              </div>
            </Transition>
          </div>
        </div>
      </div>

      <!-- ── 프로필 수정 ── -->
      <div v-if="activeTab === 'profile'">
        <div v-if="profile" class="profile-card">
          <div class="profile-section">
            <h4 class="profile-section-title">기본 정보</h4>
            <div class="profile-row">
              <label>이름</label>
              <input v-model="profileForm.name" class="profile-input" />
            </div>
            <div class="profile-row">
              <label>이메일 (아이디)</label>
              <input :value="profile.loginId" class="profile-input" disabled />
            </div>
            <div class="profile-row">
              <label>전화번호</label>
              <input v-model="profileForm.phone" class="profile-input" placeholder="010-0000-0000" />
            </div>
            <div class="profile-row">
              <label>주소</label>
              <input v-model="profileForm.address" class="profile-input" placeholder="배송 주소 입력" />
            </div>
            <div class="profile-row">
              <label>등급</label>
              <span class="profile-value">{{ gradeLabel(profile.grade) }}</span>
            </div>
            <div class="profile-row">
              <label>가입일</label>
              <span class="profile-value">{{ formatDate(profile.createdAt) }}</span>
            </div>
          </div>

          <div class="profile-section">
            <h4 class="profile-section-title">비밀번호 변경</h4>
            <div class="profile-row">
              <label>현재 비밀번호</label>
              <input v-model="profileForm.currentPw" type="password" class="profile-input" placeholder="현재 비밀번호" />
            </div>
            <div class="profile-row">
              <label>새 비밀번호</label>
              <input v-model="profileForm.newPw" type="password" class="profile-input" placeholder="새 비밀번호 (8자 이상)" />
            </div>
            <div class="profile-row">
              <label>새 비밀번호 확인</label>
              <input v-model="profileForm.confirmNewPw" type="password" class="profile-input" placeholder="새 비밀번호 재입력" />
            </div>
          </div>

          <div class="profile-actions">
            <p v-if="profileMsg" class="profile-msg" :class="{ error: profileError }">{{ profileMsg }}</p>
            <button class="btn-profile-save" @click="saveProfile">저장하기</button>
          </div>
        </div>
      </div>

      <!-- ── 내 리뷰 ── -->
      <div v-if="activeTab === 'reviews'">
        <div v-if="myReviews.length === 0" class="empty">작성한 리뷰가 없습니다.</div>
        <div v-else class="review-list">
          <div v-for="r in myReviews" :key="r.id" class="review-card">
            <div class="review-header">
              <span class="review-item-name">{{ r.itemName || '상품' }}</span>
              <span class="review-stars">{{ '★'.repeat(r.rating) }}{{ '☆'.repeat(5 - r.rating) }}</span>
              <span class="review-date">{{ formatDate(r.createdAt) }}</span>
            </div>
            <p class="review-content">{{ r.content }}</p>
          </div>
        </div>
      </div>

      <!-- ── 내 Q&A ── -->
      <div v-if="activeTab === 'qna'">
        <div v-if="myQna.length === 0" class="empty">작성한 Q&A가 없습니다.</div>
        <div v-else class="qna-list">
          <div v-for="q in myQna" :key="q.id" class="qna-card">
            <div class="qna-header" @click="q._open = !q._open">
              <span class="qna-badge" :class="q.isAnswered ? 'answered' : 'pending'">
                {{ q.isAnswered ? '답변완료' : '미답변' }}
              </span>
              <span class="qna-title">{{ q.title }}</span>
              <span class="qna-date">{{ formatDate(q.createdAt) }}</span>
              <span class="qna-toggle">{{ q._open ? '▲' : '▼' }}</span>
            </div>
            <div v-if="q._open" class="qna-body">
              <div class="qna-question-body">
                <strong>Q.</strong> {{ q.content }}
              </div>
              <div v-if="q.isAnswered && q.answerContent" class="qna-answer-body">
                <strong>A.</strong> {{ q.answerContent }}
              </div>
              <div v-else class="qna-no-answer">아직 답변이 등록되지 않았습니다.</div>
            </div>
          </div>
        </div>
      </div>

      <!-- ── 찜 목록 ── -->
      <div v-if="activeTab === 'wishlist'">
        <div v-if="wishItems.length === 0" class="empty">찜한 상품이 없습니다.</div>
        <div v-else class="wish-grid">
          <div v-for="w in wishItems" :key="w.id" class="wish-card" @click="goToItem(w.itemId)">
            <img :src="w.item?.imgPath" :alt="w.item?.name" class="wish-img" />
            <div class="wish-info">
              <p class="wish-brand">{{ w.item?.brand }}</p>
              <p class="wish-name">{{ w.item?.name }}</p>
              <p class="wish-price">
                <span v-if="w.item?.discountPer > 0" class="wish-discount-badge">-{{ w.item.discountPer }}%</span>
                <span v-if="w.item?.salePrice" class="wish-sale-price">{{ Number(w.item.salePrice).toLocaleString() }}원</span>
                <span :class="w.item?.salePrice ? 'wish-original-price' : 'wish-normal-price'">{{ Number(w.item?.price).toLocaleString() }}원</span>
              </p>
            </div>
            <button class="wish-remove" @click.stop="removeWish(w.itemId)">♥ 찜해제</button>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../api'
import { useAuth } from '../composables/useAuth.js'
import { useWishCart } from '../composables/useWishCart.js'

const { isLoggedIn, userName } = useAuth()
const { toggleWish } = useWishCart()
const router = useRouter()

const activeTab = ref('orders')
const tabs = [
  { key: 'orders',   label: '주문 내역' },
  { key: 'profile',  label: '프로필 수정' },
  { key: 'reviews',  label: '내 리뷰' },
  { key: 'qna',      label: '내 Q&A' },
  { key: 'wishlist', label: '찜 목록' },
]

/* ─── 주문 내역 ─── */
const orders = ref([])
const openOrderId = ref(null)
function toggleOrder(id) { openOrderId.value = openOrderId.value === id ? null : id }
const ORDER_STEPS = [
  { key: 'PAYMENT_DONE', label: '결제완료' },
  { key: 'PREPARING',    label: '상품준비중' },
  { key: 'SHIPPING',     label: '배송중' },
  { key: 'DELIVERED',    label: '배송완료' },
  { key: 'CONFIRMED',    label: '구매확정' },
]
const STATUS_ORDER = ORDER_STEPS.map(s => s.key)
function isStepDone(status, idx) { return STATUS_ORDER.indexOf(status) > idx }
function isStepActive(status, idx) { return STATUS_ORDER.indexOf(status) === idx }
function statusLabel(s) {
  const map = { PAYMENT_DONE: '결제완료', PREPARING: '상품준비중', SHIPPING: '배송중', DELIVERED: '배송완료', CONFIRMED: '구매확정' }
  return map[s] || s
}
function statusClass(s) {
  const map = { PAYMENT_DONE: 'st-payment', PREPARING: 'st-preparing', SHIPPING: 'st-shipping', DELIVERED: 'st-delivered', CONFIRMED: 'st-confirmed' }
  return map[s] || ''
}
const PAYMENT_MAP = { CARD: '신용카드', KAKAO: '카카오페이', NAVER: '네이버페이', TOSS: '토스페이' }
const paymentLabel = (p) => PAYMENT_MAP[p] || p

/* ─── 프로필 ─── */
const profile = ref(null)
const profileForm = ref({ name: '', phone: '', address: '', currentPw: '', newPw: '', confirmNewPw: '' })
const profileMsg = ref('')
const profileError = ref(false)

const GRADE_MAP = { BRONZE: '브론즈', SILVER: '실버', GOLD: '골드', VIP: 'VIP' }
function gradeLabel(g) { return GRADE_MAP[g] || g }

/* ─── 리뷰 / Q&A / 찜 ─── */
const myReviews = ref([])
const myQna = ref([])
const wishItems = ref([])

function formatDate(dt) {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('ko-KR')
}

function goToItem(itemId) {
  router.push(`/item/${itemId}`)
}

async function removeWish(itemId) {
  await toggleWish({ id: itemId })
  wishItems.value = wishItems.value.filter(w => w.itemId !== itemId)
}

async function loadOrders() {
  try {
    const { data } = await api.get('/orders')
    orders.value = Array.isArray(data) ? data : []
  } catch { orders.value = [] }
}

async function loadProfile() {
  try {
    const { data } = await api.get('/account/profile')
    profile.value = data
    profileForm.value.name = data.name || ''
    profileForm.value.phone = data.phone || ''
    profileForm.value.address = data.address || ''
  } catch {}
}

async function saveProfile() {
  profileMsg.value = ''
  profileError.value = false
  if (profileForm.value.newPw && profileForm.value.newPw !== profileForm.value.confirmNewPw) {
    profileMsg.value = '새 비밀번호가 일치하지 않습니다.'
    profileError.value = true
    return
  }
  if (profileForm.value.newPw && profileForm.value.newPw.length < 8) {
    profileMsg.value = '새 비밀번호는 8자 이상이어야 합니다.'
    profileError.value = true
    return
  }
  try {
    const payload = { name: profileForm.value.name, phone: profileForm.value.phone, address: profileForm.value.address }
    if (profileForm.value.newPw) {
      payload.currentPw = profileForm.value.currentPw
      payload.newPw = profileForm.value.newPw
    }
    const { data } = await api.put('/account/profile', payload)
    profile.value = data
    profileForm.value.currentPw = ''
    profileForm.value.newPw = ''
    profileForm.value.confirmNewPw = ''
    profileMsg.value = '저장되었습니다.'
  } catch (err) {
    profileMsg.value = err.response?.data || '저장 중 오류가 발생했습니다.'
    profileError.value = true
  }
}

async function loadReviews() {
  try {
    const { data } = await api.get('/reviews/my')
    myReviews.value = Array.isArray(data) ? data : []
  } catch { myReviews.value = [] }
}

async function loadQna() {
  try {
    const { data } = await api.get('/qna/my')
    myQna.value = (Array.isArray(data) ? data : []).map(q => ({ ...q, _open: false }))
  } catch { myQna.value = [] }
}

async function loadWishlist() {
  try {
    const { data } = await api.get('/wishlist/items')
    wishItems.value = Array.isArray(data) ? data : []
  } catch { wishItems.value = [] }
}

function switchTab(key) {
  activeTab.value = key
  if (key === 'orders' && orders.value.length === 0) loadOrders()
  if (key === 'profile' && !profile.value) loadProfile()
  if (key === 'reviews' && myReviews.value.length === 0) loadReviews()
  if (key === 'qna' && myQna.value.length === 0) loadQna()
  if (key === 'wishlist' && wishItems.value.length === 0) loadWishlist()
}

onMounted(() => {
  if (isLoggedIn.value) loadOrders()
})
</script>

<style scoped>
.mypage-welcome {
  font-size: 16px;
  margin-bottom: 20px;
  padding: 16px 20px;
  background: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #eee;
  display: flex;
  align-items: center;
  gap: 10px;
}
.welcome-name { font-weight: 700; }
.welcome-grade {
  font-size: 12px;
  padding: 3px 10px;
  border-radius: 20px;
  background: #111;
  color: #fff;
}

/* ── 탭 ── */
.mypage-tabs {
  display: flex;
  gap: 0;
  border-bottom: 2px solid #eee;
  margin-bottom: 28px;
}
.mypage-tab {
  padding: 10px 20px;
  font-size: 14px;
  font-weight: 600;
  border: none;
  background: none;
  cursor: pointer;
  color: #888;
  border-bottom: 2px solid transparent;
  margin-bottom: -2px;
  transition: color 0.15s, border-color 0.15s;
}
.mypage-tab:hover { color: #333; }
.mypage-tab.active { color: var(--point); border-bottom-color: var(--point); }

/* ── 주문 내역 (테이블 리스트) ── */
.order-table-wrap {
  border: 1px solid #eee;
  border-radius: 12px;
  overflow: hidden;
}
.order-table-head {
  display: grid;
  grid-template-columns: 80px 100px 1fr 120px 110px 32px;
  padding: 12px 20px;
  background: #f5f0eb;
  font-size: 12px;
  font-weight: 700;
  color: #777;
  border-bottom: 1px solid #eee;
}
.order-row-wrap { border-bottom: 1px solid #f0f0f0; }
.order-row-wrap:last-child { border-bottom: none; }

.order-table-row {
  display: grid;
  grid-template-columns: 80px 100px 1fr 120px 110px 32px;
  padding: 14px 20px;
  align-items: center;
  cursor: pointer;
  font-size: 13px;
  color: #444;
  transition: background 0.15s;
}
.order-table-row:hover { background: #fdf8f5; }
.order-table-row.expanded { background: #fdf5f0; }

.ot-id { font-weight: 700; color: #111; font-size: 13px; }
.ot-date { color: #888; font-size: 12px; }
.ot-summary { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; padding-right: 12px; }
.ot-summary em { color: #aaa; font-style: normal; font-size: 12px; }
.ot-amount { font-weight: 700; color: #111; }
.ot-status { }
.ot-arrow { color: #bbb; font-size: 11px; text-align: right; }

.order-status-badge {
  display: inline-block;
  padding: 3px 10px;
  border-radius: 20px;
  font-size: 11px;
  font-weight: 600;
  color: #fff;
}
.st-payment   { background: #888; }
.st-preparing { background: #e57c2a; }
.st-shipping  { background: #2a6ee5; }
.st-delivered { background: #27ae60; }
.st-confirmed { background: #111; }

/* 상세 카드 */
.order-detail-card {
  padding: 20px 24px;
  background: #fafafa;
  border-top: 1px solid #f0ece8;
}

/* 배송 진행바 */
.order-progress {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
  position: relative;
}
.order-progress::before {
  content: '';
  position: absolute;
  top: 9px;
  left: 9px;
  right: 9px;
  height: 2px;
  background: #e0e0e0;
  z-index: 0;
}
.order-step {
  display: flex;
  flex-direction: column;
  align-items: center;
  flex: 1;
  position: relative;
  z-index: 1;
}
.step-dot {
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #ddd;
  border: 2px solid #ddd;
  margin-bottom: 6px;
  transition: background 0.2s;
}
.order-step.done .step-dot { background: var(--point); border-color: var(--point); }
.order-step.active .step-dot { background: #fff; border: 3px solid var(--point); }
.step-label { font-size: 11px; color: #aaa; text-align: center; }
.order-step.done .step-label, .order-step.active .step-label { color: var(--point); font-weight: 600; }

.order-items { display: flex; flex-direction: column; gap: 10px; margin-bottom: 14px; }
.order-item-row { display: flex; gap: 12px; align-items: center; }
.order-thumb { width: 56px; height: 70px; object-fit: cover; border-radius: 6px; }
.order-item-name { font-size: 13px; font-weight: 600; margin-bottom: 4px; }
.order-item-price { font-size: 13px; color: #555; }
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #eee;
  font-size: 14px;
}
.order-amount { font-weight: 700; font-size: 15px; color: #111; }

/* 슬라이드 애니메이션 */
.slide-down-enter-active { transition: all 0.22s ease; }
.slide-down-leave-active { transition: all 0.18s ease; }
.slide-down-enter-from, .slide-down-leave-to { opacity: 0; transform: translateY(-6px); }

/* ── 프로필 ── */
.profile-card {
  background: #fafafa;
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 28px;
  display: flex;
  flex-direction: column;
  gap: 32px;
}
.profile-section-title {
  font-size: 14px;
  font-weight: 700;
  margin-bottom: 16px;
  color: #111;
  border-bottom: 1px solid #eee;
  padding-bottom: 8px;
}
.profile-row {
  display: grid;
  grid-template-columns: 140px 1fr;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}
.profile-row label {
  font-size: 13px;
  font-weight: 600;
  color: #555;
}
.profile-input {
  padding: 9px 12px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 14px;
  background: #fff;
  transition: border-color 0.15s;
}
.profile-input:focus { border-color: var(--point); outline: none; }
.profile-input:disabled { background: #f5f5f5; color: #aaa; }
.profile-value { font-size: 14px; color: #333; }
.profile-actions { display: flex; flex-direction: column; gap: 10px; align-items: flex-end; }
.btn-profile-save {
  padding: 11px 32px;
  background: var(--point);
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 700;
  cursor: pointer;
  transition: opacity 0.15s;
}
.btn-profile-save:hover { opacity: 0.85; }
.profile-msg { font-size: 13px; color: #27ae60; }
.profile-msg.error { color: #c0392b; }

/* ── 내 리뷰 ── */
.review-list { display: flex; flex-direction: column; gap: 14px; }
.review-card {
  border: 1px solid #eee;
  border-radius: 10px;
  padding: 18px 20px;
  background: #fafafa;
}
.review-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
}
.review-item-name { font-size: 13px; font-weight: 700; color: #333; }
.review-stars { font-size: 15px; color: #f39c12; margin-left: auto; }
.review-date { font-size: 12px; color: #aaa; }
.review-content { font-size: 14px; color: #555; line-height: 1.6; }

/* ── 내 Q&A ── */
.qna-list { display: flex; flex-direction: column; gap: 10px; }
.qna-card {
  border: 1px solid #eee;
  border-radius: 10px;
  background: #fafafa;
  overflow: hidden;
}
.qna-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 18px;
  cursor: pointer;
  user-select: none;
}
.qna-header:hover { background: #f0ece8; }
.qna-badge {
  font-size: 11px;
  padding: 3px 8px;
  border-radius: 20px;
  font-weight: 700;
  flex-shrink: 0;
}
.qna-badge.answered { background: #27ae60; color: #fff; }
.qna-badge.pending { background: #ddd; color: #666; }
.qna-title { font-size: 14px; font-weight: 600; flex: 1; }
.qna-date { font-size: 12px; color: #aaa; }
.qna-toggle { font-size: 12px; color: #aaa; }
.qna-body { padding: 14px 18px; border-top: 1px solid #eee; }
.qna-question-body { font-size: 14px; color: #333; line-height: 1.6; margin-bottom: 12px; }
.qna-answer-body {
  font-size: 14px;
  color: #555;
  line-height: 1.6;
  background: #f5f0ea;
  padding: 12px 16px;
  border-radius: 6px;
  border-left: 3px solid var(--point);
}
.qna-no-answer { font-size: 13px; color: #bbb; font-style: italic; }

/* ── 찜 목록 ── */
.wish-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}
.wish-card {
  border: 1px solid #eee;
  border-radius: 10px;
  overflow: hidden;
  background: #fafafa;
  cursor: pointer;
  transition: box-shadow 0.18s;
}
.wish-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.1); }
.wish-img { width: 100%; aspect-ratio: 3/4; object-fit: cover; }
.wish-info { padding: 12px; }
.wish-brand { font-size: 11px; color: #aaa; margin-bottom: 4px; }
.wish-name { font-size: 13px; font-weight: 600; color: #111; margin-bottom: 6px; line-height: 1.4; }
.wish-price { font-size: 13px; display: flex; align-items: center; gap: 6px; flex-wrap: wrap; }
.wish-discount-badge { background: var(--point); color: #fff; font-size: 11px; font-weight: 700; padding: 2px 6px; border-radius: 4px; }
.wish-sale-price { color: var(--point); font-weight: 700; }
.wish-original-price { color: #aaa; text-decoration: line-through; font-size: 12px; }
.wish-normal-price { color: #111; font-weight: 600; }
.wish-remove {
  width: 100%;
  padding: 10px;
  border: none;
  border-top: 1px solid #eee;
  background: none;
  font-size: 12px;
  color: var(--point);
  cursor: pointer;
  font-weight: 600;
  transition: background 0.15s;
}
.wish-remove:hover { background: #fdf5f0; }

.empty {
  padding: 60px 0;
  text-align: center;
  color: #aaa;
  font-size: 15px;
}
</style>
