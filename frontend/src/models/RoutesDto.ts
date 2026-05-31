import type { RouteCostDetailsDto } from '@/models/RouteCostDetailsDto'

export interface RoutesDto {
    durationSeconds: number
    distanceMeters: number

    costs: RouteCostDetailsDto
}