<script setup lang="ts">
import { ref } from 'vue'

import HereMap from '@/components/maps/HereMap.vue'
import RouteSidebar from '@/components/maps/RouteSidebar.vue'

const mapRef = ref()

const routeResponse = ref<any>(null)
const selectedRouteIndex = ref<number>(0)

function handleRouteCalculated(response: any) {

    routeResponse.value = response
    selectedRouteIndex.value = 0

    mapRef.value?.displayRoutes(
        response.alternatives,
        response.alternatives[0]
    )
}

function handleSelectRoute(index: number) {

    selectedRouteIndex.value = index

    const selected =
        routeResponse.value.alternatives[index]

    mapRef.value?.displayRoutes(
        routeResponse.value.alternatives,
        selected
    )
}
</script>

<template>
    <div class="relative h-full w-full overflow-hidden">

        <!-- MAP -->
        <div class="absolute inset-0">
            <HereMap ref="mapRef" />
        </div>

        <!-- SIDEBAR -->
        <div class="absolute top-0 left-0 h-full z-10">
            <RouteSidebar :route-response="routeResponse" :selected-index="selectedRouteIndex"
                @route-selected="handleSelectRoute" @route-calculated="handleRouteCalculated" />
        </div>

    </div>
</template>