import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  base: '/shop_musinsa/',
  server: {
    proxy: {
      '/v1': {
        target: 'http://localhost:8085',
        changeOrigin: true
      }
    }
  }
})
