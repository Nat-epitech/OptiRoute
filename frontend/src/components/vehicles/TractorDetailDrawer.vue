<script setup lang="ts">
import { computed, ref, watch } from "vue"

import { getTractor } from "@/api/vehicle/tractorApi"
import type { TractorDetails } from "@/models/vehicle/Tractor"

import AppDetailDrawer from "@/components/ui/AppDetailDrawer.vue"

import UpdateTractorModal from "@/components/vehicles/UpdateTractorModal.vue"
import DeleteTractorModal from "@/components/vehicles/DeleteTractorModal.vue"

const props = withDefaults(
    defineProps<{
        open: boolean
        tractorId: number | null
        refreshKey?: number
    }>(),
    {
        refreshKey: 0,
    },
)

const emit = defineEmits<{
    close: []
    deleted: []
    updated: []
}>()

const tractor = ref<TractorDetails | null>(null)

const showDeleteModal = ref(false)
const loading = ref(false)
const error = ref<string | null>(null)

/*
 * Permet d’ignorer une ancienne réponse HTTP lorsque
 * l’utilisateur sélectionne rapidement plusieurs tracteurs.
 */
let currentRequestId = 0

const loadTractor = async () => {
    if (props.tractorId === null) {
        return
    }

    const requestId = ++currentRequestId

    loading.value = true
    error.value = null
    tractor.value = null

    try {
        const loadedTractor = await getTractor(props.tractorId)

        if (requestId === currentRequestId) {
            tractor.value = loadedTractor
        }
    } catch (err) {
        console.error("Impossible de charger le tracteur", err)

        if (requestId === currentRequestId) {
            error.value = "Une erreur est survenue pendant le chargement."
        }
    } finally {
        if (requestId === currentRequestId) {
            loading.value = false
        }
    }
}

const showUpdateModal = ref(false)

const openUpdateModal = () => {
    if (!tractor.value) {
        return
    }

    showUpdateModal.value = true
}

const closeUpdateModal = () => {
    showUpdateModal.value = false
}

const handleTractorUpdated = async () => {
    showUpdateModal.value = false

    await loadTractor()
    emit("updated")
}

const tractorLabel = computed(() => {
    if (!tractor.value) {
        return ""
    }

    const parts = [
        tractor.value.brand,
        tractor.value.model,
    ].filter(Boolean)

    return parts.length > 0
        ? parts.join(" ")
        : "Marque et modèle non renseignés"
})

const fuelTypeLabel = computed(() => {
    if (!tractor.value?.fuelType) {
        return "Non renseigné"
    }

    const labels: Record<string, string> = {
        DIESEL: "Diesel",
        B100: "B100",
        HVO100: "HVO100",
        ELECTRIC: "Électrique",
        LNG: "GNL",
        CNG: "GNC",
        HYBRID: "Hybride",
        OTHER: "Autre",
    }

    return labels[tractor.value.fuelType]
        ?? tractor.value.fuelType
})

const sourceLabel = computed(() => {
    if (!tractor.value?.externalSource) {
        return "Non renseignée"
    }

    const labels: Record<string, string> = {
        MANUAL: "Saisie manuelle",
        DASHDOC: "Dashdoc",
        TRANSICS: "Transics",
    }

    return labels[tractor.value.externalSource]
        ?? tractor.value.externalSource
})

const activeLabel = computed(() => {
    if (!tractor.value) {
        return ""
    }

    return tractor.value.active
        ? "Actif"
        : "Inactif"
})

const formatNumber = (value: number | null, suffix = "", maximumFractionDigits = 2): string => {
    if (value === null) {
        return "Non renseigné"
    }

    return `${value.toLocaleString("fr-FR", { maximumFractionDigits })}${suffix}`
}

const formatConsumption = (value: number | null): string => {
    if (value === null) {
        return "Non renseignée"
    }

    return `${value.toLocaleString("fr-FR", { maximumFractionDigits: 2 })} L / 100 km`
}

