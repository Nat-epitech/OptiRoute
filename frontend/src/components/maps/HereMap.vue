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

    const selectedIndex = routes.findIndex(route => route === selectedRoute)

    const orderedRoutes = routes.map((route, index) => ({ route, index, isSelected: index === selectedIndex })).sort((a, b) => Number(a.isSelected) - Number(b.isSelected))

    orderedRoutes.forEach(({ route, index, isSelected }) => {
        const lineString = H.geo.LineString.fromFlexiblePolyline(route.polyline)

        const polyline = new H.map.Polyline(lineString,
            {
                style: {
                    lineWidth: isSelected ? 8 : 5,
                    strokeColor: isSelected
                        ? '#2563eb'
                        : '#38bdf8',
                    lineCap: 'round',
                    lineJoin: 'round'
                }
            }
        )

        polyline.routeIndex = index

        polyline.addEventListener('pointerenter', () => {
            if (mapContainer.value) {
                mapContainer.value.style.cursor = 'pointer'
            }
        })

        polyline.addEventListener('pointerleave', () => {
            if (mapContainer.value) {
                mapContainer.value.style.cursor = ''
            }
        })

        polyline.addEventListener('tap', () => { emitRouteSelected(index) })

        routePolylines.push(polyline)
        map.addObject(polyline)
    })

    const selectedPolyline = routePolylines[routePolylines.length - 1]

    if (selectedPolyline) {
        const bounds = selectedPolyline.getBoundingBox()

        const latitudePadding = (bounds.getTop() - bounds.getBottom()) * 0.15
        const longitudePadding = (bounds.getRight() - bounds.getLeft()) * 0.15

        const paddedBounds = new H.geo.Rect(
            bounds.getTop() + latitudePadding,
            bounds.getLeft() - longitudePadding,
            bounds.getBottom() - latitudePadding,
            bounds.getRight() + longitudePadding
        )

        map.getViewModel().setLookAtData(
            {
                bounds: paddedBounds
            }
        )
    }

}

defineExpose({
    displayRoutes,
    setMarkers
})

onMounted(() => {

    const platform = new H.service.Platform({ apikey: import.meta.env.VITE_HERE_API_KEY })

    const defaultLayers = platform.createDefaultLayers()

    map = new H.Map(mapContainer.value!, defaultLayers.vector.normal.map,
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
        const lat = Math.min(Math.max(center.lat, 41.0), 51.2)
        const lng = Math.min(Math.max(center.lng, -5.5), 9.8)
        map.setCenter({ lat, lng })

        const zoom = map.getZoom()

        if (zoom < 6) {
            map.setZoom(6)
        } else if (zoom > 15) {
            map.setZoom(15)
        }
    })

    new H.mapevents.Behavior(new H.mapevents.MapEvents(map))

    H.ui.UI.createDefault(map, defaultLayers)

    window.addEventListener('resize', () => map.getViewPort().resize())
})
</script>