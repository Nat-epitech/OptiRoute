<script setup lang="ts">
import { ref, watch } from 'vue'

import type { Driver } from '@/models/Driver'
import type { Customer } from '@/models/Customer'
import type { AssignTransport } from '@/models/transport/TransportRequest.ts'

import AppModal from "@/components/ui/AppModal.vue"

import { formatDateTime } from "@/utils/formatters"

// Variables

const props = defineProps<{
    show: boolean
    drivers: Driver[]
    customers: Customer[]
    startDate: string
    endDate: string
}>()

const emit = defineEmits<{
    close: []
    submit: [data: AssignTransport]
}>()

const title = ref('')
const driverId = ref<number>()
const customerId = ref<number>()

// Manage Modal

const close = () => {
    emit('close')
}

// Manage Form

const submit = () => {
    const normalizedTitle = title.value.trim()

    if (!normalizedTitle || driverId.value === undefined) {
        return
    }

    emit('submit', {
        title: normalizedTitle,
        driverId: driverId.value,
        customerId: customerId.value
    })
}

// Watch

watch(
    () => props.show,
    (show) => {
        if (show) {
            title.value = ''
            driverId.value = undefined
            customerId.value = undefined
        }
    }
)
</script>

<template>
    <AppModal :show="show" max-width="lg" @close="close">
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
            <button type="button" class="rounded-lg bg-slate-200 px-4 py-2" @click="close">
                Annuler
            </button>

            <button type="button"
                class="rounded-lg bg-green-600 px-4 py-2 text-white disabled:cursor-not-allowed disabled:opacity-50"
                :disabled="!title.trim() || driverId === undefined" @click="submit">
                Valider
            </button>
        </div>
    </AppModal>
</template>