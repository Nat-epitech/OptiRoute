import api from './axios'

export const getPlanningByDriver = async (driverId: number) => {
    const response = await api.get(`/planning-events/driver/${driverId}`)
    return response.data
}