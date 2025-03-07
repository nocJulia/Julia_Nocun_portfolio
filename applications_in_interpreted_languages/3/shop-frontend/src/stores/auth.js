import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: null,
        accessToken: localStorage.getItem('accessToken'),
        refreshToken: localStorage.getItem('refreshToken')
    }),

    getters: {
        isAuthenticated: (state) => !!state.accessToken,
        isEmployee: (state) => state.user?.role === 'PRACOWNIK'
    },

    actions: {
        async login({ username, password }) {
            const response = await axios.post('/api/auth/login', {
                username,
                password
            })

            this.setTokens(response.data)
            this.user = response.data.user
        },

        async logout() {
            if (this.refreshToken) {
                try {
                    await axios.post('/api/auth/logout', {
                        refreshToken: this.refreshToken
                    })
                } catch (error) {
                    console.error('Logout error:', error)
                }
            }

            this.clearAuth()
        },

        setTokens({ accessToken, refreshToken, user }) {
            this.accessToken = accessToken
            this.refreshToken = refreshToken
            this.user = user

            localStorage.setItem('accessToken', accessToken)
            localStorage.setItem('refreshToken', refreshToken)

            axios.defaults.headers.common['Authorization'] = `Bearer ${accessToken}`
        },

        clearAuth() {
            this.user = null
            this.accessToken = null
            this.refreshToken = null

            localStorage.removeItem('accessToken')
            localStorage.removeItem('refreshToken')
            delete axios.defaults.headers.common['Authorization']
        }
    }
})

