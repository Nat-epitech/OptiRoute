```vue
<script setup lang="ts">
import { ref } from 'vue'

import AppModal from '@/components/ui/AppModal.vue'

import api from '@/api/axios'
import { getApiErrorMessage } from '@/api/utils'

import type { CustomerCreateRequest } from '@/models/Customer'
import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const props = defineProps<{
    show: boolean
}>()

const emit = defineEmits([
    'close',
    'created'
])

const name = ref('')
const code = ref('')
const address = ref('')
const city = ref('')
const country = ref('France')

const contactName = ref('')
const deliveryInstructions = ref('')

const loading = ref(false)

const createCustomer = async () => {

    try {
        loading.value = true

        const payload: CustomerCreateRequest = {
            externalId: null,
            externalSource: 'MANUAL',

            name: name.value.trim(),

            code: code.value.trim() || null,
            address: address.value.trim() || null,
            city: city.value.trim() || null,
            country: country.value.trim() || null,

            metadata: null
        }

        await api.post('/customers', payload)

        notification.success(
            'Client créé',
            `Le client « ${name.value.trim()} » a bien été créé.`
        )

        emit('created')
        emit('close')

        resetForm()
    } catch (e) {
        notification.error(
            'Création impossible',
            getApiErrorMessage(
                e,
                'Le client n’a pas pu être créé.'
            )
        )
    } finally {
        loading.value = false
    }
}

const resetForm = () => {
    name.value = ''
    code.value = ''
    address.value = ''
    city.value = ''
    country.value = 'France'

    contactName.value = ''
    deliveryInstructions.value = ''
}
</script>

<template>

    <AppModal :show="props.show" @close="emit('close')">

        <h2 class="text-2xl font-bold mb-6">
            Ajouter un nouveau client
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

        <div class="flex justify-end gap-3 mt-6">

            <button @click="emit('close')" class="px-4 py-2 rounded-xl border">
                Annuler
            </button>

            <button @click="createCustomer" :disabled="loading"
                class="bg-blue-600 hover:bg-blue-700 disabled:opacity-50 text-white px-4 py-2 rounded-xl transition">
                {{ loading ? 'Création...' : 'Ajouter le client' }}
            </button>

        </div>

    </AppModal>

</template>
```
