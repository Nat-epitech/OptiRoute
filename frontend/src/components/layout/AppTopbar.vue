<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore'
import { useRoute, useRouter } from 'vue-router'
import { computed } from 'vue'

// Variables

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

const isRoutesSection = computed(() =>
    route.path.startsWith('/routes')
)

const isPlanningSection = computed(() =>
    route.path.startsWith('/planning')
)

const isAdminSection = computed(() =>
    route.path.startsWith('/admin')
)

// Functions

function goToRoutes() {
    router.push('/routes')
}

function goToPlanning() {
    router.push('/planning')
}

function goToAdmin() {
    router.push('/admin')
}

function handleLogout() {
    authStore.logout()
    router.push('/login')
}

</script>

<template>
    <header class="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6 shadow-sm">

        <!-- LEFT -->
        <div class="flex items-center gap-3">

            <button class="px-4 py-2 rounded-xl font-medium transition" :class="isPlanningSection
                ? 'bg-slate-900 text-white'
                : 'text-slate-600 hover:bg-slate-100'" @click="goToPlanning">

                Planification
            </button>

            <button class="px-4 py-2 rounded-xl font-medium transition" :class="isRoutesSection
                ? 'bg-slate-900 text-white'
                : 'text-slate-600 hover:bg-slate-100'" @click="goToRoutes">

                Itinéraires
            </button>

            <button class="px-4 py-2 rounded-xl font-medium transition" :class="isAdminSection
                ? 'bg-slate-900 text-white'
                : 'text-slate-600 hover:bg-slate-100'" @click="goToAdmin">

                Admin
            </button>

        </div>

        <!-- RIGHT -->
        <div class="flex items-center gap-3">

            <button class="bg-slate-900 hover:bg-slate-800 text-white px-4 py-2 rounded-xl transition"
                @click="handleLogout">

                Logout
            </button>

        </div>

    </header>
</template>