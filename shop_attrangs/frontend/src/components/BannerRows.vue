<template>
  <section class="hero-banner">
    <article class="hero-slide">
      <div class="hero-overlay" />
      <img :src="currentBanner.image" :alt="currentBanner.title" />
      <div class="hero-copy">
        <p>{{ currentBanner.subtitle }}</p>
        <h2>{{ currentBanner.title }}</h2>
      </div>
    </article>

    <div class="hero-dots">
      <button
        v-for="(banner, index) in banners"
        :key="banner.id"
        :class="{ active: index === currentIndex }"
        @click="goTo(index)"
      />
    </div>
  </section>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from 'vue'

const banners = [
  {
    id: 1,
    subtitle: '2026 SPRING COLLECTION',
    title: 'Spring Guest Look',
    image: 'https://picsum.photos/seed/atrbanner1/1280/520'
  },
  {
    id: 2,
    subtitle: 'WEEKLY BEST ITEM',
    title: 'Best Restock Picks',
    image: 'https://picsum.photos/seed/atrbanner2/1280/520'
  },
  {
    id: 3,
    subtitle: 'OUTLET SPECIAL PRICE',
    title: 'Up To 50 Percent Off',
    image: 'https://picsum.photos/seed/atrbanner3/1280/520'
  }
]

const currentIndex = ref(0)
let timer = null

const currentBanner = computed(() => banners[currentIndex.value])

function next() {
  currentIndex.value = (currentIndex.value + 1) % banners.length
}

function goTo(index) {
  currentIndex.value = index
  restartTimer()
}

function startTimer() {
  timer = setInterval(next, 3500)
}

function restartTimer() {
  if (timer) clearInterval(timer)
  startTimer()
}

onMounted(startTimer)
onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

