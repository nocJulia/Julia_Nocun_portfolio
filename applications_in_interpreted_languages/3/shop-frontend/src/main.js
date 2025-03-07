import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import 'bootstrap/dist/css/bootstrap.min.css'
import 'bootstrap'
import axios from 'axios'
import {useAuthStore} from "@/stores/auth";

axios.defaults.baseURL = 'http://localhost:3000'
axios.defaults.headers.common['Content-Type'] = 'application/json'

axios.interceptors.response.use(
    response => response,
    async error => {
        const originalRequest = error.config

        if (error.response.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true

            try {
                const authStore = useAuthStore()
                const response = await axios.post('/auth/refresh-token', {
                    refreshToken: authStore.refreshToken
                })

                authStore.setTokens(response.data)
                originalRequest.headers['Authorization'] = `Bearer ${response.data.accessToken}`
                return axios(originalRequest)
            } catch (refreshError) {
                const authStore = useAuthStore()
                authStore.clearAuth()
                router.push('/login')
                return Promise.reject(refreshError)
            }
        }

        return Promise.reject(error)
    }
)

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)
app.mount('#app')
