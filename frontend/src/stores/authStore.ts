import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {

    state: () => ({
        token: localStorage.getItem('token') || null,
        firstName: localStorage.getItem('firstName') || '',
        lastName: localStorage.getItem('lastName') || ''
    }),

    getters: {
        isAuthenticated: (state) => !!state.token
    },

    actions: {
        setToken(token: string, firstName = '', lastName = '') {
            this.token = token
            this.firstName = firstName
            this.lastName = lastName
            localStorage.setItem('token', token)
            localStorage.setItem('firstName', firstName)
            localStorage.setItem('lastName', lastName)
        },

        logout() {
            this.token = null
            this.firstName = ''
            this.lastName = ''
            localStorage.removeItem('token')
            localStorage.removeItem('firstName')
            localStorage.removeItem('lastName')
        }
    }
})