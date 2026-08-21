<script setup lang="ts">
import { computed, onMounted, ref } from "vue"

import { getCostParameters } from "@/api/cost/costParameterApi"

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

// Manage modals

const showCreateModal = ref(false)
const showDeleteModal = ref(false)

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
}

const handleCostParameterDeleted = async () => {
    closeCostParameterAction()
    closeCostParameterDetails()
    await loadCostParameters()
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
                    Coûts opérationnels
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

        <!-- Vehicle -->

        <section class="mb-8">
            <div class="mb-3">
                <h2 class="text-xl font-semibold text-slate-800">
                    Véhicules
                </h2>

                <p class="text-sm text-slate-500">
                    Coûts liés aux véhicules et à leur utilisation.
                </p>
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
                                Statut
                            </th>

                            <th class="whitespace-nowrap px-6 py-4 text-right">
                                Actions
                            </th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="cost in vehicleCosts" :key="cost.id"
                            class="cursor-pointer border-b transition last:border-b-0 hover:bg-gray-100" :class="{
                                'bg-blue-50':
                                    selectedCostParameterId === cost.id,
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

                            <td class="whitespace-nowrap px-6 py-4">
                                <span class="inline-flex rounded-full px-3 py-1 text-xs font-medium" :class="cost.active
                                    ? 'bg-emerald-100 text-emerald-700'
                                    : 'bg-slate-100 text-slate-500'
                                    ">
                                    {{ cost.active ? "Actif" : "Désactivé" }}
                                </span>
                            </td>

                            <td class="whitespace-nowrap px-6 py-4 text-right" @click.stop @keydown.stop>
                                <AppDropdown v-slot="{ close }">
                                    <button type="button"
                                        class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                        @click="close(); askDeleteCostParameter(cost)">
                                        Supprimer
                                    </button>
                                </AppDropdown>
                            </td>
                        </tr>

                        <tr v-if="vehicleCosts.length === 0">
                            <td colspan="5" class="px-6 py-12 text-center text-sm text-gray-500">
                                Aucun coût véhicule enregistré.
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </section>

        <!-- Driver -->

        <section class="mb-8">
            <div class="mb-3">
                <h2 class="text-xl font-semibold text-slate-800">
                    Conducteurs
                </h2>

                <p class="text-sm text-slate-500">
                    Coûts liés aux conducteurs et à leurs frais.
                </p>
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
                                Statut
                            </th>

                            <th class="whitespace-nowrap px-6 py-4 text-right">
                                Actions
                            </th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="cost in driverCosts" :key="cost.id"
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

                            <td class="whitespace-nowrap px-6 py-4">
                                <span class="inline-flex rounded-full px-3 py-1 text-xs font-medium" :class="cost.active
                                    ? 'bg-emerald-100 text-emerald-700'
                                    : 'bg-slate-100 text-slate-500'
                                    ">
                                    {{ cost.active ? "Actif" : "Désactivé" }}
                                </span>
                            </td>

                            <td class="whitespace-nowrap px-6 py-4 text-right" @click.stop @keydown.stop>
                                <AppDropdown v-slot="{ close }">
                                    <button type="button"
                                        class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                        @click="
                                            close();
                                        askDeleteCostParameter(cost)
                                            ">
                                        Supprimer
                                    </button>
                                </AppDropdown>
                            </td>
                        </tr>

                        <tr v-if="driverCosts.length === 0">
                            <td colspan="5" class="px-6 py-12 text-center text-sm text-gray-500">
                                Aucun coût conducteur enregistré.
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </section>

        <!-- Structure -->

        <section class="mb-8">
            <div class="mb-3">
                <h2 class="text-xl font-semibold text-slate-800">
                    Structure
                </h2>

                <p class="text-sm text-slate-500">
                    Coûts généraux liés à la structure.
                </p>
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
                                Statut
                            </th>

                            <th class="whitespace-nowrap px-6 py-4 text-right">
                                Actions
                            </th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="cost in structureCosts" :key="cost.id"
                            class="cursor-pointer border-b transition last:border-b-0 hover:bg-gray-100" :class="{
                                'bg-blue-50':
                                    selectedCostParameterId === cost.id,
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

                            <td class="whitespace-nowrap px-6 py-4">
                                <span class="inline-flex rounded-full px-3 py-1 text-xs font-medium" :class="cost.active
                                    ? 'bg-emerald-100 text-emerald-700' : 'bg-slate-100 text-slate-500'">
                                    {{ cost.active ? "Actif" : "Désactivé" }}
                                </span>
                            </td>

                            <td class="whitespace-nowrap px-6 py-4 text-right" @click.stop @keydown.stop>
                                <AppDropdown v-slot="{ close }">
                                    <button type="button"
                                        class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                        @click="close(); askDeleteCostParameter(cost)">
                                        Supprimer
                                    </button>
                                </AppDropdown>
                            </td>
                        </tr>

                        <tr v-if="structureCosts.length === 0">
                            <td colspan="5" class="px-6 py-12 text-center text-sm text-gray-500">
                                Aucun coût structure enregistré.
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