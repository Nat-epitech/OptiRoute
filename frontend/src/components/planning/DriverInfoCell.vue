<template>
  <div class="sticky left-0 z-20 flex min-h-[140px] items-start border-r border-slate-300 bg-white p-4">
    <div class="min-w-0">
      <div class="flex items-center gap-3">
        <div
          class="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-slate-100 text-sm font-semibold text-slate-600">
          {{ initials }}
        </div>

        <div class="min-w-0">
          <p class="truncate text-sm font-semibold text-slate-800">
            {{ driverName }}
          </p>

          <p class="mt-0.5 text-xs text-slate-500">
            {{ missionCountLabel }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{
  driverName: string;
  missionCount: number;
}>();

const initials = computed<string>(() => {
  return props.driverName
    .trim()
    .split(/\s+/)
    .slice(0, 2)
    .map((part) => part.charAt(0).toUpperCase())
    .join("");
});

const missionCountLabel = computed<string>(() => {
  if (props.missionCount === 0) {
    return "Aucune mission";
  }

  if (props.missionCount === 1) {
    return "1 mission";
  }

  return `${props.missionCount} missions`;
});
</script>