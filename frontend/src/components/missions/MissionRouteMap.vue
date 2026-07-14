<template>
    <div ref="mapContainer" class="h-full w-full" />
</template>

<script setup lang="ts">
import { nextTick, onBeforeUnmount, onMounted, ref, watch } from 'vue'

const props = defineProps<{
    polyline: string | null
}>()

const mapContainer = ref<HTMLElement | null>(null)

let map: any = null
let routePolyline: any = null
let resizeObserver: ResizeObserver | null = null

function clearRoute() {
    if (!map || !routePolyline) return

    map.removeObject(routePolyline)
    routePolyline = null
}

function displayRoute() {
    if (!map || !props.polyline) return

    clearRoute()

    const lineString = H.geo.LineString.fromFlexiblePolyline(
        props.polyline
    )

    routePolyline = new H.map.Polyline(lineString, {
        style: {
            strokeColor: '#059669',
            lineWidth: 5,
            lineCap: 'round',
            lineJoin: 'round'
        }
    })

    map.addObject(routePolyline)

    map.getViewModel().setLookAtData({
        bounds: routePolyline.getBoundingBox(),
        padding: {
            top: 30,
            right: 30,
            bottom: 30,
            left: 30
        }
    })
}

onMounted(async () => {
    const platform = new H.service.Platform({
        apikey: import.meta.env.VITE_HERE_API_KEY
    })

    const defaultLayers = platform.createDefaultLayers()

    map = new H.Map(
        mapContainer.value!,
        defaultLayers.vector.normal.map,
        {
            center: {
                lat: 46.6,
                lng: 2.5
            },
            zoom: 6,
            pixelRatio: window.devicePixelRatio || 1
        }
    )

    new H.mapevents.Behavior(
        new H.mapevents.MapEvents(map)
    )

    H.ui.UI.createDefault(map, defaultLayers)

    resizeObserver = new ResizeObserver(() => {
        map?.getViewPort().resize()
    })

    resizeObserver.observe(mapContainer.value!)

    await nextTick()

    map.getViewPort().resize()
    displayRoute()
})

watch(
    () => props.polyline,
    () => {
        displayRoute()
    }
)

onBeforeUnmount(() => {
    resizeObserver?.disconnect()
    map?.dispose()
})
</script>