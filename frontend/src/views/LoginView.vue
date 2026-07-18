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

            <!-- LOGO -->
            <div class="h-16 flex items-center justify-center px-4 bg-white">

                <img src="@/assets/images/logo.png" alt="OptiRoute logo" class="h-8 w-auto object-contain" />

            </div>

            <p class="text-center text-gray-500 mb-8">Connectez-vous à votre compte</p>

            <div class="space-y-4">

                <input v-model="email" type="email" placeholder="Email"
                    class="w-full px-4 py-3 border rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500" />

                <input v-model="password" type="password" placeholder="Mot de passe"
                    class="w-full px-4 py-3 border rounded-xl focus:outline-none focus:ring-2 focus:ring-blue-500" />

                <button @click="handleLogin"
                    class="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 rounded-xl font-semibold transition-all duration-200 hover:scale-[1.02]">
                    Se connecter
                </button>

            </div>

        </div>

    </div>
</template>