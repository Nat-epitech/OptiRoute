import api from './axios'

export const getPlanningByDriver = async (driverId: number) => {
    const response = await api.get(`/planning-events/driver/${driverId}`)
    return response.data
}

export const createPlanningEvent = async (payload: {
    driverId: number, title: string, startDatetime: string, endDatetime: string
}) => {
    const response = await api.post('/planning-events', payload)

    return response.data
}