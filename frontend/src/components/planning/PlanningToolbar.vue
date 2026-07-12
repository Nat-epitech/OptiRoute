<template>

    <div class="flex items-center justify-between border-b bg-white p-4">

        <div class="flex items-center gap-2">

            <button class="rounded border px-3 py-2 hover:bg-gray-100" @click="$emit('previousWeek')">
                ←
            </button>

            <button class="rounded border px-3 py-2 hover:bg-gray-100" @click="$emit('today')">
                Aujourd'hui
            </button>

            <button class="rounded border px-3 py-2 hover:bg-gray-100" @click="$emit('nextWeek')">
                →
            </button>

        </div>

        <div class="text-lg font-semibold">

            {{ weekLabel }}

        </div>

    </div>

</template>

<script setup lang="ts">

defineProps<{ weekLabel: string; }>();

defineEmits([
    "previousWeek",
    "nextWeek",
    "today"
]);

</script>