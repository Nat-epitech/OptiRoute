<script setup lang="ts">
import { reactive, ref, watch } from "vue"

import { updateSemiTrailer } from "@/api/vehicle/semiTrailerApi"

import { getApiErrorMessage } from "@/api/utils"

import type {
    SemiTrailerDetails, SemiTrailerFormData, UpdateSemiTrailerRequest,
} from "@/models/vehicle/SemiTrailer"

import { createEmptySemiTrailerForm } from "@/models/vehicle/SemiTrailer"

import AppModal from "@/components/ui/AppModal.vue"
import SemiTrailerForm from "@/components/vehicles/SemiTrailerForm.vue"

import { useNotification } from "@/composables/useNotification"

const notification = useNotification()

const props = defineProps<{
    show: boolean
    semiTrailer: SemiTrailerDetails | null
}>()

const emit = defineEmits<{
    close: []
    updated: []
}>()

const loading = ref(false)

const form = reactive<SemiTrailerFormData>(
    createEmptySemiTrailerForm(),
)

const resetForm = () => {
    Object.assign(
        form,
        createEmptySemiTrailerForm(),
    )
}

const populateForm = () => {
    if (!props.semiTrailer) {
        resetForm()
        return
    }

    Object.assign(form, {
        registration: props.semiTrailer.registration,

        brand: props.semiTrailer.brand ?? null,

        model: props.semiTrailer.model ?? null,

        maxSpeed: props.semiTrailer.maxSpeed ?? null,

        trailerType: props.semiTrailer.trailerType ?? null,

        emptyWeightKg: props.semiTrailer.emptyWeightKg ?? null,

        grossVehicleWeightKg:
            props.semiTrailer.grossVehicleWeightKg ?? null,

        heightCm: props.semiTrailer.heightCm ?? null,

        widthCm: props.semiTrailer.widthCm ?? null,

        lengthCm: props.semiTrailer.lengthCm ?? null,

        axleCount: props.semiTrailer.axleCount ?? null,

        purchaseCost: props.semiTrailer.purchaseCost ?? null,

        depreciationStartDate:
            props.semiTrailer.depreciationStartDate ?? null,

        depreciationEndDate:
            props.semiTrailer.depreciationEndDate ?? null,

        active: props.semiTrailer.active,
    } satisfies SemiTrailerFormData)
}

const closeModal = () => {
    if (loading.value) {
        return
    }

    emit("close")
}

const submitSemiTrailer = async () => {
    if (!props.semiTrailer) {
        return
    }

    try {
        loading.value = true

        const payload: UpdateSemiTrailerRequest = {
            registration: form.registration.trim().toUpperCase(),

            brand: form.brand?.trim() || null,

            model: form.model?.trim() || null,

            maxSpeed: form.maxSpeed,

            trailerType: form.trailerType,

            emptyWeightKg: form.emptyWeightKg,

            grossVehicleWeightKg: form.grossVehicleWeightKg,

            heightCm: form.heightCm,

            widthCm: form.widthCm,

            lengthCm: form.lengthCm,

            axleCount: form.axleCount,

            purchaseCost: form.purchaseCost,

            depreciationStartDate: form.depreciationStartDate || null,

            depreciationEndDate: form.depreciationEndDate || null,

            active: form.active,
        }

        await updateSemiTrailer(
            props.semiTrailer.id,
            payload,
        )

        notification.success(
            "Semi-remorque modifiée",
            `La semi-remorque « ${payload.registration} » a bien été modifiée.`,
        )

        emit("updated")
    } catch (error: unknown) {
        notification.error(
            "Modification impossible",
            getApiErrorMessage(
                error,
                "La semi-remorque n’a pas pu être modifiée.",
            ),
        )
    } finally {
        loading.value = false
    }
}

watch(
    [
        () => props.show,
        () => props.semiTrailer,
    ],
    ([show, semiTrailer]) => {
        if (show && semiTrailer) {
            populateForm()
            return
        }

        if (!show) {
            resetForm()
        }
    },
    {
        immediate: true,
    },
)
</script>

<template>
    <AppModal :show="show" @close="closeModal">
        <form @submit.prevent="submitSemiTrailer">
            <h2 class="mb-6 text-2xl font-bold text-slate-900">
                Modifier la semi-remorque
            </h2>

            <div class="max-h-[70vh] overflow-y-auto pr-2">
                <SemiTrailerForm v-model="form" :disabled="loading" />
            </div>

            <div class="mt-6 flex justify-end gap-3">
                <button type="button"
                    class="rounded-xl border border-slate-300 px-4 py-2 text-slate-700 transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-50"
                    :disabled="loading" @click="closeModal">
                    Annuler
                </button>

                <button type="submit"
                    class="rounded-xl bg-blue-600 px-4 py-2 text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-50"
                    :disabled="loading || !semiTrailer">
                    {{
                        loading
                            ? "Modification..."
                            : "Enregistrer"
                    }}
                </button>
            </div>
        </form>
    </AppModal>
</template>