<template>
    <Transition enter-active-class="transition duration-300 ease-out" enter-from-class="translate-x-full"
        enter-to-class="translate-x-0" leave-active-class="transition duration-200 ease-in"
        leave-from-class="translate-x-0" leave-to-class="translate-x-full">
        <aside v-if="open"
            class="absolute inset-y-0 right-0 z-50 flex w-full flex-col border-l border-slate-200 bg-slate-50 shadow-2xl sm:w-[35vw] sm:min-w-[420px] sm:max-w-[620px]">
            <header class="flex items-start justify-between border-b border-slate-200 bg-white px-5 py-4">
                <div class="min-w-0">
                    <h2 class="mt-1 truncate text-lg font-semibold text-slate-900">
                        {{ transport?.name ?? 'Détail de la transport' }}
                    </h2>
                </div>

                <div class="ml-4 flex shrink-0 items-center gap-1">
                    <AppDropdown v-slot="{ close }">
                        <button type="button"
                            class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                            @click="close(); askDeleteTransport()">
                            Supprimer
                        </button>
                    </AppDropdown>

                    <button type="button"
                        class="flex h-9 w-9 items-center justify-center rounded-lg text-xl text-slate-500 transition hover:bg-slate-100 hover:text-slate-800"
                        aria-label="Fermer le détail de la transport" @click="emit('close')">
                        ×
                    </button>
                </div>
            </header>

            <div v-if="loading" class="flex flex-1 items-center justify-center">
                <div class="flex items-center gap-3 text-sm text-slate-500">
                    <div class="h-5 w-5 animate-spin rounded-full border-2 border-slate-300 border-t-slate-700" />
                    Chargement de la transport...
                </div>
            </div>

            <div v-else-if="error" class="flex flex-1 flex-col items-center justify-center gap-4 px-6 text-center">
                <div>
                    <p class="font-medium text-slate-800">
                        Impossible de charger la transport
                    </p>

                    <p class="mt-1 text-sm text-slate-500">
                        {{ error }}
                    </p>
                </div>

                <button type="button"
                    class="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
                    @click="loadTransport">
                    Réessayer
                </button>
            </div>

            <div v-else-if="transport" class="flex-1 space-y-4 overflow-y-auto p-4">
                <!-- Statut et horaires -->
                <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                    <div class="flex items-center justify-between gap-3">
                        <span class="rounded-full px-3 py-1 text-xs font-semibold" :class="statusClasses">
                            {{ statusLabel }}
                        </span>
                    </div>

                    <div class="mt-4 grid grid-cols-2 gap-4">
                        <TransportDetailItem label="Début prévu" :value="formatDateTime(transport.plannedStart)" />

                        <TransportDetailItem label="Fin prévue" :value="formatDateTime(transport.plannedEnd)" />

                        <TransportDetailItem label="Début réel" :value="formatDateTime(transport.actualStart)" />

                        <TransportDetailItem label="Fin réelle" :value="formatDateTime(transport.actualEnd)" />
                    </div>
                </section>

                <!-- Affectation -->
                <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                    <h3 class="text-sm font-semibold text-slate-900">
                        Affectation
                    </h3>

                    <div class="mt-4 grid gap-4 sm:grid-cols-2">
                        <TransportDetailBlock title="Chauffeur" :primary="transport.driverName"
                            :secondary="transport.driverEmail" />

                        <TransportDetailBlock title="Véhicule" :primary="transport.vehicleRegistration"
                            :secondary="vehicleLabel" />

                        <TransportDetailBlock title="Client" :primary="transport.customerName"
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
                                    {{ transport.originName }}
                                </p>

                                <p class="text-xs text-slate-500">
                                    {{ transport.originAddress || "Adresse non renseignée" }}
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
                                    {{ transport.destinationName }}
                                </p>

                                <p class="text-xs text-slate-500">
                                    {{ transport.destinationAddress || "Adresse non renseignée" }}
                                </p>
                            </div>
                        </div>
                    </div>

                    <div class="mt-5 grid grid-cols-2 gap-3">
                        <TransportMetricCard label="Distance" :value="formatDistance(transport.distanceMeters)" />

                        <TransportMetricCard label="Durée estimée" :value="formatDuration(transport.durationSeconds)" />

                        <TransportMetricCard label="Durée avec trafic"
                            :value="formatDuration(transport.trafficDurationSeconds)" />
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
                        <TransportRouteMap :polyline="transport.polyline" />
                    </div>
                </section>

                <!-- Coûts -->
                <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                    <h3 class="text-sm font-semibold text-slate-900">
                        Estimation des coûts
                    </h3>

                    <div class="mt-4 divide-y divide-slate-100">
                        <TransportCostRow label="Carburant" :amount="transport.estimatedFuelCost" />

                        <TransportCostRow label="Péages" :amount="transport.estimatedTollCost" />

                        <TransportCostRow label="Chauffeur" :amount="transport.estimatedDriverCost" />
                    </div>

                    <div class="mt-4 flex items-center justify-between rounded-lg bg-slate-900 px-4 py-3 text-white">
                        <span class="text-sm font-medium">
                            Coût total estimé
                        </span>

                        <span class="text-xl font-semibold">
                            {{ formatCurrency(transport.estimatedTotalCost) }}
                        </span>
                    </div>
                </section>
            </div>
        </aside>
    </Transition>

    <DeleteTransportModal :show="showDeleteModal" :transport="transport" @close="closeDeleteModal"
        @deleted="handleTransportDeleted" />
