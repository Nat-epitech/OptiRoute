<script setup lang="ts">
import { ref } from 'vue'

import { deleteVehicle } from '@/api/vehicleApi'
import { getApiErrorMessage } from '@/api/utils'

import type { Vehicle } from '@/models/Vehicle'

import ConfirmDeleteModal from '@/components/ui/ConfirmDeleteModal.vue'
import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const props = defineProps<{
    show: boolean
    vehicle: Vehicle | null
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
    if (!props.vehicle) return

    try {
        deleting.value = true

        await deleteVehicle(props.vehicle.id)

        notification.success(
            'Véhicule supprimé',
            `Le véhicule « ${props.vehicle?.registration ?? ''} » a bien été supprimé.`
        )

        emit('deleted')
        emit('close')
    } catch (error) {
        notification.error(
            'Suppression impossible',
            getApiErrorMessage(
                error,
                'Le véhicule n’a pas pu être supprimé.'
            )
        )
    } finally {
        deleting.value = false
    }
}
</script>

<template>
    <ConfirmDeleteModal :show="show" :loading="deleting" title="Supprimer le véhicule"
        :message="`Voulez-vous vraiment supprimer le véhicule ${vehicle?.registration ?? ''} ?`" @close="closeModal"
        @confirm="confirmDelete" />
</template>