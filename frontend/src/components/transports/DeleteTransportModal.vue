<script setup lang="ts">
import { ref } from 'vue'

import { deleteTransport } from '@/api/transportApi'
import { getApiErrorMessage } from '@/api/utils'
import type { TransportDetail } from '@/models/planning/transportDetail'

import ConfirmDeleteModal from '@/components/ui/ConfirmDeleteModal.vue'
import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const props = defineProps<{
    show: boolean
    transport: TransportDetail | null
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
    if (!props.transport) {
        return
    }

    try {
        deleting.value = true

        await deleteTransport(props.transport.id)

        notification.success(
            'Transport supprimé',
            `Le transport « ${props.transport.name} » a bien été supprimé.`
        )

        emit('close')
        emit('deleted')
    } catch (error) {
        notification.error(
            'Suppression impossible',
            getApiErrorMessage(
                error,
                'Le transport n’a pas pu être supprimé.'
            )
        )
    } finally {
        deleting.value = false
    }
}
</script>

<template>
    <ConfirmDeleteModal :show="show" :loading="deleting" title="Supprimer le transport "
        :message="`Voulez-vous vraiment supprimer le transport « ${transport?.name ?? ''} » ? Cette action est irréversible.`"
        @close="closeModal" @confirm="confirmDelete" />
</template>