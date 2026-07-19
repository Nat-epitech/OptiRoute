<template>
    <div class="rounded-lg bg-slate-50 p-3">
        <p class="text-[11px] font-medium text-slate-400">
            {{ label }}
        </p>

        <p class="mt-1 truncate text-sm font-semibold text-slate-700">
            {{ value || emptyLabel }}
        </p>
    </div>
</template>

<script setup lang="ts">
withDefaults(
    defineProps<{
        label: string;
        value?: string | null;
        emptyLabel?: string;
    }>(),
    {
        value: null,
        emptyLabel: "Non renseigné",
    }
);
</script>