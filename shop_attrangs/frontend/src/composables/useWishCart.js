import { ref, computed } from 'vue'
import { api } from '../api'

// 싱글턴 - 모든 컴포넌트에서 공유
const wishedIds = ref(new Set(JSON.parse(localStorage.getItem('attrangs_wish') || '[]')))

function saveLocal() {
  localStorage.setItem('attrangs_wish', JSON.stringify([...wishedIds.value]))
}

// 로그인 상태면 서버에서 찜 목록을 불러와 동기화
export async function syncWishlistFromServer() {
  try {
    const { data } = await api.get('/wishlist/ids')
    if (Array.isArray(data)) {
      wishedIds.value = new Set(data.map(Number))
      saveLocal()
    }
  } catch {
    // 비로그인 또는 오류 시 로컬 상태 유지
  }
}

// 로그아웃 시 찜 목록 초기화
export function clearWishlist() {
  wishedIds.value = new Set()
  localStorage.removeItem('attrangs_wish')
}

// 앱 로드 시 로그인 상태라면 즉시 동기화
if (localStorage.getItem('at_loginId')) {
  syncWishlistFromServer()
}

export function useWishCart() {
  function isWished(itemId) {
    return wishedIds.value.has(Number(itemId))
  }

  const wishCount = computed(() => wishedIds.value.size)

  async function toggleWish(item, event) {
    if (event) event.stopPropagation()
    const id = Number(item.id)
    const wasWished = wishedIds.value.has(id)

    // 낙관적 업데이트 (즉시 UI 반영)
    if (wasWished) {
      wishedIds.value.delete(id)
    } else {
      wishedIds.value.add(id)
    }
    wishedIds.value = new Set(wishedIds.value)
    saveLocal()

    // 백엔드 동기화
    try {
      await api.post('/wishlist/toggle', { itemId: id })
    } catch {
      // 실패 시 롤백
      if (wasWished) {
        wishedIds.value.add(id)
      } else {
        wishedIds.value.delete(id)
      }
      wishedIds.value = new Set(wishedIds.value)
      saveLocal()
    }

    return !wasWished // true = 찜 추가됨
  }

  return { isWished, toggleWish, wishCount }
}
