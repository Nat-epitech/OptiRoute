<template>
  <div
    class="min-h-[120px] border-r border-gray-200 bg-gray-50 p-2"
  >
    <div
      v-if="missions.length"
      class="flex flex-col gap-2"
    >
      <MissionCard
        v-for="mission in missions"
        :key="mission.id"
        :mission="mission"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import MissionCard from "./MissionCard.vue";

defineProps<{
  missions: any[];
}>();
</script>