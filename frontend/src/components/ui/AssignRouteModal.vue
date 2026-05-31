<script setup lang="ts">
import { ref, watch } from 'vue'

import type { Driver } from '@/models/Driver'

const props = defineProps<{
    show: boolean
    drivers: Driver[]
    startDate: string
    endDate: string
}>()

const emit = defineEmits([
    'close',
    'submit'
])

const title = ref('')
const driverId = ref<number>()

watch(
    () => props.show,
    (show) => {
        if (show) {
            title.value = ''
            driverId.value = undefined
        }
    }
)

const close = () => emit('close')

const submit = () => {
    if (!title.value.trim()) {
        return
    }

    if (!driverId.value) {
        return
    }

    emit('submit', {
        title: title.value,
        driverId: driverId.value
    })
}
</script>

<template>
    <div v-if="show" class="fixed inset-0 z-50 flex items-center justify-center">
        <div class="absolute inset-0 bg-black/50" @click="close" />

        <div class="relative bg-white rounded-2xl shadow-2xl w-full max-w-lg p-6 z-10">
            <h2 class="text-xl font-bold mb-6">
                Attribuer l'itinéraire
            </h2>

            <div class="space-y-4">

                <div>
                    <label class="block text-sm font-medium mb-2">
                        Titre
                    </label>

                    <input v-model="title" type="text" class="w-full border rounded-lg p-3" />
                </div>

                <div>
                    <label class="block text-sm font-medium mb-2">
                        Chauffeur
                    </label>

                    <select v-model="driverId" class="w-full border rounded-lg p-3">
                        <option :value="undefined">
                            Sélectionner un chauffeur
                        </option>

                        <option v-for="driver in drivers" :key="driver.id" :value="driver.id">
                            {{ driver.firstName }} {{ driver.lastName }}
                        </option>
                    </select>
                </div>

                <div>
                    <label class="block text-sm font-medium mb-2">
                        Départ
                    </label>

                    <input :value="startDate" readonly class="w-full border rounded-lg p-3 bg-slate-100" />
                </div>

                <div>
                    <label class="block text-sm font-medium mb-2">
                        Arrivée estimée
                    </label>

                    <input :value="endDate" readonly class="w-full border rounded-lg p-3 bg-slate-100" />
                </div>

            </div>

            <div class="mt-6 flex justify-end gap-2">
                <button class="px-4 py-2 rounded-lg bg-slate-200" @click="close">
                    Annuler
                </button>

                <button class="px-4 py-2 rounded-lg bg-green-600 text-white" @click="submit">
                    Valider
                </button>
            </div>
        </div>
    </div>
</template>