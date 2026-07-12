import type { Position } from '@/models/route/Position'

export interface RouteRequest {
    origin: Position
    destination: Position

    departureTime?: string

    maxTravelTimeMinutes?: number

    driverHourlyRate: number
}