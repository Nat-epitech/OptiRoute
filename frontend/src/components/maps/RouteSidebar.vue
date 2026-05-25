<script setup lang="ts">
import { computed, ref } from 'vue'
import RouteForm from './RouteForm.vue'
import { ChevronLeft, ChevronRight, Map, Route, Fuel, Receipt, User, Navigation, Milestone } from 'lucide-vue-next'

//Variables

const props = defineProps({
    routeResponse: Object,
    selectedIndex: Number
})

const emit = defineEmits([
    'route-calculated',
    'route-selected'
])

const open = ref(true)

//Functions

function onRouteCalculated(payload: any) {
    emit('route-calculated', payload)
}

function selectRoute(index: number) {
    emit('route-selected', index)
}

const selectedRoute = computed(() => {
    return props.routeResponse?.alternatives?.[props.selectedIndex ?? 0]
})

const formatDuration = (seconds: number) => {
    const totalMinutes = Math.round(seconds / 60)
    const hours = Math.floor(totalMinutes / 60)
    const minutes = totalMinutes % 60

    if (minutes === 0) {
        return `${hours} h`
    }

    return `${hours} h ${minutes}`
}

</script>

<template>
    <div class="relative h-full">

        <div :class="[
            'h-full w-[420px] bg-white shadow-2xl border-r',
            'transition-all duration-300 overflow-hidden',
            open ? 'translate-x-0' : '-translate-x-full'
        ]">

            <div class="h-full flex flex-col">

                <!-- HEADER -->
                <div class="p-4 border-b">
                    <h1 class="text-xl font-bold"> Rechercher un itinéraire </h1>
                </div>

                <!-- CONTENT -->
                <div class="flex-1 overflow-y-auto p-4 space-y-6">

                    <!-- FORM -->
                    <RouteForm @route-calculated="onRouteCalculated" />

                    <!-- ALTERNATIVES -->
                    <div v-if="routeResponse?.alternatives" class="space-y-3">

                        <h2 class="text-xl font-bold text-slate-800">
                            Itinéraires trouvés
                        </h2>

                        <div class="space-y-3">

                            <div v-for="(route, index) in routeResponse.alternatives" :key="index"
                                @click="selectRoute(Number(index))"
                                class="p-4 border rounded-2xl cursor-pointer transition-all" :class="[
                                    index === selectedIndex
                                        ? 'border-blue-500 bg-blue-50 shadow-sm'
                                        : 'border-slate-200 hover:bg-slate-50'
                                ]">

                                <div class="flex items-start justify-between gap-4">

                                    <!-- LEFT -->
                                    <div class="flex items-center gap-4 text-sm text-slate-700">

                                        <div class="flex items-center gap-1">
                                            <Fuel class="h-4 w-4 text-slate-500" />
                                            <span>{{ route.costs.fuelCost.toFixed(0) }} €</span>
                                        </div>

                                        <div class="flex items-center gap-1">
                                            <Receipt class="h-4 w-4 text-slate-500" />
                                            <span>{{ route.costs.tollCost.toFixed(0) }} €</span>
                                        </div>

                                        <div class="flex items-center gap-1">
                                            <User class="h-4 w-4 text-slate-500" />
                                            <span>{{ route.costs.driverCost.toFixed(0) }} €</span>
                                        </div>

                                    </div>

                                    <!-- RIGHT -->
                                    <div class="text-right">

                                        <div class="text-lg font-semibold text-slate-800">
                                            {{ formatDuration(route.durationSeconds) }}
                                        </div>

                                        <div class="text-sm text-slate-500 mt-1">
                                            {{ (route.distanceMeters / 1000).toFixed(0) }} km
                                        </div>

                                    </div>

                                </div>

                            </div>

                        </div>
                    </div>

                    <!-- DÉTAIL COÛTS -->
                    <!-- <div v-if="selectedRoute" class="space-y-2">

                        <h2 class="font-semibold"> Détail coûts: </h2>

                        <div class="text-sm space-y-1 text-slate-600">

                            <p>
                                <Fuel class="h-4 w-4 text-slate-500" /> Carburant : {{
                                    selectedRoute.costs.fuelCost.toFixed(2) }} €
                            </p>

                            <p>
                                <Receipt class="h-4 w-4 text-slate-500" /> Péages : {{
                                    selectedRoute.costs.tollCost.toFixed(2) }} €
                            </p>

                            <p>
                                <User class="h-4 w-4 text-slate-500" /> Chauffeur : {{
                                    selectedRoute.costs.driverCost.toFixed(2) }} €
                            </p>

                        </div>

                    </div> -->

                </div>

            </div>
        </div>

        <!-- TOGGLE -->
        <button @click="open = !open" :class="[
            'absolute top-1/2 -translate-y-1/2 z-50',
            'h-16 w-8 rounded-r-xl',
            'bg-white border border-slate-200 border-l-0',
            'shadow-lg hover:bg-slate-50',
            'flex items-center justify-center',
            'transition-all duration-300',
            open ? 'left-[420px]' : 'left-0'
        ]">

            <ChevronLeft v-if="open" class="h-5 w-5 text-slate-600" />
            <ChevronRight v-else class="h-5 w-5 text-slate-600" />

        </button>

    </div>
</template>