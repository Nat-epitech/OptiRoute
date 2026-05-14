<template>
    <div ref="mapContainer" class="map"></div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'

// Variables

const mapContainer = ref<HTMLElement | null>(null)
let map: any
let routeLine: any
let fromMarker: any = null
let toMarker: any = null

// Functions

const setCenter = (position: { lat: number; lng: number }) => {
    if (!map) return

    map.setCenter(position)
    map.setZoom(14)
}

const drawRoute = (lineStrings: any[]) => {
    console.log("drawRoute appelé")
    if (!map) return

    if (routeLine) {
        map.removeObject(routeLine)
    }

    const multiLineString = new H.geo.MultiLineString(lineStrings)

    routeLine = new H.map.Polyline(multiLineString, {
        style: { strokeColor: 'blue', lineWidth: 5 }
    })

    map.addObject(routeLine)

    map.getViewModel().setLookAtData({
        bounds: routeLine.getBoundingBox()
    })
}

const setMarker = (
    position: { lat: number; lng: number },
    type: 'from' | 'to'
) => {

    if (!map) return

    const marker = new H.map.Marker(position)

    // Supprime ancien marker
    if (type === 'from' && fromMarker) {
        map.removeObject(fromMarker)
    }

    if (type === 'to' && toMarker) {
        map.removeObject(toMarker)
    }

    // Stocke nouveau marker
    if (type === 'from') {
        fromMarker = marker
    } else {
        toMarker = marker
    }

    map.addObject(marker)
}

defineExpose({
    setCenter,
    setMarker,
    drawRoute
})

// Initialization

onMounted(() => {
    const platform = new H.service.Platform({
        apikey: import.meta.env.VITE_HERE_API_KEY
    })

    const defaultLayers = platform.createDefaultLayers()

    map = new H.Map(
        mapContainer.value!,
        defaultLayers.vector.normal.map,
        {
            center: { lat: 45.7640, lng: 4.8357 }, // Lyon
            zoom: 10,
            pixelRatio: window.devicePixelRatio || 1
        }
    )

    const behavior = new H.mapevents.Behavior(
        new H.mapevents.MapEvents(map)
    )

    H.ui.UI.createDefault(map, defaultLayers)

    window.addEventListener('resize', () => {
        map.getViewPort().resize()
    })
})

</script>