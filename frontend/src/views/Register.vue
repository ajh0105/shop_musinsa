<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-logo">
        <RouterLink to="/" class="auth-logo-link">MUSINSA</RouterLink>
      </div>
      <h2 class="auth-title">회원가입</h2>

      <form class="auth-form" @submit.prevent="handleRegister">
        <div class="form-group">
          <label class="form-label">이름</label>
          <input
            v-model="name"
            type="text"
            class="form-input"
            placeholder="이름을 입력하세요"
            required
          />
        </div>
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
            placeholder="비밀번호 (8자 이상)"
            minlength="8"
            required
          />
        </div>
        <div class="form-group">
          <label class="form-label">비밀번호 확인</label>
          <input
            v-model="loginPwConfirm"
            type="password"
            class="form-input"
            placeholder="비밀번호를 다시 입력하세요"
            required
          />
        </div>

        <div class="form-check">
          <input v-model="agreeTerms" type="checkbox" id="agree-terms" required />
          <label for="agree-terms">
            <a href="#" class="form-link">이용약관</a> 및 <a href="#" class="form-link">개인정보처리방침</a>에 동의합니다
          </label>
        </div>

        <p v-if="error" class="form-error">{{ error }}</p>
        <p v-if="success" class="form-success">{{ success }}</p>

        <button type="submit" class="btn-submit" :disabled="submitting">
          <span v-if="submitting">가입 중...</span>
          <span v-else>회원가입</span>
        </button>
      </form>

      <div class="auth-links">
        <span>이미 회원이신가요?</span>
        <RouterLink to="/login" class="auth-link">로그인</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const name = ref('')
const loginId = ref('')
const loginPw = ref('')
const loginPwConfirm = ref('')
const agreeTerms = ref(false)
const error = ref('')
const success = ref('')
const submitting = ref(false)

async function handleRegister() {
  error.value = ''
  success.value = ''

  if (loginPw.value !== loginPwConfirm.value) {
    error.value = '비밀번호가 일치하지 않습니다.'
    return
  }

  submitting.value = true
  try {
    const res = await fetch('/v1/api/account/join', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ name: name.value, loginId: loginId.value, loginPw: loginPw.value })
    })
    if (res.ok) {
      success.value = '회원가입이 완료되었습니다!'
      setTimeout(() => router.push('/login'), 1500)
    } else if (res.status === 409) {
      error.value = '이미 사용 중인 이메일입니다.'
    } else {
      error.value = '회원가입에 실패했습니다. 다시 시도해주세요.'
    }
  } catch {
    error.value = '네트워크 오류가 발생했습니다.'
  } finally {
    submitting.value = false
  }
}
</script>
