<script setup lang="ts">
import { ref } from 'vue'

import AppModal from '@/components/ui/AppModal.vue'

import api from '@/api/axios'

const props = defineProps<{
    show: boolean
}>()

const emit = defineEmits([
    'close',
    'created'
])

const email = ref('')
const firstName = ref('')
const lastName = ref('')
const phoneNumber = ref('')

const loading = ref(false)

const createDriver = async () => {

    try {
        loading.value = true

        await api.post('/drivers', {
            email: email.value,
            firstName: firstName.value,
            lastName: lastName.value,
            phoneNumber: phoneNumber.value
        })

        emit('created')

        emit('close')

        resetForm()

    } catch (e) {

        console.error(e)

        alert('Error creating user')

    } finally {

        loading.value = false
    }
}

const resetForm = () => {
    email.value = ''
    firstName.value = ''
    lastName.value = ''
    phoneNumber.value = ''
}
</script>

<template>

    <AppModal :show="props.show" @close="emit('close')">

        <h2 class="text-2xl font-bold mb-6">
            Ajouter un nouveau conducteur
        </h2>

        <div class="space-y-4">

            <div>
                <label class="mb-1 block text-sm font-medium text-gray-700">
                    Email
                </label>

                <input v-model="email" type="email" required
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
            </div>

            <div>
                <label class="mb-1 block text-sm font-medium text-gray-700">
                    Prénom
                </label>

                <input v-model="firstName" type="text" required
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
            </div>

            <div>
                <label class="mb-1 block text-sm font-medium text-gray-700">
                    Nom
                </label>

                <input v-model="lastName" type="text" required
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
            </div>

            <div>
                <label class="mb-1 block text-sm font-medium text-gray-700">
                    Numéro de téléphone
                </label>

                <input v-model="phoneNumber" type="tel" required
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500" />
            </div>

        </div>

        <div class="flex justify-end gap-3 mt-6">

            <button @click="emit('close')" class="px-4 py-2 rounded-xl border">
                Annuler
            </button>

            <button @click="createDriver" :disabled="loading"
                class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-xl transition">
                {{ loading ? 'Création...' : 'Ajouter le conducteur' }}
            </button>

        </div>

    </AppModal>

</template>