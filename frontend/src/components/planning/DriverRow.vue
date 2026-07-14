<template>
    <div class="grid border-b border-slate-200 bg-white" :style="gridStyle">
        <DriverInfoCell :driver-name="driver.name" :mission-count="missionCount" />

        <DriverCostCell :total-cost="driver.totalCost" />

        <PlanningDayCell v-for="day in days" :key="day.key" :missions="driver.days[day.key] ?? []"
            @mission-select="emit('mission-select', $event)" />
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
    "mission-select": [missionId: number];
}>();

const props = defineProps<{
    driver: PlanningDriver;
    days: PlanningDay[];
}>();

const missionCount = computed<number>(() => {
    return Object.values(props.driver.days).reduce(
        (total, dayMissions) => total + dayMissions.length,
        0
    );
});

const gridStyle = computed(() => ({
    gridTemplateColumns: `240px 120px repeat(${props.days.length}, minmax(220px, 1fr))`,
    minWidth: "100%",
    width: "max-content",
}));
</script>