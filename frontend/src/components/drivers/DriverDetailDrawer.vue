<script setup lang="ts">
import { computed, ref, watch } from "vue"

import { getDriver } from "@/api/driverApi"

import type { DriverDetails } from "@/models/Driver"

import AppDetailDrawer from "@/components/ui/AppDetailDrawer.vue"

const props = withDefaults(
    defineProps<{
        open: boolean
        driverId: number | null
        refreshKey?: number
    }>(),
    {
        refreshKey: 0,
    },
)

const emit = defineEmits<{
    close: []
    edit: [driver: DriverDetails]
    delete: [driver: DriverDetails]
}>()

const driver = ref<DriverDetails | null>(null)

const loading = ref(false)
const error = ref<string | null>(null)

/*
 * Permet d'ignorer une ancienne réponse HTTP lorsque
 * l'utilisateur sélectionne rapidement plusieurs chauffeurs.
 */
let currentRequestId = 0

const loadDriver = async () => {
    if (props.driverId === null) {
        return
    }

    const requestId = ++currentRequestId

    loading.value = true
    error.value = null
    driver.value = null

    try {
        const loadedDriver = await getDriver(props.driverId)

        if (requestId === currentRequestId) {
            driver.value = loadedDriver
        }
    } catch (err) {
        console.error(
            "Impossible de charger le chauffeur",
            err,
        )

        if (requestId === currentRequestId) {
            error.value =
                "Une erreur est survenue pendant le chargement."
        }
    } finally {
        if (requestId === currentRequestId) {
            loading.value = false
        }
    }
}

const editDriver = () => {
    if (driver.value) {
        emit("edit", driver.value)
    }
}

const deleteDriver = () => {
    if (driver.value) {
        emit("delete", driver.value)
    }
}

const fullName = computed(() => {
    if (!driver.value) {
        return ""
    }

    return [
        driver.value.firstName,
        driver.value.lastName,
    ]
        .filter(Boolean)
        .join(" ")
})

const initials = computed(() => {
    if (!driver.value) {
        return ""
    }

    return [
        driver.value.firstName?.charAt(0),
        driver.value.lastName?.charAt(0),
    ]
        .filter(Boolean)
        .join("")
        .toUpperCase()
})

const hourlyCost = computed(() => {
    if (
        !driver.value ||
        driver.value.monthlySalary === null ||
        driver.value.monthlyWorkingHours === null ||
        driver.value.monthlyWorkingHours <= 0
    ) {
        return null
    }

    return (
        driver.value.monthlySalary /
        driver.value.monthlyWorkingHours
    )
})

const formatCurrency = (
    value: number | null | undefined,
): string => {
    if (value === null || value === undefined) {
        return "Non renseigné"
    }

    return new Intl.NumberFormat("fr-FR", {
        style: "currency",
        currency: "EUR",
        maximumFractionDigits: 2,
    }).format(value)
}

const formatHours = (
    value: number | null | undefined,
): string => {
    if (value === null || value === undefined) {
        return "Non renseignées"
    }

    return `${value.toLocaleString("fr-FR", {
        maximumFractionDigits: 2,
    })} h`
}

const formatDateTime = (
    value: string | null | undefined,
): string => {
    if (!value) {
        return "Non renseignée"
    }

    const date = new Date(value)

    if (Number.isNaN(date.getTime())) {
        return "Date invalide"
    }

    return new Intl.DateTimeFormat("fr-FR", {
        dateStyle: "medium",
        timeStyle: "short",
    }).format(date)
}

watch(
    [() => props.open, () => props.driverId, () => props.refreshKey,],
    ([open, driverId]) => {
        if (open && driverId !== null) {
            loadDriver()
            return
        }
        /*
         * Invalide la requête en cours lors de la fermeture.
         */
        currentRequestId++

        driver.value = null
        error.value = null
        loading.value = false
    },
    {
        immediate: true,
    },
)
</script>

