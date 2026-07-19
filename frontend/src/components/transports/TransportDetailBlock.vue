<template>
    <div>
        <p class="text-xs font-medium text-slate-400">
            {{ title }}
        </p>

        <p class="mt-1 text-sm font-semibold text-slate-800">
            {{ primary || emptyLabel }}
        </p>

        <p v-if="secondary" class="mt-0.5 text-xs text-slate-500">
            {{ secondary }}
        </p>
    </div>
</template>

<script setup lang="ts">
withDefaults(
    defineProps<{
        title: string;
        primary?: string | null;
        secondary?: string | null;
        emptyLabel?: string;
    }>(),
    {
        primary: null,
        secondary: null,
        emptyLabel: "Non renseigné",
    }
);
</script>