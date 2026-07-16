<script setup lang="ts">
import { ref, watch } from 'vue'

import type { Driver } from '@/models/Driver'
import type { Vehicle } from '@/models/Vehicle'
import type { Customer } from '@/models/Customer'

interface AssignRouteForm {
    title: string
    driverId: number
    vehicleId?: number
    customerId?: number
}

const props = defineProps<{
    show: boolean
    drivers: Driver[]
    vehicles: Vehicle[]
    customers: Customer[]
    startDate: string
    endDate: string
}>()

const emit = defineEmits<{
    close: []
    submit: [data: AssignRouteForm]
}>()

const title = ref('')
const driverId = ref<number>()
const vehicleId = ref<number>()
const customerId = ref<number>()

watch(
    () => props.show,
    (show) => {
        if (show) {
            title.value = ''
            driverId.value = undefined
            vehicleId.value = undefined
            customerId.value = undefined
        }
    }
)

const close = () => {
    emit('close')
}

const submit = () => {
    const normalizedTitle = title.value.trim()

    if (!normalizedTitle || driverId.value === undefined) {
        return
    }

    emit('submit', {
        title: normalizedTitle,
        driverId: driverId.value,
        vehicleId: vehicleId.value,
        customerId: customerId.value
    })
}

function formatDateTime(value: string): string {
    if (!value) {
        return 'Non définie'
    }

    const date = new Date(value)

    if (Number.isNaN(date.getTime())) {
        return 'Date invalide'
    }

    return new Intl.DateTimeFormat('fr-FR', {
        dateStyle: 'medium',
        timeStyle: 'short',
    }).format(date)
}
</script>

<template>
    <div v-if="show" class="fixed inset-0 z-50 flex items-center justify-center">
        <div class="absolute inset-0 bg-black/50" @click="close" />

        <div class="relative z-10 w-full max-w-lg rounded-2xl bg-white p-6 shadow-2xl">
            <h2 class="mb-6 text-xl font-bold">
                Attribuer l'itinéraire
            </h2>

            <div class="space-y-4">
                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Titre
                    </label>

                    <input v-model="title" type="text" class="w-full rounded-lg border p-3"
                        placeholder="Exemple : Livraison Paris → Lyon" />
                </div>

                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Chauffeur
                        <span class="text-red-500">*</span>
                    </label>

                    <select v-model="driverId" class="w-full rounded-lg border p-3">
                        <option :value="undefined">
                            Sélectionner un chauffeur
                        </option>

                        <option v-for="driver in drivers" :key="driver.id" :value="driver.id">
                            {{ driver.firstName }} {{ driver.lastName }}
                        </option>
                    </select>
                </div>

                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Véhicule
                        <span class="text-sm font-normal text-slate-400">
                            — optionnel
                        </span>
                    </label>

                    <select v-model="vehicleId" class="w-full rounded-lg border p-3">
                        <option :value="undefined">
                            Aucun véhicule
                        </option>

                        <option v-for="vehicle in vehicles" :key="vehicle.id" :value="vehicle.id">
                            {{ vehicle.registration }}
                            <template v-if="vehicle.brand || vehicle.model">
                                — {{ vehicle.brand }} {{ vehicle.model }}
                            </template>
                        </option>
                    </select>
                </div>

                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Client
                        <span class="text-sm font-normal text-slate-400">
                            — optionnel
                        </span>
                    </label>

                    <select v-model="customerId" class="w-full rounded-lg border p-3">
                        <option :value="undefined">
                            Aucun client
                        </option>

                        <option v-for="customer in customers" :key="customer.id" :value="customer.id">
                            {{ customer.name }}
                        </option>
                    </select>
                </div>

                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Départ
                    </label>

                    <input :value="formatDateTime(startDate)" readonly class="w-full rounded-lg border bg-slate-100 p-3" />
                </div>

                <div>
                    <label class="mb-2 block text-sm font-medium">
                        Arrivée estimée
                    </label>

                    <input :value="formatDateTime(endDate)" readonly class="w-full rounded-lg border bg-slate-100 p-3" />
                </div>
            </div>

            <div class="mt-6 flex justify-end gap-2">
                <button class="rounded-lg bg-slate-200 px-4 py-2" @click="close">
                    Annuler
                </button>

                <button
                    class="rounded-lg bg-green-600 px-4 py-2 text-white disabled:cursor-not-allowed disabled:opacity-50"
                    :disabled="!title.trim() || driverId === undefined" @click="submit">
                    Valider
                </button>
            </div>
        </div>
    </div>
</template>