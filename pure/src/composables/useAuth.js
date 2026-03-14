import { ref } from 'vue'

const loginId = ref(localStorage.getItem('loginId') || null)
const isLoggedIn = ref(!!loginId.value)

export function useAuth() {
  function setLogin(id) {
    loginId.value = id
    isLoggedIn.value = true
    localStorage.setItem('loginId', id)
  }

  function clearLogin() {
    loginId.value = null
    isLoggedIn.value = false
    localStorage.removeItem('loginId')
  }

  async function checkLoginStatus() {
    try {
      const res = await fetch('/v1/api/account/check')
      const data = await res.json()
      if (data === true) {
        isLoggedIn.value = true
      } else {
        clearLogin()
      }
    } catch {
      clearLogin()
    }
  }

  return { loginId, isLoggedIn, setLogin, clearLogin, checkLoginStatus }
}
