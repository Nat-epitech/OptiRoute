<script setup lang="ts">
import { computed, onMounted, ref } from "vue"

import { getCostParameters, setCostParameterActive } from "@/api/cost/costParameterApi"

import type { CostParameter } from "@/models/cost/CostParameter"

import CreateCostParameterModal from "@/components/cost/CreateCostParameterModal.vue"
import DeleteCostParameterModal from "@/components/cost/DeleteCostParameterModal.vue"
import CostParameterDetailDrawer from "@/components/cost/CostParameterDetailDrawer.vue"

import AppDropdown from "@/components/ui/AppDropdown.vue"

// Load cost parameters

const costParameters = ref<CostParameter[]>([])

const loadCostParameters = async () => {
    costParameters.value = await getCostParameters()
}

// Filter by category

const vehicleCosts = computed(() =>
    costParameters.value.filter(
        cost => cost.category === "VEHICLE",
    ),
)

const driverCosts = computed(() =>
    costParameters.value.filter(
        cost => cost.category === "DRIVER",
    ),
)

const structureCosts = computed(() =>
    costParameters.value.filter(
        cost => cost.category === "STRUCTURE",
    ),
)

const selectedCostCategory = ref<"ALL" | "VEHICLE" | "DRIVER" | "STRUCTURE" | "ACTIVE_ONLY">("ALL")
const selectedSort = ref<"CREATION_DATE" | "ALPHABET" | "CATEGORY" | "ACTIVE_FIRST">("CREATION_DATE")
const searchQuery = ref("")

const getCategoryRank = (category: string) => {
    switch (category) {
        case "DRIVER":
            return 0
        case "VEHICLE":
            return 1
        case "STRUCTURE":
            return 2
        default:
            return 3
    }
}

const sortCosts = (costs: CostParameter[]) => {
    return [...costs].sort((first, second) => {
        if (selectedSort.value === "ALPHABET") {
            const labelComparison = first.label.localeCompare(second.label, "fr", { sensitivity: "base" })

            if (labelComparison !== 0) {
                return labelComparison
            }

            return first.id - second.id
        }

        if (selectedSort.value === "CATEGORY") {
            const categoryComparison = getCategoryRank(first.category) - getCategoryRank(second.category)

            if (categoryComparison !== 0) {
                return categoryComparison
            }

            return first.id - second.id
        }

        if (selectedSort.value === "ACTIVE_FIRST") {
            if (first.active !== second.active) {
                return Number(second.active) - Number(first.active)
            }

            return first.id - second.id
        }

        return first.id - second.id
    })
}

const visibleCosts = computed(() => {
    const baseCosts = (() => {
        switch (selectedCostCategory.value) {
            case "VEHICLE":
                return vehicleCosts.value
            case "DRIVER":
                return driverCosts.value
            case "STRUCTURE":
                return structureCosts.value
            case "ACTIVE_ONLY":
                return costParameters.value.filter(cost => cost.active)
            default:
                return costParameters.value
        }
    })()

    const sortedBaseCosts = sortCosts(baseCosts)

    if (!searchQuery.value.trim()) {
        return sortedBaseCosts
    }

    const normalized = searchQuery.value.trim().toLowerCase()

    return sortedBaseCosts.filter(cost =>
        cost.label.toLowerCase().includes(normalized)
        || (cost.category === "VEHICLE" && "véhicule".includes(normalized))
        || (cost.category === "DRIVER" && "conducteur".includes(normalized))
        || (cost.category === "STRUCTURE" && "structure".includes(normalized))
        || formatUnit(cost.unit).toLowerCase().includes(normalized)
    )
})

// Manage modals

const showCreateModal = ref(false)
const showDeleteModal = ref(false)
const openDropdownId = ref<number | null>(null)

const actionCostParameter = ref<CostParameter | null>(null)
const selectedCostParameterId = ref<number | null>(null)

const askDeleteCostParameter = (
    costParameter: CostParameter,
) => {
    actionCostParameter.value = costParameter
    showDeleteModal.value = true
}

const openCostParameterDetails = (
    costParameter: CostParameter,
) => {
    selectedCostParameterId.value = costParameter.id

    //TODO: temporaire -> a optimiser ?
    actionCostParameter.value = costParameter
}

