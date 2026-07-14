<template>
    <div class="flex items-center justify-between py-3">
        <span class="text-sm text-slate-600">
            {{ label }}
        </span>

        <span class="text-sm font-semibold text-slate-800">
            {{ formattedAmount }}
        </span>
    </div>
</template>

<script setup lang="ts">
import { computed } from "vue";

const props = withDefaults(
    defineProps<{
        label: string;
        amount?: number | null;
    }>(),
    {
        amount: null,
    }
);

const currencyFormatter = new Intl.NumberFormat("fr-FR", {
    style: "currency",
    currency: "EUR",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
});

const formattedAmount = computed<string>(() => {
    if (props.amount === null || props.amount === undefined) {
        return "Non renseigné";
    }

    return currencyFormatter.format(props.amount);
});
</script>