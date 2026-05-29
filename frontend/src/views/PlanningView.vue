<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'

import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'

import { getDrivers } from '@/api/driverApi'
import { getPlanningByDriver } from '@/api/planningApi'

import type { Driver } from '@/models/Driver'

const selectedDriver = ref<number | undefined>()
const drivers = ref<Driver[]>([])

const events = ref<any[]>([])
const loading = ref(false)

const calendarOptions = ref({
    plugins: [
        dayGridPlugin,
        timeGridPlugin,
        interactionPlugin
    ],

    initialView: 'timeGridWeek',
    locale: 'fr',

    slotMinTime: '00:00:00',
    slotMaxTime: '23:59:59',

    headerToolbar: {
        left: '',
        center: 'title',
        right: 'prev,next'
    },

    events: events
})

const loadDrivers = async () => {
    drivers.value = await getDrivers()
}

const loadPlanning = async () => {
    if (!selectedDriver.value) {
        events.value = []
        return
    }

    loading.value = true

    try {
        const data = await getPlanningByDriver(selectedDriver.value)

        events.value = data.map((e: any) => ({
            id: e.id,
            title: e.title,
            start: e.start,
            end: e.end
        }))

    } finally {
        loading.value = false
    }
}

watch(selectedDriver, async () => {
    await loadPlanning()
})

onMounted(async () => {
    await loadDrivers()
})

</script>

<template>
    <div>

        <h1 class="text-3xl font-bold mb-6">
            Planning
        </h1>

        <div class="flex gap-4 mb-6">

            <select v-model="selectedDriver" class="border rounded px-3 py-2">
                <option :value="undefined">
                    Sélectionner un chauffeur
                </option>

                <option v-for="driver in drivers" :key="driver.id" :value="driver.id">
                    {{ driver.firstName }} {{ driver.lastName }}
                </option>
            </select>

        </div>

        <div class="bg-white rounded shadow p-4">

            <div v-if="loading" class="mb-2 text-gray-500">
                Chargement du planning...
            </div>

            <FullCalendar :options="calendarOptions" />

        </div>

    </div>
</template>