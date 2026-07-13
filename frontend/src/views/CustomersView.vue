<script setup lang="ts">
import { onMounted, ref } from 'vue'

import { getCustomers } from '@/api/customerApi'

import type { Customer } from '@/models/Customer'

import CreateCustomerModal from '@/components/customers/CreateCustomerModal.vue'

const customers = ref<Customer[]>([])

const showCreateModal = ref(false)

const loadCustomers = async () => {
    customers.value = await getCustomers()
}

onMounted(async () => {
    await loadCustomers()
})

</script>

<template>

    <div>

        <div class="flex items-center justify-between mb-6">

            <h1 class="text-3xl font-bold text-gray-800">
                Clients
            </h1>

            <button @click="showCreateModal = true"
                class="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-xl shadow transition">
                Ajouter un client
            </button>

        </div>

        <div class="bg-white rounded-2xl shadow overflow-hidden">

            <table class="w-full">

                <thead class="bg-gray-50 border-b">

                    <tr>
                        <th class="text-left px-6 py-4">Nom</th>
                        <th class="text-left px-6 py-4">Code</th>
                        <th class="text-left px-6 py-4">Ville</th>
                        <th class="text-left px-6 py-4">Adresse</th>
                    </tr>

                </thead>

                <tbody>

                    <tr v-for="customer in customers" :key="customer.id" class="border-b hover:bg-gray-50 transition">

                        <td class="px-6 py-4">
                            {{ customer.name }}
                        </td>

                        <td class="px-6 py-4">
                            {{ customer.code }}
                        </td>

                        <td class="px-6 py-4">
                            {{ customer.city }}
                        </td>

                        <td class="px-6 py-4">
                            {{ customer.address }}
                        </td>

                    </tr>

                </tbody>

            </table>

        </div>

        <CreateCustomerModal :show="showCreateModal" @close="showCreateModal = false" @created="loadCustomers" />

    </div>

</template>