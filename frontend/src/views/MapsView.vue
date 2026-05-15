<template>
    <div class="relative h-full w-full overflow-hidden bg-slate-100">

        <!-- MAP -->
        <div class="absolute inset-0">
            <HereMap ref="mapRef" class="w-full h-full" />
        </div>

        <!-- SIDEBAR -->
        <aside :class="[
            'absolute top-0 left-0 z-40 h-full bg-white border-r border-slate-200 shadow-xl',
            'transition-all duration-300 overflow-hidden',
            open ? 'w-96' : 'w-0'
        ]">

            <div v-if="open" class="h-full flex flex-col p-6">

                <!-- HEADER -->
                <div class="mb-8">
                    <h1 class="text-2xl font-semibold text-slate-800">
                        Itinéraire
                    </h1>

                    <p class="text-sm text-slate-500 mt-1">
                        Planifiez votre trajet
                    </p>
                </div>

                <!-- FORM -->
                <div class="flex flex-col gap-5">

                    <!-- départ -->
                    <div class="relative">

                        <div class="flex items-center gap-2">

                            <input v-model="from.query" @input="onSearch(from)" placeholder="Départ" class="flex-1 px-5 py-4 rounded-2xl
                       bg-white/90 shadow-lg border
                       focus:outline-none focus:ring-2 focus:ring-blue-500" />

                            <!-- bouton + -->
                            <button @click="addWaypoint" class="w-8 h-8 rounded-full
                       bg-blue-500 text-white
                       flex items-center justify-center
                       hover:bg-blue-600 transition">
                                +
                            </button>

                        </div>

                        <!-- dropdown -->
                        <ul v-if="from.results.length" class="absolute z-50 mt-3 w-full bg-white
                   rounded-xl shadow-xl max-h-72 overflow-auto
                   border border-slate-100">
                            <li v-for="item in from.results" :key="item.id" @click="selectPlace(item, from)" class="px-4 py-3 hover:bg-slate-100
                       cursor-pointer transition">
                                {{ item.title }}
                            </li>
                        </ul>

                    </div>

                    <!-- vias + arrivée -->
                    <div v-for="(waypoint, index) in waypoints.slice(1)" :key="waypoint.id" class="relative">

                        <div class="flex items-center gap-2">

                            <!-- input -->
                            <input v-model="waypoint.query" @input="onSearch(waypoint)" :placeholder="waypoint.label"
                                class="flex-1 px-5 py-4 rounded-2xl
                       bg-white/90 shadow-lg border
                       focus:outline-none focus:ring-2 focus:ring-blue-500" />

                            <!-- delete uniquement vias -->
                            <button v-if="waypoint.type === 'via'" @click="removeWaypoint(index + 1)" class="w-8 h-8 rounded-full
                       bg-red-500 text-white
                       flex items-center justify-center
                       hover:bg-red-600 transition">
                                ✕
                            </button>

                        </div>

                        <!-- dropdown -->
                        <ul v-if="waypoint.results.length" class="absolute z-50 mt-3 w-full bg-white
                   rounded-xl shadow-xl max-h-72 overflow-auto
                   border border-slate-100">
                            <li v-for="item in waypoint.results" :key="item.id" @click="selectPlace(item, waypoint)"
                                class="px-4 py-3 hover:bg-slate-100
                       cursor-pointer transition">
                                {{ item.title }}
                            </li>
                        </ul>

                    </div>

                    <!-- CHECKBOX -->
                    <label class="flex items-center gap-3 text-sm text-slate-700">
                        <input type="checkbox" v-model="truck" class="h-4 w-4" />

                        Compatible poids lourd
                    </label>

                    <!-- BUTTON -->
                    <button @click="searchRoute" :disabled="loading" class="w-full py-3 rounded-xl
                   bg-blue-600 hover:bg-blue-500
                   disabled:bg-blue-300
                   text-white font-medium
                   transition flex items-center
                   justify-center gap-2">

                        <!-- LOADING -->
                        <svg v-if="loading" class="animate-spin h-5 w-5" xmlns="http://www.w3.org/2000/svg" fill="none"
                            viewBox="0 0 24 24">
                            <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4" />

                            <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z" />
                        </svg>

                        <span>
                            {{
                                loading
                                    ? 'Recherche...'
                                    : 'Rechercher un itinéraire'
                            }}
                        </span>

                    </button>

                </div>

            </div>
        </aside>

        <!-- COLLAPSE BUTTON -->
        <button @click="open = !open" :class="[
            'absolute top-1/2 -translate-y-1/2 z-50',
            'h-16 w-8 rounded-r-xl',
            'bg-white border border-slate-200 border-l-0',
            'shadow-lg hover:bg-slate-50',
            'flex items-center justify-center',
            'transition-all duration-300',
            open ? 'left-96' : 'left-0'
        ]">

            <ChevronLeft v-if="open" class="h-5 w-5 text-slate-600" />

            <ChevronRight v-else class="h-5 w-5 text-slate-600" />

        </button>

    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import HereMap from '@/components/maps/HereMap.vue'
