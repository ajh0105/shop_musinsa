<template>
  <header class="site-header">
    <!-- 상단 유틸리티 바 -->
    <div class="header-top">
      <div class="header-top-inner">
        <span class="header-notice">무신사 신규 회원가입 시 10% 쿠폰 증정</span>
        <div class="header-top-actions">
          <template v-if="isLoggedIn">
            <span class="header-top-user">{{ loginId }}</span>
            <button class="btn-text" @click="handleLogout">로그아웃</button>
          </template>
          <template v-else>
            <RouterLink to="/login" class="btn-text">로그인</RouterLink>
            <RouterLink to="/register" class="btn-text">회원가입</RouterLink>
          </template>
        </div>
      </div>
    </div>

    <!-- 메인 헤더 -->
    <div class="header-main">
      <div class="header-main-inner">
        <!-- 로고 -->
        <RouterLink to="/" class="header-logo">
          <span class="logo-text">MUSINSA</span>
        </RouterLink>

        <!-- 검색 -->
        <div class="header-search">
          <form class="search-form" @submit.prevent>
            <input
              v-model="searchQuery"
              type="search"
              placeholder="브랜드, 상품, 카테고리 검색"
              class="search-input"
            />
            <button type="submit" class="search-btn">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
              </svg>
            </button>
          </form>
        </div>

        <!-- 우측 아이콘 메뉴 -->
        <div class="header-icons">
          <RouterLink to="/mypage" class="icon-btn">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
            <span>MY</span>
          </RouterLink>
          <RouterLink to="/cart" class="icon-btn cart-icon-btn">
            <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/>
              <line x1="3" y1="6" x2="21" y2="6"/>
              <path d="M16 10a4 4 0 0 1-8 0"/>
            </svg>
            <span>장바구니</span>
          </RouterLink>
        </div>
      </div>
    </div>

    <!-- 카테고리 네비게이션 -->
    <nav class="header-nav">
      <div class="header-nav-inner">
        <RouterLink
          v-for="cat in categories"
          :key="cat.name"
          :to="`/category/${cat.name}`"
          class="nav-link"
          :class="{ 'nav-link--sale': cat.name === 'OUTLET' }"
          active-class="nav-link--active"
        >
          {{ cat.label }}
        </RouterLink>
      </div>
    </nav>
  </header>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuth } from '../composables/useAuth.js'

const { isLoggedIn, loginId, clearLogin } = useAuth()
const router = useRouter()
const searchQuery = ref('')

const categories = [
  { name: 'OUTER', label: 'OUTER' },
  { name: 'TOP', label: 'TOP' },
  { name: 'PANTS', label: 'PANTS' },
  { name: 'SHOES', label: 'SHOES' },
  { name: 'BAG', label: 'BAG' },
  { name: 'ACC', label: 'ACC' },
  { name: 'OUTLET', label: 'OUTLET' },
]

async function handleLogout() {
  try {
    await fetch('/v1/api/account/logout', { method: 'POST', credentials: 'include' })
  } catch {}
  clearLogin()
  router.push('/')
}
</script>
