<template>
    <RouterLink :to="to" class="block px-4 py-3 rounded-xl transition font-medium" :class="isActive
        ? 'bg-white text-slate-900'
        : 'text-slate-200 hover:bg-slate-800 hover:text-white'">

        {{ label }}

    </RouterLink>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const props = defineProps<{
    to: string
    label: string
}>()

const route = useRoute()

const isActive = computed(() =>
    route.path === props.to
)
</script>