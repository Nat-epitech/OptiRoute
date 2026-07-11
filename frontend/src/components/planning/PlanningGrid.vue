<template>
  <div class="overflow-auto">
    <DriverRow
      v-for="driver in drivers"
      :key="driver.id"
      :driver="driver"
      :days="days"
    />
  </div>
</template>

<script setup lang="ts">
import DriverRow from "./DriverRow.vue";

defineProps<{
  drivers: any[];
  days: string[];
}>();
</script>