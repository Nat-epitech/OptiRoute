<script setup lang="ts">
import { computed, ref } from 'vue'
import RouteForm from './RouteForm.vue'

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
                    <h1 class="text-xl font-bold">
                        Rechercher un itinéraire
                    </h1>
                </div>

                <!-- CONTENT -->
                <div class="flex-1 overflow-y-auto p-4 space-y-6">

                    <!-- FORM -->
                    <RouteForm @route-calculated="onRouteCalculated" />

                    <!-- ALTERNATIVES -->
                    <div v-if="routeResponse?.alternatives" class="space-y-2">

                        <h2 class="font-semibold">
                            Itinéraires trouvés:
                        </h2>

                        <div class="space-y-2">

                            <div v-for="(route, index) in routeResponse.alternatives" :key="index"
                                @click="selectRoute(Number(index))"
                                class="p-3 border rounded-xl cursor-pointer transition" :class="[
                                    index === selectedIndex
                                        ? 'border-blue-500 bg-blue-50'
                                        : 'hover:bg-slate-50'
                                ]">

                                <div class="flex justify-between text-sm">

                                    <span>
                                        {{ (route.distanceMeters / 1000).toFixed(0) }} km
                                    </span>

                                    <span>
                                        {{ route.costs.totalCost.toFixed(0) }} €
                                    </span>

                                </div>

                                <div class="text-xs text-slate-500">
                                    {{ (route.durationSeconds / 3600).toFixed(1) }} h
                                </div>

                            </div>

                        </div>
                    </div>

                    <!-- DÉTAIL COÛTS -->
                    <div v-if="selectedRoute" class="space-y-2">

                        <h2 class="font-semibold">
                            Détail coûts:
                        </h2>

                        <div class="text-sm space-y-1 text-slate-600">

                            <p>
                                Carburant : {{ selectedRoute.costs.fuelCost.toFixed(2) }} €
                            </p>

                            <p>
                                Péages : {{ selectedRoute.costs.tollCost.toFixed(2) }} €
                            </p>

                            <p>
                                Chauffeur : {{ selectedRoute.costs.driverCost.toFixed(2) }} €
                            </p>

                        </div>

                    </div>

                </div>

            </div>
        </div>

        <!-- TOGGLE -->
        <button @click="open = !open"
            class="absolute top-1/2 -translate-y-1/2 left-full h-16 w-8 bg-white border rounded-r-xl">
            {{ open ? '<' : '>' }} </button>

    </div>
</template>