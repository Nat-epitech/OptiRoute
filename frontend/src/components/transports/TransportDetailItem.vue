<template>
  <div>
    <p class="text-xs font-medium text-slate-400">
      {{ label }}
    </p>

    <p class="mt-1 text-sm font-medium text-slate-700">
      {{ value || emptyLabel }}
    </p>
  </div>
</template>

<script setup lang="ts">
withDefaults(
  defineProps<{
    label: string;
    value?: string | null;
    emptyLabel?: string;
  }>(),
  {
    value: null,
    emptyLabel: "Non renseigné",
  }
);
</script>