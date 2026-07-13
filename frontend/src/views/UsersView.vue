<script setup lang="ts">
import { onMounted, ref } from 'vue'

import { getUsers } from '@/api/userApi'

import type { User } from '@/models/User'

import CreateUserModal from '@/components/users/CreateUserModal.vue'
import DeleteUserModal from '@/components/users/DeleteUserModal.vue'
import EditUserModal from '@/components/users/EditUserModal.vue'
import AppDropdown from '@/components/ui/AppDropdown.vue'

const users = ref<User[]>([])

const showCreateModal = ref(false)

const loadUsers = async () => {
    users.value = await getUsers()
}

onMounted(async () => {
    await loadUsers()
})

type UserAction = 'edit' | 'delete' | null

const activeAction = ref<UserAction>(null)
const selectedUser = ref<User | null>(null)

const openEditUserModal = (user: User) => {
    selectedUser.value = user
    activeAction.value = 'edit'
}

const askDeleteUser = (user: User) => {
    selectedUser.value = user
    activeAction.value = 'delete'
}

const closeUserAction = () => {
    selectedUser.value = null
    activeAction.value = null
}

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

        <div class="bg-white rounded-2xl shadow">

            <table class="w-full">

                <thead class="bg-gray-50 border-b">

                    <tr>
                        <th class="text-left px-6 py-4">Email</th>
                        <th class="text-left px-6 py-4">Prénom</th>
                        <th class="text-left px-6 py-4">Nom</th>
                        <th class="text-right px-6 py-4">Actions</th>
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

                        <td class="px-6 py-4 text-right">

                            <AppDropdown v-slot="{ close }">

                                <button class="flex w-full items-center px-4 py-2 text-sm hover:bg-gray-50"
                                    @click="close(); openEditUserModal(user)">
                                    Modifier
                                </button>

                                <button class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                    @click="close(); askDeleteUser(user)">
                                    Supprimer
                                </button>

                            </AppDropdown>

                        </td>

                    </tr>

                </tbody>

            </table>

        </div>

        <CreateUserModal :show="showCreateModal" @close="showCreateModal = false" @created="loadUsers" />

        <DeleteUserModal :show="activeAction === 'delete'" :user="selectedUser" @close="closeUserAction"
            @deleted="loadUsers" />

        <EditUserModal :show="activeAction === 'edit'" :user="selectedUser" @close="closeUserAction"
            @updated="loadUsers" />

    </div>

</template>