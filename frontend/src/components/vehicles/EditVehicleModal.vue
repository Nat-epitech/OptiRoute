<script setup lang="ts">
import { ref, watch } from 'vue'

import AppModal from '@/components/ui/AppModal.vue'

import { updateVehicle } from '@/api/vehicleApi'
import { getApiErrorMessage } from '@/api/utils'

import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

import type {
    Vehicle,
    UpdateVehicleRequest
} from '@/models/Vehicle'

const props = defineProps<{
    show: boolean
    vehicle: Vehicle | null
}>()

const emit = defineEmits<{
    close: []
    updated: []
}>()

const registration = ref('')
const brand = ref('')
const model = ref('')
const fuelType = ref('')
const averageConsumption = ref<number | null>(null)
const tankCapacity = ref<number | null>(null)

const loading = ref(false)

watch(
    () => props.vehicle,
    (vehicle) => {
        if (!vehicle) return

        registration.value = vehicle.registration
        brand.value = vehicle.brand ?? ''
        model.value = vehicle.model ?? ''
        fuelType.value = vehicle.fuelType ?? ''
        averageConsumption.value = vehicle.averageConsumption
        tankCapacity.value = vehicle.tankCapacity
    },
    {
        immediate: true
    }
)

const closeModal = () => {
    if (!loading.value) {
        emit('close')
    }
}

const saveVehicle = async () => {
    if (!props.vehicle) return

    const request: UpdateVehicleRequest = {
        registration: registration.value,
        brand: brand.value || null,
        model: model.value || null,
        fuelType: fuelType.value || null,
        averageConsumption: averageConsumption.value,
        tankCapacity: tankCapacity.value
    }

    try {
        loading.value = true

        await updateVehicle(props.vehicle.id, request)

        notification.success(
            'Véhicule modifié',
            `Le véhicule « ${registration.value} » a bien été mis à jour.`
        )

        emit('updated')
        emit('close')
    } catch (error) {
        notification.error(
            'Modification impossible',
            getApiErrorMessage(
                error,
                'Le véhicule n’a pas pu être mis à jour.'
            )
        )
    } finally {
        loading.value = false
    }
}
</script>

<template>
    <AppModal :show="show" @close="closeModal">
        <h2 class="mb-6 text-2xl font-bold">
            Modifier le véhicule
        </h2>

        <div class="space-y-4">
            <div>
                <label class="mb-1 block text-sm font-medium text-gray-700">
                    Immatriculation
                </label>

                <input v-model="registration" type="text" required
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Marque
                    </label>

                    <input v-model="brand" type="text"
                        class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
                </div>

                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Modèle
                    </label>

                    <input v-model="model" type="text"
                        class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
                </div>
            </div>

            <div>
                <label class="mb-1 block text-sm font-medium text-gray-700">
                    Type de carburant
                </label>

                <select v-model="fuelType"
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500">
                    <option value="">
                        Non renseigné
                    </option>
                    <option value="DIESEL">
                        Diesel
                    </option>
                    <option value="PETROL">
                        Essence
                    </option>
                    <option value="ELECTRIC">
                        Électrique
                    </option>
                    <option value="HYBRID">
                        Hybride
                    </option>
                </select>
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Consommation moyenne
                    </label>

                    <input v-model.number="averageConsumption" type="number" min="0" step="0.01"
                        class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500"
                        placeholder="L/100 km" />
                </div>

                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Capacité du réservoir
                    </label>

                    <input v-model.number="tankCapacity" type="number" min="0" step="0.01"
                        class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500"
                        placeholder="Litres" />
                </div>
            </div>
        </div>

        <div class="mt-6 flex justify-end gap-3">
            <button type="button" :disabled="loading" class="rounded-xl border px-4 py-2 disabled:opacity-50"
                @click="closeModal">
                Annuler
            </button>

            <button type="button" :disabled="loading || !registration.trim()"
                class="rounded-xl bg-blue-600 px-4 py-2 text-white transition hover:bg-blue-700 disabled:opacity-50"
                @click="saveVehicle">
                {{ loading ? 'Modification...' : 'Enregistrer' }}
            </button>
        </div>
    </AppModal>
</template>