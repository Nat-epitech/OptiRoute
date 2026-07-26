<script setup lang="ts">
import {
    computed,
    ref,
    watch,
} from "vue"

import {
    CircleDollarSign,
    Edit3,
    Info,
    LoaderCircle,
    Ruler,
    Scale,
    Tag,
    Trash2,
    Truck,
    X,
} from "lucide-vue-next"

import { getSemiTrailer } from "@/api/vehicle/semiTrailerApi"

import { getApiErrorMessage } from "@/api/utils"

import type { SemiTrailerDetails } from "@/models/vehicle/SemiTrailer"
import DetailSection from "@/components/ui/DetailSection.vue"
import DetailRow from "@/components/ui/DetailRow.vue"

import DeleteSemiTrailerModal from "@/components/vehicles/DeleteSemiTrailerModal.vue"
import UpdateSemiTrailerModal from "@/components/vehicles/UpdateSemiTrailerModal.vue"

import { useNotification } from "@/composables/useNotification"

const notification = useNotification()

const props = defineProps<{
    open: boolean
    semiTrailerId: number | null
}>()

const emit = defineEmits<{
    close: []
    updated: []
    deleted: []
}>()

const semiTrailer = ref<SemiTrailerDetails | null>(null)

const loading = ref(false)
const error = ref<string | null>(null)

const showUpdateModal = ref(false)
const showDeleteModal = ref(false)

let currentRequestId = 0

const semiTrailerName = computed(() => {
    if (!semiTrailer.value) {
        return "Semi-remorque"
    }

    const parts = [
        semiTrailer.value.brand,
        semiTrailer.value.model,
    ].filter(Boolean)

    return parts.length > 0
        ? parts.join(" ")
        : "Semi-remorque"
})

const formatKilograms = (
    value: number | null | undefined,
): string => {
    if (value === null || value === undefined) {
        return "Non renseigné"
    }

    return new Intl.NumberFormat("fr-FR").format(value) + " kg"
}

const formatCentimeters = (
    value: number | null | undefined,
): string => {
    if (value === null || value === undefined) {
        return "Non renseigné"
    }

    return `${value} cm`
}

const formatCurrency = (
    value: number | null | undefined,
): string => {
    if (value === null || value === undefined) {
        return "Non renseigné"
    }

    return new Intl.NumberFormat("fr-FR", {
        style: "currency",
        currency: "EUR",
    }).format(value)
}

const formatDate = (
    value: string | null | undefined,
): string => {
    if (!value) {
        return "Non renseignée"
    }

    return new Intl.DateTimeFormat("fr-FR", {
        day: "2-digit",
        month: "long",
        year: "numeric",
    }).format(new Date(value))
}

const formatTrailerType = (
    value: string | null | undefined,
): string => {
    if (!value) {
        return "Non renseigné"
    }

    const labels: Record<string, string> = {
        CURTAINSIDER: "Tautliner",
        BOX: "Fourgon",
        REFRIGERATED: "Frigorifique",
        FLATBED: "Plateau",
        TIPPER: "Benne",
        TANKER: "Citerne",
        CONTAINER_CHASSIS: "Porte-conteneur",
        LOW_LOADER: "Porte-engins",
        CAR_CARRIER: "Porte-voitures",
        OTHER: "Autre",
    }

    return labels[value] ?? value
}

const loadSemiTrailer = async () => {
    if (props.semiTrailerId === null) {
        return
    }

    const requestId = ++currentRequestId

    try {
        loading.value = true
        error.value = null
        semiTrailer.value = null

        const response = await getSemiTrailer(
            props.semiTrailerId,
        )

        if (requestId !== currentRequestId) {
            return
        }

        semiTrailer.value = response
    } catch (err: unknown) {
        if (requestId !== currentRequestId) {
            return
        }

        error.value = getApiErrorMessage(
            err,
            "Impossible de charger les informations de la semi-remorque.",
        )
    } finally {
        if (requestId === currentRequestId) {
            loading.value = false
        }
    }
}

const closeDrawer = () => {
    if (
        showUpdateModal.value ||
        showDeleteModal.value
    ) {
        return
    }

    emit("close")
}

