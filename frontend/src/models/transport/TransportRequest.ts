import type { RoutesDto } from '@/models/route/Route'

export interface CreateTransportRequest {
    name: string
    customerId?: number

    driverId: number
    tractorId: number
    semiTrailerId?: number

    plannedStart: string
    plannedEnd: string

    originName: string
    originAddress: string
    originLat: number
    originLng: number

    destinationName: string
    destinationAddress: string
    destinationLat: number
    destinationLng: number
}

export interface CreateTransportFromRouteRequest {
    transport: CreateTransportRequest
    selectedRoute: RoutesDto
    routingProvider: string
    routingMode: string
}

export interface AssignTransport {
    title: string
    driverId: number
    customerId?: number
}