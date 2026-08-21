<template>
    <div class="relative flex h-full min-h-0 flex-col overflow-hidden bg-slate-100">
        <PlanningToolbar :start-date="selectedStartDate" :end-date="selectedEndDate" @update:range="handleRangeChange"
            @today-range="applyTodayRange" />

        <PlanningGrid :drivers="planningDrivers" :days="days" :loading="loading" :error="error"
            @retry="loadCurrentPeriod" @transport-select="openTransport" />

        <TransportDetailDrawer :open="selectedTransportId !== null" :transport-id="selectedTransportId"
            @close="closeTransport" @deleted="handleTransportDeleted" />
    </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from "vue";

import PlanningGrid from "@/components/planning/PlanningGrid.vue";
import PlanningToolbar from "@/components/planning/PlanningToolbar.vue";
import TransportDetailDrawer from "@/components/transports/TransportDetailDrawer.vue";

import { usePlanning } from "@/utils/planningUtils";

import type { PlanningDay, PlanningDriver, PlanningTransport, } from "@/models/planning/planning";


const selectedTransportId = ref<number | null>(null);

function openTransport(transportId: number): void { selectedTransportId.value = transportId; }
function closeTransport(): void { selectedTransportId.value = null; }


const { transports, loading, error, loadPlanning } = usePlanning();

const handleTransportDeleted = async (): Promise<void> => {
    closeTransport();
    await loadCurrentPeriod();
};

/*
 * Utilitaires de dates.
 */
function addDays(date: Date, numberOfDays: number): Date {
    const result = new Date(date);
    result.setDate(result.getDate() + numberOfDays);

    return result;
}

function startOfDay(date: Date): Date {
    const result = new Date(date);
    result.setHours(0, 0, 0, 0);

    return result;
}

function formatDateKey(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, "0");
    const day = String(date.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
}

function capitalize(value: string): string {
    if (!value) {
        return value;
    }

    return value.charAt(0).toUpperCase() + value.slice(1);
}

const today = startOfDay(new Date());

const selectedStartDate = ref<Date>(
    new Date(today)
);

const selectedEndDate = ref<Date>(
    addDays(today, 2)
);

const numberOfDisplayedDays = computed<number>(() => {
    const millisecondsPerDay = 24 * 60 * 60 * 1000;
    const difference = selectedEndDate.value.getTime() - selectedStartDate.value.getTime();

    return Math.floor(difference / millisecondsPerDay) + 1;
});

const days = computed<PlanningDay[]>(() => {
    const formatter = new Intl.DateTimeFormat("fr-FR", {
        weekday: "short",
        day: "2-digit",
        month: "2-digit",
    });

    return Array.from(
        { length: numberOfDisplayedDays.value, },
        (_, index) => {
            const date = addDays(selectedStartDate.value, index);

            return {
                key: formatDateKey(date),
                label: capitalize(formatter.format(date).replace(".", "")),
                date,
            };
        }
    );
});

const planningDrivers = computed<PlanningDriver[]>(() => {
    const driversMap = new Map<number, PlanningDriver>();

    const sortedTransports = [...transports.value].sort((first, second) => {
        return (new Date(first.plannedStart).getTime() - new Date(second.plannedStart).getTime());
    });

    sortedTransports.forEach((transport: PlanningTransport) => {
        let driver = driversMap.get(transport.driverId);

        if (!driver) {
            driver = {
                id: transport.driverId,
                name: transport.driverName,
                totalCost: 0,
                days: {},
            };

            driversMap.set(transport.driverId, driver);
        }

        const dayKey = formatDateKey(new Date(transport.plannedStart));

        if (!driver.days[dayKey]) {
            driver.days[dayKey] = [];
        }

        driver.days[dayKey].push(transport);

        driver.totalCost += transport.totalCost;
    });

    return Array.from(driversMap.values()).sort((first, second) => {
        return first.name.localeCompare(second.name, "fr", { sensitivity: "base" });
    });
});

async function loadCurrentPeriod(): Promise<void> {
    const endDateExclusive = addDays(selectedEndDate.value, 1);

    await loadPlanning({
        startDate: formatDateKey(selectedStartDate.value),
        endDate: formatDateKey(endDateExclusive),
    });
}

function handleRangeChange(range: [Date, Date]): void {
    const [start, requestedEnd] = range;

    const normalizedStart = startOfDay(start);
    const normalizedEnd = startOfDay(requestedEnd);

    const maximumEnd = addDays(normalizedStart, 6);

    selectedStartDate.value = normalizedStart;
    selectedEndDate.value = normalizedEnd > maximumEnd ? maximumEnd : normalizedEnd;
}

function applyTodayRange(startOffset: number, additionalDays: number): void {
    const currentToday = startOfDay(new Date());

    selectedStartDate.value = addDays(currentToday, startOffset);
    selectedEndDate.value = addDays(currentToday, startOffset + additionalDays);
}

watch(
    [() => formatDateKey(selectedStartDate.value), () => formatDateKey(selectedEndDate.value),], () => { void loadCurrentPeriod(); },
    {
        immediate: true,
    }
);
</script>