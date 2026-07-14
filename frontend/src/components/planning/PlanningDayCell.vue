<template>
  <div class="min-h-[140px] border-r border-slate-200 bg-slate-50/60 p-2">
    <div v-if="missions.length > 0" class="flex flex-col gap-2">
      <MissionCard v-for="mission in missions" :key="mission.id" :mission="mission"
        @select="emit('mission-select', $event)" />
    </div>
  </div>
</template>

<script setup lang="ts">
import MissionCard from "@/components/planning/MissionCard.vue";

import type { PlanningMission } from "@/models/planning/planning";

defineProps<{
  missions: PlanningMission[];
}>();

const emit = defineEmits<{
  "mission-select": [missionId: number];
}>();
</script>
