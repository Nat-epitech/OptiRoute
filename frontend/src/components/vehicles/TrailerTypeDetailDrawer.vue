<script setup lang="ts">
import { ref, watch } from "vue"
import { Edit3, Info, Trash2 } from "lucide-vue-next"

import { getTrailerType } from "@/api/vehicle/semiTrailerApi"
import type { TrailerTypeOption } from "@/api/vehicle/semiTrailerApi"
import { getApiErrorMessage } from "@/api/utils"
import AppDetailDrawer from "@/components/ui/AppDetailDrawer.vue"
import DetailSection from "@/components/ui/DetailSection.vue"
import DetailRow from "@/components/ui/DetailRow.vue"
import UpdateTrailerTypeModal from "@/components/vehicles/UpdateTrailerTypeModal.vue"

const props = defineProps<{ open: boolean; trailerTypeId: number | null }>()
const emit = defineEmits<{ close: []; updated: []; delete: [] }>()
const trailerType = ref<TrailerTypeOption | null>(null)
const loading = ref(false)
const error = ref<string | null>(null)
const showUpdateModal = ref(false)
let currentRequestId = 0

const loadTrailerType = async () => {
    if (props.trailerTypeId === null) return
    const requestId = ++currentRequestId
    try {
        loading.value = true
        error.value = null
        trailerType.value = null
        const response = await getTrailerType(props.trailerTypeId)
        if (requestId === currentRequestId) trailerType.value = response
    } catch (err: unknown) {
        if (requestId === currentRequestId) error.value = getApiErrorMessage(err, "Impossible de charger le type de semi-remorque.")
    } finally {
        if (requestId === currentRequestId) loading.value = false
    }
}

const handleUpdated = async () => {
    showUpdateModal.value = false
    await loadTrailerType()
    emit("updated")
}

watch([() => props.open, () => props.trailerTypeId], ([open, id]) => {
    if (open && id !== null) {
        loadTrailerType()
        return
    }
    currentRequestId++
    trailerType.value = null
    error.value = null
    showUpdateModal.value = false
}, { immediate: true })
</script>

<template>
    <AppDetailDrawer :open="props.open" :title="trailerType?.label ?? 'Détail du type de semi-remorque'" @close="emit('close')">
        <div v-if="loading" class="flex min-h-full items-center justify-center p-6">
            <p class="text-sm text-slate-500">Chargement des informations...</p>
        </div>
        <div v-else-if="error" class="p-6 text-sm text-red-700">{{ error }}</div>
        <div v-else-if="trailerType" class="space-y-6 p-6">
            <DetailSection title="Informations" :icon="Info">
                <DetailRow label="Libellé" :value="trailerType.label" />
                <DetailRow label="Description" :value="trailerType.description ?? 'Non renseignée'" />
            </DetailSection>
        </div>

        <template v-if="trailerType" #footer>
            <div class="flex items-center justify-between gap-3">
                <button type="button" class="inline-flex items-center gap-2 rounded-xl border border-red-200 px-4 py-2 text-sm font-medium text-red-600 transition hover:bg-red-50" @click="emit('delete')">
                    <Trash2 class="h-4 w-4" />
                    Supprimer
                </button>
                <button type="button" class="inline-flex items-center gap-2 rounded-xl bg-blue-600 px-4 py-2 text-sm font-medium text-white transition hover:bg-blue-700" @click="showUpdateModal = true">
                    <Edit3 class="h-4 w-4" />
                    Modifier
                </button>
            </div>
        </template>
    </AppDetailDrawer>

    <UpdateTrailerTypeModal :show="showUpdateModal" :trailer-type="trailerType" @close="showUpdateModal = false" @updated="handleUpdated" />
</template>