const formatCurrency = (value: number | null): string => {
    if (value === null) {
        return "Non renseigné"
    }

    return value.toLocaleString("fr-FR", {
        style: "currency",
        currency: "EUR",
        maximumFractionDigits: 2,
    })
}

const formatDate = (value: string | null,): string => {
    if (!value) {
        return "Non renseignée"
    }

    const [year, month, day] = value.split("-").map(Number)

    if (!year || !month || !day) {
        return value
    }

    return new Intl.DateTimeFormat("fr-FR").format(
        new Date(year, month - 1, day),
    )
}

const formatDateTime = (value: string | null | undefined,): string => {
    if (!value) {
        return "Non renseignée"
    }

    return new Intl.DateTimeFormat("fr-FR", {
        dateStyle: "medium",
        timeStyle: "short",
    }).format(new Date(value))
}

watch(
    [() => props.open, () => props.tractorId, () => props.refreshKey,],
    ([open, tractorId]) => {
        if (open && tractorId !== null) {
            loadTractor()
            return
        }

        /*
         * Invalide la requête en cours lors de la fermeture.
         */
        currentRequestId++

        tractor.value = null
        error.value = null
        loading.value = false
    },
    {
        immediate: true,
    },
)

const openDeleteModal = () => {
    if (!tractor.value) {
        return
    }

    showDeleteModal.value = true
}

const closeDeleteModal = () => {
    showDeleteModal.value = false
}

const handleTractorDeleted = () => {
    showDeleteModal.value = false

    emit("deleted")
    emit("close")
}
</script>

