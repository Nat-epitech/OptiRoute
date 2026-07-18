<script setup lang="ts">
import { ref } from 'vue'

import { deleteDriver } from '@/api/driverApi'
import { getApiErrorMessage } from '@/api/utils'

import type { Driver } from '@/models/Driver'

import ConfirmDeleteModal from '@/components/ui/ConfirmDeleteModal.vue'
import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const props = defineProps<{
    show: boolean
    driver: Driver | null
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
    if (!props.driver) return

    try {
        deleting.value = true

        await deleteDriver(props.driver.id)

        notification.success(
            'Conducteur supprimé',
            `Le conducteur « ${props.driver?.firstName ?? ''} ${props.driver?.lastName ?? ''} » a bien été supprimé.`
        )

        emit('close')
        emit('deleted')
    } catch (error) {
        console.error(error)
        notification.error(
            'Suppression impossible',
            getApiErrorMessage(
                error,
                'Le conducteur n’a pas pu être supprimé.'
            )
        )
    } finally {
        deleting.value = false
    }
}
</script>

<template>
    <ConfirmDeleteModal :show="show" :loading="deleting" title="Supprimer le conducteur"
        :message="`Voulez-vous vraiment supprimer ${driver?.firstName ?? ''} ${driver?.lastName ?? ''} ?`"
        @close="closeModal" @confirm="confirmDelete" />
</template>