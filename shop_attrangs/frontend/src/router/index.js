import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CategoryView from '../views/CategoryView.vue'
import ProductDetailView from '../views/ProductDetailView.vue'
import LoginView from '../views/LoginView.vue'
import JoinView from '../views/JoinView.vue'
import CartView from '../views/CartView.vue'
import WishlistView from '../views/WishlistView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/category/:category', name: 'category', component: CategoryView },
    { path: '/product/:id', name: 'product', component: ProductDetailView },
    { path: '/cart', name: 'cart', component: CartView },
    { path: '/wishlist', name: 'wishlist', component: WishlistView },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/join', name: 'join', component: JoinView }
  ]
})

export default router