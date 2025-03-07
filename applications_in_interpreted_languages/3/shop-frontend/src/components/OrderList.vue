<template>
  <div>
    <h2>Lista zamówień</h2>

    <div v-if="isLoading" class="alert alert-info">
      Ładowanie zamówień...
    </div>

    <div v-else-if="error" class="alert alert-danger">
      {{ error }}
    </div>

    <div v-else-if="filteredOrders.length === 0" class="alert alert-info">
      Brak zamówień do wyświetlenia
    </div>

    <div v-else>
      <div class="mb-3">
        <select class="form-select" v-model="selectedStatus">
          <option value="">Wszystkie statusy</option>
          <option v-for="status in orderStatuses" :key="status._id" :value="status._id">
            {{ status.name }}
          </option>
        </select>
      </div>

      <div v-for="order in filteredOrders" :key="order._id" class="card mb-4">
        <div class="card-header d-flex justify-content-between align-items-center">
          <div>
            <strong>Data zamówienia:</strong> {{ formatDate(order.createdAt) }}
            <div>
              <strong>Status:</strong>
              <span class="badge ms-2" :class="getStatusBadgeClass(order.status.name)">
                {{ order.status.name }}
              </span>
            </div>
          </div>
          <div class="btn-group" v-if="authStore.isEmployee && canChangeStatus(order)">
            <button
                class="btn btn-success btn-sm"
                @click="updateOrderStatus(order, 'ZREALIZOWANE')"
                :disabled="isUpdating"
            >
              Zrealizuj
            </button>
            <button
                class="btn btn-danger btn-sm"
                @click="updateOrderStatus(order, 'ANULOWANE')"
                :disabled="isUpdating"
            >
              Anuluj
            </button>
          </div>
        </div>

        <div class="card-body">
          <div class="row">
            <div class="col-md-6">
              <h5>Szczegóły zamówienia</h5>
              <p><strong>Wartość:</strong> {{ calculateOrderValue(order) }} PLN</p>
              <h6>Produkty:</h6>
              <ul class="list-unstyled">
                <li v-for="item in order.items" :key="item._id">
                  {{ item.product.name }} ({{ item.quantity }} szt. × {{ item.product.price }} PLN)
                </li>
              </ul>
            </div>

            <div class="col-md-6">
              <h5>Dane kontaktowe</h5>
              <p>
                <strong>Użytkownik:</strong> {{ order.username }}<br>
                <strong>Email:</strong> {{ order.email }}<br>
                <strong>Telefon:</strong> {{ order.phoneNumber }}
              </p>
            </div>
          </div>

          <div class="mt-4" v-if="order.status.name === 'ZREALIZOWANE' || order.status.name === 'ANULOWANE'">
            <OrderOpinion
                :orderId="order._id"
                :orderStatus="order.status.name"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import OrderOpinion from './OrderOpinion.vue'
import { useAuthStore } from '../stores/auth'

export default {
  name: 'OrderList',
  components: {
    OrderOpinion
  },
  setup() {
    const authStore = useAuthStore()
    return { authStore }
  },
  data() {
    return {
      orders: [],
      orderStatuses: [],
      selectedStatus: '',
      isLoading: true,
      isUpdating: false,
      error: null
    }
  },
  computed: {
    filteredOrders() {
      if (!this.selectedStatus) return this.orders
      return this.orders.filter(order => order.status._id === this.selectedStatus)
    }
  },
  methods: {
    async fetchOrders() {
      this.isLoading = true
      this.error = null

      try {
        // Jeśli użytkownik jest pracownikiem, pobierz wszystkie zamówienia
        // W przeciwnym razie pobierz tylko zamówienia użytkownika
        const endpoint = this.authStore.isEmployee
            ? '/api/orders'
            : `/api/orders/user/${this.authStore.user.username}`

        const response = await axios.get(endpoint)
        this.orders = response.data
      } catch (error) {
        console.error('Error fetching orders:', error)
        this.error = 'Nie udało się pobrać zamówień. Spróbuj odświeżyć stronę.'
      } finally {
        this.isLoading = false
      }
    },

    async fetchOrderStatuses() {
      try {
        const response = await axios.get('/api/status')
        this.orderStatuses = response.data
      } catch (error) {
        console.error('Error fetching order statuses:', error)
      }
    },

    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString()
    },

    calculateOrderValue(order) {
      return order.items.reduce((total, item) => {
        return total + (item.product.price * item.quantity)
      }, 0).toFixed(2)
    },

    canChangeStatus(order) {
      return order.status.name !== 'ZREALIZOWANE' &&
          order.status.name !== 'ANULOWANE'
    },

    async updateOrderStatus(order, newStatus) {
      if (!this.authStore.isEmployee || this.isUpdating) return

      this.isUpdating = true
      try {
        const statusObj = this.orderStatuses.find(s => s.name === newStatus)
        await axios.patch(`/api/orders/${order._id}`, {
          status: statusObj._id
        })
        await this.fetchOrders()
      } catch (error) {
        alert('Wystąpił błąd podczas aktualizacji statusu zamówienia')
      } finally {
        this.isUpdating = false
      }
    },

    getStatusBadgeClass(status) {
      const classes = {
        'NIEZATWIERDZONE': 'bg-secondary',
        'ZATWIERDZONE': 'bg-primary',
        'WYSŁANE': 'bg-info',
        'ZREALIZOWANE': 'bg-success',
        'ANULOWANE': 'bg-danger'
      }
      return classes[status] || 'bg-secondary'
    }
  },
  mounted() {
    this.fetchOrders()
    if (this.authStore.isEmployee) {
      this.fetchOrderStatuses()
    }
  }
}
</script>

<style scoped>
.card {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.badge {
  font-size: 0.9rem;
  padding: 0.5em 0.7em;
}

.list-unstyled li {
  padding: 0.3rem 0;
}
</style>
