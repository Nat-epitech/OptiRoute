<template>
    <div class="relative flex h-full min-h-0 flex-col overflow-hidden bg-slate-100">
        <PlanningToolbar :week-label="weekLabel" @previous-week="showPreviousWeek" @today="showCurrentWeek"
            @next-week="showNextWeek" />

        <PlanningGrid :drivers="planningDrivers" :days="days" :loading="loading" :error="error" @retry="loadCurrentWeek"
            @mission-select="openMission" />

        <MissionDetailDrawer :open="selectedMissionId !== null" :mission-id="selectedMissionId" @close="closeMission"
            @deleted="handleMissionDeleted" />
    </div>
</template>

<script setup lang="ts">
import { computed, ref, watch } from "vue";

import PlanningGrid from "@/components/planning/PlanningGrid.vue";
import PlanningToolbar from "@/components/planning/PlanningToolbar.vue";
import MissionDetailDrawer from "@/components/missions/MissionDetailDrawer.vue";

import { usePlanning } from "@/models/planning/usePlanning";

import type {
    PlanningDay,
    PlanningDriver,
    PlanningMission,
} from "@/models/planning/planning";

const selectedMissionId = ref<number | null>(null);

function openMission(missionId: number): void {
    selectedMissionId.value = missionId;
}

function closeMission(): void {
    selectedMissionId.value = null;
}

const selectedDate = ref<Date>(new Date());

const {
    missions,
    loading,
    error,
    loadPlanning,
} = usePlanning();

const handleMissionDeleted = async () => {
    closeMission()
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
 * PlanningMission[]
 *        ↓
 * PlanningDriver[]
 *
 * Toutes les missions sont regroupées :
 * - par chauffeur ;
 * - puis par journée.
 */
const planningDrivers = computed<PlanningDriver[]>(() => {
    const driversMap = new Map<number, PlanningDriver>();

    const sortedMissions = [...missions.value].sort((first, second) => {
        return new Date(first.plannedStart).getTime()
            - new Date(second.plannedStart).getTime();
    });

    sortedMissions.forEach((mission: PlanningMission) => {
        let driver = driversMap.get(mission.driverId);

        if (!driver) {
            driver = {
                id: mission.driverId,
                name: mission.driverName,
                totalCost: 0,
                days: {},
            };

            driversMap.set(mission.driverId, driver);
        }


        const dayKey = mission.plannedStart.slice(0, 10);
        if (!driver.days[dayKey]) {
            driver.days[dayKey] = [];
        }

        driver.days[dayKey].push(mission);
        driver.totalCost += mission.estimatedTotalCost ?? 0;
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