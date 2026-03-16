<template>
  <header class="site-header">
    <!-- 상단 유틸리티 바 -->
    <div class="header-top">
      <div class="header-top-inner">
        <span class="header-notice">✦ 벙딸리제 신규 회원 첫 구매 15% 할인 쿠폰 증정 ✦</span>
        <div class="header-top-actions">
          <template v-if="isLoggedIn">
            <span class="header-top-user">{{ userName || loginId }} 님</span>
            <RouterLink v-if="isAdmin()" to="/admin" class="btn-text admin-link">관리자</RouterLink>
            <RouterLink to="/mypage" class="btn-text">마이페이지</RouterLink>
            <button class="btn-text" @click="handleLogout">로그아웃</button>
          </template>
          <template v-else>
            <RouterLink to="/login" class="btn-text">로그인</RouterLink>
            <RouterLink to="/register" class="btn-text">회원가입</RouterLink>
          </template>
        </div>
      </div>
    </div>

    <!-- 메인 헤더 (좌: 로고 | 중: 검색 | 우: 아이콘) -->
    <div class="header-main">
      <div class="header-main-inner">
        <!-- 좌측 로고 -->
        <RouterLink to="/" class="header-logo">
          <span class="logo-main">VENTALIZE</span>
          <span class="logo-sub">벙딸리제</span>
        </RouterLink>

        <!-- 중앙 검색 -->
        <div class="header-search">
          <form class="search-form" @submit.prevent="handleSearch">
            <input
              v-model="searchQuery"
              type="search"
              placeholder="스카프, 향수, 가방 등 검색"
              class="search-input"
            />
            <button type="submit" class="search-btn">
              <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="11" cy="11" r="8"/><line x1="21" y1="21" x2="16.65" y2="16.65"/>
              </svg>
            </button>
          </form>
        </div>

        <!-- 우측 아이콘 메뉴 -->
        <div class="header-icons">
          <RouterLink to="/mypage" class="icon-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
            <span>MY</span>
          </RouterLink>
          <RouterLink to="/cart" class="icon-btn">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8">
              <path d="M6 2L3 6v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V6l-3-4z"/>
              <line x1="3" y1="6" x2="21" y2="6"/>
              <path d="M16 10a4 4 0 0 1-8 0"/>
            </svg>
            <span>장바구니</span>
          </RouterLink>
        </div>
      </div>
    </div>

    <!-- 카테고리 네비게이션 (중앙 배치) -->
    <nav class="header-nav">
      <div class="header-nav-inner">
        <RouterLink
          v-for="cat in categories"
          :key="cat.name"
          :to="cat.to"
          class="nav-link"
          :class="{ 'nav-link--sale': cat.name === 'SALE', 'nav-link--board': cat.name === 'BOARD' }"
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

const { isLoggedIn, loginId, userName, isAdmin, clearLogin } = useAuth()
const router = useRouter()
const searchQuery = ref('')

function handleSearch() {
  if (searchQuery.value.trim()) {
    router.push({ path: '/category/ALL', query: { keyword: searchQuery.value.trim() } })
  }
}

const categories = [
  { name: 'SCARVES',       label: 'SCARVES',      to: '/category/SCARVES' },
  { name: 'READY_TO_WEAR', label: 'READY TO WEAR', to: '/category/READY_TO_WEAR' },
  { name: 'PERFUME',       label: 'PERFUME',       to: '/category/PERFUME' },
  { name: 'ACC',           label: 'ACC',           to: '/category/ACC' },
  { name: 'BAG',           label: 'BAG',           to: '/category/BAG' },
  { name: 'SALE',          label: 'SALE',          to: '/category/SALE' },
  { name: 'BOARD',         label: 'BOARD',         to: '/board' },
]

async function handleLogout() {
  try {
    await fetch('/v1/api/account/logout', { method: 'POST', credentials: 'include' })
  } catch {}
  clearLogin()
  router.push('/')
}
</script>

<style scoped>
.site-header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #fff;
  box-shadow: 0 1px 8px rgba(0,0,0,0.08);
}

/* ── 상단 유틸리티 바 ── */
.header-top {
  background: #1a1a1a;
  color: #ccc;
  font-size: 12px;
}
.header-top-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 7px 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-notice { color: #d4af7a; letter-spacing: 0.5px; }
.header-top-actions { display: flex; gap: 16px; align-items: center; }
.header-top-user { color: #fff; font-weight: 600; }
.btn-text {
  background: none; border: none; color: #aaa;
  cursor: pointer; font-size: 12px; padding: 0;
  text-decoration: none;
  transition: color .2s;
}
.btn-text:hover { color: #fff; }
.admin-link { color: #d4af7a !important; font-weight: 700; }

/* ── 메인 헤더 ── */
.header-main { border-bottom: 1px solid #f0f0f0; }
.header-main-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
  height: 72px;
  display: flex;
  align-items: center;
  gap: 24px;
}

/* 로고 */
.header-logo {
  text-decoration: none;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  flex-shrink: 0;
  min-width: 130px;
}
.logo-main {
  font-family: 'Georgia', serif;
  font-size: 22px;
  font-weight: 700;
  color: #1a1a1a;
  letter-spacing: 4px;
  line-height: 1;
}
.logo-sub {
  font-size: 10px;
  color: #888;
  letter-spacing: 2px;
  margin-top: 2px;
}

/* 검색 */
.header-search { flex: 1; max-width: 480px; margin: 0 auto; }
.search-form {
  display: flex;
  align-items: center;
  border: 1.5px solid #ddd;
  border-radius: 24px;
  overflow: hidden;
  transition: border-color .2s;
}
.search-form:focus-within { border-color: #1a1a1a; }
.search-input {
  flex: 1; border: none; outline: none;
  padding: 9px 16px; font-size: 13px; background: transparent;
}
.search-btn {
  background: none; border: none; cursor: pointer;
  padding: 8px 14px; color: #666;
  display: flex; align-items: center;
}

/* 아이콘 메뉴 */
.header-icons { display: flex; gap: 8px; align-items: center; flex-shrink: 0; }
.icon-btn {
  display: flex; flex-direction: column; align-items: center;
  gap: 2px; text-decoration: none; color: #333;
  padding: 6px 10px; border-radius: 6px;
  font-size: 10px; transition: background .2s, color .2s;
}
.icon-btn:hover { background: #f5f5f5; color: #000; }

/* ── 카테고리 네비게이션 ── */
.header-nav { border-top: none; }
.header-nav-inner {
  max-width: 1280px;
  margin: 0 auto;
  padding: 0 24px;
  display: flex;
  justify-content: center;
  gap: 0;
}
.nav-link {
  text-decoration: none;
  color: #444;
  font-size: 13px;
  font-weight: 600;
  letter-spacing: 0.5px;
  padding: 12px 20px;
  border-bottom: 2px solid transparent;
  transition: color .2s, border-color .2s;
  white-space: nowrap;
}
.nav-link:hover { color: #000; border-bottom-color: #1a1a1a; }
.nav-link--active { color: #000; border-bottom-color: #1a1a1a; }
.nav-link--sale { color: #c0392b; }
.nav-link--sale:hover { color: #922b21; border-bottom-color: #c0392b; }
.nav-link--board { color: #5c6bc0; }
.nav-link--board:hover { color: #3949ab; border-bottom-color: #5c6bc0; }
</style>
