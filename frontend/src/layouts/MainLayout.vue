<script setup lang="ts">
import AppSidebar from '@/components/layout/AppSidebar.vue'
import AppTopbar from '@/components/layout/AppTopbar.vue'
</script>

<template>

    <div class="flex h-screen bg-gray-100">
        <AppSidebar />
        
        <div class="flex-1 flex flex-col overflow-hidden">
            <AppTopbar />

            <main class="flex-1 overflow-y-auto p-6">
                <router-view />
            </main>

        </div>
    </div>
</template>