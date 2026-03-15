<template>
  <main class="page-wrap placeholder">
    <h2>{{ title }}</h2>
    <p>포트폴리오 데모 화면입니다. 백엔드 API 연동 후 기능 페이지로 확장할 수 있습니다.</p>
  </main>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    default: '준비중'
  }
})
</script>
