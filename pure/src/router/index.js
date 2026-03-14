import { createRouter, createWebHistory } from 'vue-router'
import Home from '../views/Home.vue'
import Category from '../views/Category.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Cart from '../views/Cart.vue'
import MyPage from '../views/MyPage.vue'

const routes = [
  { path: '/', component: Home },
  { path: '/category/:name', component: Category },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/cart', component: Cart },
  { path: '/mypage', component: MyPage },
]

export default createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior() {
    return { top: 0 }
  }
})
