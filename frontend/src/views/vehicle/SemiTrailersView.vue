<script setup lang="ts">
import { onMounted, ref } from "vue"

import { deleteTrailerType, getSemiTrailerTypes, getSemiTrailers } from "@/api/vehicle/semiTrailerApi"
import type { TrailerTypeOption } from "@/api/vehicle/semiTrailerApi"
import { getApiErrorMessage } from "@/api/utils"
import type { SemiTrailerSummary } from "@/models/vehicle/SemiTrailer"

import CreateSemiTrailerModal from "@/components/vehicles/CreateSemiTrailerModal.vue"
import DeleteSemiTrailerModal from "@/components/vehicles/DeleteSemiTrailerModal.vue"
import SemiTrailerDetailDrawer from "@/components/vehicles/SemiTrailerDetailDrawer.vue"
import CreateTrailerTypeModal from "@/components/vehicles/CreateTrailerTypeModal.vue"
import TrailerTypeDetailDrawer from "@/components/vehicles/TrailerTypeDetailDrawer.vue"

import AppDropdown from "@/components/ui/AppDropdown.vue"
import ConfirmDeleteModal from "@/components/ui/ConfirmDeleteModal.vue"
import { useNotification } from "@/composables/useNotification"

const notification = useNotification()

//Load semi-trailers

const semiTrailers = ref<SemiTrailerSummary[]>([])

const loadSemiTrailers = async () => {
    semiTrailers.value = await getSemiTrailers()
}

const trailerTypes = ref<TrailerTypeOption[]>([])

const loadTrailerTypes = async () => {
    trailerTypes.value = await getSemiTrailerTypes()
}

//Manage Modal

const showCreateModal = ref(false)
const showDeleteModal = ref(false)

const actionSemiTrailer = ref<SemiTrailerSummary | null>(null)
const selectedSemiTrailerId = ref<number | null>(null)
const actionTrailerType = ref<TrailerTypeOption | null>(null)
const selectedTrailerTypeId = ref<number | null>(null)
const showCreateTrailerTypeModal = ref(false)
const showDeleteTrailerTypeModal = ref(false)
const openActionMenuId = ref<string | null>(null)
const deletingTrailerType = ref(false)

const askDeleteSemiTrailer = (semiTrailer: SemiTrailerSummary) => {
    selectedSemiTrailerId.value = null
    selectedTrailerTypeId.value = null
    openActionMenuId.value = null
    actionSemiTrailer.value = semiTrailer
    showDeleteModal.value = true
}

const updateActionMenu = (id: string, open: boolean) => {
    if (open) {
        selectedSemiTrailerId.value = null
        selectedTrailerTypeId.value = null
    }

    openActionMenuId.value = open ? id : null
}

const openSemiTrailerDetails = (semiTrailer: SemiTrailerSummary) => {
    selectedTrailerTypeId.value = null
    openActionMenuId.value = null
    selectedSemiTrailerId.value = semiTrailer.id
}

const closeSemiTrailerDetails = () => {
    selectedSemiTrailerId.value = null
}

const closeSemiTrailerAction = () => {
    showDeleteModal.value = false
    actionSemiTrailer.value = null
}

const askDeleteTrailerType = (trailerType: TrailerTypeOption) => {
    openActionMenuId.value = null
    actionTrailerType.value = trailerType
    showDeleteTrailerTypeModal.value = true
}

const openTrailerTypeDetails = (trailerType: TrailerTypeOption) => {
    selectedSemiTrailerId.value = null
    openActionMenuId.value = null
    selectedTrailerTypeId.value = trailerType.id
}

const closeTrailerTypeDetails = () => {
    selectedTrailerTypeId.value = null
}

const closeTrailerTypeAction = () => {
    if (deletingTrailerType.value) {
        return
    }

    showDeleteTrailerTypeModal.value = false
    actionTrailerType.value = null
}

