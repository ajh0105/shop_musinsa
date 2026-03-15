<template>
  <main class="page-wrap">
    <div class="section-head">
      <h2>장바구니</h2>
    </div>
    <div v-if="items.length === 0" class="empty">장바구니가 비어 있습니다.</div>
    <div v-else class="card-grid">
      <article v-for="item in items" :key="item.id" class="item-card">
        <img :src="item.imgPath" :alt="item.name" />
        <div class="item-info">
          <span>{{ item.brand }}</span>
          <h3>{{ item.name }}</h3>
          <p>{{ Number(item.salePrice ?? item.price).toLocaleString() }}원</p>
        </div>
      </article>
    </div>
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { api } from '../api'

const items = ref([])

async function loadCart() {
  try {
    const { data } = await api.get('/cart/items')
    items.value = Array.isArray(data) ? data : []
  } catch (e) {
    items.value = []
    if (e?.response?.status === 401) {
      alert('로그인 후 이용해 주세요.')
    }
  }
}

onMounted(loadCart)
</script>