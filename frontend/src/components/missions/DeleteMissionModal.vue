<script setup lang="ts">
import { ref } from 'vue'

import { deleteMission } from '@/api/missionApi'
import { getApiErrorMessage } from '@/api/utils'
import type { MissionDetail } from '@/models/planning/missionDetail'

import ConfirmDeleteModal from '@/components/ui/ConfirmDeleteModal.vue'
import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const props = defineProps<{
    show: boolean
    mission: MissionDetail | null
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
    if (!props.mission) {
        return
    }

    try {
        deleting.value = true

        await deleteMission(props.mission.id)

        notification.success(
            'Mission supprimée',
            `La mission « ${props.mission.name} » a bien été supprimée.`
        )

        emit('close')
        emit('deleted')
    } catch (error) {
        notification.error(
            'Suppression impossible',
            getApiErrorMessage(
                error,
                'La mission n’a pas pu être supprimée.'
            )
        )
    } finally {
        deleting.value = false
    }
}
</script>

<template>
    <ConfirmDeleteModal :show="show" :loading="deleting" title="Supprimer la mission"
        :message="`Voulez-vous vraiment supprimer la mission « ${mission?.name ?? ''} » ? Cette action est irréversible.`"
        @close="closeModal" @confirm="confirmDelete" />
</template>