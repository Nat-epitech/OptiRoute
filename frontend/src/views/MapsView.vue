<template>
    <div class="relative h-full w-full overflow-hidden">

        <!-- SEARCH OVERLAY -->
        <div class="absolute top-6 left-1/2 -translate-x-1/2 w-full max-w-2xl px-4 z-50">

            <div class="relative">

                <input v-model="query" @input="onSearch" placeholder="Rechercher une adresse..." class="w-full px-5 py-4 rounded-2xl
                 bg-white/90 backdrop-blur-md
                 shadow-lg border border-slate-100
                 focus:outline-none focus:ring-2 focus:ring-blue-500" />

                <ul v-if="results.length" class="absolute mt-3 w-full bg-white
                 rounded-xl shadow-xl max-h-72 overflow-auto
                 border border-slate-100">
                    <li v-for="item in results" :key="item.id" @click="selectPlace(item)"
                        class="px-4 py-3 hover:bg-slate-100 cursor-pointer transition">
                        {{ item.title }}
                    </li>
                </ul>

            </div>
        </div>

        <!-- MAP -->
        <div class="absolute inset-0">
            <HereMap class="h-full w-full" />
        </div>

    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import HereMap from '@/components/maps/HereMap.vue'

const query = ref('')
const results = ref<any[]>([])
const mapRef = ref<any>(null)

// HERE Search service
const platform = new H.service.Platform({
    apikey: import.meta.env.VITE_HERE_API_KEY
})

const searchService = platform.getSearchService()

let debounceTimer: number | undefined

const onSearch = () => {
    clearTimeout(debounceTimer)

    if (!query.value) {
        results.value = []
        return
    }

    debounceTimer = window.setTimeout(() => {
        searchService.autosuggest(
            {
                q: query.value,
                at: '45.7640,4.8357' // bias Lyon
            },
            (res: any) => {
                results.value = res.items || []
            },
            console.error
        )
    }, 300)
}

const selectPlace = (item: any) => {
    results.value = []
    query.value = item.title

    // Si HERE renvoie directement une position
    if (item.position) {
        mapRef.value?.setCenter(item.position)
        return
    }

    // fallback: geocode si pas de position
    searchService.geocode(
        { q: item.title },
        (res: any) => {
            const pos = res.items?.[0]?.position
            if (pos) {
                mapRef.value?.setCenter(pos)
            }
        },
        console.error
    )
}
</script>