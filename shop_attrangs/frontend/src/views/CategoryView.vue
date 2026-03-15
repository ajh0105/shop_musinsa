<template>
  <main class="page-wrap category-wrap">
    <div class="section-head">
      <h2>{{ category }}</h2>
      <p>페이지 {{ currentPage }} / {{ totalPages }}</p>
    </div>

    <div class="card-grid">
      <router-link
        v-for="item in pagedItems"
        :key="item.id"
        :to="`/product/${item.id}`"
        class="item-card"
      >
        <img :src="item.imgPath" :alt="item.name" />
        <div class="item-info">
          <span>{{ item.brand }}</span>
          <h3>{{ item.name }}</h3>
          <p>{{ Number(item.salePrice ?? item.price).toLocaleString() }}원</p>
        </div>
      </router-link>
    </div>

    <div class="pager">
      <button :disabled="currentPage <= 1" @click="currentPage -= 1">이전</button>
      <button :disabled="currentPage >= totalPages" @click="currentPage += 1">다음</button>
    </div>
  </main>
</template>

<script setup>
import { computed, onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { api } from '../api'
import { getCategoryItems } from '../data/mockItems'

const route = useRoute()
const currentPage = ref(1)
const items = ref([])

const category = computed(() => String(route.params.category || '').trim())

const pageSize = 8
const totalPages = computed(() => Math.max(1, Math.ceil(items.value.length / pageSize)))
const pagedItems = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return items.value.slice(start, start + pageSize)
})

async function loadCategory() {
  currentPage.value = 1
  items.value = getCategoryItems(category.value)

  try {
    const { data } = await api.get('/items', { params: { category: category.value } })
    if (Array.isArray(data) && data.length) {
      items.value = data
    }
  } catch {
    // 백엔드 미연결 시 mock 유지
  }
}

watch(category, loadCategory)
onMounted(loadCategory)
</script>