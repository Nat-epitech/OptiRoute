<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>
    <aside class="w-64 bg-gray-900 text-white flex flex-col h-full">

        <!-- LOGO SECTION -->
        <div class="h-16 flex items-center justify-center px-4
                   bg-white border-b border-gray-200 border-r border-r-gray-200">

            <img src="@/assets/images/logo.png" alt="OptiRoute logo" class="h-12 w-auto object-contain" />

        </div>

        <!-- NAVIGATION -->
        <nav class="flex-1 p-4 space-y-2">

            <RouterLink to="/dashboard" class="block px-4 py-3 rounded-xl hover:bg-gray-800 transition">
                Dashboard
            </RouterLink>

            <RouterLink to="/users" class="block px-4 py-3 rounded-xl hover:bg-gray-800 transition">
                Users
            </RouterLink>

            <RouterLink to="/drivers" class="block px-4 py-3 rounded-xl hover:bg-gray-800 transition">
                Drivers
            </RouterLink>

            <RouterLink to="/maps" class="block px-4 py-3 rounded-xl hover:bg-gray-800 transition">
                Maps
            </RouterLink>

        </nav>

    </aside>
</template>