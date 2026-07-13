<script setup lang="ts">
import { onMounted, ref } from 'vue'

import { getUsers } from '@/api/userApi'

import type { User } from '@/models/User'

import CreateUserModal from '@/components/users/CreateUserModal.vue'

const users = ref<User[]>([])

const showCreateModal = ref(false)

const loadUsers = async () => {
    users.value = await getUsers()
}

onMounted(async () => {
    await loadUsers()
})

</script>

<template>

    <div>

        <div class="flex items-center justify-between mb-6">

            <h1 class="text-3xl font-bold text-gray-800">
                Utilisateurs
            </h1>

            <button @click="showCreateModal = true"
                class="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-xl shadow transition">
                Ajouter un utilisateur
            </button>

        </div>

        <div class="bg-white rounded-2xl shadow overflow-hidden">

            <table class="w-full">

                <thead class="bg-gray-50 border-b">

                    <tr>
                        <th class="text-left px-6 py-4">Email</th>
                        <th class="text-left px-6 py-4">Prénom</th>
                        <th class="text-left px-6 py-4">Nom</th>
                    </tr>

                </thead>

                <tbody>

                    <tr v-for="user in users" :key="user.id" class="border-b hover:bg-gray-50 transition">

                        <td class="px-6 py-4">
                            {{ user.email }}
                        </td>

                        <td class="px-6 py-4">
                            {{ user.firstName }}
                        </td>

                        <td class="px-6 py-4">
                            {{ user.lastName }}
                        </td>

                    </tr>

                </tbody>

            </table>

        </div>

        <CreateUserModal :show="showCreateModal" @close="showCreateModal = false" @created="loadUsers" />

    </div>

</template>