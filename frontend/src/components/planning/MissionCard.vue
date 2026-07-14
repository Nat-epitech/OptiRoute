<template>
    <button type="button"
        class="w-full cursor-pointer rounded-lg border border-emerald-200 bg-emerald-50 p-2 text-left shadow-sm transition hover:border-emerald-300 hover:bg-emerald-100 hover:shadow-md"
        @click="emit('select', mission.id)">
        <div class="truncate text-sm font-semibold text-slate-800">
            {{ mission.name }}
        </div>

        <div class="mt-2 space-y-1 text-xs text-slate-600">
            <p class="truncate">
                ↑ {{ startTime }} — {{ mission.originName }}
            </p>

            <p class="truncate">
                ↓ {{ endTime }} — {{ mission.destinationName }}
            </p>
        </div>

        <div class="mt-2 flex items-center justify-end border-t border-emerald-200 pt-2">
            <span class="text-xs font-semibold text-emerald-700">
                {{ formattedCost }}
            </span>
        </div>
    </button>
</template>

<script setup lang="ts">
import { computed } from "vue";

import type { PlanningMission } from "@/models/planning/planning";

const props = defineProps<{
    mission: PlanningMission;
}>();

const emit = defineEmits<{
    select: [missionId: number];
}>();

const startTime = computed(() => {
    return new Intl.DateTimeFormat("fr-FR", {
        hour: "2-digit",
        minute: "2-digit",
    }).format(new Date(props.mission.plannedStart));
});

const endTime = computed(() => {
    return new Intl.DateTimeFormat("fr-FR", {
        hour: "2-digit",
        minute: "2-digit",
    }).format(new Date(props.mission.plannedEnd));
});

const formattedCost = computed(() => {
    if (props.mission.estimatedTotalCost === null) {
        return "Coût indisponible";
    }

    return new Intl.NumberFormat("fr-FR", {
        style: "currency",
        currency: "EUR",
    }).format(props.mission.estimatedTotalCost);
});
</script>