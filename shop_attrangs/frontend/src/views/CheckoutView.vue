<template>
  <main class="page-wrap">
    <div class="section-head">
      <h2>주문하기</h2>
    </div>

    <div v-if="loading" class="empty">상품 정보를 불러오는 중...</div>
    <div v-else-if="items.length === 0" class="empty">
      주문할 상품이 없습니다.
      <router-link to="/">홈으로</router-link>
    </div>

    <div v-else class="checkout-wrap">
      <!-- 주문 상품 -->
      <div class="checkout-box">
        <h3 class="box-title">주문 상품</h3>
        <div v-for="item in items" :key="item.id" class="order-item">
          <img :src="item.imgPath" :alt="item.name" class="order-img" />
          <div class="order-info">
            <span class="order-brand">{{ item.brand }}</span>
            <p class="order-name">{{ item.name }}</p>
            <p class="order-price">{{ Number(item.salePrice ?? item.price).toLocaleString() }}원</p>
          </div>
        </div>

        <div class="price-summary">
          <div class="summary-row">
            <span>상품 금액 ({{ items.length }}개)</span>
            <span>{{ subtotal.toLocaleString() }}원</span>
          </div>
          <div class="summary-row">
            <span>배송비</span>
            <span>{{ subtotal >= 50000 ? '무료' : '3,000원' }}</span>
          </div>
          <div class="summary-divider"></div>
          <div class="summary-row summary-total">
            <span>최종 결제 금액</span>
            <span>{{ totalPrice.toLocaleString() }}원</span>
          </div>
        </div>
      </div>

      <!-- 주문 정보 입력 -->
      <div class="checkout-box">
        <h3 class="box-title">배송 / 결제 정보</h3>
        <form @submit.prevent="submitOrder">
          <div class="form-group">
            <label>받는 분 이름 <span class="req">*</span></label>
            <input v-model="form.name" type="text" placeholder="이름을 입력하세요" required />
          </div>
          <div class="form-group">
            <label>배송 주소 <span class="req">*</span></label>
            <input v-model="form.address" type="text" placeholder="배송 주소를 입력하세요" required />
          </div>
          <div class="form-group">
            <label>결제 수단</label>
            <select v-model="form.payment">
              <option value="CARD">신용카드</option>
              <option value="KAKAO">카카오페이</option>
              <option value="NAVER">네이버페이</option>
              <option value="TOSS">토스페이</option>
            </select>
          </div>
          <div v-if="form.payment === 'CARD'" class="form-group">
            <label>카드 번호</label>
            <input v-model="form.cardNumber" type="text" placeholder="0000-0000-0000-0000" maxlength="19" />
          </div>
          <p v-if="errorMsg" class="form-error">{{ errorMsg }}</p>
          <div class="form-actions">
            <button type="button" class="btn-cancel" @click="$router.back()">취소</button>
            <button type="submit" class="btn-confirm" :disabled="submitting">
              {{ submitting ? '처리 중...' : `${totalPrice.toLocaleString()}원 결제하기` }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { api } from '../api'

const route = useRoute()
const router = useRouter()

const items = ref([])
const loading = ref(true)
const submitting = ref(false)
const errorMsg = ref('')

const form = ref({ name: '', address: '', payment: 'CARD', cardNumber: '' })

const subtotal = computed(() => items.value.reduce((sum, i) => sum + (i.salePrice ?? i.price), 0))
const totalPrice = computed(() => subtotal.value + (subtotal.value >= 50000 ? 0 : 3000))

async function loadItems() {
  const ids = (route.query.items || '').split(',').map(Number).filter(Boolean)
  if (!ids.length) { loading.value = false; return }

  try {
    const results = await Promise.allSettled(ids.map(id => api.get(`/items/${id}`)))
    items.value = results
      .filter(r => r.status === 'fulfilled' && r.value.data)
      .map(r => r.value.data)
  } catch {}
  finally { loading.value = false }
}

async function submitOrder() {
  errorMsg.value = ''
  submitting.value = true
  try {
    await api.post('/orders', {
      itemIds: items.value.map(i => i.id),
      name: form.value.name,
      address: form.value.address,
      payment: form.value.payment,
      cardNumber: form.value.cardNumber
    })
    alert('주문이 완료되었습니다!')
    router.push('/mypage')
  } catch (e) {
    errorMsg.value = e?.response?.data || '주문 처리 중 오류가 발생했습니다.'
  } finally {
    submitting.value = false
  }
}

onMounted(loadItems)
</script>

<style scoped>
.checkout-wrap {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 24px;
  align-items: start;
}
@media (max-width: 768px) { .checkout-wrap { grid-template-columns: 1fr; } }

.checkout-box {
  background: #f9f9f9;
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 24px;
}
.box-title {
  font-size: 15px;
  font-weight: 700;
  margin-bottom: 16px;
  padding-bottom: 12px;
  border-bottom: 1px solid #eee;
}
.order-item { display: flex; gap: 12px; margin-bottom: 16px; padding-bottom: 16px; border-bottom: 1px solid #f0f0f0; }
.order-item:last-of-type { border-bottom: none; }
.order-img { width: 80px; height: 100px; object-fit: cover; border-radius: 6px; flex-shrink: 0; }
.order-brand { font-size: 11px; color: #888; }
.order-name { font-size: 13px; font-weight: 600; margin: 4px 0; }
.order-price { font-size: 14px; font-weight: 700; color: #111; }

.price-summary { border-top: 1px solid #eee; padding-top: 16px; margin-top: 8px; }
.summary-row { display: flex; justify-content: space-between; font-size: 13px; color: #555; margin-bottom: 8px; }
.summary-divider { border-top: 1px solid #ddd; margin: 10px 0; }
.summary-total { font-size: 15px; font-weight: 700; color: #111; }

.form-group { margin-bottom: 16px; }
.form-group label { display: block; font-size: 13px; font-weight: 600; margin-bottom: 6px; color: #333; }
.req { color: #e74c3c; }
.form-group input,
.form-group select {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 14px;
  box-sizing: border-box;
  background: #fff;
}
.form-group input:focus,
.form-group select:focus { outline: none; border-color: #111; }
.form-error { color: #e74c3c; font-size: 13px; margin-bottom: 12px; }
.form-actions { display: flex; gap: 10px; margin-top: 8px; }
.btn-cancel {
  flex: 1; padding: 13px; border: 1px solid #ddd; border-radius: 8px;
  background: #fff; font-size: 14px; cursor: pointer; color: #555;
}
.btn-confirm {
  flex: 2; padding: 13px; border: none; border-radius: 8px;
  background: #111; color: #fff; font-size: 14px; font-weight: 700; cursor: pointer;
}
.btn-confirm:disabled { background: #aaa; cursor: not-allowed; }
</style>
