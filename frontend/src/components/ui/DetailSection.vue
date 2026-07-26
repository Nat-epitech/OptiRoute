<script setup lang="ts">
import type {
    Component,
} from "vue"

defineProps<{
    title: string
    icon?: Component
}>()
</script>

<template>
    <section>
        <div class="mb-3 flex items-center gap-2">
            <component :is="icon" v-if="icon" class="h-4 w-4 text-slate-500" />

            <h3 class="text-sm font-semibold uppercase tracking-wide text-slate-500">
                {{ title }}
            </h3>
        </div>

        <div class="divide-y divide-slate-100 rounded-2xl border border-slate-200 bg-white px-5">
            <slot />
        </div>
    </section>
</template>