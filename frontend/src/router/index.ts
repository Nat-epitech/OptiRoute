import { createRouter, createWebHistory } from 'vue-router'

import DashboardView from '@/views/DashboardView.vue'
import UsersView from '@/views/UsersView.vue'
import LoginView from '@/views/LoginView.vue'
import DriversView from '@/views/DriversView.vue'
import MapsView from '@/views/MapsView.vue'
import PlanningView from '@/views/PlanningView.vue'
import CustomersView from '@/views/CustomersView.vue'
import TractorsView from '@/views/vehicle/TractorsView.vue'
import SemiTrailers from '@/views/vehicle/SemiTrailersView.vue'

import MainLayout from '@/layouts/MainLayout.vue'
import MapLayout from '@/layouts/MapLayout.vue'
import PlanningLayout from '@/layouts/PlanningLayout.vue'

import { useAuthStore } from '@/stores/authStore'


const routes = [
    {
        path: '/login',
        name: 'login',
        component: LoginView,
        meta: { public: true }
    },

    // REDIRECTION

    {
        path: '/',
        redirect: '/homepage/planning'
    },

    {
        path: '/homepage',
        redirect: '/homepage/planning'
    },

    {
        path: '/gestion',
        redirect: '/gestion/dashboard'
    },

    {
        path: '/gestion',
        redirect: '/gestion/dashboard'
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
                path: 'gestion',
                children: [
                    {
                        path: 'dashboard',
                        name: 'dashboard',
                        component: DashboardView
                    },
                    {
                        path: 'drivers',
                        name: 'drivers',
                        component: DriversView
                    },
                    {
                        path: 'tractors',
                        name: 'tractors',
                        component: TractorsView
                    },
                    {
                        path: 'semiTrailers',
                        name: 'semiTrailers',
                        component: SemiTrailers
                    },
                    {
                        path: 'customers',
                        name: 'customers',
                        component: CustomersView
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
    },

    // PLANNING LAYOUT (full screen / no padding)

    {

        path: '/homepage',
        component: PlanningLayout,
        meta: { requiresAuth: true },

        children: [
            {
                path: 'planning',
                children: [
                    {
                        path: '',
                        name: 'planning',
                        component: PlanningView
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