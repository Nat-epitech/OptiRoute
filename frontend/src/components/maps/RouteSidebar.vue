<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import RouteForm from './RouteForm.vue'

import {
    ChevronLeft,
    ChevronRight,
    Fuel,
    Receipt
} from 'lucide-vue-next'

import AssignRouteModal from '@/components/maps/AssignRouteModal.vue'

import type { CreateTransportRequest, CreateTransportFromRouteRequest, AssignTransport } from '@/models/transport/TransportRequest.ts'
import type { RouteRequest, RouteResponse } from '@/models/route/Route'
import type { Driver } from '@/models/Driver'
import type { Customer } from '@/models/Customer'

import { createTransportFromRoute } from '@/api/transportApi.ts'
import { getDrivers } from '@/api/driverApi'
import { getCustomers } from '@/api/customerApi'
import { getApiErrorMessage } from '@/api/utils'

import { formatDuration } from "@/utils/formatters"

import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const drivers = ref<Driver[]>([])
const customers = ref<Customer[]>([])

const loadAssignmentData = async () => {
    const [
        loadedDrivers,
        loadedCustomers
    ] = await Promise.all([
        getDrivers(),
        getCustomers()
    ])

    drivers.value = loadedDrivers
    customers.value = loadedCustomers
}

const props = defineProps<{
    routeResponse?: RouteResponse
    selectedIndex?: number
}>()

const emit = defineEmits([
    'route-calculated',
    'route-selected'
])

const open = ref(true)
const showAssignModal = ref(false)

const routeRequest = ref<RouteRequest>()

const onRouteCalculated = (data: { response: RouteResponse, request: RouteRequest }) => {
    routeRequest.value = data.request

    emit('route-calculated', data)
}

const assignStartDate = computed(() => {
    return routeRequest.value?.departureTime ?? ''
})

const assignEndDate = computed(() => {
    if (!routeRequest.value?.departureTime || props.selectedIndex === undefined || !props.routeResponse) {
        return ''
    }

    const start = new Date(routeRequest.value.departureTime)
    const route = props.routeResponse.routes[props.selectedIndex]

    if (route != undefined && route.durationSeconds !== undefined) {
        start.setSeconds(start.getSeconds() + route.durationSeconds)
    }

    return start.toISOString()
})

function selectRoute(index: number) {
    emit('route-selected', index)
}

const handleAssignRoute = async (data: AssignTransport) => {
    const requestValue = routeRequest.value

    if (!requestValue || props.selectedIndex === undefined || !props.routeResponse) {
        return
    }

    const selectedRoute = props.routeResponse.routes[props.selectedIndex]

    if (!selectedRoute) {
        return
    }

    const plannedStart = assignStartDate.value
    const plannedEnd = assignEndDate.value

    if (!plannedStart || !plannedEnd) {
        return
    }

    const transport: CreateTransportRequest = {
        name: data.title,
        customerId: data.customerId,

        driverId: data.driverId,
        tractorId: requestValue.tractorId,
        semiTrailerId: requestValue.semiTrailerId,

        plannedStart,
        plannedEnd,

        originName: requestValue.origin.name,
        originAddress: requestValue.origin.address,
        originLat: requestValue.origin.lat,
        originLng: requestValue.origin.lng,

        destinationName: requestValue.destination.name,
        destinationAddress: requestValue.destination.address,
        destinationLat: requestValue.destination.lat,
        destinationLng: requestValue.destination.lng
    }

    const request: CreateTransportFromRouteRequest = {
        transport,
        selectedRoute,
        routingProvider: 'HERE',
        routingMode: 'fastest'
    }

    try {
        await createTransportFromRoute(request)

        notification.success(
            'Planning enregistré',
            `Le transport « ${data.title} » a bien été ajouté.`
        )
    } catch (error) {
        notification.error(
            'Enregistrement impossible',
            getApiErrorMessage(error, 'Le transport n’a pas pu être ajouté au planning.')
        )
    } finally {
        showAssignModal.value = false
    }
}

onMounted(async () => {
    await loadAssignmentData()
})
</script>

<template>
    <div class="relative h-full">
        <div :class="[
            'h-full w-[400px] bg-white shadow-2xl border-r',
            'transition-all duration-300 overflow-hidden',
            open ? 'translate-x-0' : '-translate-x-full']">
            <div class="h-full flex flex-col">
                <div class="flex-1 overflow-y-auto p-4 space-y-6">
                    <!-- FORM -->
                    <RouteForm @route-calculated="onRouteCalculated" />

                    <!-- RESULTS -->
                    <div v-if="routeResponse?.routes" class="space-y-3">
                        <h2 class="text-xl font-bold text-slate-800">Itinéraires trouvés</h2>

                        <div class="space-y-3">
                            <div v-for="(route, index) in routeResponse.routes" :key="index"
                                @click="selectRoute(Number(index))"
                                class="p-4 border rounded-2xl cursor-pointer transition-all" :class="[
                                    index === selectedIndex
                                        ? 'border-blue-500 bg-blue-50 shadow-sm'
                                        : 'border-slate-200 hover:bg-slate-50',
                                ]">
                                <div class="flex items-start justify-between gap-4">
                                    <!-- LEFT -->
                                    <div>
                                        <div class="text-lg font-semibold text-slate-800">
                                            {{ route.costs.totalCost.toFixed(0) }} €
                                        </div>

                                        <div class="flex items-center gap-4 text-sm text-slate-500 mt-1">
                                            <div class="flex items-center gap-1">
                                                <Fuel class="h-4 w-4" />
                                                <span>{{ route.costs.fuelCost.toFixed(0) }} €</span>
                                            </div>

                                            <div class="flex items-center gap-1">
                                                <Receipt class="h-4 w-4" />
                                                <span>{{ route.costs.tollCost.toFixed(0) }} €</span>
                                            </div>
                                        </div>
                                    </div>

                                    <!-- RIGHT -->
                                    <div class="text-right">
                                        <div class="text-lg font-semibold text-slate-800">
                                            {{ formatDuration(route.durationSeconds) }}
                                        </div>

                                        <div class="text-sm text-slate-500 mt-1">
                                            {{ (route.distanceMeters / 1000).toFixed(0) }} km
                                        </div>
                                    </div>
                                </div>

                                <!-- Assign button -->
                                <div v-if="index === selectedIndex" class="mt-4">
                                    <button
                                        class="w-full bg-blue-600 text-white py-2 rounded-xl hover:bg-blue-700 transition"
                                        @click.stop="showAssignModal = true">
                                        Attribuer l'itinéraire
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- TOGGLE -->
        <button @click="open = !open" :class="[
            'absolute top-1/2 -translate-y-1/2 z-50',
            'h-16 w-8 rounded-r-xl',
            'bg-white border border-slate-200 border-l-0',
            'shadow-lg hover:bg-slate-50',
            'flex items-center justify-center',
            'transition-all duration-300',
            open ? 'left-[400px]' : 'left-0',
        ]">
            <ChevronLeft v-if="open" class="h-5 w-5 text-slate-600" />
            <ChevronRight v-else class="h-5 w-5 text-slate-600" />
        </button>

    </div>

    <AssignRouteModal :show="showAssignModal" :drivers="drivers" :customers="customers" :start-date="assignStartDate"
        :end-date="assignEndDate" @close="showAssignModal = false" @submit="handleAssignRoute" />
</template>
