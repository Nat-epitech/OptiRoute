<script setup lang="ts">
import { computed, ref, watch } from "vue"

import { getVehicle } from "@/api/vehicleApi"

import type { VehicleDetails } from "@/models/Vehicle"

import AppDetailDrawer from "@/components/ui/AppDetailDrawer.vue"

const props = defineProps<{
    open: boolean
    vehicleId: number | null
}>()

const emit = defineEmits<{
    close: []
    edit: [vehicle: VehicleDetails]
    delete: [vehicle: VehicleDetails]
}>()

const vehicle = ref<VehicleDetails | null>(null)

const loading = ref(false)
const error = ref<string | null>(null)

/*
 * Permet d'ignorer une ancienne réponse HTTP lorsque
 * l'utilisateur sélectionne rapidement plusieurs véhicules.
 */
let currentRequestId = 0

const loadVehicle = async () => {
    if (props.vehicleId === null) {
        return
    }

    const requestId = ++currentRequestId

    loading.value = true
    error.value = null
    vehicle.value = null

    try {
        const loadedVehicle = await getVehicle(props.vehicleId)

        if (requestId === currentRequestId) {
            vehicle.value = loadedVehicle
        }
    } catch (err) {
        console.error("Impossible de charger le véhicule", err)

        if (requestId === currentRequestId) {
            error.value = "Une erreur est survenue pendant le chargement."
        }
    } finally {
        if (requestId === currentRequestId) {
            loading.value = false
        }
    }
}

const closeDrawer = () => {
    emit("close")
}

const editVehicle = () => {
    if (vehicle.value) {
        emit("edit", vehicle.value)
    }
}

const deleteVehicle = () => {
    if (vehicle.value) {
        emit("delete", vehicle.value)
    }
}

const vehicleLabel = computed(() => {
    if (!vehicle.value) {
        return ""
    }

    const parts = [
        vehicle.value.brand,
        vehicle.value.model,
    ].filter(Boolean)

    return parts.length > 0
        ? parts.join(" ")
        : "Marque et modèle non renseignés"
})

const fuelTypeLabel = computed(() => {
    if (!vehicle.value?.fuelType) {
        return "Non renseigné"
    }

    const labels: Record<string, string> = {
        DIESEL: "Diesel",
        GASOLINE: "Essence",
        PETROL: "Essence",
        ELECTRIC: "Électrique",
        HYBRID: "Hybride",
        LPG: "GPL",
        CNG: "GNV",
        LNG: "GNL",
    }

    return labels[vehicle.value.fuelType]
        ?? vehicle.value.fuelType
})

const sourceLabel = computed(() => {
    if (!vehicle.value?.externalSource) {
        return "Non renseignée"
    }

    return vehicle.value.externalSource
})

const formatConsumption = (
    value: number | null,
): string => {
    if (value === null) {
        return "Non renseignée"
    }

    return `${value.toLocaleString("fr-FR", {
        maximumFractionDigits: 2,
    })} L / 100 km`
}

const formatTankCapacity = (
    value: number | null,
): string => {
    if (value === null) {
        return "Non renseignée"
    }

    return `${value.toLocaleString("fr-FR", {
        maximumFractionDigits: 2,
    })} L`
}

watch(
    [() => props.open, () => props.vehicleId],
    ([open, vehicleId]) => {
        if (open && vehicleId !== null) {
            loadVehicle()
            return
        }

        /*
         * Invalide la requête en cours lors de la fermeture.
         */
        currentRequestId++

        vehicle.value = null
        error.value = null
        loading.value = false
    },
    {
        immediate: true,
    },
)
</script>

<template>
    <AppDetailDrawer :open="open" :title="vehicle?.registration ?? 'Détail du véhicule'" @close="emit('close')">
        <template #header>
            <div class="min-w-0">
                <p class="text-xs font-medium uppercase tracking-wide text-slate-400">
                    Véhicule
                </p>

                <h2 class="mt-1 truncate text-lg font-semibold text-slate-900">
                    {{ vehicle?.registration ?? "Détail du véhicule" }}
                </h2>

                <p v-if="vehicle" class="mt-0.5 truncate text-sm text-slate-500">
                    {{ vehicleLabel }}
                </p>
            </div>
        </template>

        <div v-if="loading" class="flex min-h-full items-center justify-center">
            <div class="flex items-center gap-3 text-sm text-slate-500">
                <div class="h-5 w-5 animate-spin rounded-full border-2 border-slate-300 border-t-slate-700" />

                Chargement du véhicule...
            </div>
        </div>

        <div v-else-if="error" class="flex min-h-full flex-col items-center justify-center gap-4 px-6 text-center">
            <div>
                <p class="font-medium text-slate-800">
                    Impossible de charger le véhicule
                </p>

                <p class="mt-1 text-sm text-slate-500">
                    {{ error }}
                </p>
            </div>

            <button type="button"
                class="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
                @click="loadVehicle">
                Réessayer
            </button>
        </div>

        <div v-else-if="vehicle" class="space-y-4 p-4">
            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Identification
                </h3>

                <div class="mt-4 grid gap-4 sm:grid-cols-2">
                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Immatriculation
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ vehicle.registration }}
                        </p>
                    </div>

                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Source
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ sourceLabel }}
                        </p>
                    </div>

                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Marque
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ vehicle.brand || "Non renseignée" }}
                        </p>
                    </div>

                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Modèle
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ vehicle.model || "Non renseigné" }}
                        </p>
                    </div>
                </div>
            </section>

            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Carburant et consommation
                </h3>

                <div class="mt-4 grid gap-3 sm:grid-cols-2">
                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Type de carburant
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ fuelTypeLabel }}
                        </p>
                    </div>

                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Consommation moyenne
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ formatConsumption(vehicle.averageConsumption) }}
                        </p>
                    </div>

                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Capacité du réservoir
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ formatTankCapacity(vehicle.tankCapacity) }}
                        </p>
                    </div>
                </div>
            </section>

            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Synchronisation
                </h3>

                <div class="mt-4 divide-y divide-slate-100">
                    <div class="flex items-start justify-between gap-4 py-3 first:pt-0">
                        <span class="text-sm text-slate-500">
                            Source externe
                        </span>

                        <span class="text-right text-sm font-medium text-slate-800">
                            {{ sourceLabel }}
                        </span>
                    </div>

                    <div class="flex items-start justify-between gap-4 py-3 last:pb-0">
                        <span class="text-sm text-slate-500">
                            Identifiant externe
                        </span>

                        <span class="max-w-[60%] break-all text-right text-sm font-medium text-slate-800">
                            {{ vehicle.externalId || "Aucun identifiant externe" }}
                        </span>
                    </div>
                </div>
            </section>

            <section v-if="vehicle.metadata && Object.keys(vehicle.metadata).length > 0"
                class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Métadonnées
                </h3>

                <div class="mt-4 overflow-x-auto rounded-lg bg-slate-900 p-4">
                    <pre class="text-xs leading-5 text-slate-100">{{ JSON.stringify(vehicle.metadata, null, 2) }}</pre>
                </div>
            </section>
        </div>

        <template v-if="vehicle" #footer>
            <div class="flex justify-end gap-3">
                <button type="button"
                    class="rounded-lg border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
                    @click="editVehicle">
                    Modifier
                </button>

                <button type="button"
                    class="rounded-lg border border-red-200 px-4 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50"
                    @click="deleteVehicle">
                    Supprimer
                </button>
            </div>
        </template>
    </AppDetailDrawer>
</template>