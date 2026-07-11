<template>
    <div class="flex h-full flex-col bg-gray-100">
        <PlanningHeader :days="days" />

        <PlanningGrid :drivers="drivers" :days="days" />
    </div>
</template>

<script setup lang="ts">
import PlanningGrid from "@/components/planning/PlanningGrid.vue";
import PlanningHeader from "@/components/planning/PlanningHeader.vue";

const days = [
    "03/07",
    "04/07",
    "05/07",
    "06/07",
    "07/07",
    "08/07",
    "09/07"
];

const drivers = [
    {
        id: 1,
        name: "Bernard Poutoux",
        price: 520,
        missions: []
    },
    {
        id: 2,
        name: "Jean Martin",
        price: 190,
        missions: []
    },
    {
        id: 3,
        name: "Paul Dubois",
        price: 840,
        missions: []
    }
];
</script>