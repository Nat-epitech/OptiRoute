<script setup lang="ts">
import { ref } from "vue"

import { Edit3, Settings2, Trash2, WalletCards, Info, ListChecks } from "lucide-vue-next"

import AppDetailDrawer from "@/components/ui/AppDetailDrawer.vue"
import DetailSection from "@/components/ui/DetailSection.vue"
import DetailRow from "@/components/ui/DetailRow.vue"

import UpdateCostParameterModal from "@/components/cost/UpdateCostParameterModal.vue"
import DeleteCostParameterModal from "@/components/cost/DeleteCostParameterModal.vue"

import type { CostParameter, CostCondition } from "@/models/cost/CostParameter"
import type { CostParameterCategory, CostParameterUnit } from '@/types/CostParameterType'

import { formatNumber } from "@/utils/formatters"

// Props

const props = defineProps<{
    open: boolean
    costParameter: CostParameter | null
}>()

const emit = defineEmits<{
    close: []
    updated: []
    deleted: []
}>()

// Manage Modal

const showUpdateModal = ref(false)
const showDeleteModal = ref(false)

const openUpdateModal = () => {
    showUpdateModal.value = true
}

const closeUpdateModal = () => {
    showUpdateModal.value = false
}

const openDeleteModal = () => {
    showDeleteModal.value = true
}

const closeDeleteModal = () => {
    showDeleteModal.value = false
}

// Handle

const handleCostParameterUpdated = () => {
    closeUpdateModal()
    emit("updated")
}

const handleCostParameterDeleted = () => {
    closeDeleteModal()
    emit("deleted")
    emit("close")
}

// Formatting

const categoryLabels: Record<CostParameterCategory, string> = {
    VEHICLE: "Véhicule",
    DRIVER: "Conducteur",
    STRUCTURE: "Structure",
}

const unitLabels: Record<CostParameterUnit, string> = {
    EUR_PER_KM: "€/km",
    EUR_PER_TRIP: "€/trajet",
    EUR_PER_HOUR: "€/heure",
    EUR_PER_DAY: "€/jour",
    EUR_PER_MONTH: "€/mois",
    EUR_PER_YEAR: "€/an",
}

const conditionSourceLabels: Record<string, string> = {
    TRIP: "Trajet",
    VEHICLE: "Véhicule",
    DRIVER: "Conducteur",
}

const conditionFieldLabels: Record<string, string> = {
    DISTANCE: "Distance",
    DURATION: "Durée",
    DEPARTURE_TIME: "Heure de départ",
    VEHICLE_TYPE: "Type de véhicule",
}

const conditionOperatorLabels: Record<string, string> = {
    EQUALS: "est égal à",
    NOT_EQUALS: "est différent de",
    LESS_THAN: "est inférieur à",
    GREATER_THAN: "est supérieur à",
    BEFORE: "avant",
    AFTER: "après",
}

const formatValue = (value: number) => {
    return formatNumber(value)
}

const formatUnit = (unit: CostParameterUnit) => {
    return unitLabels[unit] ?? unit
}

const formatCondition = (condition: CostCondition) => {
    const field = conditionFieldLabels[condition.field] ?? condition.field

    const operator = conditionOperatorLabels[condition.operator] ?? condition.operator

    return `${field} ${operator} ${condition.value}`
}
</script>

<template>
    <AppDetailDrawer :open="props.open" :title="props.costParameter?.label ?? 'Détails du coût'" @close="emit('close')">
        <template #header>
            <div class="min-w-0">
                <p class="text-xs font-medium uppercase tracking-wide text-blue-600">
                    Coût opérationnel
                </p>

                <h2 class="mt-1 truncate text-xl font-bold text-slate-900">
                    {{ props.costParameter?.label ?? "Détails du coût" }}
                </h2>

                <p v-if="props.costParameter" class="mt-1 text-sm text-slate-500">
                    {{ categoryLabels[props.costParameter.category] }}
                </p>
            </div>
        </template>

        <div v-if="props.costParameter" class="space-y-6 p-6">
            <!-- Informations générales -->

            <DetailSection title="Informations générales" :icon="Info">
                <DetailRow label="Catégorie" :value="categoryLabels[props.costParameter.category]" />

                <DetailRow label="Libellé" :value="props.costParameter.label" />

                <DetailRow label="Statut" :value="props.costParameter.active ? 'Actif' : 'Désactivé'" />
            </DetailSection>

            <!-- Valeur -->

            <DetailSection title="Valeur" :icon="WalletCards">
                <DetailRow label="Montant" :value="formatValue(props.costParameter.value)" />

                <DetailRow label="Unité" :value="formatUnit(props.costParameter.unit)" />

                <DetailRow label="Coût"
                    :value="`${formatValue(props.costParameter.value)} ${formatUnit(props.costParameter.unit)}`" />
            </DetailSection>

            <!-- Conditions -->

            <DetailSection title="Logique" :icon="Settings2">
                <div class="min-h-[44px] flex items-center px-1">
                    <p class="text-sm leading-5 text-slate-600">
                        <template v-if="!props.costParameter.rule">
                            Ce coût est toujours applicable.
                        </template>

                        <template v-else-if="props.costParameter.rule.logicalOperator === 'AND'">
                            Toutes les conditions doivent être respectées.
                        </template>

                        <template v-else>
                            Au moins une condition doit être respectée.
                        </template>
                    </p>
                </div>
            </DetailSection>

            <DetailSection v-if="props.costParameter.rule" title="Conditions" :icon="ListChecks">
                <div class="space-y-3 py-3">
                    <div v-for="condition in props.costParameter.rule.conditions" :key="condition.id"
                        class="rounded-xl border border-slate-200 bg-white px-4 py-3">
                        <p class="text-xs font-medium uppercase tracking-wide text-slate-500">
                            {{ conditionSourceLabels[condition.source] ?? condition.source }}
                        </p>

                        <p class="mt-1 text-sm font-medium text-slate-800">
                            {{ formatCondition(condition) }}
                        </p>
                    </div>
                </div>
            </DetailSection>
        </div>

        <!-- Actions -->

        <template v-if="props.costParameter" #footer>
            <div class="flex items-center justify-between gap-3">
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
            </div>
        </template>
    </AppDetailDrawer>

    <UpdateCostParameterModal :show="showUpdateModal" :cost-parameter="props.costParameter" @close="closeUpdateModal"
        @updated="handleCostParameterUpdated" />

    <DeleteCostParameterModal :show="showDeleteModal" :cost-parameter="props.costParameter" @close="closeDeleteModal"
        @deleted="handleCostParameterDeleted" />
</template>