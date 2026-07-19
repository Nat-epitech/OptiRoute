<template>
  <div class="min-h-[140px] border-r border-slate-200 bg-slate-50/60 p-2">
    <div v-if="transports.length > 0" class="flex flex-col gap-2">
      <TransportCard v-for="transport in transports" :key="transport.id" :transport="transport"
        @select="emit('transport-select', $event)" />
    </div>
  </div>
</template>

<script setup lang="ts">
import TransportCard from "@/components/planning/TransportCard.vue";

import type { PlanningTransport } from "@/models/planning/planning";

defineProps<{
  transports: PlanningTransport[];
}>();

const emit = defineEmits<{
  "transport-select": [transportId: number];
}>();
</script>
