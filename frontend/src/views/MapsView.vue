<script setup lang="ts">
import { ref } from 'vue'

import HereMap from '@/components/maps/HereMap.vue'
import RouteSidebar from '@/components/maps/RouteSidebar.vue'

//Variables

const mapRef = ref()

const lastRequest = ref<any>(null)
const routeResponse = ref<any>(null)

const selectedRouteIndex = ref<number>(0)

//Functions

function handleRouteCalculated(payload: { response: any, request: any }) {

    routeResponse.value = payload.response
    lastRequest.value = payload.request
    selectedRouteIndex.value = 0

    mapRef.value?.displayRoutes(
        payload.response.alternatives,
        payload.response.alternatives[0]
    )

    mapRef.value?.setMarkers(
        payload.request.origin,
        payload.request.destination
    )
}

function handleSelectRoute(index: number) {
    selectedRouteIndex.value = index

    const selected = routeResponse.value.alternatives[index]

    mapRef.value?.displayRoutes(routeResponse.value.alternatives, selected)
}

</script>

<template>
    <div class="relative h-full w-full overflow-hidden">

        <!-- MAP -->
        <div class="absolute inset-0">
            <HereMap ref="mapRef" @route-selected="handleSelectRoute" />s
        </div>

        <!-- SIDEBAR -->
        <div class="absolute top-0 left-0 h-full z-10">
            <RouteSidebar :route-response="routeResponse" :selected-index="selectedRouteIndex"
                @route-selected="handleSelectRoute" @route-calculated="handleRouteCalculated" />
        </div>

    </div>
</template>