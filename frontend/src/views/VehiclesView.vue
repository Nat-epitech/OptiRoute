<script setup lang="ts">
import { onMounted, ref } from 'vue'

import { getVehicles } from '@/api/vehicleApi'

import type { Vehicle } from '@/models/Vehicle'

import CreateVehicleModal from '@/components/vehicles/CreateVehicleModal.vue'

const vehicles = ref<Vehicle[]>([])

const showCreateModal = ref(false)

const loadVehicles = async () => {
    vehicles.value = await getVehicles()
}

onMounted(async () => {
    await loadVehicles()
})

</script>

<template>

    <div>

        <div class="flex items-center justify-between mb-6">

            <h1 class="text-3xl font-bold text-gray-800">
                Véhicules
            </h1>

            <button @click="showCreateModal = true"
                class="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-xl shadow transition">
                Ajouter un véhicule
            </button>

        </div>

        <div class="bg-white rounded-2xl shadow overflow-hidden">

            <table class="w-full">

                <thead class="bg-gray-50 border-b">

                    <tr>
                        <th class="text-left px-6 py-4">Registration</th>
                        <th class="text-left px-6 py-4">Brand</th>
                        <th class="text-left px-6 py-4">Model</th>
                    </tr>

                </thead>

                <tbody>

                    <tr v-for="vehicle in vehicles" :key="vehicle.id" class="border-b hover:bg-gray-50 transition">

                        <td class="px-6 py-4">
                            {{ vehicle.registration }}
                        </td>

                        <td class="px-6 py-4">
                            {{ vehicle.brand }}
                        </td>

                        <td class="px-6 py-4">
                            {{ vehicle.model }}
                        </td>

                    </tr>

                </tbody>

            </table>

        </div>

        <CreateVehicleModal :show="showCreateModal" @close="showCreateModal = false" @created="loadVehicles" />

    </div>

</template>