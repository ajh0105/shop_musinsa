<template>
  <main class="page-wrap auth-wrap">
    <h2>로그인</h2>
    <form @submit.prevent="submit" class="auth-form">
      <input v-model="loginId" type="email" placeholder="이메일" required />
      <input v-model="loginPw" type="password" placeholder="비밀번호" required />
      <button type="submit">로그인</button>
    </form>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../api'

const router = useRouter()
const loginId = ref('')
const loginPw = ref('')

async function submit() {
  try {
    await api.post('/account/login', { loginId: loginId.value, loginPw: loginPw.value })
    alert('로그인되었습니다.')
    router.push('/')
  } catch (e) {
    alert(String(e?.response?.data || '로그인 실패'))
  }
}
</script>