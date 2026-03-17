import { ref } from 'vue'
import { api } from '../api'

// 싱글턴 - 모든 컴포넌트에서 공유
const wishedIds = ref(new Set(JSON.parse(localStorage.getItem('attrangs_wish') || '[]')))

function saveWish() {
  localStorage.setItem('attrangs_wish', JSON.stringify([...wishedIds.value]))
}

export function useWishCart() {
  function isWished(itemId) {
    return wishedIds.value.has(Number(itemId))
  }

  async function toggleWish(item, event) {
    if (event) event.stopPropagation()
    const id = Number(item.id)
    const wasWished = wishedIds.value.has(id)

    if (wasWished) {
      wishedIds.value.delete(id)
    } else {
      wishedIds.value.add(id)
      try {
        await api.post('/cart/item', { itemId: id, quantity: 1 })
      } catch {
        // 백엔드 미연결 시에도 하트 상태는 유지
      }
    }
    wishedIds.value = new Set(wishedIds.value)
    saveWish()
    return !wasWished // true = 장바구니에 추가됨
  }

  return { isWished, toggleWish }
}
