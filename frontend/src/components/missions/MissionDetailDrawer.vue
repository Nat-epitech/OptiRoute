<template>
    <Transition enter-active-class="transition duration-300 ease-out" enter-from-class="translate-x-full"
        enter-to-class="translate-x-0" leave-active-class="transition duration-200 ease-in"
        leave-from-class="translate-x-0" leave-to-class="translate-x-full">
        <aside v-if="open"
            class="absolute inset-y-0 right-0 z-50 flex w-full flex-col border-l border-slate-200 bg-slate-50 shadow-2xl sm:w-[35vw] sm:min-w-[420px] sm:max-w-[620px]">
            <header class="flex items-start justify-between border-b border-slate-200 bg-white px-5 py-4">
                <div class="min-w-0">
                    <h2 class="mt-1 truncate text-lg font-semibold text-slate-900">
                        {{ mission?.name ?? "Détail de la mission" }}
                    </h2>
                </div>

                <button type="button"
                    class="ml-4 flex h-9 w-9 shrink-0 items-center justify-center rounded-lg text-xl text-slate-500 transition hover:bg-slate-100 hover:text-slate-800"
                    aria-label="Fermer le détail de la mission" @click="emit('close')">
                    ×
                </button>
            </header>

            <div v-if="loading" class="flex flex-1 items-center justify-center">
                <div class="flex items-center gap-3 text-sm text-slate-500">
                    <div class="h-5 w-5 animate-spin rounded-full border-2 border-slate-300 border-t-slate-700" />
                    Chargement de la mission...
                </div>
            </div>

            <div v-else-if="error" class="flex flex-1 flex-col items-center justify-center gap-4 px-6 text-center">
                <div>
                    <p class="font-medium text-slate-800">
                        Impossible de charger la mission
                    </p>

                    <p class="mt-1 text-sm text-slate-500">
                        {{ error }}
                    </p>
                </div>

                <button type="button"
                    class="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
                    @click="loadMission">
                    Réessayer
                </button>
            </div>

            <div v-else-if="mission" class="flex-1 space-y-4 overflow-y-auto p-4">
                <!-- Statut et horaires -->
                <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                    <div class="flex items-center justify-between gap-3">
                        <span class="rounded-full px-3 py-1 text-xs font-semibold" :class="statusClasses">
                            {{ statusLabel }}
                        </span>
                    </div>

                    <div class="mt-4 grid grid-cols-2 gap-4">
                        <MissionDetailItem label="Début prévu" :value="formatDateTime(mission.plannedStart)" />

                        <MissionDetailItem label="Fin prévue" :value="formatDateTime(mission.plannedEnd)" />

                        <MissionDetailItem label="Début réel" :value="formatDateTime(mission.actualStart)" />

                        <MissionDetailItem label="Fin réelle" :value="formatDateTime(mission.actualEnd)" />
                    </div>
                </section>

                <!-- Affectation -->
                <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                    <h3 class="text-sm font-semibold text-slate-900">
                        Affectation
                    </h3>

                    <div class="mt-4 grid gap-4 sm:grid-cols-2">
                        <MissionDetailBlock title="Chauffeur" :primary="mission.driverName"
                            :secondary="mission.driverEmail" />

                        <MissionDetailBlock title="Véhicule" :primary="mission.vehicleRegistration"
                            :secondary="vehicleLabel" />

                        <MissionDetailBlock title="Client" :primary="mission.customerName"
                            :secondary="customerAddress" />
                    </div>
                </section>

                <!-- Itinéraire -->
                <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                    <h3 class="text-sm font-semibold text-slate-900">
                        Itinéraire
                    </h3>

                    <div class="mt-4">
                        <div class="flex gap-3">
                            <div class="mt-1 h-3 w-3 shrink-0 rounded-full bg-emerald-500" />

                            <div class="min-w-0">
                                <p class="text-xs font-medium text-slate-400">
                                    Départ
                                </p>

                                <p class="mt-0.5 text-sm font-semibold text-slate-800">
                                    {{ mission.originName }}
                                </p>

                                <p class="text-xs text-slate-500">
                                    {{ mission.originAddress || "Adresse non renseignée" }}
                                </p>
                            </div>
                        </div>

                        <div class="ml-[5px] h-6 border-l border-dashed border-slate-300" />

                        <div class="flex gap-3">
                            <div class="mt-1 h-3 w-3 shrink-0 rounded-full bg-blue-500" />

                            <div class="min-w-0">
                                <p class="text-xs font-medium text-slate-400">
                                    Destination
                                </p>

                                <p class="mt-0.5 text-sm font-semibold text-slate-800">
                                    {{ mission.destinationName }}
                                </p>

                                <p class="text-xs text-slate-500">
                                    {{ mission.destinationAddress || "Adresse non renseignée" }}
                                </p>
                            </div>
                        </div>
                    </div>

                    <div class="mt-5 grid grid-cols-2 gap-3">
                        <MissionMetricCard label="Distance" :value="formatDistance(mission.distanceMeters)" />

                        <MissionMetricCard label="Durée estimée" :value="formatDuration(mission.durationSeconds)" />

                        <MissionMetricCard label="Durée avec trafic"
                            :value="formatDuration(mission.trafficDurationSeconds)" />
                    </div>
                </section>

                <!-- Carte -->
                <section class="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-sm">
                    <div class="border-b border-slate-200 px-4 py-3">
                        <h3 class="text-sm font-semibold text-slate-900">
                            Carte du trajet
                        </h3>
                    </div>

                    <div class="h-64">
                        <MissionRouteMap :polyline="mission.polyline" />
                    </div>
                </section>

                <!-- Coûts -->
                <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                    <h3 class="text-sm font-semibold text-slate-900">
                        Estimation des coûts
                    </h3>

                    <div class="mt-4 divide-y divide-slate-100">
                        <MissionCostRow label="Carburant" :amount="mission.estimatedFuelCost" />

                        <MissionCostRow label="Péages" :amount="mission.estimatedTollCost" />

                        <MissionCostRow label="Chauffeur" :amount="mission.estimatedDriverCost" />
                    </div>

                    <div class="mt-4 flex items-center justify-between rounded-lg bg-slate-900 px-4 py-3 text-white">
                        <span class="text-sm font-medium">
                            Coût total estimé
                        </span>

                        <span class="text-xl font-semibold">
                            {{ formatCurrency(mission.estimatedTotalCost) }}
                        </span>
                    </div>
                </section>
            </div>
        </aside>
    </Transition>
