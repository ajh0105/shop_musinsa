<template>
  <header class="top-header">
    <div class="top-row">
      <router-link to="/" class="logo">ATTRANGS</router-link>
      <div class="menu-links">
        <router-link to="/cart">장바구니</router-link>
        <router-link to="/wishlist" class="wish-link">
          <span class="wish-icon">♡</span>
          <span v-if="wishCount > 0" class="wish-badge">{{ wishCount }}</span>
        </router-link>
        <template v-if="isLoggedIn">
          <span class="user-name">{{ userName }}</span>
          <router-link to="/mypage">마이페이지</router-link>
          <router-link v-if="isAdmin" to="/admin" class="admin-link">관리자</router-link>
          <button class="link-btn" @click="handleLogout">로그아웃</button>
        </template>
        <template v-else>
          <router-link to="/login">로그인</router-link>
          <router-link to="/join">회원가입</router-link>
        </template>
      </div>
    </div>
    <nav class="category-nav">
      <router-link
        v-for="category in categories"
        :key="category"
        :to="`/category/${category}`"
      >
        {{ category }}
      </router-link>
      <router-link to="/community" class="community-link">리뷰/Q&A</router-link>
    </nav>
  </header>
</template>

<script setup>
import { computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { CATEGORIES } from '../data/mockItems'
import { useAuth } from '../composables/useAuth.js'
import { useWishCart, syncWishlistFromServer } from '../composables/useWishCart.js'

const categories = CATEGORIES
const router = useRouter()
const { isLoggedIn, userName, role, logout } = useAuth()
const isAdmin = computed(() => role.value === 'ROLE_ADMIN')
const { wishCount } = useWishCart()

// 로그인 시 서버 찜 목록 동기화
watch(isLoggedIn, (loggedIn) => {
  if (loggedIn) syncWishlistFromServer()
})

async function handleLogout() {
  await logout()
  router.push('/')
}
</script>

<style scoped>
.user-name {
  font-size: 13px;
  color: #333;
  font-weight: 500;
}
.link-btn {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 13px;
  color: inherit;
  padding: 0;
  text-decoration: underline;
}
.wish-link {
  position: relative;
  display: inline-flex;
  align-items: center;
}
.wish-icon {
  font-size: 16px;
}
.wish-badge {
  position: absolute;
  top: -7px;
  right: -10px;
  background: #e53935;
  color: #fff;
  font-size: 10px;
  font-weight: 700;
  min-width: 16px;
  height: 16px;
  border-radius: 999px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 3px;
  line-height: 1;
}
.community-link {
  font-weight: 600;
  color: var(--point) !important;
  border-color: rgba(190,77,37,0.35) !important;
}
.community-link:hover {
  background: rgba(190,77,37,0.06);
}
</style>
