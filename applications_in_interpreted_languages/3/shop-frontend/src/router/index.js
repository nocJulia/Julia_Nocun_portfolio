import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

import ProductList from '../components/ProductList.vue'
import ShoppingCart from '../components/ShoppingCart.vue'
import OrderList from '../components/OrderList.vue'
import ProductEdit from '../components/ProductEdit.vue'
import LoginForm from '../components/LoginForm.vue'
import RegisterForm from '../components/RegisterForm.vue'
import DatabaseInit from '../components/DatabaseInit.vue'

const routes = [
    {
        path: '/',
        component: ProductList
    },
    {
        path: '/cart',
        component: ShoppingCart
    },
    {
        path: '/orders',
        component: OrderList,
        meta: { requiresAuth: true }
    },
    {
        path: '/product/edit/:id',
        component: ProductEdit,
        meta: { requiresAuth: true, requiresEmployee: true }
    },
    {
        path: '/login',
        component: LoginForm
    },
    {
        path: '/register',
        component: RegisterForm
    },
    {
        path: '/init',
        component: DatabaseInit,
        meta: {
            requiresAuth: true,
            requiresEmployee: true
        }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()

    if (to.meta.requiresAuth && !authStore.isAuthenticated) {
        next({
            path: '/login',
            query: { redirect: to.fullPath }
        })
        return
    }

    if (to.meta.requiresEmployee && !authStore.isEmployee) {
        next('/')
        return
    }

    next()
})

export default router

