<script setup lang="ts">
import { ref, watch } from 'vue'

import AppModal from '@/components/ui/AppModal.vue'

import { updateDriver } from '@/api/driverApi'
import type { Driver } from '@/models/Driver'

const props = defineProps<{
    show: boolean
    driver: Driver | null
}>()

const emit = defineEmits<{
    close: []
    updated: []
}>()

const email = ref('')
const firstName = ref('')
const lastName = ref('')
const phoneNumber = ref('')
const loading = ref(false)

watch(
    () => props.driver, (driver) => {
        if (!driver) return

        email.value = driver.email
        firstName.value = driver.firstName
        lastName.value = driver.lastName
        phoneNumber.value = driver.phoneNumber
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

const saveDriver = async () => {
    if (!props.driver) return

    try {
        loading.value = true

        await updateDriver(props.driver.id, {
            email: email.value,
            firstName: firstName.value,
            lastName: lastName.value,
            phoneNumber: phoneNumber.value
        })

        emit('updated')
        emit('close')
    } catch (error) {
        console.error(error)
        alert('Erreur lors de la modification du conducteur')
    } finally {
        loading.value = false
    }
}
</script>

<template>
    <AppModal :show="show" @close="closeModal">
        <h2 class="mb-6 text-2xl font-bold">
            Modifier le conducteur
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
                    Numéro de téléphone
                </label>

                <input v-model="phoneNumber" type="tel" required
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

        <div class="mt-6 flex justify-end gap-3">
            <button type="button" :disabled="loading" class="rounded-xl border px-4 py-2 disabled:opacity-50"
                @click="closeModal">
                Annuler
            </button>

            <button type="button" :disabled="loading"
                class="rounded-xl bg-blue-600 px-4 py-2 text-white transition hover:bg-blue-700 disabled:opacity-50"
                @click="saveDriver">
                {{ loading ? 'Modification...' : 'Enregistrer' }}
            </button>
        </div>
    </AppModal>
</template>