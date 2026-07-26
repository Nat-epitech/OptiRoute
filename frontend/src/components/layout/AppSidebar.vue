<script setup lang="ts">
import { computed, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import { ChevronDown } from 'lucide-vue-next'
import SidebarLink from '@/components/layout/SidebarLink.vue'

// Variables

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

const isVehiclesSection = computed(() =>
    route.path.startsWith('/planning/tractors')
    || route.path.startsWith('/planning/semiTrailers')
)

const vehiclesOpen = ref(isVehiclesSection.value)

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

            <!-- PLANNING SECTION -->
            <template v-else-if="isPlanningSection">
                <SidebarLink to="/planning/dashboard" label="Dashboard" />

                <SidebarLink to="/planning/drivers" label="Conducteurs" />

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
                        <SidebarLink to="/planning/tractors" label="Tracteurs" />

                        <SidebarLink to="/planning/semiTrailers" label="Semi-remorques" />
                    </div>
                </div>

                <SidebarLink to="/planning/customers" label="Clients" />

                <SidebarLink to="/planning/schedule" label="Planning" />
            </template>

            <!-- ADMIN SECTION -->
            <template v-else-if="isAdminSection">
                <SidebarLink to="/admin/users" label="Utilisateurs" />
            </template>

        </nav>

    </aside>
</template>