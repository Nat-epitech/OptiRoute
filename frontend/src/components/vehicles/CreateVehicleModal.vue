<script setup lang="ts">
import { reactive, ref, watch } from 'vue'
import AppModal from '@/components/ui/AppModal.vue'
import { createVehicle } from '@/api/vehicleApi'
import type { CreateVehicleRequest } from '@/models/Vehicle'

const props = defineProps<{
    show: boolean
}>()

const emit = defineEmits<{
    close: []
    created: []
}>()

const loading = ref(false)
const errorMessage = ref('')

const createEmptyForm = (): CreateVehicleRequest => ({
    externalId: null,
    externalSource: 'MANUAL',
    registration: '',
    brand: '',
    model: '',
    fuelType: 'DIESEL',
    averageConsumption: null,
    tankCapacity: null,
    metadata: null
})

const form = reactive<CreateVehicleRequest>(createEmptyForm())

const resetForm = () => {
    Object.assign(form, createEmptyForm())
    errorMessage.value = ''
}

const closeModal = () => {
    resetForm()
    emit('close')
}

const submitVehicle = async () => {
    errorMessage.value = ''

    if (!form.registration.trim()) {
        errorMessage.value = 'Registration is required.'
        return
    }

    loading.value = true

    try {
        await createVehicle({
            externalId: null,
            externalSource: 'MANUAL',
            registration: form.registration.trim(),
            brand: form.brand?.trim() || null,
            model: form.model?.trim() || null,
            fuelType: form.fuelType || null,
            averageConsumption: form.averageConsumption,
            tankCapacity: form.tankCapacity,
            metadata: null
        })

        resetForm()
        emit('created')
        emit('close')
    } catch (error: any) {
        console.error('Unable to create vehicle:', error)

        errorMessage.value =
            error.response?.data?.message ??
            'An error occurred while creating the vehicle.'
    } finally {
        loading.value = false
    }
}

watch(
    () => props.show,
    (show) => {
        if (!show) {
            resetForm()
        }
    }
)
</script>

<template>
    <AppModal :show="props.show" @close="closeModal">
        <form @submit.prevent="submitVehicle">
            <h2 class="mb-6 text-2xl font-bold">
                Ajouter un nouveau véhicule
            </h2>

            <div class="space-y-4">
                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Inscription
                    </label>

                    <input v-model="form.registration" type="text" placeholder="AB-123-CD" required
                        class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
                </div>

                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Marque
                    </label>

                    <input v-model="form.brand" type="text" placeholder="Renault Trucks"
                        class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
                </div>

                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Modèle
                    </label>

                    <input v-model="form.model" type="text" placeholder="T High"
                        class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
                </div>

                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Type de carburant
                    </label>

                    <select v-model="form.fuelType"
                        class="w-full rounded-xl border bg-white px-4 py-3 outline-none focus:border-blue-500">
                        <option value="DIESEL">
                            Diesel
                        </option>

                        <option value="ELECTRIC">
                            Electric
                        </option>

                        <option value="HYBRID">
                            Hybrid
                        </option>

                        <option value="GAS">
                            Gas
                        </option>
                    </select>
                </div>

                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="mb-1 block text-sm font-medium text-gray-700">
                            Consommation moyenne
                        </label>

                        <input v-model.number="form.averageConsumption" type="number" min="0" step="0.01"
                            placeholder="31.50"
                            class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />

                        <p class="mt-1 text-xs text-gray-500">
                            Litres / 100 km
                        </p>
                    </div>

                    <div>
                        <label class="mb-1 block text-sm font-medium text-gray-700">
                            Capacité du réservoir
                        </label>

                        <input v-model.number="form.tankCapacity" type="number" min="0" step="0.01" placeholder="750"
                            class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />

                        <p class="mt-1 text-xs text-gray-500">
                            Litres
                        </p>
                    </div>
                </div>

                <p v-if="errorMessage" class="rounded-xl bg-red-50 px-4 py-3 text-sm text-red-600">
                    {{ errorMessage }}
                </p>
            </div>

            <div class="mt-6 flex justify-end gap-3">
                <button type="button" :disabled="loading"
                    class="rounded-xl border px-4 py-2 disabled:cursor-not-allowed disabled:opacity-50"
                    @click="closeModal">
                    Annuler
                </button>

                <button type="submit" :disabled="loading"
                    class="rounded-xl bg-blue-600 px-4 py-2 text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-50">
                    {{ loading ? 'Création en cours...' : 'Ajouter le véhicule' }}
                </button>
            </div>
        </form>
    </AppModal>
</template>