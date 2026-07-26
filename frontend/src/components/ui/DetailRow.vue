<script setup lang="ts">
defineProps<{
    label: string
    value: string
}>()
</script>

<template>
    <div class="flex items-start justify-between gap-6 py-3.5">
        <span class="text-sm text-slate-500">
            {{ label }}
        </span>

        <span class="text-right text-sm font-medium text-slate-900">
            {{ value }}
        </span>
    </div>
</template>