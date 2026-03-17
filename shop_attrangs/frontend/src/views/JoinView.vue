<template>
  <main class="page-wrap auth-wrap">
    <h2 style="font-size:26px; margin-bottom:28px;">회원가입</h2>
    <form @submit.prevent="submit" class="auth-form">
      <input v-model="name" type="text" placeholder="이름" required />
      <input v-model="loginId" type="email" placeholder="이메일" required />
      <input v-model="loginPw" type="password" placeholder="비밀번호" required />
      <button type="submit">회원가입</button>
    </form>
  </main>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { api } from '../api'

const router = useRouter()
const name = ref('')
const loginId = ref('')
const loginPw = ref('')

async function submit() {
  try {
    await api.post('/account/join', {
      name: name.value,
      loginId: loginId.value,
      loginPw: loginPw.value
    })
    alert('회원가입이 완료되었습니다.')
    router.push('/login')
  } catch (e) {
    alert(String(e?.response?.data || '회원가입 실패'))
  }
}
</script>