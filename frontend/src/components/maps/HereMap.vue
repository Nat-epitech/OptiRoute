<template>
  <div ref="mapContainer" class="map"></div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'

const mapContainer = ref<HTMLElement | null>(null)

onMounted(() => {
  const platform = new H.service.Platform({
    apikey: import.meta.env.VITE_HERE_API_KEY
  })

  const defaultLayers = platform.createDefaultLayers()

  console.log(defaultLayers)

  const map = new H.Map(
    mapContainer.value!,
    defaultLayers.vector.normal.map,
    {
      center: { lat: 45.764, lng: 4.8357 },
      zoom: 10,
      pixelRatio: window.devicePixelRatio || 1
    }
  )

  new H.mapevents.Behavior(
    new H.mapevents.MapEvents(map)
  )

  H.ui.UI.createDefault(map, defaultLayers)

  window.addEventListener('resize', () => {
    map.getViewPort().resize()
  })
})
</script>

<style scoped>
.map {
  width: 100%;
  height: 100vh;
}
</style>