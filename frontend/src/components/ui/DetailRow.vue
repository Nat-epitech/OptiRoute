<script setup lang="ts">
withDefaults(
    defineProps<{
        label: string
        value: string
        breakValue?: boolean
    }>(),
    {
        breakValue: false,
    },
)
</script>

<template>
    <div class="flex items-start justify-between gap-6 py-3.5">
        <span class="shrink-0 text-sm text-slate-500">
            {{ label }}
        </span>

        <span class="text-right text-sm font-medium text-slate-900" :class="{
            'max-w-[60%] break-all': breakValue
        }">
            {{ value }}
        </span>
    </div>
</template>