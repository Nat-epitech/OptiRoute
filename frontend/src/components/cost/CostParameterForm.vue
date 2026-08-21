<script setup lang="ts">

import type {
    CostConditionRequest,
    CreateCostParameterRequest,
} from "@/models/cost/CostParameter"
import type { CostParameterCategory, CostParameterUnit, CostConditionSource, CostConditionField, CostConditionOperator } from '@/types/CostParameterType'

const form = defineModel<CreateCostParameterRequest>({
    required: true,
})

withDefaults(
    defineProps<{
        disabled?: boolean
    }>(),
    {
        disabled: false,
    },
)

// Labels

const categoryOptions: {
    value: CostParameterCategory
    label: string
}[] = [
        {
            value: "VEHICLE",
            label: "Véhicule",
        },
        {
            value: "DRIVER",
            label: "Conducteur",
        },
        {
            value: "STRUCTURE",
            label: "Structure",
        },
    ]

const unitOptions: {
    value: CostParameterUnit
    label: string
}[] = [
        {
            value: "EUR_PER_KM",
            label: "€/km",
        },
        {
            value: "EUR_PER_TRIP",
            label: "€/trajet",
        },
        {
            value: "EUR_PER_HOUR",
            label: "€/heure",
        },
        {
            value: "EUR_PER_DAY",
            label: "€/jour",
        },
        {
            value: "EUR_PER_MONTH",
            label: "€/mois",
        },
        {
            value: "EUR_PER_YEAR",
            label: "€/an",
        },
    ]

const sourceOptions: {
    value: CostConditionSource
    label: string
}[] = [
        {
            value: "TRIP",
            label: "Trajet",
        },
        {
            value: "VEHICLE",
            label: "Véhicule",
        },
        {
            value: "DRIVER",
            label: "Conducteur",
        },
    ]

const tripFieldOptions: {
    value: CostConditionField
    label: string
}[] = [
        {
            value: "DISTANCE",
            label: "Distance",
        },
        {
            value: "DURATION",
            label: "Durée",
        },
        {
            value: "DEPARTURE_TIME",
            label: "Heure de départ",
        },
    ]

const vehicleFieldOptions: {
    value: CostConditionField
    label: string
}[] = [
        {
            value: "VEHICLE_TYPE",
            label: "Type de véhicule",
        },
    ]

const driverFieldOptions: {
    value: CostConditionField
    label: string
}[] = []

const operatorOptions: {
    value: CostConditionOperator
    label: string
}[] = [
        {
            value: "EQUALS",
            label: "est égal à",
        },
        {
            value: "NOT_EQUALS",
            label: "est différent de",
        },
        {
            value: "LESS_THAN",
            label: "est inférieur à",
        },
        {
            value: "GREATER_THAN",
            label: "est supérieur à",
        },
        {
            value: "BEFORE",
            label: "avant",
        },
        {
            value: "AFTER",
            label: "après",
        },
    ]

const vehicleTypeOptions = [
    {
        value: "CURTAINSIDER",
        label: "Curtainsider",
    },
    {
        value: "BOX",
        label: "Fourgon",
    },
    {
        value: "REFRIGERATED",
        label: "Réfrigéré",
    },
    {
        value: "TANKER",
        label: "Citerne",
    },
    {
        value: "FLATBED",
        label: "Plateau",
    },
    {
        value: "CONTAINER_CHASSIS",
        label: "Châssis porte-conteneur",
    },
    {
        value: "TIPPER",
        label: "Benne",
    },
    {
        value: "OTHER",
        label: "Autre",
    },
]

// Conditions

const createEmptyCondition = (): CostConditionRequest => ({
    source: "TRIP",
    field: "DISTANCE",
    operator: "GREATER_THAN",
    value: "",
})

const addCondition = () => {
    if (!form.value.rule) {
        form.value.rule = {
            logicalOperator: "AND",
            conditions: [],
        }
    }

    form.value.rule.conditions.push(
        createEmptyCondition(),
    )
}

