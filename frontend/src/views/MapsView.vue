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

                    <!-- FROM -->
                    <div class="space-y-4">

                        <!-- Départ -->
                        <div class="relative">
                            <input v-model="fromQuery" @input="onSearch('from')" placeholder="Adresse de départ" class="w-full px-5 py-4 rounded-2xl
                   bg-white/90 backdrop-blur-md
                   shadow-lg border border-slate-100
                   focus:outline-none focus:ring-2 focus:ring-blue-500" />

                            <ul v-if="fromResults.length" class="absolute z-50 mt-3 w-full bg-white
                   rounded-xl shadow-xl max-h-72 overflow-auto
                   border border-slate-100">
                                <li v-for="item in fromResults" :key="item.id" @click="selectPlace(item, 'from')"
                                    class="px-4 py-3 hover:bg-slate-100 cursor-pointer transition">
                                    {{ item.title }}
                                </li>
                            </ul>
                        </div>

                        <!-- Arrivée -->
                        <div class="relative">
                            <input v-model="toQuery" @input="onSearch('to')" placeholder="Adresse d'arrivée" class="w-full px-5 py-4 rounded-2xl
                   bg-white/90 backdrop-blur-md
                   shadow-lg border border-slate-100
                   focus:outline-none focus:ring-2 focus:ring-blue-500" />

                            <ul v-if="toResults.length" class="absolute z-50 mt-3 w-full bg-white
                   rounded-xl shadow-xl max-h-72 overflow-auto
                   border border-slate-100">
                                <li v-for="item in toResults" :key="item.id" @click="selectPlace(item, 'to')"
                                    class="px-4 py-3 hover:bg-slate-100 cursor-pointer transition">
                                    {{ item.title }}
                                </li>
                            </ul>
                        </div>

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
import { ref } from 'vue'
import HereMap from '@/components/maps/HereMap.vue'

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

const truck = ref(false)

const fromQuery = ref('')
const toQuery = ref('')

const fromResults = ref<any[]>([])
const toResults = ref<any[]>([])

const fromPosition = ref<any>(null)
const toPosition = ref<any>(null)

// HERE Implementation
const platform = new H.service.Platform({
    apikey: import.meta.env.VITE_HERE_API_KEY
})
const searchService = platform.getSearchService()
const router = platform.getRoutingService()

// Functions

const onSearch = (type: 'from' | 'to') => {
    clearTimeout(debounceTimer)

    const query = type === 'from'
        ? fromQuery.value
        : toQuery.value

    if (!query) {
        if (type === 'from') {
            fromResults.value = []
        } else {
            toResults.value = []
        }
        return
    }

    debounceTimer = window.setTimeout(() => {
        searchService.autosuggest(
            {
                q: query,
                at: '45.7640,4.8357'
            },
            (res: any) => {
                if (type === 'from') {
                    fromResults.value = res.items || []
                } else {
                    toResults.value = res.items || []
                }
            },
            console.error
        )
    }, 250)
}

const selectPlace = (item: any, type: 'from' | 'to') => {

    const applyPosition = (pos: any) => {
        if (type === 'from') {
            fromQuery.value = item.title
            fromResults.value = []
            fromPosition.value = pos
        } else {
            toQuery.value = item.title
            toResults.value = []
            toPosition.value = pos
        }
        mapRef.value?.setCenter(pos)
        mapRef.value?.setMarker(pos, type)
    }

    // HERE renvoie parfois directement la position
    if (item.position) {
        applyPosition(item.position)
        return
    }

    // fallback geocode
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
    if (!fromPosition.value || !toPosition.value) return

    loading.value = true

    router.calculateRoute(
        {
            transportMode: truck.value ? 'truck' : 'car',
            origin: `${fromPosition.value.lat},${fromPosition.value.lng}`,
            destination: `${toPosition.value.lat},${toPosition.value.lng}`,
            return: 'polyline,summary,travelSummary',

            ...(truck.value && {
                truck: {
                    grossWeight: 40000
                }
            })
        },

        (result: any) => {
            try {
                const route = result.routes?.[0]

                if (!route) {
                    return
                }

                const sections = route.sections

                const lineStrings = sections.map((s: any) =>
                    H.geo.LineString.fromFlexiblePolyline(s.polyline)
                )

                mapRef.value?.drawRoute(lineStrings)

            } finally {
                loading.value = false
            }
        },

        (error: any) => {
            console.error(error)
            loading.value = false
        }
    )
}
</script>