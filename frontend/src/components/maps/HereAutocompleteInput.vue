<script setup lang="ts">
import { ref, watch } from 'vue'
import { autocompletePlaces, geocodePlace } from '@/api/placeApi'

//Variables

const props = defineProps({ label: String })

const emit = defineEmits(['selected'])

const query = ref('')

const loading = ref(false)

const results = ref<any[]>([])

//Functions

watch(query, async (value) => {

    if (!value || value.length < 3) {
        results.value = []
        return
    }

    loading.value = true
    try {
        results.value = await autocompletePlaces(value)
    } catch (e) {
        console.error(e)
    }

    loading.value = false
})

async function selectPlace(item: any) {
    try {
        const place = await geocodePlace(item.id)

        query.value = place.address.label

        results.value = []

        emit('selected', {
            label: place.address.label,

            position: {
                lat: place.position.lat,
                lng: place.position.lng
            }
        })
    } catch (e) {
        console.error(e)
    }
}

</script>

<template>
    <div class="relative">

        <label class="block text-sm font-medium mb-2">
            {{ label }}
        </label>

        <input v-model="query" type="text" class="w-full rounded-xl border border-slate-300 p-3" :placeholder="label" />

        <!-- RESULTS -->
        <div v-if="results.length > 0"
            class="absolute z-50 mt-2 w-full bg-white border border-slate-200 rounded-xl shadow-xl overflow-hidden">

            <button v-for="item in results" :key="item.id" @click="selectPlace(item)"
                class="w-full text-left px-4 py-3 hover:bg-slate-50 border-b border-slate-100">

                <div class="text-sm font-medium">
                    {{ item.title }}
                </div>

                <div class="text-xs text-slate-500">
                    {{ item.address.label }}
                </div>

            </button>

        </div>

    </div>
</template>