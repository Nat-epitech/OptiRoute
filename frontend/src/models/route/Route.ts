import type { Position } from '@/models/route/Position'

export interface RouteRequest {
    origin: Position
    destination: Position

    tractorId: number
    semiTrailerId?: number

    emptyTrip: boolean

    departureTime?: string

    maxTravelTimeMinutes?: number

    driverHourlyRate: number
}

export interface RouteResponse {
    routes: RouteDto[]
}

export interface RouteDto {
    duration: number
    baseDuration: number
    distanceMeters: number

    costs: RouteCostDetailsDto
}

export interface RouteCostDetailsDto {
    fuelCost: number
    tollCost: number
    totalCost: number
}