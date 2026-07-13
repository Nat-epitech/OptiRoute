<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
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

</script>

<template>
    <aside class="w-64 bg-slate-900 text-white flex flex-col h-full">

        <!-- LOGO -->
        <div class="h-16 flex items-center justify-center px-4 bg-white border-b border-r border-gray-200">

            <img src="@/assets/images/logo.png" alt="OptiRoute logo" class="h-8 w-auto object-contain" />

        </div>

        <!-- NAVIGATION -->
        <nav class="flex-1 p-4 space-y-2 overflow-y-auto">

            <!-- ROUTES SECTION -->
            <template v-if="isRoutesSection">

                <SidebarLink to="/routes" label="Rechercher un itinéraire" />

            </template>

            <!-- PLANNING SECTION -->
            <template v-else-if="isPlanningSection">

                <SidebarLink to="/planning/dashboard" label="Dashboard" />

                <SidebarLink to="/planning/drivers" label="Conducteurs" />

                <SidebarLink to="/planning/vehicles" label="Véhicules" />

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