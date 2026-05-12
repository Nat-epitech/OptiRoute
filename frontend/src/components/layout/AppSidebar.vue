<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>

<template>

    <aside class="w-64 bg-gray-900 text-white flex flex-col">

        <div class="h-16 flex items-center px-6 border-b border-gray-800">
            <h1 class="text-2xl font-bold tracking-wide">
                OptiRoute
            </h1>
        </div>

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
        </nav>

    </aside>

</template>