const closeCostParameterDetails = () => {
    selectedCostParameterId.value = null
}

const closeCostParameterAction = () => {
    showDeleteModal.value = false
    actionCostParameter.value = null
}

// Handle

const handleCostParameterCreated = async () => {
    showCreateModal.value = false
    await loadCostParameters()
}

const handleCostParameterUpdated = async () => {
    await loadCostParameters()

    if (selectedCostParameterId.value !== null) {
        actionCostParameter.value = costParameters.value.find(
            cost => cost.id === selectedCostParameterId.value,
        ) ?? null
    }
}

const handleCostParameterDeleted = async () => {
    closeCostParameterAction()
    closeCostParameterDetails()
    await loadCostParameters()
}

const toggleCostParameterActive = async (costParameter: CostParameter) => {
    try {
        await setCostParameterActive(costParameter.id, !costParameter.active)
        await loadCostParameters()
    } catch (error) {
        console.error("Impossible de modifier le statut du coût :", error)
    }
}

// Helpers

const formatUnit = (unit: string) => {
    switch (unit) {
        case "EUR_PER_KM":
            return "€/km"

        case "EUR_PER_TRIP":
            return "€/trajet"

        case "EUR_PER_HOUR":
            return "€/heure"

        case "EUR_PER_DAY":
            return "€/jour"

        case "EUR_PER_MONTH":
            return "€/mois"

        case "EUR_PER_YEAR":
            return "€/an"

        default:
            return unit
    }
}

const formatValue = (
    value: number,
    unit: string,
) => {
    return `${value} ${formatUnit(unit)}`
}

onMounted(loadCostParameters)
</script>

