<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { ArrowUpDown } from 'lucide-vue-next'
import HereAutocompleteInput from './HereAutocompleteInput.vue'

import { calculateRoute } from '@/api/here/mapsApi'
import { getTractors } from "@/api/vehicle/tractorApi"
import { getSemiTrailers } from "@/api/vehicle/semiTrailerApi"

import type { Position } from '@/models/route/Position'
import type { TractorSummary } from "@/models/vehicle/Tractor"
import type { SemiTrailerSummary } from "@/models/vehicle/SemiTrailer"
import { formatVehicleLabel } from "@/utils/vehicleUtils"

//Variables
const departureMode = ref('NOW')
const tractors = ref<TractorSummary[]>([])
const semiTrailers = ref<SemiTrailerSummary[]>([])

const emit = defineEmits(['route-calculated'])

const form = reactive({
    origin: null as any,
    destination: null as any,

    departureTime: null as any,

    mode: 'FASTEST',
    maxTravelTimeMinutes: null,

    tractorId: null,
    semiTrailerId: null,
})

// Functions

function toPosition(place: {
    name: string
    address: string
    position: {
        lat: number
        lng: number
    }
}): Position {
    return {
        name: place.name,
        address: place.address,
        lat: place.position.lat,
        lng: place.position.lng
    }
}

function swapLocations() {
    const origin = form.origin
    form.origin = form.destination
    form.destination = origin
}

async function submit() {

    if (!form.origin || !form.destination || !form.tractorId) {
        return
    }

    try {
        const effectiveDepartureTime = toOffsetDateTime(form.departureTime) ?? new Date().toISOString()

        const payload = {
            origin: toPosition(form.origin),
            destination: toPosition(form.destination),

            departureTime: effectiveDepartureTime,

            mode: form.mode,
            maxTravelTimeMinutes: form.maxTravelTimeMinutes,

            tractorId: form.tractorId,
            semiTrailerId: form.semiTrailerId,
        }

        const response = await calculateRoute(payload)
        emit('route-calculated',
            {
                response,
                request: payload
            }
        )
    } catch (e) {
        console.error(e)
    }
}

function toOffsetDateTime(value: string) {
    if (departureMode.value === 'NOW' || !value) return null

    return new Date(value).toISOString()
}

onMounted(async () => {
    tractors.value = await getTractors()
    semiTrailers.value = await getSemiTrailers()
})

</script>

<template>
    <div class="space-y-5">

        <!-- LOCATIONS -->
        <div class="relative">
            <HereAutocompleteInput v-model="form.origin" label="Départ" />
            <div class="mt-5">
                <HereAutocompleteInput v-model="form.destination" label="Arrivée" />
            </div>

            <!-- SWAP -->
            <button type="button" @click="swapLocations"
                class="absolute right-0 top-[58%] z-10 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-full border border-slate-300 bg-white text-slate-600 shadow-sm transition hover:border-slate-400 hover:bg-slate-50 hover:text-slate-900"
                aria-label="Inverser le départ et l'arrivée" title="Inverser le départ et l'arrivée">
                <ArrowUpDown :size="16" :stroke-width="2" aria-hidden="true" />
            </button>
        </div>

        <!-- MODE -->
        <div class="space-y-3">

            <label class="block text-sm font-medium">
                Mode
            </label>

            <select v-model="form.mode" class="w-full rounded-xl border border-slate-300 p-3">
                <option value="FASTEST">
                    Plus rapide
                </option>

                <option value="CHEAPEST">
                    Plus économique
                </option>

            </select>

            <!-- MAX TIME -->
            <input v-if="form.mode === 'CHEAPEST'" v-model="form.maxTravelTimeMinutes" type="number"
                placeholder="(Optionnel) Temps de trajet max en minutes"
                class="w-full rounded-xl border border-slate-300 p-3" />

        </div>

        <!-- DEPARTURE TIME -->
        <div class="space-y-3">

            <label class="block text-sm font-medium">
                Départ
            </label>

            <select v-model="departureMode" class="w-full rounded-xl border border-slate-300 p-3">
                <option value="NOW">
                    Départ maintenant
                </option>

                <option value="PLANNED">
                    Départ prévu à
                </option>
            </select>

            <input v-if="departureMode === 'PLANNED'" v-model="form.departureTime" type="datetime-local"
                class="w-full rounded-xl border border-slate-300 p-3" />

        </div>

        <!-- VEHICULE -->
        <div class="space-y-3">

            <label class="block text-sm font-medium">
                Tracteur
            </label>

            <select v-model="form.tractorId" class="w-full rounded-xl border border-slate-300 p-3">
                <option :value="null">
                    Sélectionner un tracteur
                </option>

                <option v-for="tractor in tractors" :key="tractor.id" :value="tractor.id">
                    {{ tractor.registration }} - {{ formatVehicleLabel(tractor.brand, tractor.model) }}
                </option>
            </select>

        </div>

        <div class="space-y-3">

            <label class="block text-sm font-medium">
                Semi-remorque
            </label>

            <select v-model="form.semiTrailerId" class="w-full rounded-xl border border-slate-300 p-3">
                <option :value="null">
                    Sélectionner une semi-remorque
                </option>

                <option v-for="semiTrailer in semiTrailers" :key="semiTrailer.id" :value="semiTrailer.id">
                    {{ semiTrailer.registration }} - {{ formatVehicleLabel(semiTrailer.brand, semiTrailer.model) }}
                </option>
            </select>

        </div>

        <!-- BUTTON -->
        <button @click="submit" class="w-full bg-slate-900 text-white rounded-xl p-3 hover:bg-slate-800 transition">
            Calculer l'itinéraire
        </button>

    </div>
</template>