<template>
  <header class="top-header">
    <div class="top-row">
      <router-link to="/" class="logo">ATTRANGS</router-link>
      <div class="menu-links">
        <router-link to="/cart">장바구니</router-link>
        <router-link to="/wishlist">♡</router-link>
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
    </nav>
  </header>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { CATEGORIES } from '../data/mockItems'
import { useAuth } from '../composables/useAuth.js'

const categories = CATEGORIES
const router = useRouter()
const { isLoggedIn, userName, role, logout } = useAuth()
const isAdmin = computed(() => role.value === 'ROLE_ADMIN')

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
</style>
