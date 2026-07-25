<script setup lang="ts">
import { onMounted, ref } from "vue"

import { getDrivers } from "@/api/driverApi"

import type {
    Driver,
    DriverDetails,
} from "@/models/Driver"

import CreateDriverModal from "@/components/drivers/CreateDriverModal.vue"
import DeleteDriverModal from "@/components/drivers/DeleteDriverModal.vue"
import DriverDetailDrawer from "@/components/drivers/DriverDetailDrawer.vue"
import EditDriverModal from "@/components/drivers/EditDriverModal.vue"
import AppDropdown from "@/components/ui/AppDropdown.vue"

const drivers = ref<Driver[]>([])

const showCreateModal = ref(false)

const selectedDriverId = ref<number | null>(null)
const actionDriver = ref<Driver | null>(null)

type DriverAction = "edit" | "delete" | null

const activeAction = ref<DriverAction>(null)
const driverDrawerRefreshKey = ref(0)

const loadDrivers = async () => {
    drivers.value = await getDrivers()
}

const openDriverDetails = (driver: Driver) => {
    selectedDriverId.value = driver.id
}

const closeDriverDetails = () => {
    selectedDriverId.value = null
}

const openEditDriverModal = (driver: Driver) => {
    actionDriver.value = driver
    activeAction.value = "edit"
}

const askDeleteDriver = (driver: Driver) => {
    actionDriver.value = driver
    activeAction.value = "delete"
}

const closeDriverAction = () => {
    actionDriver.value = null
    activeAction.value = null
}

const handleDrawerEdit = (driver: DriverDetails) => {
    openEditDriverModal(driver)
}

const handleDrawerDelete = (driver: DriverDetails) => {
    closeDriverDetails()
    askDeleteDriver(driver)
}

const handleDriverCreated = async () => {
    showCreateModal.value = false
    await loadDrivers()
}

const handleDriverUpdated = async () => {
    closeDriverAction()
    await loadDrivers()
    driverDrawerRefreshKey.value++
}

const handleDriverDeleted = async () => {
    closeDriverAction()
    closeDriverDetails()
    await loadDrivers()
}

const displayValue = (value: string | null | undefined,): string => {
    return value || "—"
}

onMounted(async () => {
    await loadDrivers()
})
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
                            Email
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Prénom
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Nom
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Numéro de téléphone
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-right">
                            Actions
                        </th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="driver in drivers" :key="driver.id"
                        class="cursor-pointer border-b transition last:border-b-0 hover:bg-gray-50" :class="{
                            'bg-blue-50':
                                selectedDriverId === driver.id,
                        }" tabindex="0" @click="openDriverDetails(driver)" @keydown.enter="openDriverDetails(driver)"
                        @keydown.space.prevent="openDriverDetails(driver)">
                        <td class="whitespace-nowrap px-6 py-4">
                            {{ displayValue(driver.email) }}
                        </td>

                        <td class="whitespace-nowrap px-6 py-4">
                            {{ displayValue(driver.firstName) }}
                        </td>

                        <td class="whitespace-nowrap px-6 py-4 font-medium text-gray-900">
                            {{ displayValue(driver.lastName) }}
                        </td>

                        <td class="whitespace-nowrap px-6 py-4">
                            {{ displayValue(driver.phoneNumber) }}
                        </td>

                        <!--
                            Empêche le clic sur le menu d'ouvrir
                            également le drawer.
                        -->
                        <td class="whitespace-nowrap px-6 py-4 text-right" @click.stop @keydown.stop>
                            <AppDropdown v-slot="{ close }">
                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm hover:bg-gray-50" @click="
                                        close();
                                    openEditDriverModal(driver)
                                        ">
                                    Modifier
                                </button>

                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                    @click="
                                        close();
                                    askDeleteDriver(driver)
                                        ">
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

        <DriverDetailDrawer :open="selectedDriverId !== null" :driver-id="selectedDriverId"
            :refresh-key="driverDrawerRefreshKey" @close="closeDriverDetails" @edit="handleDrawerEdit"
            @delete="handleDrawerDelete" />

        <CreateDriverModal :show="showCreateModal" @close="showCreateModal = false" @created="handleDriverCreated" />

        <EditDriverModal :show="activeAction === 'edit'" :driver="actionDriver" @close="closeDriverAction"
            @updated="handleDriverUpdated" />

        <DeleteDriverModal :show="activeAction === 'delete'" :driver="actionDriver" @close="closeDriverAction"
            @deleted="handleDriverDeleted" />
    </div>
</template>