const askDeleteSelectedTrailerType = () => {
    const selectedTrailerType = trailerTypes.value.find(type => type.id === selectedTrailerTypeId.value)
    if (!selectedTrailerType) return

    selectedTrailerTypeId.value = null
    askDeleteTrailerType(selectedTrailerType)
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

const handleTrailerTypeDeleted = async () => {
    try {
        deletingTrailerType.value = true
        if (actionTrailerType.value) {
            await deleteTrailerType(actionTrailerType.value.id)
        }

        notification.success(
            "Type supprimé",
            `Le type « ${actionTrailerType.value?.label ?? ""} » a bien été supprimé.`,
        )
        showDeleteTrailerTypeModal.value = false
        actionTrailerType.value = null
        selectedTrailerTypeId.value = null
        await loadTrailerTypes()
    } catch (error: unknown) {
        notification.error(
            "Suppression impossible",
            getApiErrorMessage(error, "Le type de semi-remorque n’a pas pu être supprimé."),
        )
    } finally {
        deletingTrailerType.value = false
    }
}

onMounted(() => {
    loadSemiTrailers()
    loadTrailerTypes()
})
</script>

<template>
    <div>
        <div class="mb-6 flex items-center justify-between">
            <h1 class="text-3xl font-bold text-gray-800">
                Semi-remorques
            </h1>

            <button type="button"
                class="rounded-xl bg-blue-600 px-5 py-3 text-white shadow transition hover:bg-blue-700"
                @click="openActionMenuId = null; selectedSemiTrailerId = null; selectedTrailerTypeId = null; showCreateModal = true">
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
                            <AppDropdown :open="openActionMenuId === `semi-${semiTrailer.id}`"
                                @update:open="value => updateActionMenu(`semi-${semiTrailer.id}`, value)" v-slot="{ close }">
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

        <section class="mt-8">
            <div class="mb-4 flex items-center justify-between">
                <h2 class="text-2xl font-bold text-gray-800">
                    Types de semi-remorque
                </h2>

                <button type="button"
                    class="rounded-xl bg-blue-600 px-5 py-3 text-white shadow transition hover:bg-blue-700"
                    @click="openActionMenuId = null; selectedSemiTrailerId = null; selectedTrailerTypeId = null; showCreateTrailerTypeModal = true">
                    Créer un type de semi-remorque
                </button>
            </div>

            <div class="rounded-2xl bg-white shadow">
                <table class="w-full">
                    <thead class="border-b bg-gray-50">
                        <tr>
                            <th class="px-6 py-4 text-left">
                                Libellé
                            </th>

                            <th class="px-6 py-4 text-right">
                                Actions
                            </th>
                        </tr>
                    </thead>

                    <tbody>
                        <tr v-for="trailerType in trailerTypes" :key="trailerType.id"
                            class="cursor-pointer border-b transition last:border-b-0 hover:bg-gray-100" :class="{
                                'bg-blue-50': selectedTrailerTypeId === trailerType.id
                            }" tabindex="0" @click="openTrailerTypeDetails(trailerType)"
                            @keydown.enter="openTrailerTypeDetails(trailerType)"
                            @keydown.space.prevent="openTrailerTypeDetails(trailerType)">
                            <td class="px-6 py-4 font-medium text-gray-900">
                                {{ trailerType.label }}
                            </td>

                            <td class="px-6 py-4 text-right" @click.stop @keydown.stop>
                                <AppDropdown :open="openActionMenuId === `type-${trailerType.id}`"
                                    @update:open="value => updateActionMenu(`type-${trailerType.id}`, value)"
                                    v-slot="{ close }">
                                    <button type="button"
                                        class="flex w-full items-center px-4 py-2 text-sm text-red-600 hover:bg-red-50"
                                        @click="close(); askDeleteTrailerType(trailerType)">
                                        Supprimer
                                    </button>
                                </AppDropdown>
                            </td>
                        </tr>

                        <tr v-if="trailerTypes.length === 0">
                            <td colspan="2" class="px-6 py-12 text-center text-sm text-gray-500">
                                Aucun type de semi-remorque enregistré.
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </section>

        <SemiTrailerDetailDrawer :open="selectedSemiTrailerId !== null" :semiTrailerId="selectedSemiTrailerId"
            @close="closeSemiTrailerDetails" @updated="loadSemiTrailers" @deleted="handleSemiTrailerDeleted" />

        <CreateSemiTrailerModal :show="showCreateModal" @close="showCreateModal = false"
            @created="handleSemiTrailerCreated" />

        <DeleteSemiTrailerModal :show="showDeleteModal" :semiTrailer="actionSemiTrailer" @close="closeSemiTrailerAction"
            @deleted="handleSemiTrailerDeleted" />

        <TrailerTypeDetailDrawer :open="selectedTrailerTypeId !== null" :trailer-type-id="selectedTrailerTypeId"
            @close="closeTrailerTypeDetails" @delete="askDeleteSelectedTrailerType"
            @updated="loadTrailerTypes" />

        <CreateTrailerTypeModal :show="showCreateTrailerTypeModal" @close="showCreateTrailerTypeModal = false"
            @created="showCreateTrailerTypeModal = false; loadTrailerTypes()" />

        <ConfirmDeleteModal :show="showDeleteTrailerTypeModal" :loading="deletingTrailerType"
            title="Supprimer le type de semi-remorque"
            :message="`Voulez-vous vraiment supprimer le type ${actionTrailerType?.label ?? ''} ?`"
            @close="closeTrailerTypeAction" @confirm="handleTrailerTypeDeleted" />
    </div>
</template>