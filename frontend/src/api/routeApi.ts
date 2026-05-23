import api from './axios'

export async function calculateRoute(payload: any) {
    const response = await api.post('routes/calculate', payload)
    return response.data
}