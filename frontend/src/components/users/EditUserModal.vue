<script setup lang="ts">
import { ref, watch } from 'vue'

import AppModal from '@/components/ui/AppModal.vue'

import { updateUser } from '@/api/userApi'
import type { User } from '@/models/User'

const props = defineProps<{
    show: boolean
    user: User | null
}>()

const emit = defineEmits<{
    close: []
    updated: []
}>()

const email = ref('')
const password = ref('')
const firstName = ref('')
const lastName = ref('')

const loading = ref(false)

watch(
    () => props.user,
    (user) => {
        if (!user) return

        email.value = user.email
        firstName.value = user.firstName
        lastName.value = user.lastName
        password.value = ''
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

const saveUser = async () => {
    if (!props.user) return

    try {
        loading.value = true

        await updateUser(props.user.id, {
            email: email.value,
            firstName: firstName.value,
            lastName: lastName.value,
            password: password.value || undefined
        })

        emit('updated')
        emit('close')
    } catch (e) {
        console.error(e)
        alert('Erreur lors de la modification')
    } finally {
        loading.value = false
    }
}
</script>

<template>
    <AppModal :show="show" @close="closeModal">
        <h2 class="mb-6 text-2xl font-bold">
            Modifier l'utilisateur
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
                    Nouveau mot de passe
                </label>

                <input v-model="password" type="password"
                    class="w-full rounded-xl border px-4 py-3 outline-none focus:border-blue-500"
                    placeholder="Optionnel" />
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
            <button type="button" class="rounded-xl border px-4 py-2" :disabled="loading" @click="closeModal">
                Annuler
            </button>

            <button type="button"
                class="rounded-xl bg-blue-600 px-4 py-2 text-white transition hover:bg-blue-700 disabled:opacity-50"
                :disabled="loading" @click="saveUser">
                {{ loading ? 'Modification...' : 'Enregistrer' }}
            </button>
        </div>
    </AppModal>
</template>