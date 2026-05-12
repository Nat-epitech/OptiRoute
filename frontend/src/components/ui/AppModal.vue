<script setup lang="ts">
defineProps<{
    show: boolean
}>()

const emit = defineEmits(['close'])

</script>

<template>

  <div v-if="show" class="fixed inset-0 z-50 flex items-center justify-center">

    <div class="absolute inset-0 bg-black/50" @click="emit('close')"/>

    <div class="relative bg-white rounded-2xl shadow-2xl w-full max-w-lg p-6">
      <slot />
    </div>

  </div>

</template>