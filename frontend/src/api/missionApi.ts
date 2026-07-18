import api from '@/api/axios'
import type { CreateMissionRequest } from '@/models/mission/CreateMissionRequest'
import type { CreateMissionFromRouteRequest } from '@/models/mission/CreateMissionFromRouteRequest'

export const createMission = async (request: CreateMissionRequest) => {
    const response = await api.post('/missions', request)

    return response.data
}

export const createMissionFromRoute = async (request: CreateMissionFromRouteRequest) => {
    const response = await api.post('/missions/from-route', request)

    return response.data
}

export const deleteMission = async (id: number) => {
    await api.delete(`/missions/${id}`)
}