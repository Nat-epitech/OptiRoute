import api from './axios'

export const getUsers = async () => {
  const response = await api.get('/users')
  return response.data
}

export const createUser = async (user: any) => {
  const response = await api.post('/users', user)
  return response.data
}