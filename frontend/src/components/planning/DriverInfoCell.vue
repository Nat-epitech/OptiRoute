<template>
  <div
    class="sticky left-0 z-20 flex items-center border-r border-gray-200 bg-white p-4"
  >
    <div class="flex flex-col">
      <span class="font-semibold text-gray-800">
        {{ driver.name }}
      </span>

      <span class="text-xs text-gray-500">
        {{ missionCount }} mission(s)
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{
  driver: any;
}>();

const missionCount = computed(() => props.driver.missions?.length ?? 0);
</script>