import type { CreateMissionRequest } from './CreateMissionRequest'
import type { RoutesDto } from '@/models/route/RoutesDto'

export interface CreateMissionFromRouteRequest {
    mission: CreateMissionRequest
    selectedRoute: RoutesDto
    routingProvider: string
    routingMode: string
}