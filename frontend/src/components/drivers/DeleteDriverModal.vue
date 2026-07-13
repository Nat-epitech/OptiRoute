<script setup lang="ts">
import { ref } from 'vue'

import { deleteDriver } from '@/api/driverApi'
import type { Driver } from '@/models/Driver'

import ConfirmDeleteModal from '@/components/ui/ConfirmDeleteModal.vue'

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

        emit('close')
        emit('deleted')
    } catch (error) {
        console.error(error)
        alert('Erreur lors de la suppression du conducteur')
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