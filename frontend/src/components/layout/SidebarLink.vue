<template>
    <RouterLink :to="to" class="flex items-center gap-3 rounded-xl px-4 py-3 font-medium transition" :class="isActive
        ? 'bg-white text-slate-900'
        : 'text-slate-200 hover:bg-slate-800 hover:text-white'">

        <component v-if="icon" :is="icon" class="h-4 w-4" />
        <span>{{ label }}</span>

    </RouterLink>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import type { Component } from 'vue'

const props = defineProps<{
    to: string
    label: string
    icon?: Component
}>()

const route = useRoute()

const isActive = computed(() =>
    route.path === props.to
)
</script>