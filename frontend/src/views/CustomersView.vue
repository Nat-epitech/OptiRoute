<script setup lang="ts">
import { onMounted, ref } from 'vue'

import { getCustomers } from '@/api/customerApi'

import type { Customer } from '@/models/Customer'

import CreateCustomerModal from '@/components/customers/CreateCustomerModal.vue'
import EditCustomerModal from '@/components/customers/EditCustomerModal.vue'
import DeleteCustomerModal from '@/components/customers/DeleteCustomerModal.vue'
import AppDropdown from '@/components/ui/AppDropdown.vue'


const customers = ref<Customer[]>([])

const showCreateModal = ref(false)

const loadCustomers = async () => {
    customers.value = await getCustomers()
}

type CustomerAction = 'edit' | 'delete' | null
const selectedCustomer = ref<Customer | null>(null)
const activeAction = ref<CustomerAction>(null)

const openEditCustomerModal = (customer: Customer) => {
    selectedCustomer.value = customer
    activeAction.value = 'edit'
}

const askDeleteCustomer = (customer: Customer) => {
    selectedCustomer.value = customer
    activeAction.value = 'delete'
}

const closeCustomerAction = () => {
    selectedCustomer.value = null
    activeAction.value = null
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

        <div class="bg-white rounded-2xl shadow">

            <table class="w-full">

                <thead class="bg-gray-50 border-b">

                    <tr>
                        <th class="text-left px-6 py-4">Nom</th>
                        <th class="text-left px-6 py-4">Code</th>
                        <th class="text-left px-6 py-4">Ville</th>
                        <th class="text-left px-6 py-4">Adresse</th>
                        <th class="text-right px-6 py-4">Actions</th>
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

                        <td class="px-6 py-4 text-right">
                            <AppDropdown v-slot="{ close }">
                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm hover:bg-gray-50"
                                    @click="close(); openEditCustomerModal(customer)">
                                    Modifier
                                </button>

                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                    @click="close(); askDeleteCustomer(customer)">
                                    Supprimer
                                </button>
                            </AppDropdown>
                        </td>

                    </tr>

                </tbody>

            </table>

        </div>

        <CreateCustomerModal :show="showCreateModal" @close="showCreateModal = false" @created="loadCustomers" />
        <EditCustomerModal :show="activeAction === 'edit'" :customer="selectedCustomer" @close="closeCustomerAction"
            @updated="loadCustomers" />

        <DeleteCustomerModal :show="activeAction === 'delete'" :customer="selectedCustomer" @close="closeCustomerAction"
            @deleted="loadCustomers" />

    </div>

</template>