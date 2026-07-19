import api from '@/api/axios'
import type { CreateTransportRequest, CreateTransportFromRouteRequest } from '@/models/transport/CreateTransportRequest'

export const createTransport = async (request: CreateTransportRequest) => {
    const response = await api.post('/transports', request)

    return response.data
}

export const createTransportFromRoute = async (request: CreateTransportFromRouteRequest) => {
    const response = await api.post('/transports/from-route', request)

    return response.data
}

export const deleteTransport = async (id: number) => {
    await api.delete(`/transports/${id}`)
}