<script setup lang="ts">
import { reactive } from 'vue'
import { calculateRoute } from '@/api/routeApi'

const emit = defineEmits([
    'route-calculated'
])

const form = reactive({
    origin: {
        lat: 45.7640,
        lng: 4.8357
    },

    destination: {
        lat: 45.8992,
        lng: 6.1294
    },

    mode: 'CHEAPEST',

    departureTime: '2026-05-26T08:30:00+02:00',

    fuelPricePerLiter: 1.85,

    driverHourlyRate: 38,

    maxTravelTimeMinutes: 240,

    truck: {
        grossWeightKg: 32000,
        heightCm: 400,
        widthCm: 255,
        lengthCm: 1800,
        fuelConsumptionLitersPer100Km: 32
    }
})

const loading = false

async function submit() {

    try {

        const response =
            await calculateRoute(form)

        emit(
            'route-calculated',
            response
        )

    } catch (e) {

        console.error(e)
    }
}
</script>

<template>
    <div class="space-y-6">

        <div>

            <label class="block text-sm font-medium mb-2">
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

        </div>

        <button @click="submit" class="w-full bg-slate-900 text-white rounded-xl p-3
                   hover:bg-slate-800 transition">
            Calculer l'itinéraire
        </button>

    </div>
</template>