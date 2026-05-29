<script setup lang="ts">
import { ref } from 'vue'
import { login } from '@/api/authApi'
import { useAuthStore } from '@/stores/authStore'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')

const authStore = useAuthStore()
const router = useRouter()

const handleLogin = async () => {
    try {
        const response = await login(email.value, password.value)
        authStore.setToken(response.token)
        router.push('/planning/dashboard')
    } catch (e) {
        alert('Invalid credentials')
    }
}
</script>

<template>
    <div class="min-h-screen flex items-center justify-center bg-gradient-to-br from-gray-100 to-gray-300">

        <div class="w-full max-w-md bg-white rounded-2xl shadow-xl p-8">

            <h1 class="text-3xl font-bold text-center text-gray-800 mb-2">OptiRoute</h1>

            <p class="text-center text-gray-500 mb-8">Sign in to your account</p>

            <div class="space-y-4">

                <input v-model="email" type="email" placeholder="Email"
                    class="w-full px-4 py-3 border rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500" />

                <input v-model="password" type="password" placeholder="Password"
                    class="w-full px-4 py-3 border rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500" />

                <button @click="handleLogin"
                    class="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-xl font-semibold transition-all duration-200 hover:scale-[1.02]">
                    Login
                </button>

            </div>

        </div>

    </div>
</template>