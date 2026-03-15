<template>
  <section class="section-wrap">
    <div class="section-head">
      <h2>{{ title }}</h2>
      <p>{{ subtitle }}</p>
    </div>
    <div class="card-grid">
      <router-link
        v-for="item in items"
        :key="item.id"
        :to="`/product/${item.id}`"
        class="item-card"
      >
        <img :src="item.imgPath" :alt="item.name" />
        <div class="item-info">
          <span>{{ item.brand }}</span>
          <h3>{{ item.name }}</h3>
          <p>{{ Number(item.salePrice ?? item.price).toLocaleString() }}원</p>
        </div>
      </router-link>
    </div>
  </section>
</template>

<script setup>
defineProps({
  title: String,
  subtitle: String,
  items: {
    type: Array,
    default: () => []
  }
})
</script>