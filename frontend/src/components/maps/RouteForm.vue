<script setup lang="ts">
import { ref, reactive } from 'vue'
import HereAutocompleteInput from './HereAutocompleteInput.vue'
import { calculateRoute } from '@/api/routeApi'

//Variables
const departureMode = ref('NOW')

const emit = defineEmits([
    'route-calculated'
])

const form = reactive({
    origin: null as any,
    destination: null as any,

    departureTime: null as any,

    mode: 'FASTEST',
    driverHourlyRate: 0,
    maxTravelTimeMinutes: null,

    truck: {
        grossWeightKg: 32000,
        heightCm: 400,
        widthCm: 255,
        lengthCm: 1800,
        fuelConsumptionLitersPer100Km: 32
    }
})

// Functions

async function submit() {

    if (!form.origin || !form.destination) {
        return
    }

    try {
        const payload = {
            origin: form.origin.position,
            destination: form.destination.position,

            departureTime: toOffsetDateTime(form.departureTime),

            mode: form.mode,
            driverHourlyRate: form.driverHourlyRate,
            maxTravelTimeMinutes: form.maxTravelTimeMinutes,

            truck: form.truck
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

</script>

<template>
    <div class="space-y-5">

        <!-- ORIGIN -->
        <HereAutocompleteInput label="Départ" @selected="form.origin = $event" />

        <!-- DESTINATION -->
        <HereAutocompleteInput label="Arrivée" @selected="form.destination = $event" />

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

        <!-- DRIVER -->
        <div>

            <label class="block text-sm font-medium mb-2">
                Coût chauffeur €/h
            </label>

            <input v-model="form.driverHourlyRate" type="number"
                class="w-full rounded-xl border border-slate-300 p-3" />

        </div>

        <!-- BUTTON -->
        <button @click="submit" class="w-full bg-slate-900 text-white rounded-xl p-3 hover:bg-slate-800 transition">
            Calculer l'itinéraire
        </button>

    </div>
</template>