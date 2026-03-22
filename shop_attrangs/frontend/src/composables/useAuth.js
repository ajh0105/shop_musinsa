import { ref } from 'vue'
import { api } from '../api'
import { clearWishlist } from './useWishCart.js'

// 모듈 스코프 싱글턴 상태 (전체 앱에서 공유)
const isLoggedIn = ref(!!localStorage.getItem('at_loginId'))
const userName  = ref(localStorage.getItem('at_userName') || '')
const loginId   = ref(localStorage.getItem('at_loginId') || '')
const role      = ref(localStorage.getItem('at_role') || '')

export function useAuth() {
  function setLogin({ loginId: id, name, role: r }) {
    isLoggedIn.value = true
    loginId.value    = id
    userName.value   = name
    role.value       = r
    localStorage.setItem('at_loginId',  id)
    localStorage.setItem('at_userName', name)
    localStorage.setItem('at_role',     r)
  }

  function clearLogin() {
    isLoggedIn.value = false
    loginId.value    = ''
    userName.value   = ''
    role.value       = ''
    localStorage.removeItem('at_loginId')
    localStorage.removeItem('at_userName')
    localStorage.removeItem('at_role')
    clearWishlist()
  }

  async function checkLoginStatus() {
    try {
      const { data } = await api.get('/account/check')
      if (data.loggedIn) {
        setLogin({ loginId: data.loginId, name: data.name, role: data.role })
      } else {
        clearLogin()
      }
    } catch {
      clearLogin()
    }
  }

  async function logout() {
    try { await api.post('/account/logout') } catch {}
    clearLogin()
  }

  return { isLoggedIn, userName, loginId, role, setLogin, clearLogin, checkLoginStatus, logout }
}