const openUpdateModal = () => {
    if (!semiTrailer.value) {
        return
    }

    showUpdateModal.value = true
}

const closeUpdateModal = () => {
    showUpdateModal.value = false
}

const handleSemiTrailerUpdated = async () => {
    showUpdateModal.value = false

    await loadSemiTrailer()

    emit("updated")
}

const openDeleteModal = () => {
    if (!semiTrailer.value) {
        return
    }

    showDeleteModal.value = true
}

const closeDeleteModal = () => {
    showDeleteModal.value = false
}

const handleSemiTrailerDeleted = () => {
    showDeleteModal.value = false
    semiTrailer.value = null

    emit("deleted")
    emit("close")
}

watch(
    [
        () => props.open,
        () => props.semiTrailerId,
    ],
    ([open, semiTrailerId]) => {
        if (
            open &&
            semiTrailerId !== null
        ) {
            loadSemiTrailer()
            return
        }

        currentRequestId++

        semiTrailer.value = null
        error.value = null
        loading.value = false

        showUpdateModal.value = false
        showDeleteModal.value = false
    },
    {
        immediate: true,
    },
)
</script>

<template>
    <Teleport to="body">
        <Transition enter-active-class="transition duration-300 ease-out" enter-from-class="translate-x-full"
            enter-to-class="translate-x-0" leave-active-class="transition duration-200 ease-in"
            leave-from-class="translate-x-0" leave-to-class="translate-x-full">
            <aside v-if="open"
                class="fixed inset-y-0 right-0 z-50 flex w-full max-w-xl flex-col border-l border-slate-200 bg-white shadow-2xl"
                aria-label="Détails de la semi-remorque">
                <header class="flex items-start justify-between border-b border-slate-200 px-6 py-5">
                    <div class="min-w-0">
                        <p class="mb-1 text-sm font-medium text-blue-600">
                            Détails de la semi-remorque
                        </p>

                        <h2 class="truncate text-xl font-bold text-slate-900">
                            {{
                                semiTrailer?.registration ??
                                "Chargement..."
                            }}
                        </h2>

                        <p v-if="semiTrailer" class="mt-1 truncate text-sm text-slate-500">
                            {{ semiTrailerName }}
                        </p>
                    </div>

                    <button type="button"
                        class="ml-4 rounded-xl p-2 text-slate-500 transition hover:bg-slate-100 hover:text-slate-900"
                        aria-label="Fermer" @click="closeDrawer">
                        <X class="h-5 w-5" />
                    </button>
                </header>

                <div v-if="loading" class="flex flex-1 items-center justify-center">
                    <div class="text-center">
                        <LoaderCircle class="mx-auto h-8 w-8 animate-spin text-blue-600" />

                        <p class="mt-3 text-sm text-slate-500">
                            Chargement des informations...
                        </p>
                    </div>
                </div>

                <div v-else-if="error" class="flex flex-1 items-center justify-center p-6">
                    <div class="w-full rounded-2xl border border-red-200 bg-red-50 p-5 text-center">
                        <p class="font-medium text-red-800">
                            Chargement impossible
                        </p>

                        <p class="mt-2 text-sm text-red-700">
                            {{ error }}
                        </p>

                        <button type="button"
                            class="mt-4 rounded-xl bg-red-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-red-700"
                            @click="loadSemiTrailer">
                            Réessayer
                        </button>
                    </div>
                </div>

                <template v-else-if="semiTrailer">
                    <main class="flex-1 space-y-6 overflow-y-auto px-6 py-6">
                        <section class="rounded-2xl border border-slate-200 bg-slate-50 p-5">
                            <div class="flex items-start justify-between gap-4">
                                <div class="flex items-center gap-4">
                                    <div
                                        class="flex h-12 w-12 shrink-0 items-center justify-center rounded-xl bg-blue-100 text-blue-700">
                                        <Truck class="h-6 w-6" />
                                    </div>

                                    <div>
                                        <h3 class="font-semibold text-slate-900">
                                            {{ semiTrailer.registration }}
                                        </h3>

                                        <p class="text-sm text-slate-500">
                                            {{ semiTrailerName }}
                                        </p>
                                    </div>
                                </div>

                                <span class="shrink-0 rounded-full px-3 py-1 text-xs font-semibold" :class="semiTrailer.active
                                    ? 'bg-emerald-100 text-emerald-700'
                                    : 'bg-slate-200 text-slate-600'
                                    ">
                                    {{
                                        semiTrailer.active
                                            ? "Active"
                                            : "Inactive"
                                    }}
                                </span>
                            </div>
                        </section>

                        <DetailSection title="Identification" :icon="Info">
                            <DetailRow label="Immatriculation" :value="semiTrailer.registration" />

                            <DetailRow label="Marque" :value="semiTrailer.brand ??
                                'Non renseignée'
                                " />

                            <DetailRow label="Modèle" :value="semiTrailer.model ??
                                'Non renseigné'
                                " />

                            <DetailRow label="Type" :value="formatTrailerType(
                                semiTrailer.trailerType,
                            )
                                " />

                            <DetailRow label="Nombre d’essieux" :value="semiTrailer.axleCount !== null &&
                                semiTrailer.axleCount !== undefined
                                ? String(semiTrailer.axleCount)
                                : 'Non renseigné'
                                " />
                        </DetailSection>

                        <DetailSection title="Poids" :icon="Scale">
                            <DetailRow label="Poids à vide" :value="formatKilograms(
                                semiTrailer.emptyWeightKg,
                            )
                                " />

                            <DetailRow label="PTAC" :value="formatKilograms(
                                semiTrailer.grossVehicleWeightKg,
                            )
                                " />
                        </DetailSection>

                        <DetailSection title="Dimensions" :icon="Ruler">
                            <DetailRow label="Hauteur" :value="formatCentimeters(
                                semiTrailer.heightCm,
                            )
                                " />

                            <DetailRow label="Largeur" :value="formatCentimeters(
                                semiTrailer.widthCm,
                            )
                                " />

                            <DetailRow label="Longueur" :value="formatCentimeters(
                                semiTrailer.lengthCm,
                            )
                                " />
                        </DetailSection>

                        <DetailSection title="Achat et amortissement" :icon="CircleDollarSign">
                            <DetailRow label="Coût d’achat" :value="formatCurrency(
                                semiTrailer.purchaseCost,
                            )
                                " />

                            <DetailRow label="Début d’amortissement" :value="formatDate(
                                semiTrailer.depreciationStartDate,
                            )
                                " />

                            <DetailRow label="Fin d’amortissement" :value="formatDate(
                                semiTrailer.depreciationEndDate,
                            )
                                " />
                        </DetailSection>

                        <DetailSection title="Source" :icon="Tag">
                            <DetailRow label="Source externe" :value="semiTrailer.externalSource ??
                                'Non renseignée'
                                " />

                            <DetailRow label="Identifiant externe" :value="semiTrailer.externalId ??
                                'Non renseigné'
                                " />
                        </DetailSection>
                    </main>

                    <footer
                        class="flex items-center justify-between gap-3 border-t border-slate-200 bg-white px-6 py-4">
                        <button type="button"
                            class="inline-flex items-center gap-2 rounded-xl border border-red-200 px-4 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50"
                            @click="openDeleteModal">
                            <Trash2 class="h-4 w-4" />
                            Supprimer
                        </button>

                        <button type="button"
                            class="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-700"
                            @click="openUpdateModal">
                            <Edit3 class="h-4 w-4" />
                            Modifier
                        </button>
                    </footer>
                </template>
            </aside>
        </Transition>

        <Transition enter-active-class="transition-opacity duration-300" enter-from-class="opacity-0"
            enter-to-class="opacity-100" leave-active-class="transition-opacity duration-200"
            leave-from-class="opacity-100" leave-to-class="opacity-0">
            <div v-if="open" class="fixed inset-0 z-40 bg-slate-950/30" @click="closeDrawer" />
        </Transition>
    </Teleport>

    <UpdateSemiTrailerModal :show="showUpdateModal" :semiTrailer="semiTrailer" @close="closeUpdateModal"
        @updated="handleSemiTrailerUpdated" />

    <DeleteSemiTrailerModal :show="showDeleteModal" :semiTrailer="semiTrailer" @close="closeDeleteModal"
        @deleted="handleSemiTrailerDeleted" />
</template>