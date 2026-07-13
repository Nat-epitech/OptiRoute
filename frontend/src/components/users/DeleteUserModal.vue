<script setup lang="ts">
import { ref } from 'vue'

import { deleteUser } from '@/api/userApi'
import type { User } from '@/models/User'

import ConfirmDeleteModal from '@/components/ui/ConfirmDeleteModal.vue'

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

        emit('close')
        emit('deleted')
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