<template>
    <div>
        <!-- Header -->

        <div class="mb-6 flex items-center justify-between">
            <div>
                <h1 class="text-3xl font-bold text-gray-800">
                    Coûts référentiels
                </h1>

                <p class="mt-1 text-sm text-gray-500">
                    Gérez les coûts utilisés pour les estimations.
                </p>
            </div>

            <button type="button"
                class="rounded-xl bg-blue-600 px-5 py-3 text-white shadow transition hover:bg-blue-700"
                @click="showCreateModal = true">
                Ajouter un coût
            </button>
        </div>

        <section class="mb-8">
            <div class="mb-4 flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">

                <div class="flex w-full flex-col gap-3 sm:flex-row sm:items-end sm:justify-between">
                    <div class="flex w-full flex-col gap-3 sm:max-w-xl sm:flex-row">
                        <div class="w-full sm:max-w-xs">
                            <label class="mb-1 block text-sm font-medium text-slate-700">
                                Filtre
                            </label>

                            <select v-model="selectedCostCategory"
                                class="w-full rounded-xl border border-slate-300 bg-white px-3 py-2.5 text-sm outline-none transition focus:border-blue-500">
                                <option value="ALL">
                                    Tous les coûts
                                </option>

                                <option value="ACTIVE_ONLY">
                                    Coûts activés
                                </option>

                                <option value="DRIVER">
                                    Coûts chauffeur
                                </option>

                                <option value="VEHICLE">
                                    Coûts véhicule
                                </option>

                                <option value="STRUCTURE">
                                    Coûts structure
                                </option>
                            </select>
                        </div>

                        <div class="w-full sm:max-w-xs">
                            <label class="mb-1 block text-sm font-medium text-slate-700">
                                Tri
                            </label>

                            <select v-model="selectedSort"
                                class="w-full rounded-xl border border-slate-300 bg-white px-3 py-2.5 text-sm outline-none transition focus:border-blue-500">
                                <option value="CREATION_DATE">
                                    Date de création
                                </option>

                                <option value="ALPHABET">
                                    Alphabet
                                </option>

                                <option value="CATEGORY">
                                    Catégorie
                                </option>

                                <option value="ACTIVE_FIRST">
                                    Actif en premier
                                </option>
                            </select>
                        </div>
                    </div>

                    <div class="w-full sm:max-w-xs">
                        <label class="mb-1 block text-sm font-medium text-slate-700">
                            Rechercher
                        </label>

                        <input v-model="searchQuery" type="text" placeholder="Rechercher..."
                            class="w-full rounded-xl border border-slate-300 bg-white px-3 py-2.5 text-sm outline-none transition focus:border-blue-500" />
                    </div>
                </div>
            </div>

            <div class="rounded-2xl bg-white shadow">
                <table class="w-full">
                    <thead class="border-b bg-gray-50">
                        <tr>
                            <th class="whitespace-nowrap px-6 py-4 text-left">
                                Libellé
                            </th>

                            <th class="whitespace-nowrap px-6 py-4 text-left">
                                Valeur
                            </th>

                            <th class="whitespace-nowrap px-6 py-4 text-left">
                                Unité
                            </th>

                            <th class="whitespace-nowrap px-6 py-4 text-left">
                                Catégorie
                            </th>

                            <th class="whitespace-nowrap px-6 py-4 text-left">
                                Statut
                            </th>

                            <th class="whitespace-nowrap px-6 py-4 text-right">
                                Actions
                            </th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="cost in visibleCosts" :key="cost.id"
                            class="cursor-pointer border-b transition last:border-b-0 hover:bg-gray-100" :class="{
                                'bg-blue-50': selectedCostParameterId === cost.id,
                            }" tabindex="0" @click="openCostParameterDetails(cost)"
                            @keydown.enter="openCostParameterDetails(cost)"
                            @keydown.space.prevent="openCostParameterDetails(cost)">
                            <td class="whitespace-nowrap px-6 py-4 font-medium text-gray-900">
                                {{ cost.label }}
                            </td>

                            <td class="whitespace-nowrap px-6 py-4">
                                {{ formatValue(cost.value, cost.unit) }}
                            </td>

                            <td class="whitespace-nowrap px-6 py-4 text-slate-600">
                                {{ formatUnit(cost.unit) }}
                            </td>

                            <td class="whitespace-nowrap px-6 py-4 text-slate-600">
                                {{ cost.category === 'VEHICLE' ? 'Véhicule' : cost.category === 'DRIVER' ? 'Conducteur'
                                    : 'Structure' }}
                            </td>

                            <td class="whitespace-nowrap px-6 py-4">
                                <span class="inline-flex rounded-full px-3 py-1 text-xs font-medium" :class="cost.active
                                    ? 'bg-emerald-100 text-emerald-700'
                                    : 'bg-slate-100 text-slate-500'">
                                    {{ cost.active ? "Actif" : "Désactivé" }}
                                </span>
                            </td>

                            <td class="whitespace-nowrap px-6 py-4 text-right" @click.stop @keydown.stop>
                                <AppDropdown :open="openDropdownId === cost.id" @update:open="value => {
                                    openDropdownId = value ? cost.id : null
                                }" v-slot="{ close }">
                                    <button type="button"
                                        class="flex w-full items-center px-4 py-2 text-sm text-slate-700 hover:bg-slate-100"
                                        @click="close(); void toggleCostParameterActive(cost)">
                                        {{ cost.active ? "Désactiver" : "Activer" }}
                                    </button>

                                    <button type="button"
                                        class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                        @click="close(); askDeleteCostParameter(cost)">
                                        Supprimer
                                    </button>
                                </AppDropdown>
                            </td>
                        </tr>

                        <tr v-if="visibleCosts.length === 0">
                            <td colspan="6" class="px-6 py-12 text-center text-sm text-gray-500">
                                Aucun coût correspondant à ce filtre.
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </section>

        <!-- Detail drawer -->

        <CostParameterDetailDrawer :open="selectedCostParameterId !== null" :costParameter="actionCostParameter"
            @close="closeCostParameterDetails" @updated="handleCostParameterUpdated"
            @deleted="handleCostParameterDeleted" />

        <!-- Create -->

        <CreateCostParameterModal :show="showCreateModal" @close="showCreateModal = false"
            @created="handleCostParameterCreated" />

        <!-- Delete -->

        <DeleteCostParameterModal :show="showDeleteModal" :cost-parameter="actionCostParameter"
            @close="closeCostParameterAction" @deleted="handleCostParameterDeleted" />
    </div>
</template>