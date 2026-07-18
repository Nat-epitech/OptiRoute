<script setup lang="ts">
import { ref } from 'vue'

import { deleteUser } from '@/api/userApi'
import type { User } from '@/models/User'
import { getApiErrorMessage } from '@/api/utils'

import ConfirmDeleteModal from '@/components/ui/ConfirmDeleteModal.vue'
import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const props = defineProps<{
    show: boolean
    user: User | null
}>()

const emit = defineEmits<{
    close: []
    deleted: []
}>()

const deleting = ref(false)

const closeModal = () => {
    if (!deleting.value) {
        emit('close')
    }
}

const confirmDelete = async () => {
    if (!props.user) return

    try {
        deleting.value = true

        await deleteUser(props.user.id)

        notification.success(
            'Utilisateur supprimé',
            `L'utilisateur « ${props.user?.firstName ?? ''} ${props.user?.lastName ?? ''} » a bien été supprimé.`
        )

        emit('close')
        emit('deleted')
    } catch (error) {
        notification.error(
            'Suppression impossible',
            getApiErrorMessage(
                error,
                'L\'utilisateur n’a pas pu être supprimé.'
            )
        )
    } finally {
        deleting.value = false
    }
}
</script>

<template>
    <ConfirmDeleteModal :show="show" :loading="deleting" title="Supprimer l'utilisateur"
        :message="`Voulez-vous vraiment supprimer ${user?.firstName ?? ''} ${user?.lastName ?? ''} ?`"
        @close="closeModal" @confirm="confirmDelete" />
</template>