<script setup lang="ts">
import { reactive, ref } from 'vue'
import { KeyRound } from 'lucide-vue-next'

import api from '@/api/axios'
import { getApiErrorMessage } from '@/api/utils'
import { useNotification } from '@/composables/useNotification'

const notification = useNotification()
const loading = ref(false)
const form = reactive({ currentPassword: '', newPassword: '', confirmation: '' })

const submit = async () => {
    if (form.newPassword !== form.confirmation) {
        notification.error('Modification impossible', 'Les nouveaux mots de passe ne correspondent pas.')
        return
    }

    try {
        loading.value = true
        await api.put('/users/me/password', {
            currentPassword: form.currentPassword,
            newPassword: form.newPassword,
        })
        form.currentPassword = ''
        form.newPassword = ''
        form.confirmation = ''
        notification.success('Mot de passe modifié', 'Votre mot de passe a bien été modifié.')
    } catch (error: unknown) {
        notification.error('Modification impossible', getApiErrorMessage(error, 'Le mot de passe n’a pas pu être modifié.'))
    } finally {
        loading.value = false
    }
}
</script>

<template>
    <div class="w-full max-w-3xl">
        <div class="mb-6 flex items-center gap-3">
            <div class="flex h-11 w-11 items-center justify-center rounded-xl bg-blue-100 text-blue-600">
                <KeyRound class="h-6 w-6" />
            </div>
            <div>
                <h1 class="text-3xl font-bold text-gray-800">Modifier mon mot de passe</h1>
                <p class="mt-1 text-sm text-gray-500">Mettez à jour vos identifiants de connexion</p>
            </div>
        </div>

        <form class="rounded-2xl bg-white p-6 shadow" @submit.prevent="submit">
            <div class="space-y-5">
                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">Mot de passe actuel</label>
                    <input v-model="form.currentPassword" type="password" required :disabled="loading"
                        class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none focus:border-blue-500 disabled:bg-slate-100" />
                </div>
                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">Nouveau mot de passe</label>
                    <input v-model="form.newPassword" type="password" required minlength="5" :disabled="loading"
                        class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none focus:border-blue-500 disabled:bg-slate-100" />
                </div>
                <div>
                    <label class="mb-1 block text-sm font-medium text-slate-700">Confirmation du nouveau mot de passe</label>
                    <input v-model="form.confirmation" type="password" required minlength="5" :disabled="loading"
                        class="w-full rounded-xl border border-slate-300 px-4 py-3 outline-none focus:border-blue-500 disabled:bg-slate-100" />
                </div>
            </div>
            <div class="mt-6 flex justify-end">
                <button type="submit" :disabled="loading"
                    class="rounded-xl bg-blue-600 px-4 py-2 text-white transition hover:bg-blue-700 disabled:opacity-50">
                    {{ loading ? 'Enregistrement...' : 'Modifier le mot de passe' }}
                </button>
            </div>
        </form>
    </div>
</template>