</template>

<script setup lang="ts">
import { computed, ref, watch } from "vue";

import { getTransportById } from "@/api/planningApi";

import TransportCostRow from "@/components/transports/TransportCostRow.vue";
import TransportDetailBlock from "@/components/transports/TransportDetailBlock.vue";
import TransportDetailItem from "@/components/transports/TransportDetailItem.vue";
import TransportMetricCard from "@/components/transports/TransportMetricCard.vue";
import TransportRouteMap from '@/components/transports/TransportRouteMap.vue'

import DeleteTransportModal from "@/components/transports/DeleteTransportModal.vue";
import AppDropdown from '@/components/ui/AppDropdown.vue'

import type { TransportDetail } from "@/models/planning/transportDetail";

const showDeleteModal = ref(false)

const askDeleteTransport = () => {
    if (!transport.value) {
        return
    }

    showDeleteModal.value = true
}

const closeDeleteModal = () => {
    showDeleteModal.value = false
}

const handleTransportDeleted = () => {
    showDeleteModal.value = false

    emit('deleted')
    emit('close')
}

const props = defineProps<{
    open: boolean;
    transportId: number | null;
}>();

const emit = defineEmits<{
    close: [];
    deleted: []
}>();

const transport = ref<TransportDetail | null>(null);
const loading = ref(false);
const error = ref<string | null>(null);

const currencyFormatter = new Intl.NumberFormat("fr-FR", {
    style: "currency",
    currency: "EUR",
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
});

const vehicleLabel = computed<string | null>(() => {
    if (!transport.value) {
        return null;
    }

    return [
        transport.value.vehicleBrand,
        transport.value.vehicleModel,
    ]
        .filter((value): value is string => Boolean(value))
        .join(" ") || null;
});

const customerAddress = computed<string | null>(() => {
    if (!transport.value) {
        return null;
    }

    return [
        transport.value.customerAddress,
        transport.value.customerCity,
    ]
        .filter((value): value is string => Boolean(value))
        .join(", ") || null;
});

const statusLabel = computed<string>(() => {
    if (!transport.value) {
        return "";
    }

    const labels: Record<string, string> = {
        PLANNED: "Planifiée",
        IN_PROGRESS: "En cours",
        COMPLETED: "Terminée",
        CANCELLED: "Annulée",
    };

    return labels[transport.value.status] ?? transport.value.status;
});

const statusClasses = computed<string>(() => {
    if (!transport.value) {
        return "bg-slate-100 text-slate-700";
    }

    const classes: Record<string, string> = {
        PLANNED: "bg-blue-100 text-blue-700",
        IN_PROGRESS: "bg-amber-100 text-amber-700",
        COMPLETED: "bg-emerald-100 text-emerald-700",
        CANCELLED: "bg-red-100 text-red-700",
    };

    return (
        classes[transport.value.status]
        ?? "bg-slate-100 text-slate-700"
    );
});

async function loadTransport(): Promise<void> {
    if (props.transportId === null) {
        transport.value = null;
        return;
    }

    loading.value = true;
    error.value = null;

    try {
        transport.value = await getTransportById(props.transportId);
    } catch (exception) {
        console.error(exception);

        transport.value = null;
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
    () => [props.open, props.transportId] as const,
    ([open]) => {
        if (open) {
            void loadTransport();
        } else {
            transport.value = null;
            error.value = null;
        }
    },
    {
        immediate: true,
    }
);
</script>