<template>
    <AppDetailDrawer :open="open" :title="fullName || 'Détail du chauffeur'" @close="emit('close')">
        <template #header>
            <div class="flex min-w-0 items-center gap-3">
                <div
                    class="flex h-11 w-11 shrink-0 items-center justify-center rounded-full bg-slate-900 text-sm font-semibold text-white">
                    {{ initials || "—" }}
                </div>

                <div class="min-w-0">
                    <p class="text-xs font-medium uppercase tracking-wide text-slate-400">
                        Chauffeur
                    </p>

                    <h2 class="mt-1 truncate text-lg font-semibold text-slate-900">
                        {{ fullName || "Détail du chauffeur" }}
                    </h2>

                    <p v-if="driver" class="mt-0.5 truncate text-sm text-slate-500">
                        {{ driver.email }}
                    </p>
                </div>
            </div>
        </template>

        <div v-if="loading" class="flex min-h-full items-center justify-center">
            <div class="flex items-center gap-3 text-sm text-slate-500">
                <div class="h-5 w-5 animate-spin rounded-full border-2 border-slate-300 border-t-slate-700" />

                Chargement du chauffeur...
            </div>
        </div>

        <div v-else-if="error" class="flex min-h-full flex-col items-center justify-center gap-4 px-6 text-center">
            <div>
                <p class="font-medium text-slate-800">
                    Impossible de charger le chauffeur
                </p>

                <p class="mt-1 text-sm text-slate-500">
                    {{ error }}
                </p>
            </div>

            <button type="button"
                class="rounded-lg border border-slate-300 bg-white px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
                @click="loadDriver">
                Réessayer
            </button>
        </div>

        <div v-else-if="driver" class="space-y-4 p-4">
            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <div class="flex items-center justify-between gap-4">
                    <h3 class="text-sm font-semibold text-slate-900">
                        Statut
                    </h3>

                    <span class="rounded-full px-3 py-1 text-xs font-semibold" :class="driver.isActive
                        ? 'bg-emerald-100 text-emerald-700'
                        : 'bg-slate-100 text-slate-600'
                        ">
                        {{
                            driver.isActive
                                ? "Actif"
                                : "Inactif"
                        }}
                    </span>
                </div>
            </section>

            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Informations personnelles
                </h3>

                <div class="mt-4 grid gap-4 sm:grid-cols-2">
                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Prénom
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{
                                driver.firstName ||
                                "Non renseigné"
                            }}
                        </p>
                    </div>

                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Nom
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{
                                driver.lastName ||
                                "Non renseigné"
                            }}
                        </p>
                    </div>

                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Adresse email
                        </p>

                        <p class="mt-1 break-all text-sm font-semibold text-slate-800">
                            {{
                                driver.email ||
                                "Non renseignée"
                            }}
                        </p>
                    </div>

                    <div>
                        <p class="text-xs font-medium text-slate-400">
                            Téléphone
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{
                                driver.phoneNumber ||
                                "Non renseigné"
                            }}
                        </p>
                    </div>
                </div>
            </section>

            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Coût du chauffeur
                </h3>

                <div class="mt-4 grid gap-3 sm:grid-cols-2">
                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Salaire mensuel
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{
                                formatCurrency(
                                    driver.monthlySalary,
                                )
                            }}
                        </p>
                    </div>

                    <div class="rounded-lg border border-slate-200 bg-slate-50 p-3">
                        <p class="text-xs font-medium text-slate-400">
                            Temps mensuel
                        </p>

                        <p class="mt-1 text-sm font-semibold text-slate-800">
                            {{
                                formatHours(
                                    driver.monthlyWorkingHours,
                                )
                            }}
                        </p>
                    </div>
                </div>

                <div class="mt-4 flex items-center justify-between rounded-lg bg-slate-900 px-4 py-3 text-white">
                    <span class="text-sm font-medium">
                        Coût horaire estimé
                    </span>

                    <span class="text-xl font-semibold">
                        {{
                            hourlyCost === null
                                ? "Non calculable"
                                : formatCurrency(hourlyCost)
                        }}
                    </span>
                </div>
            </section>

            <section class="rounded-xl border border-slate-200 bg-white p-4 shadow-sm">
                <h3 class="text-sm font-semibold text-slate-900">
                    Informations système
                </h3>

                <div class="mt-4 divide-y divide-slate-100">
                    <div class="flex items-start justify-between gap-4 py-3 first:pt-0">
                        <span class="text-sm text-slate-500">
                            Date de création
                        </span>

                        <span class="text-right text-sm font-medium text-slate-800">
                            {{
                                formatDateTime(
                                    driver.createdAt,
                                )
                            }}
                        </span>
                    </div>

                    <div class="flex items-start justify-between gap-4 py-3 last:pb-0">
                        <span class="text-sm text-slate-500">
                            Dernière modification
                        </span>

                        <span class="text-right text-sm font-medium text-slate-800">
                            {{
                                formatDateTime(
                                    driver.updatedAt,
                                )
                            }}
                        </span>
                    </div>
                </div>
            </section>
        </div>

        <template v-if="driver" #footer>
            <div class="flex justify-end gap-3">
                <button type="button"
                    class="rounded-lg border border-slate-300 px-4 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-50"
                    @click="editDriver">
                    Modifier
                </button>

                <button type="button"
                    class="rounded-lg border border-red-200 px-4 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50"
                    @click="deleteDriver">
                    Supprimer
                </button>
            </div>
        </template>
    </AppDetailDrawer>
</template>