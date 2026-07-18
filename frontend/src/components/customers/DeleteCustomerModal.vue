<script setup lang="ts">
import { ref } from 'vue'

import { deleteCustomer } from '@/api/customerApi'
import { getApiErrorMessage } from '@/api/utils'

import type { Customer } from '@/models/Customer'

import ConfirmDeleteModal from '@/components/ui/ConfirmDeleteModal.vue'
import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const props = defineProps<{
    show: boolean
    customer: Customer | null
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
    if (!props.customer) return

    try {
        deleting.value = true

        await deleteCustomer(props.customer.id)

        notification.success(
            'Client supprimé',
            `Le client « ${props.customer?.name ?? ''} » a bien été supprimé.`
        )

        emit('deleted')
        emit('close')
    } catch (error) {
        notification.error(
            'Suppression impossible',
            getApiErrorMessage(
                error,
                'Le client n’a pas pu être supprimé.'
            )
        )
    } finally {
        deleting.value = false
    }
}
</script>

<template>
    <ConfirmDeleteModal :show="show" :loading="deleting" title="Supprimer le client"
        :message="`Voulez-vous vraiment supprimer le client ${customer?.name ?? ''} ?`" @close="closeModal"
        @confirm="confirmDelete" />
</template>