</template>

<script setup lang="ts">
import { computed, ref, watch } from "vue";

import { getMissionById } from "@/api/planningApi";

import MissionCostRow from "@/components/missions/MissionCostRow.vue";
import MissionDetailBlock from "@/components/missions/MissionDetailBlock.vue";
import MissionDetailItem from "@/components/missions/MissionDetailItem.vue";
import MissionMetricCard from "@/components/missions/MissionMetricCard.vue";
import MissionRouteMap from '@/components/missions/MissionRouteMap.vue'

import type { MissionDetail } from "@/models/planning/missionDetail";

const props = defineProps<{
    open: boolean;
    missionId: number | null;
}>();

const emit = defineEmits<{
    close: [];
}>();

const mission = ref<MissionDetail | null>(null);
const loading = ref(false);
const error = ref<string | null>(null);

const currencyFormatter = new Intl.NumberFormat("fr-FR", {
    style: "currency",
    currency: "EUR",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
});

const vehicleLabel = computed<string | null>(() => {
    if (!mission.value) {
        return null;
    }

    return [
        mission.value.vehicleBrand,
        mission.value.vehicleModel,
    ]
        .filter((value): value is string => Boolean(value))
        .join(" ") || null;
});

const customerAddress = computed<string | null>(() => {
    if (!mission.value) {
        return null;
    }

    return [
        mission.value.customerAddress,
        mission.value.customerCity,
    ]
        .filter((value): value is string => Boolean(value))
        .join(", ") || null;
});

const statusLabel = computed<string>(() => {
    if (!mission.value) {
        return "";
    }

    const labels: Record<string, string> = {
        PLANNED: "Planifiée",
        IN_PROGRESS: "En cours",
        COMPLETED: "Terminée",
        CANCELLED: "Annulée",
    };

    return labels[mission.value.status] ?? mission.value.status;
});

const statusClasses = computed<string>(() => {
    if (!mission.value) {
        return "bg-slate-100 text-slate-700";
    }

    const classes: Record<string, string> = {
        PLANNED: "bg-blue-100 text-blue-700",
        IN_PROGRESS: "bg-amber-100 text-amber-700",
        COMPLETED: "bg-emerald-100 text-emerald-700",
        CANCELLED: "bg-red-100 text-red-700",
    };

    return (
        classes[mission.value.status]
        ?? "bg-slate-100 text-slate-700"
    );
});

async function loadMission(): Promise<void> {
    if (props.missionId === null) {
        mission.value = null;
        return;
    }

    loading.value = true;
    error.value = null;

    try {
        mission.value = await getMissionById(props.missionId);
    } catch (exception) {
        console.error(exception);

        mission.value = null;
        error.value = "Une erreur est survenue pendant le chargement.";
    } finally {
        loading.value = false;
    }
}

function formatDateTime(value: string | null): string {
    if (!value) {
        return "Non renseigné";
    }

    return new Intl.DateTimeFormat("fr-FR", {
        dateStyle: "short",
        timeStyle: "short",
    }).format(new Date(value));
}

function formatCurrency(value: number | null): string {
    if (value === null) {
        return "Non renseigné";
    }

    return currencyFormatter.format(value);
}

function formatDistance(value: number | null): string {
    if (value === null) {
        return "Non renseignée";
    }

    return `${(value / 1000).toLocaleString("fr-FR", {
        minimumFractionDigits: 0,
        maximumFractionDigits: 1,
    })} km`;
}

function formatDuration(value: number | null): string {
    if (value === null) {
        return "Non renseignée";
    }

    const hours = Math.floor(value / 3600);
    const minutes = Math.round((value % 3600) / 60);

    if (hours === 0) {
        return `${minutes} min`;
    }

    if (minutes === 0) {
        return `${hours} h`;
    }

    return `${hours} h ${String(minutes).padStart(2, "0")}`;
}

watch(
    () => [props.open, props.missionId] as const,
    ([open]) => {
        if (open) {
            void loadMission();
        } else {
            mission.value = null;
            error.value = null;
        }
    },
    {
        immediate: true,
    }
);
</script>