<script setup lang="ts">
import { ref } from 'vue'

import HereMap from '@/components/maps/HereMap.vue'
import RouteSidebar from '@/components/maps/RouteSidebar.vue'

const mapRef = ref()

const routeResponse = ref()

function handleRouteCalculated(response: any) {

    routeResponse.value = response

    const selected = response.selectedRoute

    mapRef.value?.displayRoutes(response.alternatives, selected)
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
            <RouteSidebar @route-calculated="handleRouteCalculated" />
        </div>

    </div>
</template>