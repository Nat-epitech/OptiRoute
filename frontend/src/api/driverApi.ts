import api from './axios'
import type { Driver, DriverDetails, CreateDriverRequest, UpdateDriverRequest } from '@/models/Driver'

export const getDrivers = async (): Promise<Driver[]> => {
  const response = await api.get<Driver[]>('/drivers')
  return response.data
}

export const getDriver = async (id: number): Promise<DriverDetails> => {
  const response = await api.get<DriverDetails>(`/drivers/${id}`);
  return response.data;
};

export const createDriver = async (driver: CreateDriverRequest) => {
  const response = await api.post('/drivers', driver)
  return response.data
}

export const deleteDriver = async (id: number) => {
  await api.delete(`/drivers/${id}`)
}

export const updateDriver = async (id: number, driver: UpdateDriverRequest) => {
  const response = await api.put(`/drivers/${id}`, driver)
  return response.data
}