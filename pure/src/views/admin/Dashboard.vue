<template>
  <div class="admin-page">
    <h1 class="admin-page-title">대시보드</h1>

    <div v-if="loading" class="loading-box"><div class="spinner"></div></div>
    <template v-else>
      <!-- 통계 카드 -->
      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">👥</div>
          <div class="stat-body">
            <p class="stat-label">전체 회원</p>
            <p class="stat-value">{{ stats.totalMembers?.toLocaleString() }}</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">📦</div>
          <div class="stat-body">
            <p class="stat-label">전체 상품</p>
            <p class="stat-value">{{ stats.totalItems?.toLocaleString() }}</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🛒</div>
          <div class="stat-body">
            <p class="stat-label">전체 주문</p>
            <p class="stat-value">{{ stats.totalOrders?.toLocaleString() }}</p>
          </div>
        </div>
        <div class="stat-card stat-card--primary">
          <div class="stat-icon">💰</div>
          <div class="stat-body">
            <p class="stat-label">총 매출</p>
            <p class="stat-value">{{ stats.totalSales?.toLocaleString() }}원</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">✅</div>
          <div class="stat-body">
            <p class="stat-label">결제완료</p>
            <p class="stat-value">{{ stats.paidOrders?.toLocaleString() }}</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">❌</div>
          <div class="stat-body">
            <p class="stat-label">취소된 주문</p>
            <p class="stat-value">{{ stats.cancelledOrders?.toLocaleString() }}</p>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">⭐</div>
          <div class="stat-body">
            <p class="stat-label">전체 리뷰</p>
            <p class="stat-value">{{ stats.totalReviews?.toLocaleString() }}</p>
          </div>
        </div>
      </div>

      <!-- 최근 주문 -->
      <div class="admin-card mt-4">
        <h2 class="admin-card-title">최근 주문 (5건)</h2>
        <table class="admin-table">
          <thead>
            <tr>
              <th>주문번호</th>
              <th>주문자</th>
              <th>결제금액</th>
              <th>상태</th>
              <th>일시</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="order in stats.recentOrders" :key="order.id">
              <td>#{{ order.id }}</td>
              <td>{{ order.name }}</td>
              <td>{{ order.amount?.toLocaleString() }}원</td>
              <td>
                <span class="order-status" :class="`status-${order.status?.toLowerCase()}`">
                  {{ order.status === 'PAID' ? '결제완료' : '취소됨' }}
                </span>
              </td>
              <td>{{ formatDate(order.createdAt) }}</td>
            </tr>
          </tbody>
        </table>
      </div>
    </template>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const stats = ref({})
const loading = ref(true)

function formatDate(dt) {
  if (!dt) return ''
  return new Date(dt).toLocaleDateString('ko-KR')
}

async function loadStats() {
  try {
    const res = await fetch('/v1/api/admin/dashboard', { credentials: 'include' })
    if (res.ok) stats.value = await res.json()
  } catch {} finally { loading.value = false }
}

onMounted(loadStats)
</script>
