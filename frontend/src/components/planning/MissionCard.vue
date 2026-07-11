<template>
    <div
        class="cursor-pointer rounded-lg border border-green-300 bg-green-100 p-2 shadow-sm transition hover:shadow-md">
        <div class="truncate text-sm font-semibold text-gray-800">
            {{ mission.name }}
        </div>

        <div class="mt-1 text-xs text-gray-600">
            {{ mission.originName }}
        </div>

        <div class="text-xs text-gray-600">
            {{ mission.destinationName }}
        </div>

        <div class="mt-2 flex justify-between text-xs">
            <span>
                {{ startTime }} - {{ endTime }}
            </span>

            <span class="font-semibold text-green-700">
                {{ mission.estimatedTotalCost ?? "-" }} €
            </span>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{
    mission: any;
}>();

const startTime = computed(() =>
    new Date(props.mission.plannedStart).toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
    })
);

const endTime = computed(() =>
    new Date(props.mission.plannedEnd).toLocaleTimeString([], {
        hour: "2-digit",
        minute: "2-digit",
    })
);
</script>