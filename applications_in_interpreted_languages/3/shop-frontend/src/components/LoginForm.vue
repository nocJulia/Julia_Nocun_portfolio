<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card">
          <div class="card-header">
            <h3 class="mb-0">Logowanie</h3>
          </div>
          <div class="card-body">
            <form @submit.prevent="handleSubmit">
              <div class="mb-3">
                <label class="form-label">Nazwa użytkownika</label>
                <input
                    type="text"
                    class="form-control"
                    v-model="credentials.username"
                    :class="{'is-invalid': errors.username}"
                    required
                />
                <div class="invalid-feedback">{{ errors.username }}</div>
              </div>

              <div class="mb-3">
                <label class="form-label">Hasło</label>
                <input
                    type="password"
                    class="form-control"
                    v-model="credentials.password"
                    :class="{'is-invalid': errors.password}"
                    required
                />
                <div class="invalid-feedback">{{ errors.password }}</div>
              </div>

              <button type="submit" class="btn btn-primary w-100" :disabled="isLoading">
                {{ isLoading ? 'Logowanie...' : 'Zaloguj się' }}
              </button>

              <div class="text-center mt-3">
                <router-link to="/register">Nie masz konta? Zarejestruj się</router-link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'

export default {
  name: 'LoginForm',
  setup() {
    const router = useRouter()
    const route = useRoute()
    const authStore = useAuthStore()
    return { router, route, authStore }
  },
  data() {
    return {
      credentials: {
        username: '',
        password: ''
      },
      errors: {},
      isLoading: false
    }
  },
  methods: {
    async handleSubmit() {
      this.isLoading = true
      this.errors = {}

      try {
        await this.authStore.login(this.credentials)

        const redirectPath = this.route.query.redirect || '/'
        this.router.push(redirectPath)
      } catch (error) {
        if (error.response?.data?.errors) {
          this.errors = error.response.data.errors
        } else {
          alert('Wystąpił błąd podczas logowania')
        }
      } finally {
        this.isLoading = false
      }
    }
  }
}
</script>
