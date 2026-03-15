<template>
  <main class="page-wrap">
    <div class="section-head">
      <h2>장바구니</h2>
    </div>

    <div v-if="items.length === 0" class="empty">장바구니가 비어 있습니다.</div>

    <div v-else>
      <div class="cart-list">
        <article v-for="item in items" :key="item.id" class="cart-item">
          <label class="cart-check">
            <input type="checkbox" :value="item.id" v-model="selected" />
          </label>
          <img :src="item.imgPath" :alt="item.name" class="cart-img" />
          <div class="item-info">
            <span class="item-brand">{{ item.brand }}</span>
            <h3>{{ item.name }}</h3>
            <p class="item-price">{{ Number(item.salePrice ?? item.price).toLocaleString() }}원</p>
          </div>
          <button class="btn-remove" @click="removeItem(item.id)">삭제</button>
        </article>
      </div>

      <div class="cart-summary">
        <div class="summary-row">
          <span>선택 상품 ({{ selected.length }}개)</span>
          <span>{{ selectedTotal.toLocaleString() }}원</span>
        </div>
        <div class="summary-row">
          <span>배송비</span>
          <span>{{ selectedTotal >= 50000 ? '무료' : '3,000원' }}</span>
        </div>
        <div class="summary-divider"></div>
        <div class="summary-row summary-total">
          <span>합계</span>
          <span>{{ finalTotal.toLocaleString() }}원</span>
        </div>
        <button class="btn-order" :disabled="selected.length === 0" @click="goToCheckout">
          주문하기 ({{ selected.length }}개)
        </button>
      </div>
    </div>
  </main>
</template>

<script setup>
import { computed, onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../api'

const router = useRouter()
const items = ref([])
const selected = ref([])

const selectedTotal = computed(() =>
  items.value
    .filter(i => selected.value.includes(i.id))
    .reduce((sum, i) => sum + (i.salePrice ?? i.price), 0)
)
const finalTotal = computed(() =>
  selectedTotal.value + (selectedTotal.value >= 50000 ? 0 : 3000)
)

async function loadCart() {
  try {
    const { data } = await api.get('/cart/items')
    items.value = Array.isArray(data) ? data : []
    selected.value = items.value.map(i => i.id)
  } catch (e) {
    items.value = []
    if (e?.response?.status === 401) alert('로그인 후 이용해 주세요.')
  }
}

async function removeItem(itemId) {
  try {
    await api.delete(`/cart/item/${itemId}`)
    items.value = items.value.filter(i => i.id !== itemId)
    selected.value = selected.value.filter(id => id !== itemId)
  } catch {
    alert('삭제에 실패했습니다.')
  }
}

function goToCheckout() {
  router.push({ path: '/checkout', query: { items: selected.value.join(',') } })
}

onMounted(loadCart)
</script>

<style scoped>
.cart-list { display: flex; flex-direction: column; gap: 16px; margin-bottom: 32px; }
.cart-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  border: 1px solid #eee;
  border-radius: 8px;
  background: #fafafa;
}
.cart-check { flex-shrink: 0; }
.cart-img { width: 80px; height: 100px; object-fit: cover; border-radius: 6px; flex-shrink: 0; }
.item-info { flex: 1; }
.item-brand { font-size: 12px; color: #888; }
.item-info h3 { font-size: 14px; font-weight: 600; margin: 4px 0; }
.item-price { font-size: 15px; font-weight: 700; color: #111; }
.btn-remove {
  padding: 6px 12px; border: 1px solid #ddd; border-radius: 6px;
  background: #fff; font-size: 13px; cursor: pointer; color: #666;
}
.btn-remove:hover { background: #f5f5f5; }
.cart-summary {
  background: #f9f9f9; border: 1px solid #eee; border-radius: 12px;
  padding: 24px; max-width: 400px; margin-left: auto;
}
.summary-row { display: flex; justify-content: space-between; font-size: 14px; color: #555; margin-bottom: 10px; }
.summary-divider { border-top: 1px solid #ddd; margin: 12px 0; }
.summary-total { font-size: 16px; font-weight: 700; color: #111; margin-bottom: 16px; }
.btn-order {
  width: 100%; padding: 14px; background: #111; color: #fff;
  border: none; border-radius: 8px; font-size: 15px; font-weight: 700;
  cursor: pointer;
}
.btn-order:disabled { background: #aaa; cursor: not-allowed; }
</style>
