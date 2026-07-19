<template>
    <div class="grid min-w-0 border-b border-slate-200 bg-white" :style="gridStyle">
        <DriverInfoCell :driver-name="driver.name" :transport-count="transportCount" />

        <DriverCostCell :total-cost="driver.totalCost" />

        <PlanningDayCell v-for="day in days" :key="day.key" :transports="driver.days[day.key] ?? []"
            @transport-select="emit('transport-select', $event)" />
    </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

import DriverCostCell from "@/components/planning/DriverCostCell.vue";
import DriverInfoCell from "@/components/planning/DriverInfoCell.vue";
import PlanningDayCell from "@/components/planning/PlanningDayCell.vue";

import type {
    PlanningDay,
    PlanningDriver,
} from "@/models/planning/planning";

const emit = defineEmits<{
    "transport-select": [transportId: number];
}>();

const props = defineProps<{
    driver: PlanningDriver;
    days: PlanningDay[];
}>();

const transportCount = computed<number>(() => {
    return Object.values(props.driver.days).reduce(
        (total, dayTransports) => total + dayTransports.length,
        0
    );
});

const gridStyle = computed(() => ({
    gridTemplateColumns: `240px 120px repeat(${props.days.length}, minmax(220px, 1fr))`,
    minWidth: `${240 + 120 + props.days.length * 220}px`,
    width: '100%',
}))
</script>