<script setup lang="ts">
import { onMounted, ref } from "vue"

import { getVehicles } from "@/api/vehicleApi"

import type { Vehicle, VehicleDetails } from "@/models/Vehicle"

import CreateVehicleModal from "@/components/vehicles/CreateVehicleModal.vue"
import EditVehicleModal from "@/components/vehicles/EditVehicleModal.vue"
import DeleteVehicleModal from "@/components/vehicles/DeleteVehicleModal.vue"
import VehicleDetailDrawer from "@/components/vehicles/VehicleDetailDrawer.vue"

import AppDropdown from "@/components/ui/AppDropdown.vue"

const vehicles = ref<Vehicle[]>([])

const showCreateModal = ref(false)

/*
 * Véhicule actuellement affiché dans le drawer.
 * On conserve uniquement son identifiant puisque le drawer
 * appelle lui-même GET /vehicles/{id}.
 */
const selectedVehicleId = ref<number | null>(null)

/*
 * Véhicule utilisé par les modales Modifier/Supprimer.
 * Cette sélection est indépendante de celle du drawer.
 */
type VehicleAction = "edit" | "delete" | null

const actionVehicle = ref<Vehicle | null>(null)
const activeAction = ref<VehicleAction>(null)
const vehicleDrawerRefreshKey = ref(0)

const loadVehicles = async () => {
    vehicles.value = await getVehicles()
}

const openVehicleDetails = (vehicle: Vehicle) => {
    selectedVehicleId.value = vehicle.id
}

const closeVehicleDetails = () => {
    selectedVehicleId.value = null
}

const openEditVehicleModal = (vehicle: Vehicle) => {
    actionVehicle.value = vehicle
    activeAction.value = "edit"
}

const askDeleteVehicle = (vehicle: Vehicle) => {
    actionVehicle.value = vehicle
    activeAction.value = "delete"
}

const closeVehicleAction = () => {
    actionVehicle.value = null
    activeAction.value = null
}

/*
 * Événements déclenchés depuis le drawer.
 *
 * VehicleDetails peut être passé aux modales car VehicleDetails
 * étend Vehicle.
 */
const handleDrawerEdit = (vehicle: VehicleDetails) => {
    openEditVehicleModal(vehicle)
}

const handleDrawerDelete = (vehicle: VehicleDetails) => {
    closeVehicleDetails()
    askDeleteVehicle(vehicle)
}

const handleVehicleCreated = async () => {
    showCreateModal.value = false
    await loadVehicles()
}

const handleVehicleUpdated = async () => {
    closeVehicleAction()
    await loadVehicles()
    vehicleDrawerRefreshKey.value++
}

const handleVehicleDeleted = async () => {
    const deletedVehicleId = actionVehicle.value?.id

    closeVehicleAction()

    if (selectedVehicleId.value === deletedVehicleId) {
        closeVehicleDetails()
    }

    await loadVehicles()
}

const displayValue = (
    value: string | number | null | undefined,
    suffix = "",
) => {
    if (value === null || value === undefined || value === "") {
        return "—"
    }

    return `${value}${suffix}`
}

onMounted(async () => {
    await loadVehicles()
})
</script>

<template>
    <div>
        <div class="mb-6 flex items-center justify-between">
            <h1 class="text-3xl font-bold text-gray-800">
                Véhicules
            </h1>

            <button type="button"
                class="rounded-xl bg-blue-600 px-5 py-3 text-white shadow transition hover:bg-blue-700"
                @click="showCreateModal = true">
                Ajouter un véhicule
            </button>
        </div>

        <div class="rounded-2xl bg-white shadow">
            <table class="w-full">
                <thead class="border-b bg-gray-50">
                    <tr>
                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Immatriculation
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Marque
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Modèle
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-right">
                            Actions
                        </th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="vehicle in vehicles" :key="vehicle.id"
                        class="cursor-pointer border-b transition last:border-b-0 hover:bg-gray-100" :class="{
                            'bg-blue-50':
                                selectedVehicleId === vehicle.id,
                        }" tabindex="0" @click="openVehicleDetails(vehicle)"
                        @keydown.enter="openVehicleDetails(vehicle)"
                        @keydown.space.prevent="openVehicleDetails(vehicle)">
                        <td class="whitespace-nowrap px-6 py-4 font-medium text-gray-900">
                            {{ vehicle.registration }}
                        </td>

                        <td class="whitespace-nowrap px-6 py-4">
                            {{ displayValue(vehicle.brand) }}
                        </td>

                        <td class="whitespace-nowrap px-6 py-4">
                            {{ displayValue(vehicle.model) }}
                        </td>

                        <!--
                                Le stop empêche le clic sur les actions
                                d'ouvrir également le drawer.
                            -->
                        <td class="whitespace-nowrap px-6 py-4 text-right" @click.stop @keydown.stop>
                            <AppDropdown v-slot="{ close }">
                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm hover:bg-gray-50" @click="
                                        close(); openEditVehicleModal(vehicle)
                                        ">
                                    Modifier
                                </button>

                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                    @click="
                                        close(); askDeleteVehicle(vehicle)
                                        ">
                                    Supprimer
                                </button>
                            </AppDropdown>
                        </td>
                    </tr>

                    <tr v-if="vehicles.length === 0">
                        <td colspan="7" class="px-6 py-12 text-center text-sm text-gray-500">
                            Aucun véhicule enregistré.
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <VehicleDetailDrawer :open="selectedVehicleId !== null" :vehicle-id="selectedVehicleId"
            :refresh-key="vehicleDrawerRefreshKey" @close="closeVehicleDetails" @edit="handleDrawerEdit"
            @delete="handleDrawerDelete" />

        <CreateVehicleModal :show="showCreateModal" @close="showCreateModal = false" @created="handleVehicleCreated" />

        <EditVehicleModal :show="activeAction === 'edit'" :vehicle="actionVehicle" @close="closeVehicleAction"
            @updated="handleVehicleUpdated" />

        <DeleteVehicleModal :show="activeAction === 'delete'" :vehicle="actionVehicle" @close="closeVehicleAction"
            @deleted="handleVehicleDeleted" />
    </div>
</template>