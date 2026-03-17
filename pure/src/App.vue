<template>
  <div id="app-wrapper">
    <template v-if="!isAdminRoute">
      <Header />
    </template>
    <main class="main-content">
      <RouterView />
    </main>
    <template v-if="!isAdminRoute">
      <Footer />
    </template>
  </div>
</template>

<script setup>
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import Header from './components/Header.vue'
import Footer from './components/Footer.vue'
import { useAuth } from './composables/useAuth.js'

const { checkLoginStatus } = useAuth()
const route = useRoute()

const isAdminRoute = computed(() => route.path.startsWith('/admin'))

onMounted(() => {
  checkLoginStatus()
})
</script>

<style>
#app-wrapper {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
.main-content {
  flex: 1;
}
</style>
