<template>
  <main class="page-wrap">
    <BannerRows />
    <ProductSection title="신상품" subtitle="최근 등록 상품" :items="newItems" />
    <ProductSection title="베스트" subtitle="조회수 상위 상품" :items="bestItems" />
    <ProductSection title="추천" subtitle="할인율 높은 상품" :items="recommendItems" />
  </main>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { api } from '../api'
import BannerRows from '../components/BannerRows.vue'
import ProductSection from '../components/ProductSection.vue'
import { getSectionItems } from '../data/mockItems'

const newItems = ref(getSectionItems('new'))
const bestItems = ref(getSectionItems('best'))
const recommendItems = ref(getSectionItems('recommend'))

async function tryLoadSection(type, targetRef) {
  try {
    const { data } = await api.get('/items', { params: { sort: type } })
    if (Array.isArray(data) && data.length) {
      targetRef.value = data.slice(0, 4)
    }
  } catch {
    // 백엔드 미연결 시 mock 유지
  }
}

onMounted(() => {
  tryLoadSection('new', newItems)
  tryLoadSection('best', bestItems)
  tryLoadSection('recommend', recommendItems)
})
</script>