<script setup lang="ts">
import { computed, ref, watch } from "vue";

import { Trash2 } from "lucide-vue-next"

import { getTransportById } from "@/api/planningApi";

import type { TransportDetail } from "@/models/transport/TransportDetail";

import TransportCostRow from "@/components/transports/TransportCostRow.vue";
import TransportDetailBlock from "@/components/transports/TransportDetailBlock.vue";
import TransportDetailItem from "@/components/transports/TransportDetailItem.vue";
import TransportMetricCard from "@/components/transports/TransportMetricCard.vue";
import TransportRouteMap from '@/components/transports/TransportRouteMap.vue'
import DeleteTransportModal from "@/components/transports/DeleteTransportModal.vue";
import AppDetailDrawer from "@/components/ui/AppDetailDrawer.vue"

import { formatVehicleLabel } from "@/utils/vehicleUtils"
import { formatDuration, formatCurrency, formatDateTime, formatDistance } from "@/utils/formatters"

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

const customerAddress = computed<string | null>(() => {
    if (!transport.value) {
        return null;
    }

    return [
        transport.value.customerAddress,
        transport.value.customerCity,
    ].filter((value): value is string => Boolean(value)).join(", ") || null;
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

<template>
    <AppDetailDrawer :open="open" :title="transport?.name ?? 'Détail du transport'" @close="emit('close')">

        <template #header>
            <div class="min-w-0">
                <p class="text-xs font-medium uppercase tracking-wide text-blue-600">
                    Détails de l'itinéraire
                </p>

                <h2 class="mt-1 truncate text-lg font-semibold text-slate-900">
                    {{ transport?.name ?? "Chargement..." }}
                </h2>

                <p v-if="transport" class="mt-0.5 truncate text-sm text-slate-500">
                    {{ transport.originName }} → {{ transport.destinationName }}
                </p>
            </div>
        </template>

        <div v-if="loading" class="flex min-h-full items-center justify-center">
            <div class="flex items-center gap-3 text-sm text-slate-500">
                <div class="h-5 w-5 animate-spin rounded-full border-2 border-slate-300 border-t-slate-700" />
                Chargement du transport...
            </div>
        </div>

        <div v-else-if="error" class="flex min-h-full flex-col items-center justify-center gap-4 px-6 text-center">
            <div>
                <p class="font-medium text-slate-800">
                    Impossible de charger le transport
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

        <div v-else-if="transport" class="space-y-4 p-4">
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

                    <TransportDetailBlock title="Client" :primary="transport.customerName"
                        :secondary="customerAddress" />

                    <TransportDetailBlock title="Tracteur" :primary="transport.tractorRegistration"
                        :secondary="formatVehicleLabel(transport.tractorBrand, transport.tractorModel)" />

                    <TransportDetailBlock title="Semi-remorque" :primary="transport.semiTrailerRegistration"
                        :secondary="formatVehicleLabel(transport.semiTrailerBrand, transport.semiTrailerModel)" />


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

        <!-- Actions -->
        <template v-if="transport" #footer>
            <div class="flex items-center justify-between gap-3">
                <button type="button"
                    class="inline-flex items-center gap-2 rounded-xl border border-red-200 px-4 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50"
                    @click="askDeleteTransport">
                    <Trash2 class="h-4 w-4" />
                    Supprimer
                </button>
            </div>
        </template>
    </AppDetailDrawer>

    <DeleteTransportModal :show="showDeleteModal" :transport="transport" @close="closeDeleteModal"
        @deleted="handleTransportDeleted" />
</template>