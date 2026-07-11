<template>
  <div
    class="sticky left-[240px] z-20 flex items-center justify-center border-r border-gray-200 bg-white"
  >
    <span class="font-semibold text-green-600">
      {{ totalCost.toFixed(2) }} €
    </span>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{
  driver: any;
}>();

const totalCost = computed(() => {
  return props.driver.missions.reduce(
    (sum: number, mission: any) => sum + (mission.estimatedTotalCost ?? 0),
    0
  );
});
</script>