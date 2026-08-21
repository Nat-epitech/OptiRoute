<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from "vue"

const props = withDefaults(
    defineProps<{
        open?: boolean
    }>(),
    {
        open: false,
    },
)

const emit = defineEmits<{
    "update:open": [value: boolean]
}>()

const dropdown = ref<HTMLElement | null>(null)

const toggle = () => {
    emit("update:open", !props.open)
}

const close = () => {
    emit("update:open", false)
}

const handleClickOutside = (event: MouseEvent) => {
    if (!dropdown.value?.contains(event.target as Node)) {
        close()
    }
}

onMounted(() => {
    document.addEventListener("click", handleClickOutside)
})

onBeforeUnmount(() => {
    document.removeEventListener("click", handleClickOutside)
})
</script>

<template>

    <div ref="dropdown" class="relative inline-block">

        <button type="button" class="rounded-lg p-2 text-gray-500 transition hover:bg-gray-100 hover:text-gray-800"
            @click.stop="toggle">
            ⋮
        </button>

        <div v-if="props.open"
            class="absolute right-0 z-50 mt-2 w-44 overflow-hidden rounded-xl border border-gray-200 bg-white shadow-lg">
            <slot :close="close" />
        </div>

    </div>

</template>