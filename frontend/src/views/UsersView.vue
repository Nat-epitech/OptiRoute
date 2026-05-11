<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getUsers } from '@/api/userApi'
import type { User } from '@/models/User'

const users = ref<User[]>([])

onMounted(async () => {
    users.value = await getUsers()
})
</script>

<template>
    <div>

        <h1 class="text-3xl font-bold text-gray-800 mb-6">Users</h1>

        <div class="bg-white rounded-2xl shadow overflow-hidden">
            <table class="w-full">
                <thead class="bg-gray-50 border-b">
                    <tr>
                        <th class="text-left px-6 py-4">ID</th>
                        <th class="text-left px-6 py-4">Email</th>
                        <th class="text-left px-6 py-4">First Name</th>
                        <th class="text-left px-6 py-4">Last Name</th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="user in users" :key="user.id" class="border-b hover:bg-gray-50 transition">
                        <td class="px-6 py-4">{{ user.id }}</td>
                        <td class="px-6 py-4">{{ user.email }}</td>
                        <td class="px-6 py-4">{{ user.firstName }}</td>
                        <td class="px-6 py-4">{{ user.lastName }}</td>
                    </tr>
                </tbody>
            </table>
        </div>

    </div>
</template>