import type { HereItem } from '@/models/Waypoint'
import type { Position } from '@/models/Waypoint'
import type { Waypoint } from '@/models/Waypoint'

import {
    ChevronLeft,
    ChevronRight
} from 'lucide-vue-next'

// Variables UI

const open = ref(true)
const loading = ref(false)
const mapRef = ref<InstanceType<typeof HereMap> | null>(null)
let debounceTimer: number | undefined

// Route variables

const from = computed(() => waypoints.value.find(w => w.type === 'from')!)
const truck = ref(false)
const waypoints = ref<Waypoint[]>([
    {
        id: crypto.randomUUID(),
        type: 'from',
        label: 'Départ',
        query: '',
        results: [] as HereItem[],
        position: null
    },
    {
        id: crypto.randomUUID(),
        type: 'to',
        label: 'Arrivée',
        query: '',
        results: [] as HereItem[],
        position: null
    }
])

// HERE Implementation
const platform = new H.service.Platform({
    apikey: import.meta.env.VITE_HERE_API_KEY
})
const searchService = platform.getSearchService()
const router = platform.getRoutingService()

// Functions

const removeWaypoint = (index: number) => {
    const wp = waypoints.value[index]

    // remove marker map
    if (wp?.id) {
        mapRef.value?.removeMarker?.(wp.id)
    }

    waypoints.value.splice(index, 1)
}

const addWaypoint = () => {
    const arrival = waypoints.value.pop()

    waypoints.value.push({
        id: crypto.randomUUID(),
        type: 'via',
        label: 'Via',
        query: '',
        results: [],
        position: null
    })

    if (arrival) {
        waypoints.value.push(arrival)
    }
}

const onSearch = (waypoint: any) => {
    clearTimeout(debounceTimer)

    if (!waypoint.query) {
        waypoint.results = []
        return
    }

    debounceTimer = window.setTimeout(() => {
        searchService.autosuggest(
            {
                q: waypoint.query,
                at: '45.7640,4.8357'
            },
            (res: any) => {
                waypoint.results = res.items || []
            },
            console.error
        )
    }, 250)
}

const selectPlace = (item: any, waypoint: any) => {
    const applyPosition = (pos: any) => {

        waypoint.query = item.title
        waypoint.results = []
        waypoint.position = pos

        mapRef.value?.setCenter(pos)
        mapRef.value?.setMarker(pos, waypoint.id)
    }

    if (item.position) {
        applyPosition(item.position)
        return
    }

    searchService.geocode(
        { q: item.title },
        (res: any) => {
            const pos = res.items?.[0]?.position

            if (pos) {
                applyPosition(pos)
            }
        },
        console.error
    )
}

const searchRoute = () => {
    loading.value = true

    const validPoints = waypoints.value.filter(
        (w): w is Waypoint & { position: Position } => w.position !== null
    )

    if (validPoints.length < 2) {
        loading.value = false
        return
    }

    const origin = validPoints[0]!
    const destination = validPoints[validPoints.length - 1]!

    const params: any = {
        transportMode: truck.value ? 'truck' : 'car',
        origin: `${origin.position.lat},${origin.position.lng}`,
        destination: `${destination.position.lat},${destination.position.lng}`,
        return: 'polyline,summary,travelSummary'
    }

    if (validPoints.length > 2) {
        params.via = validPoints.slice(1, -1).map(v =>
            `${v.position.lat},${v.position.lng}`
        )
    }

    if (truck.value) {
        params.truck = { grossWeight: 40000 }
    }

    router.calculateRoute(
        params,
        (result: any) => {
            const route = result.routes?.[0]
            if (!route) {
                loading.value = false
                return
            }

            const lineStrings = route.sections.map((s: any) =>
                H.geo.LineString.fromFlexiblePolyline(s.polyline)
            )

            mapRef.value?.drawRoute(lineStrings)

            loading.value = false
        },
        (err: any) => {
            console.error(err)
            loading.value = false
        }
    )
}
</script>