<script setup lang="ts">
import { onMounted, ref } from "vue"

import { getDrivers } from "@/api/driver/driverApi"

import type { DriverSummary } from "@/models/driver/Driver"

import CreateDriverModal from "@/components/drivers/CreateDriverModal.vue"
import DeleteDriverModal from "@/components/drivers/DeleteDriverModal.vue"
import DriverDetailDrawer from "@/components/drivers/DriverDetailDrawer.vue"
import AppDropdown from "@/components/ui/AppDropdown.vue"

//Load drivers

const drivers = ref<DriverSummary[]>([])

const loadDrivers = async () => {
    drivers.value = await getDrivers()
}

//Manage Modal

const showCreateModal = ref(false)
const showDeleteModal = ref(false)

const actionDriver = ref<DriverSummary | null>(null)
const selectedDriverId = ref<number | null>(null)

const askDeleteDriver = (driver: DriverSummary) => {
    actionDriver.value = driver
    showDeleteModal.value = true
}

const openDriverDetails = (driver: DriverSummary) => {
    selectedDriverId.value = driver.id
}

const closeDriverDetails = () => {
    selectedDriverId.value = null
}

const closeDriverAction = () => {
    showDeleteModal.value = false
    actionDriver.value = null
}

// Handle

const handleDriverCreated = async () => {
    showCreateModal.value = false
    await loadDrivers()
}

const handleDriverDeleted = async () => {
    closeDriverAction()
    closeDriverDetails()
    await loadDrivers()
}

onMounted(loadDrivers)
</script>

<template>
    <div>
        <div class="mb-6 flex items-center justify-between">
            <h1 class="text-3xl font-bold text-gray-800">
                Conducteurs
            </h1>

            <button type="button"
                class="rounded-xl bg-blue-600 px-5 py-3 text-white shadow transition hover:bg-blue-700"
                @click="showCreateModal = true">
                Ajouter un conducteur
            </button>
        </div>

        <div class="rounded-2xl bg-white shadow">
            <table class="w-full">
                <thead class="border-b bg-gray-50">
                    <tr>
                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Nom
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Prénom
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-right">
                            Actions
                        </th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="driver in drivers" :key="driver.id"
                        class="cursor-pointer border-b transition last:border-b-0 hover:bg-gray-100" :class="{
                            'bg-blue-50': selectedDriverId === driver.id
                        }" tabindex="0" @click="openDriverDetails(driver)" @keydown.enter="openDriverDetails(driver)"
                        @keydown.space.prevent="openDriverDetails(driver)">

                        <td class="whitespace-nowrap px-6 py-4 font-medium text-gray-900">
                            {{ driver.lastName }}
                        </td>

                        <td class="whitespace-nowrap px-6 py-4">
                            {{ driver.firstName }}
                        </td>

                        <!--Empêche le clic sur le menu d'ouvrir également le drawer -->
                        <td class="whitespace-nowrap px-6 py-4 text-right" @click.stop @keydown.stop>
                            <AppDropdown v-slot="{ close }">
                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                    @click="close(); askDeleteDriver(driver)">
                                    Supprimer
                                </button>
                            </AppDropdown>
                        </td>
                    </tr>

                    <tr v-if="drivers.length === 0">
                        <td colspan="5" class="px-6 py-12 text-center text-sm text-gray-500">
                            Aucun conducteur enregistré.
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <DriverDetailDrawer :open="selectedDriverId !== null" :driver-id="selectedDriverId" @close="closeDriverDetails"
            @updated="loadDrivers" @deleted="handleDriverDeleted" />

        <CreateDriverModal :show="showCreateModal" @close="showCreateModal = false" @created="handleDriverCreated" />

        <DeleteDriverModal :show="showDeleteModal" :driver="actionDriver" @close="closeDriverAction"
            @deleted="handleDriverDeleted" />
    </div>
</template>