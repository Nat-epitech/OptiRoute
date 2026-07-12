<template>
  <div
    class="sticky left-[240px] z-20 flex min-h-[140px] items-start justify-center border-r border-slate-300 bg-white px-3 py-4">
    <div class="text-center">
      <p class="text-sm font-semibold text-emerald-700">
        {{ formattedTotalCost }}
      </p>

      <p class="mt-1 text-[11px] text-slate-400">
        Semaine
      </p>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = defineProps<{ totalCost: number; }>();

const currencyFormatter = new Intl.NumberFormat("fr-FR", {
  style: "currency",
  currency: "EUR",
  minimumFractionDigits: 2,
  maximumFractionDigits: 2,
});

const formattedTotalCost = computed<string>(() => {
  return currencyFormatter.format(props.totalCost);
});

</script>