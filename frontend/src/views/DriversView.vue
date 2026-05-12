<script setup lang="ts">
import { onMounted, ref } from 'vue'

import { getDrivers } from '@/api/driverApi'

import type { Driver } from '@/models/Driver'

import CreateDriverModal
    from '@/components/drivers/CreateDriverModal.vue'

const drivers = ref<Driver[]>([])

const showCreateModal = ref(false)

const loadDrivers = async () => {
    drivers.value = await getDrivers()
}

onMounted(async () => {
    await loadDrivers()
})

</script>

<template>

    <div>

        <div class="flex items-center justify-between mb-6">

            <h1 class="text-3xl font-bold text-gray-800">
                Drivers
            </h1>

            <button @click="showCreateModal = true"
                class="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-xl shadow transition">
                + Create Driver
            </button>

        </div>

        <div class="bg-white rounded-2xl shadow overflow-hidden">

            <table class="w-full">

                <thead class="bg-gray-50 border-b">

                    <tr>
                        <th class="text-left px-6 py-4">Email</th>
                        <th class="text-left px-6 py-4">First Name</th>
                        <th class="text-left px-6 py-4">Last Name</th>
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

                    </tr>

                </tbody>

            </table>

        </div>

        <CreateDriverModal :show="showCreateModal" @close="showCreateModal = false" @created="loadDrivers" />

    </div>

</template>