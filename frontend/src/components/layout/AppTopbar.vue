<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const handleLogout = () => {
    authStore.logout()
    router.push('/login')
}
</script>

<template>

    <header class="h-16 bg-white border-b flex items-center justify-between px-6 shadow-sm">

        <div>
            <h2 class="text-xl font-semibold text-gray-800">
                OptiRoute Dashboard
            </h2>
        </div>

        <button @click="handleLogout" class="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-xl transition">
            Logout
        </button>

    </header>

</template>