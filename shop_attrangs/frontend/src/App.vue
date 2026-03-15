<template>
  <div class="app-shell">
    <TopHeader />
    <router-view />
    <FooterSplit />
  </div>
</template>

<script setup>
import TopHeader from './components/TopHeader.vue'
import FooterSplit from './components/FooterSplit.vue'
</script>
