import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CategoryView from '../views/CategoryView.vue'
import ProductDetailView from '../views/ProductDetailView.vue'
import LoginView from '../views/LoginView.vue'
import JoinView from '../views/JoinView.vue'
import CartView from '../views/CartView.vue'
import WishlistView from '../views/WishlistView.vue'
import CheckoutView from '../views/CheckoutView.vue'
import MyPageView from '../views/MyPageView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/category/:category', name: 'category', component: CategoryView },
    { path: '/product/:id', name: 'product', component: ProductDetailView },
    { path: '/cart', name: 'cart', component: CartView },
    { path: '/wishlist', name: 'wishlist', component: WishlistView },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/join', name: 'join', component: JoinView },
    { path: '/checkout', name: 'checkout', component: CheckoutView, meta: { requiresAuth: true } },
    { path: '/mypage', name: 'mypage', component: MyPageView, meta: { requiresAuth: true } },

    // 관리자 라우트
    {
      path: '/admin',
      component: () => import('../views/admin/AdminLayout.vue'),
      meta: { requiresAdmin: true },
      children: [
        { path: '', redirect: '/admin/dashboard' },
        { path: 'dashboard', component: () => import('../views/admin/Dashboard.vue') },
        { path: 'members', component: () => import('../views/admin/Members.vue') },
        { path: 'products', component: () => import('../views/admin/Products.vue') },
        { path: 'orders', component: () => import('../views/admin/Orders.vue') },
        { path: 'reviews', component: () => import('../views/admin/Reviews.vue') },
        { path: 'qna', component: () => import('../views/admin/QnA.vue') }
      ]
    }
  ],
  scrollBehavior() {
    return { top: 0 }
  }
})

router.beforeEach((to, from, next) => {
  const role = localStorage.getItem('at_role')
  const loginId = localStorage.getItem('at_loginId')

  if (to.meta.requiresAdmin && role !== 'ROLE_ADMIN') {
    return next('/login')
  }
  if (to.meta.requiresAuth && !loginId) {
    return next('/login')
  }
  next()
})

export default router
