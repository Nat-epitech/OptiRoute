<script setup lang="ts">
import { ref, watch } from 'vue'

import AppModal from '@/components/ui/AppModal.vue'

import { updateCustomer } from '@/api/customerApi'

import type {
    Customer,
    CustomerUpdateRequest
} from '@/models/Customer'

const props = defineProps<{
    show: boolean
    customer: Customer | null
}>()

const emit = defineEmits<{
    close: []
    updated: []
}>()

const name = ref('')
const code = ref('')
const address = ref('')
const city = ref('')
const country = ref('')

const loading = ref(false)

watch(
    () => props.customer,
    (customer) => {
        if (!customer) return

        name.value = customer.name
        code.value = customer.code ?? ''
        address.value = customer.address ?? ''
        city.value = customer.city ?? ''
        country.value = customer.country ?? ''
    },
    {
        immediate: true
    }
)

const closeModal = () => {
    if (!loading.value) {
        emit('close')
    }
}

const saveCustomer = async () => {
    if (!props.customer) return

    const request: CustomerUpdateRequest = {
        name: name.value.trim(),
        code: code.value.trim() || null,
        address: address.value.trim() || null,
        city: city.value.trim() || null,
        country: country.value.trim() || null,
        metadata: props.customer.metadata
    }

    try {
        loading.value = true

        await updateCustomer(props.customer.id, request)

        emit('updated')
        emit('close')
    } catch (error) {
        console.error(error)
        alert('Erreur lors de la modification du client')
    } finally {
        loading.value = false
    }
}
</script>

<template>
    <AppModal :show="show" @close="closeModal">
        <h2 class="mb-6 text-2xl font-bold">
            Modifier le client
        </h2>

        <div class="space-y-4">
            <div>
                <label class="mb-1 block text-sm font-medium text-gray-700">
                    Nom
                </label>

                <input v-model="name" type="text" required
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
            </div>

            <div>
                <label class="mb-1 block text-sm font-medium text-gray-700">
                    Code
                </label>

                <input v-model="code" type="text"
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
            </div>

            <div>
                <label class="mb-1 block text-sm font-medium text-gray-700">
                    Adresse
                </label>

                <input v-model="address" type="text"
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
            </div>

            <div class="grid grid-cols-2 gap-4">
                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Ville
                    </label>

                    <input v-model="city" type="text"
                        class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
                </div>

                <div>
                    <label class="mb-1 block text-sm font-medium text-gray-700">
                        Pays
                    </label>

                    <input v-model="country" type="text"
                        class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
                </div>
            </div>
        </div>

        <div class="mt-6 flex justify-end gap-3">
            <button type="button" :disabled="loading" class="rounded-xl border px-4 py-2 disabled:opacity-50"
                @click="closeModal">
                Annuler
            </button>

            <button type="button" :disabled="loading || !name.trim()"
                class="rounded-xl bg-blue-600 px-4 py-2 text-white transition hover:bg-blue-700 disabled:opacity-50"
                @click="saveCustomer">
                {{ loading ? 'Modification...' : 'Enregistrer' }}
            </button>
        </div>
    </AppModal>
</template>