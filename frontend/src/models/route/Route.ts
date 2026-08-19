import type { Position } from '@/models/route/Position'

export interface RouteRequest {
    origin: Position
    destination: Position

    tractorId: number
    semiTrailerId?: number

    departureTime?: string

    maxTravelTimeMinutes?: number

    driverHourlyRate: number
}

export interface RouteResponse {
    routes: RoutesDto[]
}

export interface RoutesDto {
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