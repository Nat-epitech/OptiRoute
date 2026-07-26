<script setup lang="ts">
import { reactive, ref, watch } from "vue"

import { createTractor } from "@/api/vehicle/tractorApi"
import { getApiErrorMessage } from "@/api/utils"

import type { CreateTractorRequest } from "@/models/vehicle/Tractor"
import { createEmptyTractorForm, type TractorFormData } from "@/models/vehicle/Tractor"

import AppModal from "@/components/ui/AppModal.vue"
import TractorForm from "@/components/vehicles/TractorForm.vue"

import { useNotification } from "@/composables/useNotification"

const notification = useNotification()

const props = defineProps<{
    show: boolean
}>()

const emit = defineEmits<{
    close: []
    created: []
}>()

const loading = ref(false)

const form = reactive<TractorFormData>(
    createEmptyTractorForm(),
)

const resetForm = () => {
    Object.assign(form, createEmptyTractorForm())
}

const closeModal = () => {
    if (!loading.value) {
        emit("close")
    }
}

const submitTractor = async () => {
    try {
        loading.value = true

        const payload: CreateTractorRequest = {
            externalSource: "MANUAL",
            externalId: null,

            registration: form.registration.trim().toUpperCase(),
            brand: form.brand?.trim() || null,
            model: form.model?.trim() || null,

            maxSpeed: form.maxSpeed,

            fuelType: form.fuelType,
            averageConsumption: form.averageConsumption,

            emptyWeightKg: form.emptyWeightKg,
            grossCombinationWeightKg:
                form.grossCombinationWeightKg,

            heightCm: form.heightCm,
            widthCm: form.widthCm,
            lengthCm: form.lengthCm,

            axleCount: form.axleCount,

            purchaseCost: form.purchaseCost,
            depreciationStartDate:
                form.depreciationStartDate || null,
            depreciationEndDate:
                form.depreciationEndDate || null,

            active: form.active,
        }

        await createTractor(payload)

        notification.success(
            "Tracteur créé",
            `Le tracteur « ${payload.registration} » a bien été créé.`,
        )

        emit("created")
    } catch (error: unknown) {
        notification.error(
            "Création impossible",
            getApiErrorMessage(
                error,
                "Le tracteur n’a pas pu être créé.",
            ),
        )
    } finally {
        loading.value = false
    }
}

watch(
    () => props.show,
    (show) => {
        if (show) {
            resetForm()
        }
    },
)
</script>

<template>
    <AppModal :show="props.show" @close="closeModal">
        <form @submit.prevent="submitTractor">
            <h2 class="mb-6 text-2xl font-bold text-slate-900">
                Ajouter un tracteur
            </h2>

            <div class="max-h-[70vh] overflow-y-auto pr-2">
                <TractorForm v-model="form" :disabled="loading" />
            </div>

            <div class="mt-6 flex justify-end gap-3">
                <button type="button" :disabled="loading"
                    class="rounded-xl border border-slate-300 px-4 py-2 text-slate-700 transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-50"
                    @click="closeModal">
                    Annuler
                </button>

                <button type="submit" :disabled="loading"
                    class="rounded-xl bg-blue-600 px-4 py-2 text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-50">
                    {{ loading ? "Création..." : "Ajouter le tracteur" }}
                </button>
            </div>
        </form>
    </AppModal>
</template>