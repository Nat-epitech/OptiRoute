import type { RouteCostDetailsDto } from '@/models/route/RouteCostDetailsDto'

export interface RoutesDto {
    durationSeconds: number
    distanceMeters: number

    costs: RouteCostDetailsDto
}