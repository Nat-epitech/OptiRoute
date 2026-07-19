<script setup lang="ts">
import { onMounted, ref } from 'vue'

import { getVehicles } from '@/api/vehicleApi'

import type { Vehicle } from '@/models/Vehicle'

import CreateVehicleModal from '@/components/vehicles/CreateVehicleModal.vue'
import EditVehicleModal from '@/components/vehicles/EditVehicleModal.vue'
import DeleteVehicleModal from '@/components/vehicles/DeleteVehicleModal.vue'
import AppDropdown from '@/components/ui/AppDropdown.vue'

const vehicles = ref<Vehicle[]>([])

const showCreateModal = ref(false)

const loadVehicles = async () => {
    vehicles.value = await getVehicles()
}

type VehicleAction = 'edit' | 'delete' | null

const selectedVehicle = ref<Vehicle | null>(null)
const activeAction = ref<VehicleAction>(null)

const openEditVehicleModal = (vehicle: Vehicle) => {
    selectedVehicle.value = vehicle
    activeAction.value = 'edit'
}

const askDeleteVehicle = (vehicle: Vehicle) => {
    selectedVehicle.value = vehicle
    activeAction.value = 'delete'
}

const closeVehicleAction = () => {
    selectedVehicle.value = null
    activeAction.value = null
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

        <div class="bg-white rounded-2xl shadow">

            <table class="w-full">

                <thead class="bg-gray-50 border-b">

                    <tr>
                        <th class="text-left px-6 py-4">Immatriculation</th>
                        <th class="text-left px-6 py-4">Marque</th>
                        <th class="text-left px-6 py-4">Modèle</th>
                        <th class="text-left px-6 py-4">Type de carburant</th>
                        <th class="text-left px-6 py-4">Consommation moyenne</th>
                        <th class="text-left px-6 py-4">Capacité du réservoir</th>
                        <th class="text-right px-6 py-4">Actions</th>
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

                        <td class="px-6 py-4">
                            {{ vehicle.fuelType }}
                        </td>

                        <td class="px-6 py-4">
                            {{ vehicle.averageConsumption }}L / 100 km
                        </td>

                        <td class="px-6 py-4">
                            {{ vehicle.tankCapacity }} Litres
                        </td>

                        <td class="px-6 py-4 text-right">
                            <AppDropdown v-slot="{ close }">
                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm hover:bg-gray-50"
                                    @click="close(); openEditVehicleModal(vehicle)">
                                    Modifier
                                </button>

                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                    @click="close(); askDeleteVehicle(vehicle)">
                                    Supprimer
                                </button>
                            </AppDropdown>
                        </td>

                    </tr>

                </tbody>

            </table>

        </div>

        <CreateVehicleModal :show="showCreateModal" @close="showCreateModal = false" @created="loadVehicles" />
        <EditVehicleModal :show="activeAction === 'edit'" :vehicle="selectedVehicle" @close="closeVehicleAction"
            @updated="loadVehicles" />

        <DeleteVehicleModal :show="activeAction === 'delete'" :vehicle="selectedVehicle" @close="closeVehicleAction"
            @deleted="loadVehicles" />

    </div>

</template>