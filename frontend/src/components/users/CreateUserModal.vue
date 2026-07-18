<script setup lang="ts">
import { ref } from 'vue'

import AppModal from '@/components/ui/AppModal.vue'

import api from '@/api/axios'
import { getApiErrorMessage } from '@/api/utils'

import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const props = defineProps<{
    show: boolean
}>()

const emit = defineEmits([
    'close',
    'created'
])

const email = ref('')
const password = ref('')
const firstName = ref('')
const lastName = ref('')

const loading = ref(false)

const createUser = async () => {

    try {
        loading.value = true

        await api.post('/users', {
            email: email.value,
            password: password.value,
            firstName: firstName.value,
            lastName: lastName.value
        })

        notification.success(
            'User enregistré',
            `L'utilisateur « ${firstName.value} ${lastName.value} » a bien été ajouté.`
        )

        emit('created')
        emit('close')

        resetForm()
    } catch (error) {
        notification.error(
            'Enregistrement impossible',
            getApiErrorMessage(
                error,
                'L\'utilisateur n’a pas pu être ajouté.'
            )
        )
    } finally {
        loading.value = false
    }
}

const resetForm = () => {

    email.value = ''
    password.value = ''
    firstName.value = ''
    lastName.value = ''
}
</script>

<template>

    <AppModal :show="props.show" @close="emit('close')">

        <h2 class="text-2xl font-bold mb-6">
            Ajouter un nouveau utilisateur
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
                    Mot de passe
                </label>

                <input v-model="password" type="password" required
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

        </div>

        <div class="flex justify-end gap-3 mt-6">

            <button @click="emit('close')" class="px-4 py-2 rounded-xl border">
                Annuler
            </button>

            <button @click="createUser" :disabled="loading"
                class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-xl transition">
                {{ loading ? 'Création...' : 'Ajouter l\'utilisateur' }}
            </button>

        </div>

    </AppModal>

</template>