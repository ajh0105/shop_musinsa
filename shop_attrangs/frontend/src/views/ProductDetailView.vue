<template>
  <main class="page-wrap" v-if="item">
    <section class="detail-wrap">
      <img :src="item.imgPath" :alt="item.name" class="detail-image" />
      <div>
        <h2>{{ item.name }}</h2>
        <p class="detail-brand">{{ item.brand }} | {{ item.category }}</p>
        <p class="detail-price">{{ Number(item.salePrice ?? item.price).toLocaleString() }}원</p>
        <p class="detail-desc">{{ item.description || '상품 설명이 없습니다.' }}</p>
        <button class="detail-btn" @click="addToCart">장바구니 담기</button>
      </div>
    </section>

    <section class="section-wrap">
      <div class="section-head">
        <h2>리뷰</h2>
      </div>
      <div v-if="reviews.length === 0" class="empty">등록된 리뷰가 없습니다.</div>
      <ul v-else class="list-box">
        <li v-for="r in reviews" :key="r.id">
          <strong>{{ r.memberName }}</strong> ({{ r.rating }}점) - {{ r.content }}
        </li>
      </ul>
    </section>

    <section class="section-wrap">
      <div class="section-head">
        <h2>Q&A</h2>
      </div>
      <div v-if="qna.length === 0" class="empty">등록된 문의가 없습니다.</div>
      <ul v-else class="list-box">
        <li v-for="q in qna" :key="q.id">
          <strong>{{ q.title }}</strong> - {{ q.content }}
        </li>
      </ul>
    </section>
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { api } from '../api'
import { MOCK_ITEMS } from '../data/mockItems'

const route = useRoute()
const item = ref(null)
const reviews = ref([])
const qna = ref([])

async function loadItem() {
  const id = Number(route.params.id)
  item.value = MOCK_ITEMS.find((v) => v.id === id) || null

  try {
    const { data } = await api.get(`/items/${id}`)
    if (data) item.value = data
  } catch {}

  try {
    const { data } = await api.get(`/reviews/item/${id}`)
    reviews.value = Array.isArray(data) ? data : []
  } catch {
    reviews.value = []
  }

  try {
    const { data } = await api.get(`/qna/item/${id}`)
    qna.value = Array.isArray(data) ? data : []
  } catch {
    qna.value = []
  }
}

async function addToCart() {
  if (!item.value) return
  try {
    await api.post('/carts', { itemId: item.value.id })
    alert('장바구니에 담았습니다.')
  } catch (e) {
    const msg = e?.response?.data || '로그인 후 이용해 주세요.'
    alert(String(msg))
  }
}

onMounted(loadItem)
</script>