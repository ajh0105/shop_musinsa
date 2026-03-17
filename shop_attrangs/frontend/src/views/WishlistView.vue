<template>
  <main class="page-wrap">
    <div class="section-head">
      <h2>찜 목록</h2>
      <p>총 {{ items.length }}개의 상품을 찜했습니다.</p>
    </div>

    <div v-if="loading" class="loading-box">
      <div class="spinner"></div>
    </div>

    <div v-else-if="items.length === 0" class="wish-empty">
      <p>찜한 상품이 없습니다.</p>
      <router-link to="/" class="btn-go-home">쇼핑 계속하기</router-link>
    </div>

    <div v-else class="card-grid">
      <div v-for="item in items" :key="item.id" class="item-card-wrapper">
        <router-link :to="`/product/${item.itemId}`" class="item-card">
          <div class="item-thumb">
            <img :src="item.item?.imgPath" :alt="item.item?.name" />
            <div class="item-hover-overlay">
              <span class="item-hover-text">자세히 보기</span>
            </div>
          </div>
          <div class="item-info">
            <span>{{ item.item?.brand }}</span>
            <h3>{{ item.item?.name }}</h3>
            <div class="item-price-row">
              <div class="price-stack">
                <p class="item-sale-price">
                  {{ Number(item.item?.salePrice ?? item.item?.price).toLocaleString() }}원
                </p>
                <span
                  v-if="item.item?.salePrice && item.item.salePrice < item.item.price"
                  class="item-origin-price"
                >
                  {{ Number(item.item?.price).toLocaleString() }}원
                </span>
              </div>
              <button
                class="wish-btn wish-btn--active"
                @click.stop.prevent="removeWish(item)"
                title="찜 해제"
              >
                <svg viewBox="0 0 24 24" fill="#e53935" width="18" height="18">
                  <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                </svg>
              </button>
            </div>
          </div>
        </router-link>
      </div>
    </div>

    <Transition name="toast">
      <div v-if="toastMsg" class="toast">{{ toastMsg }}</div>
    </Transition>
  </main>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { api } from '../api'
import { useWishCart, syncWishlistFromServer } from '../composables/useWishCart.js'

const { toggleWish } = useWishCart()
const items = ref([])
const loading = ref(true)
const toastMsg = ref('')
let toastTimer = null

async function loadWishlist() {
  loading.value = true
  try {
    const { data } = await api.get('/wishlist/items')
    items.value = Array.isArray(data) ? data : []
  } catch (e) {
    if (e?.response?.status === 401) {
      items.value = []
    }
  } finally {
    loading.value = false
  }
}

async function removeWish(wishItem) {
  const fakeItem = { id: wishItem.itemId }
  await toggleWish(fakeItem, null)
  items.value = items.value.filter(i => i.itemId !== wishItem.itemId)
  showToast(`'${wishItem.item?.name}' 찜을 해제했습니다.`)
}

function showToast(msg) {
  toastMsg.value = msg
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => { toastMsg.value = '' }, 2000)
}

onMounted(loadWishlist)
</script>

<style scoped>
.wish-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 18px;
  padding: 80px 0;
  color: var(--sub);
}
.btn-go-home {
  padding: 10px 24px;
  border: 1px solid var(--point);
  border-radius: 999px;
  color: var(--point);
  font-size: 14px;
  font-weight: 600;
  transition: background 0.2s, color 0.2s;
}
.btn-go-home:hover {
  background: var(--point);
  color: #fff;
}
</style>
