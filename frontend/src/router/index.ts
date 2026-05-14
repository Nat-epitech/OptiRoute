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
        component: LoginView
    },
    {
        path: '/',
        component: MainLayout,
        children: [
            {
                path: 'dashboard',
                component: DashboardView
            },
            {
                path: 'users',
                component: UsersView
            },
            {
                path: 'drivers',
                component: DriversView
            }
        ]
    },
    {
        path: '/maps',
        component: MapLayout,
        children: [
            { path: '', component: MapsView }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()
    const publicPages = ['/login']
    const authRequired = !publicPages.includes(to.path)

    if (authRequired && !authStore.isAuthenticated) {
        return next('/login')
    }

    next()
})

export default router