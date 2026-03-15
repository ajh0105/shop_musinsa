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
      </div>

      <h3 class="sub-title">주문 내역</h3>
      <div v-if="orders.length === 0" class="empty">주문 내역이 없습니다.</div>

      <div v-else class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-card">
          <div class="order-meta">
            <span class="order-id">주문 #{{ order.id }}</span>
            <span class="order-date">{{ formatDate(order.createdAt) }}</span>
            <span class="order-status">{{ order.status }}</span>
          </div>
          <div class="order-items">
            <div v-for="item in order.items" :key="item.id" class="order-item-row">
              <img :src="item.imgPath" :alt="item.name" class="order-thumb" />
              <div>
                <p class="order-item-name">{{ item.name }}</p>
                <p class="order-item-price">{{ Number(item.salePrice ?? item.price).toLocaleString() }}원</p>
              </div>
            </div>
          </div>
          <div class="order-footer">
            <span class="order-payment">{{ paymentLabel(order.payment) }}</span>
            <span class="order-amount">총 {{ Number(order.amount).toLocaleString() }}원</span>
          </div>
        </div>
      </div>
    </div>
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { api } from '../api'
import { useAuth } from '../composables/useAuth.js'

const { isLoggedIn, userName } = useAuth()
const orders = ref([])

const PAYMENT_MAP = { CARD: '신용카드', KAKAO: '카카오페이', NAVER: '네이버페이', TOSS: '토스페이' }
const paymentLabel = (p) => PAYMENT_MAP[p] || p

function formatDate(dt) {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('ko-KR')
}

async function loadOrders() {
  if (!isLoggedIn.value) return
  try {
    const { data } = await api.get('/orders')
    orders.value = Array.isArray(data) ? data : []
  } catch {
    orders.value = []
  }
}

onMounted(loadOrders)
</script>

<style scoped>
.mypage-welcome {
  font-size: 16px;
  margin-bottom: 28px;
  padding: 16px 20px;
  background: #f9f9f9;
  border-radius: 8px;
  border: 1px solid #eee;
}
.welcome-name { font-weight: 700; }
.sub-title { font-size: 16px; font-weight: 700; margin-bottom: 16px; }

.order-list { display: flex; flex-direction: column; gap: 16px; }
.order-card {
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 20px;
  background: #fafafa;
}
.order-meta {
  display: flex;
  gap: 12px;
  align-items: center;
  margin-bottom: 14px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
  font-size: 13px;
  color: #555;
}
.order-id { font-weight: 700; color: #111; }
.order-status {
  margin-left: auto;
  padding: 3px 10px;
  border-radius: 20px;
  background: #111;
  color: #fff;
  font-size: 12px;
}
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
</style>
