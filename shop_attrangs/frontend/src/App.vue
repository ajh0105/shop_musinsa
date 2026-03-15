<template>
  <div class="app-shell">
    <TopHeader />
    <router-view />
    <FooterSplit />
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import TopHeader from './components/TopHeader.vue'
import FooterSplit from './components/FooterSplit.vue'
import { useAuth } from './composables/useAuth.js'

const { checkLoginStatus } = useAuth()

onMounted(() => {
  checkLoginStatus()
})
</script>
