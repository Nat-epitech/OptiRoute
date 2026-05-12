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
    password.value = ''
    firstName.value = ''
    lastName.value = ''
}
</script>

<template>

    <AppModal :show="props.show" @close="emit('close')">

        <h2 class="text-2xl font-bold mb-6">
            Create User
        </h2>

        <div class="space-y-4">

            <input v-model="email" type="email" placeholder="Email" class="w-full border rounded-xl px-4 py-3" />

            <input v-model="password" type="password" placeholder="Password"
                class="w-full border rounded-xl px-4 py-3" />

            <input v-model="firstName" placeholder="First Name" class="w-full border rounded-xl px-4 py-3" />

            <input v-model="lastName" placeholder="Last Name" class="w-full border rounded-xl px-4 py-3" />

        </div>

        <div class="flex justify-end gap-3 mt-6">

            <button @click="emit('close')" class="px-4 py-2 rounded-xl border">
                Cancel
            </button>

            <button @click="createUser" :disabled="loading"
                class="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-xl transition">
                {{ loading ? 'Creating...' : 'Create' }}
            </button>

        </div>

    </AppModal>

</template>