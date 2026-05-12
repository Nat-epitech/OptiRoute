import api from './axios'

export const getDrivers = async () => {
  const response = await api.get('/drivers')
  return response.data
}

export const createDriver = async (driver: any) => {
  const response = await api.post('/drivers', driver)
  return response.data
}