<template>
  <div class="container">
    <nav class="navbar navbar-expand-lg navbar-light bg-light mb-4">
      <div class="container-fluid">
        <router-link class="navbar-brand" to="/">Sklep</router-link>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav me-auto">
            <li class="nav-item">
              <router-link class="nav-link" to="/">Produkty</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/cart">Koszyk</router-link>
            </li>
            <li class="nav-item" v-if="authStore.isAuthenticated">
              <router-link class="nav-link" to="/orders">Zamówienia</router-link>
            </li>
          </ul>

          <ul class="navbar-nav" v-if="authStore.isAuthenticated">
            <li class="nav-item">
              <span class="nav-link">
                Witaj, {{ authStore.user?.username }}
                <span v-if="authStore.isEmployee" class="badge bg-info ms-1">Pracownik</span>
              </span>
            </li>
            <li class="nav-item">
              <button class="btn btn-outline-danger" @click="handleLogout">
                Wyloguj
              </button>
            </li>
          </ul>
          <ul class="navbar-nav" v-else>
            <li class="nav-item">
              <router-link class="nav-link" to="/login">Zaloguj</router-link>
            </li>
            <li class="nav-item">
              <router-link class="nav-link" to="/register">Zarejestruj</router-link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <router-view></router-view>
  </div>
</template>

<script>
import { useAuthStore } from './stores/auth'
import { useRouter } from 'vue-router'

export default {
  setup() {
    const authStore = useAuthStore()
    const router = useRouter()

    const handleLogout = async () => {
      await authStore.logout()
      router.push('/login')
    }

    return {
      authStore,
      handleLogout
    }
  }
}
</script>
