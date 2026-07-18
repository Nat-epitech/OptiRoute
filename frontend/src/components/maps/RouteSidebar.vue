<script setup lang="ts">
import { computed, ref, onMounted } from 'vue'
import RouteForm from './RouteForm.vue'

import {
    ChevronLeft,
    ChevronRight,
    Fuel,
    Receipt,
    User
} from 'lucide-vue-next'

import AssignRouteModal from '@/components/ui/AssignRouteModal.vue'

import type { RouteRequest } from '@/models/route/RouteRequest.ts'
import type { RouteResponse } from '@/models/route/RouteResponse.ts'
import type { Driver } from '@/models/Driver'
import type { Vehicle } from '@/models/Vehicle'
import type { Customer } from '@/models/Customer'
import type { CreateMissionRequest } from '@/models/mission/CreateMissionRequest'

import { createMissionFromRoute } from '@/api/missionApi'
import type { CreateMissionFromRouteRequest } from '@/models/mission/CreateMissionFromRouteRequest'
import { getDrivers } from '@/api/driverApi'
import { getVehicles } from '@/api/vehicleApi'
import { getCustomers } from '@/api/customerApi'
import { getApiErrorMessage } from '@/api/utils'

import { useNotification } from '@/composables/useNotification'

const notification = useNotification()

const drivers = ref<Driver[]>([])
const vehicles = ref<Vehicle[]>([])
const customers = ref<Customer[]>([])

const loadAssignmentData = async () => {
    const [
        loadedDrivers,
        loadedVehicles,
        loadedCustomers
    ] = await Promise.all([
        getDrivers(),
        getVehicles(),
        getCustomers()
    ])

    drivers.value = loadedDrivers
    vehicles.value = loadedVehicles
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

const formatDuration = (seconds: number) => {
    const totalMinutes = Math.round(seconds / 60)
    const hours = Math.floor(totalMinutes / 60)
    const minutes = totalMinutes % 60

    if (minutes === 0) {
        return `${hours} h`
    }

    return `${hours} h ${minutes}`
}

interface AssignRouteData {
    title: string
    driverId: number
    vehicleId?: number
    customerId?: number
}

const handleAssignRoute = async (data: AssignRouteData) => {
    const requestValue = routeRequest.value

    if (
        !requestValue ||
        props.selectedIndex === undefined ||
        !props.routeResponse
    ) {
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

    const mission: CreateMissionRequest = {
        name: data.title,
        driverId: data.driverId,
        vehicleId: data.vehicleId,
        customerId: data.customerId,

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

    const request: CreateMissionFromRouteRequest = {
        mission,
        selectedRoute,
        routingProvider: 'HERE',
        routingMode: 'fastest'
    }

    try {
        await createMissionFromRoute(request)

        notification.success(
            'Planning enregistré',
            `La mission « ${data.title} » a bien été ajoutée.`
        )
    } catch (error) {
        notification.error(
            'Enregistrement impossible',
            getApiErrorMessage(
                error,
                'La mission n’a pas pu être ajoutée au planning.'
            )
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
            'h-full w-[420px] bg-white shadow-2xl border-r',
            'transition-all duration-300 overflow-hidden',
            open ? 'translate-x-0' : '-translate-x-full',
        ]">
            <div class="h-full flex flex-col">
                <!-- HEADER -->
                <div class="p-4 border-b">
                    <h1 class="text-xl font-bold">Rechercher un itinéraire</h1>
                </div>

                <!-- CONTENT -->
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

                                            <!-- <div class="flex items-center gap-1">
                                                <User class="h-4 w-4" />
                                                <span>{{ route.costs.driverCost.toFixed(0) }} €</span>
                                            </div> -->
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

                                <!-- ✅ BOUTON UNIQUEMENT SI SELECTED -->
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

            <!-- TOGGLE -->
            <button @click="open = !open" :class="[
                'absolute top-1/2 -translate-y-1/2 z-50',
                'h-16 w-8 rounded-r-xl',
                'bg-white border border-slate-200 border-l-0',
                'shadow-lg hover:bg-slate-50',
                'flex items-center justify-center',
                'transition-all duration-300',
                open ? 'left-[420px]' : 'left-0',
            ]">
                <ChevronLeft v-if="open" class="h-5 w-5 text-slate-600" />
                <ChevronRight v-else class="h-5 w-5 text-slate-600" />
            </button>
        </div>
    </div>

    <AssignRouteModal :show="showAssignModal" :drivers="drivers" :vehicles="vehicles" :customers="customers"
        :start-date="assignStartDate" :end-date="assignEndDate" @close="showAssignModal = false"
        @submit="handleAssignRoute" />
</template>
