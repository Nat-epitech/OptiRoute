<script setup lang="ts">
import { reactive, ref, watch } from "vue"

import { createTrailerType } from "@/api/vehicle/semiTrailerApi"
import { getApiErrorMessage } from "@/api/utils"
import AppModal from "@/components/ui/AppModal.vue"
import TrailerTypeForm, { type TrailerTypeFormData } from "@/components/vehicles/TrailerTypeForm.vue"
import { useNotification } from "@/composables/useNotification"

const props = defineProps<{ show: boolean }>()
const emit = defineEmits<{ close: []; created: [] }>()
const notification = useNotification()
const loading = ref(false)
const form = reactive<TrailerTypeFormData>({ label: "", description: null })

const resetForm = () => Object.assign(form, { label: "", description: null })
const closeModal = () => {
    if (!loading.value) emit("close")
}

const submit = async () => {
    try {
        loading.value = true
        await createTrailerType({ label: form.label.trim(), description: form.description?.trim() || null })
        notification.success("Type créé", `Le type « ${form.label} » a bien été ajouté.`)
        emit("created")
    } catch (error: unknown) {
        notification.error("Création impossible", getApiErrorMessage(error, "Le type n’a pas pu être créé."))
    } finally {
        loading.value = false
    }
}

watch(() => props.show, (show) => {
    if (show) resetForm()
})
</script>

<template>
    <AppModal :show="props.show" @close="closeModal">
        <form @submit.prevent="submit">
            <h2 class="mb-6 text-2xl font-bold text-slate-900">Créer un type de semi-remorque</h2>
            <TrailerTypeForm v-model="form" :disabled="loading" />
            <div class="mt-6 flex justify-end gap-3">
                <button type="button" :disabled="loading" class="rounded-xl border border-slate-300 px-4 py-2 text-slate-700 transition hover:bg-slate-50 disabled:opacity-50" @click="closeModal">
                    Annuler
                </button>
                <button type="submit" :disabled="loading" class="rounded-xl bg-blue-600 px-4 py-2 text-white transition hover:bg-blue-700 disabled:opacity-50">
                    {{ loading ? "Enregistrement..." : "Enregistrer" }}
                </button>
            </div>
        </form>
    </AppModal>
</template>
