<template>
  <div class="board-page">
    <div class="board-hero">
      <div class="container">
        <h1 class="board-hero-title">자주 묻는 질문</h1>
        <p class="board-hero-desc">궁금하신 사항을 빠르게 확인하세요.</p>
      </div>
    </div>

    <div class="container board-container">
      <!-- 카테고리 탭 -->
      <div class="faq-tabs">
        <button
          v-for="tab in tabs"
          :key="tab.value"
          class="faq-tab"
          :class="{ 'faq-tab--active': activeTab === tab.value }"
          @click="activeTab = tab.value"
        >
          {{ tab.label }}
        </button>
      </div>

      <div v-if="loading" class="loading-box"><div class="spinner"></div></div>

      <div v-else-if="filteredFaqs.length === 0" class="empty-box">
        <p>등록된 FAQ가 없습니다.</p>
      </div>

      <div v-else class="faq-list">
        <div
          v-for="faq in filteredFaqs"
          :key="faq.id"
          class="faq-item"
          :class="{ 'faq-item--open': openId === faq.id }"
        >
          <button class="faq-question" @click="toggle(faq.id)">
            <span class="faq-q-mark">Q</span>
            <span class="faq-q-text">{{ faq.question }}</span>
            <span class="faq-arrow">{{ openId === faq.id ? '▲' : '▼' }}</span>
          </button>
          <div v-if="openId === faq.id" class="faq-answer">
            <span class="faq-a-mark">A</span>
            <span class="faq-a-text">{{ faq.answer }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'

const faqs = ref([])
const loading = ref(true)
const openId = ref(null)
const activeTab = ref('전체')

const tabs = [
  { label: '전체', value: '전체' },
  { label: '주문/결제', value: '주문/결제' },
  { label: '배송', value: '배송' },
  { label: '교환/반품', value: '교환/반품' },
  { label: '회원/계정', value: '회원/계정' },
  { label: '기타', value: '기타' },
]

const filteredFaqs = computed(() => {
  if (activeTab.value === '전체') return faqs.value
  return faqs.value.filter(f => f.category === activeTab.value)
})

function toggle(id) {
  openId.value = openId.value === id ? null : id
}

async function loadFaqs() {
  try {
    const res = await fetch('/v1/api/faq')
    if (res.ok) faqs.value = await res.json()
  } catch {} finally {
    loading.value = false
  }
}

onMounted(loadFaqs)
</script>

<style scoped>
.board-page { min-height: 60vh; }

.board-hero {
  background: var(--color-primary);
  color: #fff;
  padding: 48px 0 40px;
  margin-bottom: 40px;
}
.board-hero-title { font-size: 2rem; font-weight: 800; margin-bottom: 8px; }
.board-hero-desc { font-size: 0.95rem; opacity: 0.8; }

.board-container { padding-bottom: 80px; }

.faq-tabs {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 32px;
}
.faq-tab {
  padding: 8px 20px;
  border: 1px solid var(--color-border-dark);
  border-radius: 20px;
  font-size: 0.88rem;
  color: var(--color-text-muted);
  background: #fff;
  cursor: pointer;
  transition: all var(--transition);
}
.faq-tab:hover { border-color: var(--color-primary); color: var(--color-primary); }
.faq-tab--active {
  background: var(--color-primary);
  color: #fff;
  border-color: var(--color-primary);
}

.faq-list { display: flex; flex-direction: column; gap: 0; border-top: 2px solid var(--color-primary); }

.faq-item { border-bottom: 1px solid var(--color-border); }

.faq-question {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 16px;
  text-align: left;
  background: none;
  cursor: pointer;
  font-size: 0.95rem;
  font-weight: 500;
  transition: background var(--transition);
}
.faq-question:hover { background: var(--color-bg-gray); }
.faq-item--open .faq-question { background: var(--color-bg-gray); }

.faq-q-mark {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: var(--color-primary);
  color: #fff;
  font-weight: 700;
  font-size: 0.88rem;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.faq-q-text { flex: 1; }
.faq-arrow { color: var(--color-text-muted); font-size: 0.75rem; flex-shrink: 0; }

.faq-answer {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 20px 16px;
  background: #f9f9f9;
  border-top: 1px solid var(--color-border);
  font-size: 0.9rem;
  line-height: 1.7;
  color: var(--color-text);
}
.faq-a-mark {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  background: #4f8ef7;
  color: #fff;
  font-weight: 700;
  font-size: 0.88rem;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}
.faq-a-text { flex: 1; white-space: pre-wrap; }
</style>