const removeCondition = (index: number) => {
    if (!form.value.rule) return

    form.value.rule.conditions.splice(index, 1)

    if (form.value.rule.conditions.length === 0) {
        form.value.rule = null
    }
}

const getFieldOptions = (
    source: CostConditionSource,
) => {
    switch (source) {
        case "TRIP":
            return tripFieldOptions

        case "VEHICLE":
            return vehicleFieldOptions

        case "DRIVER":
            return driverFieldOptions
    }
}

const handleSourceChange = (
    condition: CostConditionRequest,
) => {
    const fields = getFieldOptions(condition.source)

    condition.field = fields[0]?.value ?? "DISTANCE"
    condition.value = ""
}
</script>

<template>
    <div class="space-y-6">

        <!-- Informations générales -->

        <section class="space-y-4">
            <h3 class="text-sm font-semibold uppercase tracking-wide text-slate-500">
                Informations générales
            </h3>

            <div>
                <label class="mb-1 block text-sm font-medium text-slate-700">
                    Catégorie
                </label>

                <select v-model="form.category" required :disabled="disabled"
                    class="w-full rounded-xl border border-slate-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100">
                    <option v-for="option in categoryOptions" :key="option.value" :value="option.value">
                        {{ option.label }}
                    </option>
                </select>
            </div>

            <div>
                <label class="mb-1 block text-sm font-medium text-slate-700">
                    Libellé
                </label>

                <input v-model="form.label" type="text" required maxlength="150" :disabled="disabled"
                    placeholder="Ex. Entretien"
                    class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100" />
            </div>
        </section>

        <!-- Coût -->

        <section class="space-y-4">
            <h3 class="text-sm font-semibold uppercase tracking-wide text-slate-500">
                Coût
            </h3>

            <div class="grid grid-cols-1 gap-4 sm:grid-cols-2">
                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">
                        Valeur
                    </label>

                    <input v-model.number="form.value" type="number" min="0" step="0.01" required :disabled="disabled"
                        class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100" />
                </div>

                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">
                        Unité
                    </label>

                    <select v-model="form.unit" required :disabled="disabled"
                        class="w-full rounded-xl border border-slate-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100">
                        <option v-for="option in unitOptions" :key="option.value" :value="option.value">
                            {{ option.label }}
                        </option>
                    </select>
                </div>
            </div>

            <label class="flex cursor-pointer items-center gap-3">
                <input v-model="form.active" type="checkbox" :disabled="disabled"
                    class="h-4 w-4 rounded border-slate-300 text-blue-600 focus:ring-blue-500" />

                <span class="text-sm font-medium text-slate-700">
                    Coût actif
                </span>
            </label>
        </section>

        <!-- Conditions -->

        <section class="space-y-4">
            <div class="flex items-center justify-between">
                <h3 class="text-sm font-semibold uppercase tracking-wide text-slate-500">
                    Conditions
                </h3>

                <button type="button" :disabled="disabled"
                    class="text-sm font-medium text-blue-600 transition hover:text-blue-700 disabled:cursor-not-allowed disabled:opacity-50"
                    @click="addCondition">
                    + Ajouter une condition
                </button>
            </div>

            <!-- Pas de condition -->

            <div v-if="!form.rule"
                class="rounded-xl border border-dashed border-slate-300 bg-slate-50 p-4 text-sm text-slate-500">
                Aucune condition.
                Ce coût sera toujours applicable.
            </div>

            <!-- Règle -->

            <div v-else class="space-y-4">
                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">
                        Application des conditions
                    </label>

                    <select v-model="form.rule.logicalOperator" :disabled="disabled"
                        class="w-full rounded-xl border border-slate-300 bg-white px-4 py-3 outline-none transition focus:border-blue-500 disabled:cursor-not-allowed disabled:bg-slate-100">
                        <option value="AND">
                            Toutes les conditions doivent être respectées
                        </option>

                        <option value="OR">
                            Au moins une condition doit être respectée
                        </option>
                    </select>
                </div>

                <div v-for="(condition, index) in form.rule.conditions" :key="index"
                    class="rounded-xl border border-slate-200 bg-slate-50 p-4">
                    <div class="mb-3 flex items-center justify-between">
                        <span class="text-sm font-semibold text-slate-700">
                            Condition {{ index + 1 }}
                        </span>

                        <button type="button" :disabled="disabled"
                            class="text-sm font-medium text-red-600 hover:text-red-700 disabled:opacity-50"
                            @click="removeCondition(index)">
                            Supprimer
                        </button>
                    </div>

                    <div class="space-y-3">

                        <!-- Source -->

                        <div>
                            <label class="mb-1 block text-xs font-medium text-slate-600">
                                Élément
                            </label>

                            <select v-model="condition.source" :disabled="disabled"
                                class="w-full rounded-xl border border-slate-300 bg-white px-3 py-2.5 outline-none focus:border-blue-500"
                                @change="handleSourceChange(condition)">
                                <option v-for="option in sourceOptions" :key="option.value" :value="option.value">
                                    {{ option.label }}
                                </option>
                            </select>
                        </div>

                        <!-- Field -->

                        <div>
                            <label class="mb-1 block text-xs font-medium text-slate-600">
                                Paramètre
                            </label>

                            <select v-model="condition.field" :disabled="disabled"
                                class="w-full rounded-xl border border-slate-300 bg-white px-3 py-2.5 outline-none focus:border-blue-500">
                                <option v-for="option in getFieldOptions(condition.source)" :key="option.value"
                                    :value="option.value">
                                    {{ option.label }}
                                </option>
                            </select>
                        </div>

                        <!-- Operator -->

                        <div>
                            <label class="mb-1 block text-xs font-medium text-slate-600">
                                Condition
                            </label>

                            <select v-model="condition.operator" :disabled="disabled"
                                class="w-full rounded-xl border border-slate-300 bg-white px-3 py-2.5 outline-none focus:border-blue-500">
                                <option v-for="option in operatorOptions" :key="option.value" :value="option.value">
                                    {{ option.label }}
                                </option>
                            </select>
                        </div>

                        <!-- Value -->

                        <div>
                            <label class="mb-1 block text-xs font-medium text-slate-600">
                                Valeur
                            </label>

                            <!-- Heure -->

                            <input v-if="condition.field === 'DEPARTURE_TIME'" v-model="condition.value" type="time"
                                required :disabled="disabled"
                                class="w-full rounded-xl border border-slate-300 bg-white px-3 py-2.5 outline-none focus:border-blue-500" />

                            <!-- Type véhicule -->

                            <select v-else-if="condition.field === 'VEHICLE_TYPE'" v-model="condition.value" required
                                :disabled="disabled"
                                class="w-full rounded-xl border border-slate-300 bg-white px-3 py-2.5 outline-none focus:border-blue-500">
                                <option value="" disabled>
                                    Sélectionner un type
                                </option>

                                <option v-for="option in vehicleTypeOptions" :key="option.value" :value="option.value">
                                    {{ option.label }}
                                </option>
                            </select>

                            <!-- Valeur numérique -->

                            <input v-else v-model="condition.value" type="number" step="0.01" required
                                :disabled="disabled"
                                class="w-full rounded-xl border border-slate-300 bg-white px-3 py-2.5 outline-none focus:border-blue-500" />
                        </div>
                    </div>
                </div>

                <button type="button" :disabled="disabled"
                    class="w-full rounded-xl border border-dashed border-blue-300 px-4 py-3 text-sm font-medium text-blue-600 transition hover:bg-blue-50 disabled:cursor-not-allowed disabled:opacity-50"
                    @click="addCondition">
                    + Ajouter une condition
                </button>
            </div>
        </section>
    </div>
</template>