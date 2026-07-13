import api from './axios'
import type { UpdateUserRequest } from '@/models/User'

export const getUsers = async () => {
  const response = await api.get('/users')
  return response.data
}

export const createUser = async (user: any) => {
  const response = await api.post('/users', user)
  return response.data
}

export const deleteUser = async (id: number) => {
  await api.delete(`/users/${id}`)
}

export const updateUser = async (id: number, user: UpdateUserRequest) => {
  const response = await api.put(`/users/${id}`, user)
  return response.data
}