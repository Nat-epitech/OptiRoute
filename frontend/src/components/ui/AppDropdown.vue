<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue'

const isOpen = ref(false)

const dropdown = ref<HTMLElement | null>(null)

const toggle = () => {
    isOpen.value = !isOpen.value
}

const close = () => {
    isOpen.value = false
}

const handleClickOutside = (event: MouseEvent) => {
    if (!dropdown.value?.contains(event.target as Node)) {
        close()
    }
}

onMounted(() => {
    document.addEventListener('click', handleClickOutside)
})

onBeforeUnmount(() => {
    document.removeEventListener('click', handleClickOutside)
})
</script>

<template>

    <div ref="dropdown" class="relative inline-block">

        <button @click.stop="toggle"
            class="rounded-lg p-2 text-gray-500 hover:bg-gray-100 hover:text-gray-800 transition">
            ⋮
        </button>

        <div v-if="isOpen"
            class="absolute right-0 z-50 mt-2 w-44 overflow-hidden rounded-xl border border-gray-200 bg-white shadow-lg">
            <slot :close="close" />
        </div>

    </div>

</template>