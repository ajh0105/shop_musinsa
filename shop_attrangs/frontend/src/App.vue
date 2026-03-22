<template>
  <div class="app-shell">
    <TopHeader />
    <router-view />
    <FooterSplit v-if="!isAdminRoute" />
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import TopHeader from './components/TopHeader.vue'
import FooterSplit from './components/FooterSplit.vue'
import { useAuth } from './composables/useAuth.js'

const { checkLoginStatus } = useAuth()
const route = useRoute()
const isAdminRoute = computed(() => route.path.startsWith('/admin'))

onMounted(() => {
  checkLoginStatus()
})
</script>
