<template>
  <div class="cart-page">
    <div class="container">
      <h1 class="page-title">장바구니</h1>

      <div v-if="!isLoggedIn" class="empty-box">
        <p>로그인 후 장바구니를 이용하실 수 있습니다.</p>
        <RouterLink to="/login" class="btn-primary">로그인하기</RouterLink>
      </div>

      <div v-else-if="loading" class="loading-box">
        <div class="spinner"></div>
      </div>

      <div v-else-if="cartItems.length === 0" class="empty-box">
        <svg width="64" height="64" viewBox="0 0 24 24" fill="none" stroke="#ccc" stroke-width="1.5">
          <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/>
          <line x1="3" y1="6" x2="21" y2="6"/>
          <path d="M16 10a4 4 0 0 1-8 0"/>
        </svg>
        <p>장바구니가 비어 있습니다.</p>
        <RouterLink to="/" class="btn-primary">쇼핑 계속하기</RouterLink>
      </div>

      <div v-else class="cart-layout">
        <!-- 장바구니 아이템 목록 -->
        <div class="cart-items">
          <div class="cart-items-header">
            <label class="cart-check-all">
              <input type="checkbox" v-model="allSelected" @change="toggleAll" />
              전체 선택 ({{ selectedItems.length }}/{{ cartItems.length }})
            </label>
            <button class="btn-delete-selected" @click="removeSelected">선택 삭제</button>
          </div>

          <div
            v-for="ci in cartItems"
            :key="ci.id"
            class="cart-item"
          >
            <div class="cart-item-check">
              <input type="checkbox" :value="ci.itemId" v-model="selectedItems" />
            </div>
            <div class="cart-item-img">
              <img :src="ci.item?.imgPath" :alt="ci.item?.name" />
            </div>
            <div class="cart-item-info">
              <p class="cart-item-brand">{{ ci.item?.brand }}</p>
              <p class="cart-item-name">{{ ci.item?.name }}</p>
              <div class="cart-item-price-wrap">
                <span v-if="ci.item?.discountPer > 0" class="product-price-original">{{ formatPrice(ci.item?.price) }}</span>
                <span class="cart-item-price">{{ formatPrice(ci.item?.salePrice) }}</span>
                <span v-if="ci.item?.discountPer > 0" class="product-discount">{{ ci.item?.discountPer }}%</span>
              </div>
            </div>
            <button class="cart-item-remove" @click="removeItem(ci.itemId)">✕</button>
          </div>
        </div>

        <!-- 주문 요약 -->
        <div class="cart-summary">
          <h3 class="summary-title">주문 요약</h3>
          <div class="summary-row">
            <span>상품 금액</span>
            <span>{{ formatPrice(totalPrice) }}</span>
          </div>
          <div class="summary-row">
            <span>배송비</span>
            <span>{{ totalPrice >= 50000 ? '무료' : '3,000원' }}</span>
          </div>
          <div class="summary-divider"></div>
          <div class="summary-row summary-row--total">
            <span>최종 결제 금액</span>
            <span>{{ formatPrice(finalPrice) }}</span>
          </div>

          <button class="btn-order" :disabled="selectedItems.length === 0" @click="goToCheckout">
            주문하기 ({{ selectedItems.length }}개)
          </button>
          <p class="summary-note">50,000원 이상 구매 시 무료 배송</p>
        </div>
      </div>
    </div>

    <!-- 토스트 -->
    <Transition name="toast">
      <div v-if="toastMsg" class="toast">{{ toastMsg }}</div>
    </Transition>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth.js'

const router = useRouter()
const { isLoggedIn } = useAuth()

const loading = ref(false)
const cartItems = ref([])
const selectedItems = ref([])
const allSelected = ref(false)
const toastMsg = ref('')

function showToast(msg) {
  toastMsg.value = msg
  setTimeout(() => { toastMsg.value = '' }, 2500)
}

function formatPrice(price) {
  if (!price) return '0원'
  return price.toLocaleString('ko-KR') + '원'
}

function toggleAll() {
  if (allSelected.value) {
    selectedItems.value = cartItems.value.map(c => c.itemId)
  } else {
    selectedItems.value = []
  }
}

const totalPrice = computed(() => {
  return cartItems.value
    .filter(c => selectedItems.value.includes(c.itemId))
    .reduce((sum, c) => sum + (c.item?.salePrice || 0), 0)
})

const finalPrice = computed(() => {
  return totalPrice.value + (totalPrice.value >= 50000 ? 0 : 3000)
})

async function fetchCart() {
  if (!isLoggedIn.value) return
  loading.value = true
  try {
    const res = await fetch('/v1/api/cart/items', { credentials: 'include' })
    if (!res.ok) { cartItems.value = []; return }
    cartItems.value = await res.json()
    selectedItems.value = cartItems.value.map(c => c.itemId)
    allSelected.value = true
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

async function removeItem(itemId) {
  try {
    await fetch(`/v1/api/cart/item/${itemId}`, { method: 'DELETE', credentials: 'include' })
    cartItems.value = cartItems.value.filter(c => c.itemId !== itemId)
    selectedItems.value = selectedItems.value.filter(id => id !== itemId)
    showToast('상품이 장바구니에서 삭제되었습니다.')
  } catch {
    showToast('삭제에 실패했습니다.')
  }
}

async function removeSelected() {
  for (const itemId of selectedItems.value) {
    await removeItem(itemId)
  }
}

function goToCheckout() {
  router.push({
    path: '/cart-checkout',
    query: { items: selectedItems.value.join(',') }
  })
}

onMounted(fetchCart)
</script>
