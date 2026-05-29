<script setup lang="ts">
import { ref } from 'vue'

import FullCalendar from '@fullcalendar/vue3'
import dayGridPlugin from '@fullcalendar/daygrid'
import timeGridPlugin from '@fullcalendar/timegrid'
import interactionPlugin from '@fullcalendar/interaction'

const selectedDriver = ref<number>()
const selectedDate = ref(new Date().toISOString().split('T')[0])

const calendarOptions = ref({
    plugins: [
        dayGridPlugin,
        timeGridPlugin,
        interactionPlugin
    ],

    initialView: 'timeGridWeek',

    locale: 'fr',

    slotMinTime: '05:00:00',
    slotMaxTime: '23:00:00',

    headerToolbar: {
        left: '',
        center: 'title',
        right: 'prev,next'
    },

    events: [
        {
            id: '1',
            title: 'Livraison Lyon',
            start: '2026-05-30T08:00:00',
            end: '2026-05-30T11:00:00'
        },
        {
            id: '2',
            title: 'Livraison Grenoble',
            start: '2026-05-30T13:00:00',
            end: '2026-05-30T16:00:00'
        }
    ]
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

                <!-- drivers -->
            </select>
        </div>

        <div class="bg-white rounded shadow p-4">
            <FullCalendar :options="calendarOptions" />
        </div>

    </div>
</template>