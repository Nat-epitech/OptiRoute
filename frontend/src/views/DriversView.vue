<script setup lang="ts">
import { onMounted, ref } from 'vue'

import { getDrivers } from '@/api/driverApi'

import type { Driver } from '@/models/Driver'

import DeleteDriverModal from '@/components/drivers/DeleteDriverModal.vue'
import EditDriverModal from '@/components/drivers/EditDriverModal.vue'
import CreateDriverModal from '@/components/drivers/CreateDriverModal.vue'
import AppDropdown from '@/components/ui/AppDropdown.vue'

const drivers = ref<Driver[]>([])

const showCreateModal = ref(false)

const loadDrivers = async () => {
    drivers.value = await getDrivers()
}



type DriverAction = 'edit' | 'delete' | null

const selectedDriver = ref<Driver | null>(null)
const activeAction = ref<DriverAction>(null)

const openEditDriverModal = (driver: Driver) => {
    selectedDriver.value = driver
    activeAction.value = 'edit'
}

const askDeleteDriver = (driver: Driver) => {
    selectedDriver.value = driver
    activeAction.value = 'delete'
}

const closeDriverAction = () => {
    selectedDriver.value = null
    activeAction.value = null
}

onMounted(async () => {
    await loadDrivers()
})

</script>

<template>

    <div>

        <div class="flex items-center justify-between mb-6">

            <h1 class="text-3xl font-bold text-gray-800">
                Conducteurs
            </h1>

            <button @click="showCreateModal = true"
                class="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-xl shadow transition">
                Ajouter un conducteur
            </button>

        </div>

        <div class="bg-white rounded-2xl shadow">

            <table class="w-full">

                <thead class="bg-gray-50 border-b">

                    <tr>
                        <th class="text-left px-6 py-4">Email</th>
                        <th class="text-left px-6 py-4">Prénom</th>
                        <th class="text-left px-6 py-4">Nom</th>
                        <th class="text-left px-6 py-4">Numéro de téléphone</th>
                        <th class="text-right px-6 py-4">Actions</th>
                    </tr>

                </thead>

                <tbody>

                    <tr v-for="driver in drivers" :key="driver.id" class="border-b hover:bg-gray-50 transition">

                        <td class="px-6 py-4">
                            {{ driver.email }}
                        </td>

                        <td class="px-6 py-4">
                            {{ driver.firstName }}
                        </td>

                        <td class="px-6 py-4">
                            {{ driver.lastName }}
                        </td>

                        <td class="px-6 py-4">
                            {{ driver.phoneNumber }}
                        </td>

                        <td class="px-6 py-4 text-right">
                            <AppDropdown v-slot="{ close }">
                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm hover:bg-gray-50"
                                    @click="close(); openEditDriverModal(driver)">
                                    Modifier
                                </button>

                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                    @click="close(); askDeleteDriver(driver)">
                                    Supprimer
                                </button>
                            </AppDropdown>
                        </td>

                    </tr>

                </tbody>

            </table>

        </div>

        <CreateDriverModal :show="showCreateModal" @close="showCreateModal = false" @created="loadDrivers" />
        <EditDriverModal :show="activeAction === 'edit'" :driver="selectedDriver" @close="closeDriverAction"
            @updated="loadDrivers" />
        <DeleteDriverModal :show="activeAction === 'delete'" :driver="selectedDriver" @close="closeDriverAction"
            @deleted="loadDrivers" />

    </div>

</template>