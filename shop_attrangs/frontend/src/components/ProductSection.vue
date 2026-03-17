<template>
  <section class="section-wrap">
    <div class="section-head">
      <h2>{{ title }}</h2>
      <p>{{ subtitle }}</p>
    </div>
    <div class="card-grid">
      <div v-for="item in items" :key="item.id" class="item-card-wrapper">
        <router-link :to="`/product/${item.id}`" class="item-card">
          <div class="item-thumb">
            <img :src="item.imgPath" :alt="item.name" />
            <div class="item-hover-overlay">
              <span class="item-hover-text">자세히 보기</span>
            </div>
          </div>
          <div class="item-info">
            <span>{{ item.brand }}</span>
            <h3>{{ item.name }}</h3>
            <div class="item-price-row">
              <div class="price-stack">
                <p class="item-sale-price">{{ Number(item.salePrice ?? item.price).toLocaleString() }}원</p>
                <span v-if="item.salePrice && item.salePrice < item.price" class="item-origin-price">
                  {{ Number(item.price).toLocaleString() }}원
                </span>
              </div>
              <button
                class="wish-btn"
                :class="{ 'wish-btn--active': isWished(item.id) }"
                @click.stop="onWish(item, $event)"
                :title="isWished(item.id) ? '찜 해제' : '찜하기'"
              >
                <svg v-if="isWished(item.id)" viewBox="0 0 24 24" fill="#e53935" width="18" height="18">
                  <path d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z"/>
                </svg>
                <svg v-else viewBox="0 0 24 24" fill="none" stroke="#bbb" stroke-width="2" width="18" height="18">
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
  </section>
</template>

<script setup>
import { ref } from 'vue'
import { useWishCart } from '../composables/useWishCart.js'

defineProps({
  title: String,
  subtitle: String,
  items: { type: Array, default: () => [] }
})

const { isWished, toggleWish } = useWishCart()
const toastMsg = ref('')
let toastTimer = null

async function onWish(item, event) {
  const added = await toggleWish(item, event)
  if (added) {
    showToast(`'${item.name}' 장바구니에 담겼습니다 ♥`)
  }
}

function showToast(msg) {
  toastMsg.value = msg
  clearTimeout(toastTimer)
  toastTimer = setTimeout(() => { toastMsg.value = '' }, 2200)
}
</script>
