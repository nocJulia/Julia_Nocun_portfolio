<template>
  <div class="container mt-5">
    <div class="row justify-content-center">
      <div class="col-md-6">
        <div class="card">
          <div class="card-header">
            <h3 class="mb-0">Rejestracja</h3>
          </div>
          <div class="card-body">
            <form @submit.prevent="handleSubmit">
              <div class="mb-3">
                <label class="form-label">Nazwa użytkownika</label>
                <input
                    type="text"
                    class="form-control"
                    v-model="userData.username"
                    :class="{'is-invalid': errors.username}"
                    required
                />
                <div class="invalid-feedback">{{ errors.username }}</div>
              </div>

              <div class="mb-3">
                <label class="form-label">Email</label>
                <input
                    type="email"
                    class="form-control"
                    v-model="userData.email"
                    :class="{'is-invalid': errors.email}"
                    required
                />
                <div class="invalid-feedback">{{ errors.email }}</div>
              </div>

              <div class="mb-3">
                <label class="form-label">Hasło</label>
                <input
                    type="password"
                    class="form-control"
                    v-model="userData.password"
                    :class="{'is-invalid': errors.password}"
                    required
                />
                <div class="invalid-feedback">{{ errors.password }}</div>
              </div>

              <button type="submit" class="btn btn-primary w-100" :disabled="isLoading">
                {{ isLoading ? 'Rejestracja...' : 'Zarejestruj się' }}
              </button>

              <div class="text-center mt-3">
                <router-link to="/login">Masz już konto? Zaloguj się</router-link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { useRouter } from 'vue-router'
import axios from 'axios'

export default {
  name: 'RegisterForm',
  setup() {
    const router = useRouter()
    return { router }
  },
  data() {
    return {
      userData: {
        username: '',
        email: '',
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
        await axios.post('/api/auth/register', {
          ...this.userData,
          role: 'KLIENT'
        })

        alert('Rejestracja udana. Możesz się teraz zalogować.')
        this.router.push('/login')
      } catch (error) {
        if (error.response?.data?.errors) {
          this.errors = error.response.data.errors
        } else {
          alert('Wystąpił błąd podczas rejestracji')
        }
      } finally {
        this.isLoading = false
      }
    }
  }
}
</script>
