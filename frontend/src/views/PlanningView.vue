<template>
    <div class="relative flex h-full min-h-0 flex-col overflow-hidden bg-slate-100">
        <PlanningToolbar :week-label="weekLabel" @previous-week="showPreviousWeek" @today="showCurrentWeek"
            @next-week="showNextWeek" />

        <PlanningGrid :drivers="planningDrivers" :days="days" :loading="loading" :error="error" @retry="loadCurrentWeek"
            @transport-select="openTransport" />

        <TransportDetailDrawer :open="selectedTransportId !== null" :transport-id="selectedTransportId"
            @close="closeTransport" @deleted="handleTransportDeleted" />
    </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from "vue";

import PlanningGrid from "@/components/planning/PlanningGrid.vue";
import PlanningToolbar from "@/components/planning/PlanningToolbar.vue";
import TransportDetailDrawer from "@/components/transports/TransportDetailDrawer.vue";

import { usePlanning } from "@/models/planning/usePlanning";

import type {
    PlanningDay,
    PlanningDriver,
    PlanningTransport,
} from "@/models/planning/planning";

const selectedTransportId = ref<number | null>(null);

function openTransport(transportId: number): void {
    selectedTransportId.value = transportId;
}

function closeTransport(): void {
    selectedTransportId.value = null;
}

const selectedDate = ref<Date>(new Date());

const {
    transports,
    loading,
    error,
    loadPlanning,
} = usePlanning();

const handleTransportDeleted = async () => {
    closeTransport()
    await loadCurrentWeek()
}

/**
 * Retourne une nouvelle date, sans modifier la date reçue.
 */
function addDays(date: Date, numberOfDays: number): Date {
    const result = new Date(date);
    result.setDate(result.getDate() + numberOfDays);

    return result;
}

/**
 * Retourne le lundi de la semaine contenant la date reçue.
 */
function getMonday(date: Date): Date {
    const result = new Date(date);
    result.setHours(0, 0, 0, 0);

    const day = result.getDay();
    const differenceFromMonday = day === 0 ? -6 : 1 - day;

    result.setDate(result.getDate() + differenceFromMonday);

    return result;
}

/**
 * Transforme une date locale en YYYY-MM-DD.
 *
 * On ne passe pas par toISOString(), car toISOString() transforme
 * la date en UTC et peut décaler le jour selon le fuseau horaire.
 */
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

const weekStart = computed<Date>(() => {
    return getMonday(selectedDate.value);
});

/* La date de fin envoyée au backend est exclusive */
const weekEndExclusive = computed<Date>(() => {
    return addDays(weekStart.value, 7);
});

const days = computed<PlanningDay[]>(() => {
    const formatter = new Intl.DateTimeFormat("fr-FR", {
        weekday: "short",
        day: "2-digit",
        month: "2-digit",
    });

    return Array.from({ length: 7 }, (_, index) => {
        const date = addDays(weekStart.value, index);

        return {
            key: formatDateKey(date),
            label: capitalize(
                formatter
                    .format(date)
                    .replace(".", "")
            ),
            date,
        };
    });
});

const weekLabel = computed<string>(() => {
    const start = weekStart.value;
    const end = addDays(start, 6);

    const startDay = String(start.getDate()).padStart(2, "0");
    const endDay = String(end.getDate()).padStart(2, "0");

    const startMonth = new Intl.DateTimeFormat("fr-FR", {
        month: "long",
    }).format(start);

    const endMonth = new Intl.DateTimeFormat("fr-FR", {
        month: "long",
    }).format(end);

    const startYear = start.getFullYear();
    const endYear = end.getFullYear();

    if (
        start.getMonth() === end.getMonth()
        && startYear === endYear
    ) {
        return `${startDay} – ${endDay} ${endMonth} ${endYear}`;
    }

    if (startYear === endYear) {
        return `${startDay} ${startMonth} – ${endDay} ${endMonth} ${endYear}`;
    }

    return `${startDay} ${startMonth} ${startYear} – ${endDay} ${endMonth} ${endYear}`;
});

/**
 * Transformation :
 *
 * PlanningTransport[] -> PlanningDriver[]
 *
 * Tout les transports sont regroupés par chauffeur puis par journée.
 */
const planningDrivers = computed<PlanningDriver[]>(() => {
    const driversMap = new Map<number, PlanningDriver>();

    const sortedTransports = [...transports.value].sort((first, second) => {
        return new Date(first.plannedStart).getTime()
            - new Date(second.plannedStart).getTime();
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


        const dayKey = transport.plannedStart.slice(0, 10);
        if (!driver.days[dayKey]) {
            driver.days[dayKey] = [];
        }

        driver.days[dayKey].push(transport);
        driver.totalCost += transport.estimatedTotalCost ?? 0;
    });

    return Array.from(driversMap.values()).sort((first, second) => {
        return first.name.localeCompare(second.name, "fr", {
            sensitivity: "base",
        });
    });
});

async function loadCurrentWeek(): Promise<void> {
    await loadPlanning({
        startDate: formatDateKey(weekStart.value),
        endDate: formatDateKey(weekEndExclusive.value),
    });
}

function showPreviousWeek(): void {
    selectedDate.value = addDays(weekStart.value, -7);
}

function showNextWeek(): void {
    selectedDate.value = addDays(weekStart.value, 7);
}

function showCurrentWeek(): void {
    selectedDate.value = new Date();
}

/*  Le chargement est exécuté à l'ouverture de la vue et à chaque changement de semaine. */
watch(
    weekStart,
    () => {
        void loadCurrentWeek();
    },
    {
        immediate: true,
    }
);
</script>