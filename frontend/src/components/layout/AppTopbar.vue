<script setup lang="ts">
import { useAuthStore } from '@/stores/authStore'
import { useRoute, useRouter } from 'vue-router'
import { computed, ref } from 'vue'
import { UserRound } from 'lucide-vue-next'
import AppDropdown from '@/components/ui/AppDropdown.vue'

// Variables

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()
const userMenuOpen = ref(false)

const currentUserName = computed(() =>
    `${authStore.firstName} ${authStore.lastName}`.trim() || 'Mon compte'
)

const isRoutesSection = computed(() =>
    route.path.startsWith('/routes')
)

const isHomepageSection = computed(() =>
    route.path.startsWith('/homepage')
)

const isGestionSection = computed(() =>
    route.path.startsWith('/gestion')
)

const isAdminSection = computed(() =>
    route.path.startsWith('/admin')
)

// Functions

function goToRoutes() {
    router.push('/routes')
}

function goToHomepage() {
    router.push('/homepage')
}

function goToGestion() {
    router.push('/gestion')
}

function goToAdmin() {
    router.push('/admin')
}

function handleLogout() {
    userMenuOpen.value = false
    authStore.logout()
    router.push('/login')
}

function goToAccount(close: () => void) {
    close()
    router.push('/account/account')
}

</script>

<template>
    <header class="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6 shadow-sm">

        <!-- LEFT -->
        <div class="flex items-center gap-3">

            <button class="px-4 py-2 rounded-xl font-medium transition" :class="isHomepageSection
                ? 'bg-slate-900 text-white'
                : 'text-slate-600 hover:bg-slate-100'" @click="goToHomepage">
                Planification
            </button>

            <button class="px-4 py-2 rounded-xl font-medium transition" :class="isGestionSection
                ? 'bg-slate-900 text-white'
                : 'text-slate-600 hover:bg-slate-100'" @click="goToGestion">
                Gestion
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

            <AppDropdown v-model:open="userMenuOpen">
                <template #default="{ close }">
                    <button type="button"
                        class="flex w-full items-center gap-2 px-4 py-2 text-left text-sm text-slate-700 hover:bg-slate-100"
                        @click="goToAccount(close)">
                        Mon compte
                    </button>

                    <button type="button"
                        class="flex w-full items-center gap-2 px-4 py-2 text-left text-sm text-red-600 hover:bg-red-50"
                        @click="handleLogout(); close()">
                        Se déconnecter
                    </button>
                </template>

                <template #trigger="{ toggle }">
                    <button type="button"
                        class="inline-flex items-center gap-2 rounded-xl px-3 py-2 text-slate-700 transition hover:bg-slate-100"
                        aria-label="Menu utilisateur" @click.stop="toggle">
                        <UserRound class="h-5 w-5 text-slate-600" />
                        <span class="font-medium">{{ currentUserName }}</span>
                    </button>
                </template>
            </AppDropdown>

        </div>

    </header>
</template>