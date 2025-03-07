<template>
  <div>
    <h2>Koszyk</h2>

    <div v-if="cartItems.length === 0" class="alert alert-info">
      Koszyk jest pusty
    </div>

    <div v-else>
      <table class="table">
        <thead>
        <tr>
          <th>Produkt</th>
          <th>Ilość</th>
          <th>Cena jednostkowa</th>
          <th>Suma</th>
          <th>Akcje</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in cartItems" :key="item.product._id">
          <td>{{ item.product.name }}</td>
          <td>
            <div class="input-group" style="width: 150px">
              <button
                  class="btn btn-outline-secondary"
                  @click="updateQuantity(item, -1)"
                  :disabled="item.quantity <= 1"
              >-</button>
              <input
                  type="number"
                  class="form-control text-center"
                  v-model.number="item.quantity"
                  min="1"
              >
              <button
                  class="btn btn-outline-secondary"
                  @click="updateQuantity(item, 1)"
              >+</button>
            </div>
          </td>
          <td>{{ item.product.price }} PLN</td>
          <td>{{ (item.product.price * item.quantity).toFixed(2) }} PLN</td>
          <td>
            <button
                class="btn btn-danger"
                @click="removeItem(item)"
            >
              Usuń
            </button>
          </td>
        </tr>
        </tbody>
        <tfoot>
        <tr>
          <td colspan="3" class="text-end"><strong>Suma całkowita:</strong></td>
          <td colspan="2"><strong>{{ totalPrice.toFixed(2) }} PLN</strong></td>
        </tr>
        </tfoot>
      </table>

      <div class="card mt-4">
        <div class="card-header">
          <h4>Dane zamówienia</h4>
        </div>
        <div class="card-body">
          <form @submit.prevent="submitOrder" class="row g-3">
            <div class="col-md-6">
              <label class="form-label">Nazwa użytkownika</label>
              <input
                  type="text"
                  class="form-control"
                  v-model="orderData.username"
                  :class="{'is-invalid': errors.username}"
                  required
              >
              <div class="invalid-feedback">{{ errors.username }}</div>
            </div>

            <div class="col-md-6">
              <label class="form-label">Email</label>
              <input
                  type="email"
                  class="form-control"
                  v-model="orderData.email"
                  :class="{'is-invalid': errors.email}"
                  required
              >
              <div class="invalid-feedback">{{ errors.email }}</div>
            </div>

            <div class="col-md-6">
              <label class="form-label">Telefon</label>
              <input
                  type="tel"
                  class="form-control"
                  v-model="orderData.phoneNumber"
                  :class="{'is-invalid': errors.phoneNumber}"
                  required
                  pattern="[0-9]*"
              >
              <div class="invalid-feedback">{{ errors.phoneNumber }}</div>
            </div>

            <div class="col-12">
              <button type="submit" class="btn btn-primary" :disabled="isSubmitting || cartItems.length === 0">
                {{ isSubmitting ? 'Wysyłanie...' : 'Złóż zamówienie' }}
              </button>
            </div>

            <div v-if="submitError" class="col-12">
              <div class="alert alert-danger">
                {{ submitError }}
              </div>
            </div>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {useRouter} from 'vue-router'
import {useAuthStore} from '../stores/auth'

export default {
  name: 'ShoppingCart',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()
    return {router, authStore}
  },
  data() {
    return {
      cartItems: [],
      orderData: {
        username: '',
        email: '',
        phoneNumber: ''
      },
      errors: {},
      isSubmitting: false,
      submitError: ''
    }
  },
  computed: {
    totalPrice() {
      return this.cartItems.reduce((total, item) => {
        return total + (item.product.price * item.quantity)
      }, 0)
    }
  },
  methods: {
    loadCart() {
      const cart = JSON.parse(localStorage.getItem('cart') || '[]')
      this.cartItems = cart
    },

    updateQuantity(item, change) {
      const newQuantity = item.quantity + change
      if (newQuantity >= 1) {
        item.quantity = newQuantity
        this.saveCart()
      }
    },

    removeItem(item) {
      const index = this.cartItems.indexOf(item)
      if (index > -1) {
        this.cartItems.splice(index, 1)
        this.saveCart()
      }
    },

    saveCart() {
      localStorage.setItem('cart', JSON.stringify(this.cartItems))
    },

    async submitOrder() {
      if (this.cartItems.length === 0) return

      this.isSubmitting = true
      this.errors = {}
      this.submitError = ''

      try {
        // Przygotuj dane zamówienia
        const orderItems = this.cartItems.map(item => ({
          product: item.product._id,
          quantity: parseInt(item.quantity)
        }))

        // Użyj danych zalogowanego użytkownika, jeśli jest dostępny
        const orderData = {
          username: this.authStore.isAuthenticated ? this.authStore.user.username : this.orderData.username,
          email: this.authStore.isAuthenticated ? this.authStore.user.email : this.orderData.email,
          phoneNumber: this.orderData.phoneNumber,
          items: orderItems
        }

        // Wyślij zamówienie
        const response = await axios.post('/api/orders', orderData, {
          headers: {
            'Authorization': `Bearer ${this.authStore.accessToken}`
          }
        })

        if (response.status === 201) {
          // Wyczyść koszyk po udanym zamówieniu
          localStorage.removeItem('cart')
          this.cartItems = []

          alert('Zamówienie zostało złożone pomyślnie')
          this.router.push('/orders')
        }
      } catch (error) {
        console.error('Order submission error:', error)

        if (error.response?.data?.errors) {
          this.errors = error.response.data.errors
        } else {
          this.submitError = error.response?.data?.message || 'Wystąpił błąd podczas składania zamówienia'
        }
      } finally {
        this.isSubmitting = false
      }
    }
  },
  mounted() {
    this.loadCart()

    if (this.authStore.isAuthenticated) {
      this.orderData.username = this.authStore.user.username
      this.orderData.email = this.authStore.user.email
    }
  },
  watch: {
    'authStore.isAuthenticated'(newValue) {
      if (newValue) {
        this.orderData.username = this.authStore.user.username
        this.orderData.email = this.authStore.user.email
      }
    }
  }
}
</script>

<style scoped>
.input-group .form-control {
  text-align: center;
}

.card {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}
</style>
