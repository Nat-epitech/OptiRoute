<script setup lang="ts">
import { ref } from "vue"

import { deleteCostParameter } from "@/api/cost/costParameterApi"
import { getApiErrorMessage } from "@/api/utils"

import type { CostParameter } from "@/models/cost/CostParameter"

import ConfirmDeleteModal from "@/components/ui/ConfirmDeleteModal.vue"
import { useNotification } from "@/composables/useNotification"

const notification = useNotification()

const props = defineProps<{
    show: boolean
    costParameter: CostParameter | null
}>()

const emit = defineEmits<{
    close: []
    deleted: []
}>()

const deleting = ref(false)

const closeModal = () => {
    if (!deleting.value) {
        emit("close")
    }
}

const confirmDelete = async () => {
    if (!props.costParameter) return

    try {
        deleting.value = true

        await deleteCostParameter(props.costParameter.id)

        notification.success(
            "Coût supprimé",
            `Le coût « ${props.costParameter.label} » a bien été supprimé.`,
        )

        emit("deleted")
        emit("close")
    } catch (error: unknown) {
        notification.error(
            "Suppression impossible",
            getApiErrorMessage(
                error,
                "Le coût n’a pas pu être supprimé.",
            ),
        )
    } finally {
        deleting.value = false
    }
}
</script>

<template>
    <ConfirmDeleteModal :show="props.show" :loading="deleting" title="Supprimer le coût"
        :message="`Voulez-vous vraiment supprimer le coût ${props.costParameter?.label ?? ''} ?`" @close="closeModal"
        @confirm="confirmDelete" />
</template>