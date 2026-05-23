<script setup lang="ts">
import { ref } from 'vue'
import RouteForm from './RouteForm.vue'

const emit = defineEmits([
    'route-calculated'
])

const open = ref(true)

function handleRouteCalculated(
    response: any
) {
    emit('route-calculated', response)
}
</script>

<template>
    <div class="relative h-full">

        <!-- PANEL -->
        <div :class="[
            'h-full w-[420px] bg-white shadow-2xl border-r border-slate-200',
            'transition-all duration-300 overflow-hidden',
            open ? 'translate-x-0' : '-translate-x-full'
        ]">

            <div class="h-full flex flex-col">

                <!-- HEADER -->
                <div class="p-4 border-b border-slate-200">
                    <h1 class="text-2xl font-bold text-slate-800">
                        Rechercher un itinéraire
                    </h1>
                </div>

                <!-- CONTENT -->
                <div class="flex-1 overflow-y-auto p-4">
                    <RouteForm @route-calculated="handleRouteCalculated" />
                </div>

            </div>

        </div>

        <!-- TOGGLE -->
        <button @click="open = !open" class="absolute top-1/2 -translate-y-1/2 left-full
                   h-16 w-8 bg-white border border-l-0 border-slate-200
                   rounded-r-xl shadow-lg">
            {{ open ? '<' : '>' }} </button>

    </div>
</template>