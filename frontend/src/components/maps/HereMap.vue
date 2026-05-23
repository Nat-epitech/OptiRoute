<template>
    <div ref="mapContainer" class="h-full w-full" />
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'

const mapContainer =
    ref<HTMLElement | null>(null)

let map: any

const routePolylines: any[] = []

const markers = new Map<string, any>()

function clearRoutes() {

    for (const route of routePolylines) {
        map.removeObject(route)
    }

    routePolylines.length = 0
}

function addMarker(
    position: any,
    id: string
) {

    const existing =
        markers.get(id)

    if (existing) {
        map.removeObject(existing)
    }

    const marker =
        new H.map.Marker(position)

    markers.set(id, marker)

    map.addObject(marker)
}

function displayRoutes(
    alternatives: any[],
    selectedRoute: any
) {

    if (!map) return

    clearRoutes()

    alternatives.forEach((route) => {

        const lineString =
            H.geo.LineString.fromFlexiblePolyline(
                route.polyline
            )

        const polyline =
            new H.map.Polyline(
                lineString,
                {
                    style: {
                        lineWidth:
                            route === selectedRoute
                                ? 7
                                : 4,

                        strokeColor:
                            route === selectedRoute
                                ? '#2563eb'
                                : '#94a3b8'
                    }
                }
            )

        routePolylines.push(polyline)

        map.addObject(polyline)
    })

    if (routePolylines.length > 0) {

        map.getViewModel()
            .setLookAtData({
                bounds:
                    routePolylines[0]
                        .getBoundingBox()
            })
    }
}

defineExpose({
    displayRoutes
})

onMounted(() => {

    const platform =
        new H.service.Platform({
            apikey:
                import.meta.env
                    .VITE_HERE_API_KEY
        })

    const defaultLayers =
        platform.createDefaultLayers()

    map = new H.Map(
        mapContainer.value!,
        defaultLayers.vector.normal.map,
        {
            center: {
                lat: 45.7640,
                lng: 4.8357
            },

            zoom: 8,

            pixelRatio:
                window.devicePixelRatio || 1
        }
    )

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