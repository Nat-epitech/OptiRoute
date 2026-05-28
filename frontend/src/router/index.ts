import { createRouter, createWebHistory } from 'vue-router'

import DashboardView from '@/views/DashboardView.vue'
import UsersView from '@/views/UsersView.vue'
import LoginView from '@/views/LoginView.vue'
import DriversView from '@/views/DriversView.vue'
import MapsView from '@/views/MapsView.vue'

import MainLayout from '@/layouts/MainLayout.vue'
import MapLayout from '@/layouts/MapLayout.vue'

import { useAuthStore } from '@/stores/authStore'

const routes = [
    {
        path: '/login',
        name: 'login',
        component: LoginView,
        meta: { public: true }
    },

    {
        path: '/',
        redirect: '/dashboard'
    },

    {
        path: '/',
        component: MainLayout,
        meta: { requiresAuth: true },

        children: [
            {
                path: 'dashboard',
                name: 'dashboard',
                component: DashboardView
            },
            {
                path: 'users',
                name: 'users',
                component: UsersView
            },
            {
                path: 'drivers',
                name: 'drivers',
                component: DriversView
            }
        ]
    },

    {
        path: '/maps',
        component: MapLayout,
        meta: { requiresAuth: true },

        children: [
            {
                path: '',
                name: 'maps',
                component: MapsView
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()

    const requiresAuth = to.matched.some(
        record => record.meta.requiresAuth
    )

    const isPublic = to.matched.some(
        record => record.meta.public
    )

    if (requiresAuth && !authStore.isAuthenticated) {
        return next('/login')
    }

    if (isPublic && authStore.isAuthenticated) {
        return next('/dashboard')
    }

    next()
})

export default router