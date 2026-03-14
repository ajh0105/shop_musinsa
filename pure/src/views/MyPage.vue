<template>
  <div class="mypage">
    <div class="container">
      <h1 class="page-title">마이페이지</h1>

      <div v-if="!isLoggedIn" class="empty-box">
        <p>로그인 후 이용하실 수 있습니다.</p>
        <RouterLink to="/login" class="btn-primary">로그인하기</RouterLink>
      </div>

      <div v-else class="mypage-layout">
        <!-- 사이드바 -->
        <aside class="mypage-sidebar">
          <div class="mypage-user">
            <div class="mypage-avatar">
              <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
                <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                <circle cx="12" cy="7" r="4"/>
              </svg>
            </div>
            <div>
              <p class="mypage-user-id">{{ loginId }}</p>
              <p class="mypage-user-grade">일반 회원</p>
            </div>
          </div>

          <nav class="mypage-nav">
            <button
              v-for="tab in tabs"
              :key="tab.key"
              class="mypage-nav-btn"
              :class="{ 'mypage-nav-btn--active': activeTab === tab.key }"
              @click="activeTab = tab.key"
            >
              {{ tab.label }}
            </button>
          </nav>
        </aside>

        <!-- 컨텐츠 -->
        <div class="mypage-content">
          <!-- 주문 내역 -->
          <div v-if="activeTab === 'orders'">
            <h2 class="mypage-section-title">주문 내역</h2>
            <div v-if="ordersLoading" class="loading-box"><div class="spinner"></div></div>
            <div v-else-if="orders.length === 0" class="empty-box">
              <p>주문 내역이 없습니다.</p>
            </div>
            <div v-else class="order-list">
              <div v-for="order in orders" :key="order.id" class="order-card">
                <div class="order-card-header">
                  <span class="order-number">주문번호 #{{ order.id }}</span>
                  <span class="order-date">{{ formatDate(order.createdAt) }}</span>
                  <span class="order-status">배송 완료</span>
                </div>
                <div class="order-card-body">
                  <div class="order-info-row">
                    <span>배송지</span>
                    <span>{{ order.address }}</span>
                  </div>
                  <div class="order-info-row">
                    <span>결제 수단</span>
                    <span>{{ order.payment === 'CARD' ? '신용카드' : '무통장입금' }}</span>
                  </div>
                  <div class="order-info-row">
                    <span>결제 금액</span>
                    <span class="order-amount">{{ formatPrice(order.amount) }}</span>
                  </div>
                  <div v-if="order.items?.length" class="order-items">
                    <span class="order-items-label">주문 상품</span>
                    <span class="order-items-count">{{ order.items.length }}개</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- 관심 브랜드 -->
          <div v-if="activeTab === 'brands'">
            <h2 class="mypage-section-title">관심 브랜드</h2>
            <div class="brand-grid">
              <div v-for="brand in favBrands" :key="brand" class="brand-chip">{{ brand }}</div>
            </div>
            <p class="mypage-hint">관심 브랜드는 카테고리 페이지에서 추가할 수 있습니다.</p>
          </div>

          <!-- 적립금 -->
          <div v-if="activeTab === 'points'">
            <h2 class="mypage-section-title">적립금</h2>
            <div class="points-card">
              <p class="points-label">보유 적립금</p>
              <p class="points-value">0원</p>
            </div>
            <p class="mypage-hint">주문 완료 시 결제금액의 1%가 적립됩니다.</p>
          </div>

          <!-- 계정 정보 -->
          <div v-if="activeTab === 'account'">
            <h2 class="mypage-section-title">계정 정보</h2>
            <div class="account-info">
              <div class="account-row">
                <span class="account-label">이메일</span>
                <span class="account-value">{{ loginId }}</span>
              </div>
              <div class="account-row">
                <span class="account-label">회원 등급</span>
                <span class="account-value">일반 회원</span>
              </div>
            </div>
            <button class="btn-logout" @click="handleLogout">로그아웃</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth.js'

const { isLoggedIn, loginId, clearLogin } = useAuth()
const router = useRouter()

const activeTab = ref('orders')
const ordersLoading = ref(false)
const orders = ref([])

const tabs = [
  { key: 'orders', label: '주문 내역' },
  { key: 'brands', label: '관심 브랜드' },
  { key: 'points', label: '적립금' },
  { key: 'account', label: '계정 정보' },
]

const favBrands = ['나이키', '아디다스', '노스페이스', '무신사 스탠다드', '커버낫']

function formatPrice(price) {
  return price?.toLocaleString('ko-KR') + '원'
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('ko-KR', { year: 'numeric', month: 'long', day: 'numeric' })
}

async function fetchOrders() {
  if (!isLoggedIn.value) return
  ordersLoading.value = true
  try {
    const res = await fetch('/v1/api/orders', { credentials: 'include' })
    if (res.ok) orders.value = await res.json()
  } catch (e) {
    console.error(e)
  } finally {
    ordersLoading.value = false
  }
}

async function handleLogout() {
  try {
    await fetch('/v1/api/account/logout', { method: 'POST', credentials: 'include' })
  } catch {}
  clearLogin()
  router.push('/')
}

onMounted(fetchOrders)
</script>
