<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ChevronDown, KeyRound, LogOut, CircleUser } from 'lucide-vue-next'
import SidebarLink from '@/components/layout/SidebarLink.vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/authStore'

// Variables

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const isGestionSection = computed(() =>
    route.path.startsWith('/gestion')
)

const isRoutesSection = computed(() =>
    route.path.startsWith('/routes')
)

const isHomepageSection = computed(() =>
    route.path.startsWith('/homepage')
)

const isUserSection = computed(() =>
    route.path.startsWith('/user')
)

const isAdminSection = computed(() =>
    route.path.startsWith('/admin')
)

const isVehiclesSection = computed(() =>
    route.path.startsWith('/gestion/tractors') || route.path.startsWith('/gestion/semiTrailers')
)

const vehiclesOpen = ref(isVehiclesSection.value)

const logout = () => {
    authStore.logout()
    router.push('/login')
}

watch(isVehiclesSection, (isActive) => {
    if (isActive) {
        vehiclesOpen.value = true
    }
})
</script>

<template>
    <aside class="flex h-full w-64 flex-col bg-slate-900 text-white">

        <!-- LOGO -->
        <div class="flex h-16 items-center justify-center border-b border-r border-gray-200 bg-white px-4">
            <img src="@/assets/images/logo.png" alt="OptiRoute logo" class="h-8 w-auto object-contain" />
        </div>

        <!-- NAVIGATION -->
        <nav class="flex-1 space-y-2 overflow-y-auto p-4">

            <!-- ROUTES SECTION -->
            <template v-if="isRoutesSection">
                <SidebarLink to="/routes" label="Rechercher un itinéraire" />
            </template>

            <!-- HOMEPAGE SECTION -->
            <template v-else-if="isHomepageSection">
                <SidebarLink to="/homepage/planning" label="Planning" />
            </template>


            <!-- GESTION SECTION -->
            <template v-else-if="isGestionSection">
                <SidebarLink to="/gestion/operationCosts" label="Coûts référentiels" />

                <SidebarLink to="/gestion/drivers" label="Conducteurs" />

                <!-- VEHICLES MENU -->
                <div>
                    <button type="button"
                        class="flex w-full items-center justify-between rounded-xl px-4 py-3 text-left font-medium transition text-slate-200 hover:bg-slate-800 hover:text-white"
                        :aria-expanded="vehiclesOpen" @click="vehiclesOpen = !vehiclesOpen">
                        <span>Véhicules</span>

                        <ChevronDown class="h-4 w-4 transition-transform duration-200"
                            :class="{ 'rotate-180': vehiclesOpen }" />
                    </button>

                    <div v-show="vehiclesOpen" class="mt-1 space-y-1 border-l border-slate-700 pl-3">
                        <SidebarLink to="/gestion/tractors" label="Tracteurs" />

                        <SidebarLink to="/gestion/semiTrailers" label="Semi-remorques" />
                    </div>
                </div>

                <SidebarLink to="/gestion/customers" label="Donneurs d’ordres" />
            </template>

            <!-- USER SECTION -->
            <template v-else-if="isUserSection">
                <SidebarLink to="/user" label="Mes informations" :icon="CircleUser" />

                <SidebarLink to="/user/password" label="Modifier mon mot de passe" :icon="KeyRound" />

                <button type="button"
                    class="flex w-full items-center gap-3 rounded-xl px-4 py-3 text-left font-medium text-slate-200 transition hover:bg-slate-800 hover:text-white"
                    @click="logout">
                    <LogOut class="h-4 w-4" />
                    <span>Se déconnecter</span>
                </button>
            </template>

            <!-- ADMIN SECTION -->
            <template v-else-if="isAdminSection">
                <SidebarLink to="/admin/homepage" label="Homepage" />
            </template>

        </nav>

    </aside>
</template>