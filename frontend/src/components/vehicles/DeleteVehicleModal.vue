<script setup lang="ts">
import { ref } from 'vue'

import { deleteVehicle } from '@/api/vehicleApi'
import type { Vehicle } from '@/models/Vehicle'

import ConfirmDeleteModal from '@/components/ui/ConfirmDeleteModal.vue'

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

        emit('deleted')
        emit('close')
    } catch (error) {
        console.error(error)
        alert('Erreur lors de la suppression du véhicule')
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