<template>
    <AppDetailDrawer :open="props.open" :title="tractor?.registration ?? 'Détail du tracteur'" @close="emit('close')">
        <template #header>
            <div class="min-w-0">
                <p class="text-xs font-medium uppercase tracking-wide text-slate-400">
                    Tracteur
                </p>

                <h2 class="mt-1 truncate text-lg font-semibold text-slate-900">
                    {{ tractor?.registration ?? "Détail du tracteur" }}
                </h2>

                <p v-if="tractor" class="mt-0.5 truncate text-sm text-slate-500">
                    {{ tractorLabel }}
                </p>
            </div>
        </template>

        <div v-if="loading" class="flex min-h-full items-center justify-center">
            <div class="flex items-center gap-3 text-sm text-slate-500">
                <div class="h-5 w-5 animate-spin rounded-full border-2 border-slate-300 border-t-slate-700" />
                Chargement du tracteur...
            </div>
        </div>

        <div v-else-if="error" class="flex min-h-full flex-col items-center justify-center gap-4 px-6 text-center">
            <div>
                <p class="font-medium text-slate-800">
                    Impossible de charger le tracteur
                </p>

                <p class="mt-1 text-sm text-slate-500">
                    {{ error }}
                </p>
            </div>

            <button type="button"
                class="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
                @click="loadTractor">
                Réessayer
            </button>
        </div>

        <div v-else-if="tractor" class="space-y-4 p-4">
            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <div class="flex items-center justify-between gap-4">
                    <h3 class="text-sm font-semibold text-slate-900">
                        Identification
                    </h3>

                    <span class="rounded-full px-2.5 py-1 text-xs font-medium" :class="tractor.active
                        ? 'bg-emerald-50 text-emerald-700'
                        : 'bg-slate-100 text-slate-500'
                        ">
                        {{ activeLabel }}
                    </span>
                </div>

                <div class="mt-4 grid gap-4 sm:grid-cols-2">
                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Immatriculation
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ tractor.registration }}
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
                            {{ tractor.brand || "Non renseignée" }}
                        </p>
                    </div>

                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Modèle
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ tractor.model || "Non renseigné" }}
                        </p>
                    </div>
                </div>
            </section>

            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Motorisation
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
                            {{ formatConsumption(tractor.averageConsumption) }}
                        </p>
                    </div>

                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Vitesse maximale
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ formatNumber(tractor.maxSpeed, " km/h", 0) }}
                        </p>
                    </div>

                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Nombre d’essieux
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ formatNumber(tractor.axleCount, "", 0) }}
                        </p>
                    </div>
                </div>
            </section>

            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Poids
                </h3>

                <div class="mt-4 grid gap-3 sm:grid-cols-2">
                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Poids à vide
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ formatNumber(tractor.emptyWeightKg, " kg", 0) }}
                        </p>
                    </div>

                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            PTRA
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{
                                formatNumber(
                                    tractor.grossCombinationWeightKg,
                                    " kg",
                                    0,
                                )
                            }}
                        </p>
                    </div>
                </div>
            </section>

            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Dimensions
                </h3>

                <div class="mt-4 grid gap-3 sm:grid-cols-3">
                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Hauteur
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ formatNumber(tractor.heightCm, " cm", 0) }}
                        </p>
                    </div>

                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Largeur
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ formatNumber(tractor.widthCm, " cm", 0) }}
                        </p>
                    </div>

                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Longueur
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{ formatNumber(tractor.lengthCm, " cm", 0) }}
                        </p>
                    </div>
                </div>
            </section>

            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Achat et amortissement
                </h3>

                <div class="mt-4 divide-y divide-slate-100">
                    <div class="flex items-start justify-between gap-4 py-3 first:pt-0">
                        <span class="text-sm text-slate-500">
                            Coût d’achat
                        </span>

                        <span class="text-right text-sm font-medium text-slate-800">
                            {{ formatCurrency(tractor.purchaseCost) }}
                        </span>
                    </div>

                    <div class="flex items-start justify-between gap-4 py-3">
                        <span class="text-sm text-slate-500">
                            Début d’amortissement
                        </span>

                        <span class="text-right text-sm font-medium text-slate-800">
                            {{ formatDate(tractor.depreciationStartDate) }}
                        </span>
                    </div>

                    <div class="flex items-start justify-between gap-4 py-3 last:pb-0">
                        <span class="text-sm text-slate-500">
                            Fin d’amortissement
                        </span>

                        <span class="text-right text-sm font-medium text-slate-800">
                            {{ formatDate(tractor.depreciationEndDate) }}
                        </span>
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

                    <div class="flex items-start justify-between gap-4 py-3">
                        <span class="text-sm text-slate-500">
                            Identifiant externe
                        </span>

                        <span class="max-w-[60%] break-all text-right text-sm font-medium text-slate-800">
                            {{
                                tractor.externalId
                                || "Aucun identifiant externe"
                            }}
                        </span>
                    </div>

                    <div class="flex items-start justify-between gap-4 py-3">
                        <span class="text-sm text-slate-500">
                            Créé le
                        </span>

                        <span class="text-right text-sm font-medium text-slate-800">
                            {{ formatDateTime(tractor.createdAt) }}
                        </span>
                    </div>

                    <div class="flex items-start justify-between gap-4 py-3 last:pb-0">
                        <span class="text-sm text-slate-500">
                            Modifié le
                        </span>

                        <span class="text-right text-sm font-medium text-slate-800">
                            {{ formatDateTime(tractor.updatedAt) }}
                        </span>
                    </div>
                </div>
            </section>
        </div>

        <template v-if="tractor" #footer>
            <div class="flex justify-end gap-3">
                <button type="button"
                    class="rounded-lg border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
                    @click="openUpdateModal">
                    Modifier
                </button>

                <button type="button"
                    class="rounded-lg border border-red-200 px-4 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50"
                    @click="openDeleteModal">
                    Supprimer
                </button>
            </div>
        </template>
    </AppDetailDrawer>

    <UpdateTractorModal :show="showUpdateModal" :tractor="tractor" @close="closeUpdateModal"
        @updated="handleTractorUpdated" />

    <DeleteTractorModal :show="showDeleteModal" :tractor="tractor" @close="closeDeleteModal"
        @deleted="handleTractorDeleted" />
</template>