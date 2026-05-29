<template>
    <div ref="mapContainer" class="h-full w-full" />
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'

//Variables

const mapContainer = ref<HTMLElement | null>(null)
let map: any

const routePolylines: any[] = []

let startMarker: any = null
let endMarker: any = null

const emit = defineEmits([
    'route-selected'
])

//Functions

function emitRouteSelected(index: number) {
    emit('route-selected', index)
}

function clearRoutes() {
    for (const route of routePolylines) {
        map.removeObject(route)
    }

    routePolylines.length = 0
}

function clearMarkers() {
    if (startMarker) {
        map.removeObject(startMarker)
    }

    if (endMarker) {
        map.removeObject(endMarker)
    }
}

function setMarkers(origin: any, destination: any) {
    clearMarkers()

    startMarker = new H.map.Marker(origin)
    endMarker = new H.map.Marker(destination)

    map.addObject(startMarker)
    map.addObject(endMarker)
}

function displayRoutes(routes: any[], selectedRoute: any) {
    if (!map) return
    clearRoutes()

    routes.forEach((route, index) => {
        const lineString = H.geo.LineString.fromFlexiblePolyline(route.polyline)
        const isSelected = route === selectedRoute
        const polyline = new H.map.Polyline(lineString,
            {
                style: {
                    lineWidth: isSelected ? 7 : 4,
                    strokeColor: isSelected ? '#2563eb' : '#94a3b8'
                }
            }
        )

        polyline.routeIndex = index
        polyline.addEventListener('tap',
            () => {

                emitRouteSelected(index)
            }
        )

        routePolylines.push(polyline)
        map.addObject(polyline)
    })

    if (routePolylines.length > 0) {
        map.getViewModel().setLookAtData({
            bounds:
                routePolylines[0]
                    .getBoundingBox()
        })
    }
}

defineExpose({
    displayRoutes,
    setMarkers
})

onMounted(() => {

    const platform = new H.service.Platform({
        apikey:
            import.meta.env.VITE_HERE_API_KEY
    })

    const defaultLayers = platform.createDefaultLayers()

    map = new H.Map(mapContainer.value!,
        defaultLayers.vector.normal.map,
        {
            center: { lat: 46.6, lng: 2.5 },
            zoom: 7,
            pixelRatio: window.devicePixelRatio || 1,
        }
    )

    map.addEventListener('mapviewchangeend', () => {
        const bounds = map.getViewModel().getLookAtData().bounds
        if (!bounds) return

        const center = map.getCenter()

        const lat = Math.min(
            Math.max(center.lat, 41.0),
            51.2
        )

        const lng = Math.min(
            Math.max(center.lng, -5.5),
            9.8
        )

        map.setCenter({ lat, lng })

        const zoom = map.getZoom()
        if (zoom < 6) {
            map.setZoom(6)
        } else if (zoom > 12) {
            map.setZoom(12)
        }
    })

    new H.mapevents.Behavior(
        new H.mapevents.MapEvents(map)
    )

    H.ui.UI.createDefault(
        map,
        defaultLayers
    )

    window.addEventListener(
        'resize',
        () => map.getViewPort().resize()
    )
})
</script>