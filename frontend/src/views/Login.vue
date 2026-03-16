<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-logo">
        <RouterLink to="/" class="auth-logo-link">MUSINSA</RouterLink>
      </div>
      <h2 class="auth-title">로그인</h2>

      <form class="auth-form" @submit.prevent="handleLogin">
        <div class="form-group">
          <label class="form-label">이메일</label>
          <input
            v-model="loginId"
            type="email"
            class="form-input"
            placeholder="이메일을 입력하세요"
            required
          />
        </div>
        <div class="form-group">
          <label class="form-label">비밀번호</label>
          <input
            v-model="loginPw"
            type="password"
            class="form-input"
            placeholder="비밀번호를 입력하세요"
            required
          />
        </div>

        <p v-if="error" class="form-error">{{ error }}</p>

        <button type="submit" class="btn-submit" :disabled="submitting">
          <span v-if="submitting">로그인 중...</span>
          <span v-else>로그인</span>
        </button>
      </form>

      <div class="auth-divider">
        <span>또는</span>
      </div>

      <div class="auth-links">
        <span>아직 회원이 아니신가요?</span>
        <RouterLink to="/register" class="auth-link">회원가입</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth.js'

const router = useRouter()
const { setLogin } = useAuth()

const loginId = ref('')
const loginPw = ref('')
const error = ref('')
const submitting = ref(false)

async function handleLogin() {
  error.value = ''
  submitting.value = true
  try {
    const res = await fetch('/v1/api/account/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ loginId: loginId.value, loginPw: loginPw.value })
    })
    if (res.ok) {
      const data = await res.json()
      setLogin(data.loginId, data.name, data.role)
      if (data.role === 'ROLE_ADMIN') {
        router.push('/admin/dashboard')
      } else {
        router.push('/')
      }
    } else if (res.status === 404) {
      error.value = '이메일 또는 비밀번호가 올바르지 않습니다.'
    } else {
      error.value = '로그인에 실패했습니다. 다시 시도해주세요.'
    }
  } catch {
    error.value = '네트워크 오류가 발생했습니다.'
  } finally {
    submitting.value = false
  }
}
</script>
