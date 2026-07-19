<template>
  <div class="sticky top-0 z-40 grid border-b border-slate-300 bg-white shadow-sm" :style="gridStyle">
    <div class="sticky left-0 z-50 flex min-h-16 items-center border-r border-slate-300 bg-white px-4">
      <span class="text-sm font-semibold text-slate-700">
        Chauffeur
      </span>
    </div>

    <div
      class="sticky left-[240px] z-50 flex min-h-16 items-center justify-center border-r border-slate-300 bg-white px-3">
      <span class="text-sm font-semibold text-slate-700">
        Prix
      </span>
    </div>

    <div v-for="day in days" :key="day.key"
      class="flex min-h-16 items-center justify-center border-r border-slate-200 bg-slate-50 px-3">
      <span class="text-sm font-semibold capitalize text-slate-700">
        {{ day.label }}
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";
import type { PlanningDay } from "@/models/planning/planning";

const props = defineProps<{ days: PlanningDay[]; }>();

const gridStyle = computed(() => ({
  gridTemplateColumns: `240px 120px repeat(${props.days.length}, minmax(220px, 1fr))`,
  minWidth: `${240 + 120 + props.days.length * 220}px`,
  width: '100%',
}));

</script>