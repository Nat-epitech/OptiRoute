<script setup lang="ts">
import { onMounted, ref } from "vue"

import { getSemiTrailers } from "@/api/vehicle/semiTrailerApi"
import type { SemiTrailerSummary } from "@/models/vehicle/SemiTrailer"

import CreateSemiTrailerModal from "@/components/vehicles/CreateSemiTrailerModal.vue"
import DeleteSemiTrailerModal from "@/components/vehicles/DeleteSemiTrailerModal.vue"
import SemiTrailerDetailDrawer from "@/components/vehicles/SemiTrailerDetailDrawer.vue"

import AppDropdown from "@/components/ui/AppDropdown.vue"

//Load semi-trailers

const semiTrailers = ref<SemiTrailerSummary[]>([])

const loadSemiTrailers = async () => {
    semiTrailers.value = await getSemiTrailers()
}

//Manage Modal

const showCreateModal = ref(false)
const showDeleteModal = ref(false)

const actionSemiTrailer = ref<SemiTrailerSummary | null>(null)
const selectedSemiTrailerId = ref<number | null>(null)

const askDeleteSemiTrailer = (semiTrailer: SemiTrailerSummary) => {
    actionSemiTrailer.value = semiTrailer
    showDeleteModal.value = true
}

const openSemiTrailerDetails = (semiTrailer: SemiTrailerSummary) => {
    selectedSemiTrailerId.value = semiTrailer.id
}

const closeSemiTrailerDetails = () => {
    selectedSemiTrailerId.value = null
}

const closeSemiTrailerAction = () => {
    showDeleteModal.value = false
    actionSemiTrailer.value = null
}

//Handle

const handleSemiTrailerCreated = async () => {
    showCreateModal.value = false
    await loadSemiTrailers()
}

const handleSemiTrailerDeleted = async () => {
    closeSemiTrailerAction()
    selectedSemiTrailerId.value = null
    await loadSemiTrailers()
}

onMounted(loadSemiTrailers)
</script>

<template>
    <div>
        <div class="mb-6 flex items-center justify-between">
            <h1 class="text-3xl font-bold text-gray-800">
                Semi-remorques
            </h1>

            <button type="button"
                class="rounded-xl bg-blue-600 px-5 py-3 text-white shadow transition hover:bg-blue-700"
                @click="showCreateModal = true">
                Ajouter un semi-remorque
            </button>
        </div>

        <div class="rounded-2xl bg-white shadow">
            <table class="w-full">
                <thead class="border-b bg-gray-50">
                    <tr>
                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Immatriculation
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Marque
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-left">
                            Modèle
                        </th>

                        <th class="whitespace-nowrap px-6 py-4 text-right">
                            Actions
                        </th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="semiTrailer in semiTrailers" :key="semiTrailer.id"
                        class="cursor-pointer border-b transition last:border-b-0 hover:bg-gray-100" :class="{
                            'bg-blue-50': selectedSemiTrailerId === semiTrailer.id
                        }" tabindex="0" @click="openSemiTrailerDetails(semiTrailer)"
                        @keydown.enter="openSemiTrailerDetails(semiTrailer)"
                        @keydown.space.prevent="openSemiTrailerDetails(semiTrailer)">

                        <td class="whitespace-nowrap px-6 py-4 font-medium text-gray-900">
                            {{ semiTrailer.registration }}
                        </td>

                        <td class="whitespace-nowrap px-6 py-4">
                            {{ semiTrailer.brand }}
                        </td>

                        <td class="whitespace-nowrap px-6 py-4">
                            {{ semiTrailer.model }}
                        </td>

                        <!-- Le stop empêche le clic sur les actions d'ouvrir également le drawer. -->
                        <td class="whitespace-nowrap px-6 py-4 text-right" @click.stop @keydown.stop>
                            <AppDropdown v-slot="{ close }">
                                <button type="button"
                                    class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                    @click="
                                        close(); askDeleteSemiTrailer(semiTrailer)
                                        ">
                                    Supprimer
                                </button>
                            </AppDropdown>
                        </td>
                    </tr>

                    <tr v-if="semiTrailers.length === 0">
                        <td colspan="7" class="px-6 py-12 text-center text-sm text-gray-500">
                            Aucun semi-remorque enregistré.
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>

        <!-- todo -->
        <SemiTrailerDetailDrawer :open="selectedSemiTrailerId !== null" :semiTrailerId="selectedSemiTrailerId"
            @close="closeSemiTrailerDetails" @updated="loadSemiTrailers" @deleted="handleSemiTrailerDeleted" />

        <CreateSemiTrailerModal :show="showCreateModal" @close="showCreateModal = false"
            @created="handleSemiTrailerCreated" />

        <DeleteSemiTrailerModal :show="showDeleteModal" :semiTrailer="actionSemiTrailer" @close="closeSemiTrailerAction"
            @deleted="handleSemiTrailerDeleted" />
    </div>
</template>