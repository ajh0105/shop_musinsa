<template>
  <header class="top-header">
    <div class="top-row">
      <router-link to="/" class="logo">ATTRANGS</router-link>
      <div class="menu-links">
        <router-link to="/cart">장바구니</router-link>
        <router-link to="/wishlist">♡</router-link>
        <router-link to="/login">로그인</router-link>
        <router-link to="/join">회원가입</router-link>
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
import { CATEGORIES } from '../data/mockItems'

const categories = CATEGORIES
</script>
