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
        redirect: '/planning/dashboard'
    },

    {
        path: '/planning',
        redirect: '/planning/dashboard'
    },

    {
        path: '/admin',
        redirect: '/admin/users'
    },

    // MAIN APP LAYOUT (padding / standard pages)

    {
        path: '/',
        component: MainLayout,
        meta: { requiresAuth: true },

        children: [
            {
                path: 'planning',
                children: [
                    {
                        path: 'dashboard',
                        name: 'planning-dashboard',
                        component: DashboardView
                    },
                    {
                        path: 'drivers',
                        name: 'drivers',
                        component: DriversView
                    }
                ]
            },

            {
                path: 'admin',
                children: [
                    {
                        path: 'users',
                        name: 'admin-users',
                        component: UsersView
                    }
                ]
            }
        ]
    },

    // MAP LAYOUT (full screen / no padding)

    {

        path: '/',
        component: MapLayout,
        meta: { requiresAuth: true },

        children: [
            {
                path: 'routes',
                children: [
                    {
                        path: '',
                        name: 'routes',
                        component: MapsView
                    }
                ]
            }
        ]
    }
]

// Router configurations

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
        return next('/planning/dashboard')
    }

    next()
})

export default router