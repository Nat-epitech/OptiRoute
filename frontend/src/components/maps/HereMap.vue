<template>
  <div ref="mapContainer" class="map"></div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'

const mapContainer = ref<HTMLElement | null>(null)
let map: any

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

const setCenter = (position: { lat: number; lng: number }) => {
  if (!map) return

  map.setCenter(position)
  map.setZoom(14)
}

defineExpose({
  setCenter
})
</script>