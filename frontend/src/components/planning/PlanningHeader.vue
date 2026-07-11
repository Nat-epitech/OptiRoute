<template>
  <div
    class="sticky top-0 z-30 grid border-b border-gray-300 bg-white"
    :style="gridStyle"
  >
    <div class="sticky left-0 z-40 border-r bg-white p-3 font-semibold">
      Chauffeur
    </div>

    <div class="sticky left-[240px] z-40 border-r bg-white p-3 text-center font-semibold">
      Prix
    </div>

    <div
      v-for="day in days"
      :key="day"
      class="border-r p-3 text-center font-semibold"
    >
      {{ day }}
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{
  days: string[];
}>();

const gridStyle = computed(() => ({
  gridTemplateColumns: `240px 120px repeat(${props.days.length}, minmax(220px, 1fr))`
}));
</script>