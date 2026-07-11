<template>
    <div class="grid border-b border-gray-200 bg-white" :style="gridStyle">
        <DriverInfoCell :driver="driver" />

        <DriverCostCell :driver="driver" />

        <PlanningDayCell v-for="day in days" :key="day" :missions="missionsByDay(day)" />
    </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

import DriverInfoCell from "./DriverInfoCell.vue";
import DriverCostCell from "./DriverCostCell.vue";
import PlanningDayCell from "./PlanningDayCell.vue";

const props = defineProps<{
    driver: any;
    days: string[];
}>();

const gridStyle = computed(() => ({
    gridTemplateColumns: `240px 120px repeat(${props.days.length}, minmax(220px,1fr))`
}));

function missionsByDay(day: string) {
    return props.driver.missions.filter((mission: any) => {
        const missionDate = new Date(mission.plannedStart)
            .toLocaleDateString("fr-FR")
            .slice(0, 5);

        return missionDate === day;
    });
}
</script>