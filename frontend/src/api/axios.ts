import axios from 'axios'
import { useAuthStore } from '@/stores/authStore'

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    headers: {
        'Content-Type': 'application/json'
    }
})

api.interceptors.request.use((config) => {
    const authStore = useAuthStore()

    if (authStore.token) {
        config.headers.Authorization = `Bearer ${authStore.token}`
    }

    return config
})

api.interceptors.response.use(
    (response) => response,

    (error) => {
        const authStore = useAuthStore()

        if (error.response?.status === 401) {
            authStore.logout()
            window.location.href = '/login'
        }

        return Promise.reject(error)
    }
)

export default api