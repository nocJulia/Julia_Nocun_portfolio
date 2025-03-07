// src/components/OrderOpinion.vue
<template>
  <div class="card">
    <div class="card-header">
      <h4>Opinie o zamówieniu</h4>
    </div>
    <div class="card-body">
      <div v-if="canAddOpinion" class="mb-4">
        <h5>Dodaj opinię</h5>
        <form @submit.prevent="submitOpinion">
          <div class="mb-3">
            <label class="form-label">Ocena</label>
            <div class="rating">
              <div class="btn-group" role="group">
                <template v-for="n in 5" :key="n">
                  <input
                      type="radio"
                      class="btn-check"
                      :id="'rating' + n"
                      name="rating"
                      :value="n"
                      v-model="opinionForm.rating"
                      required
                  >
                  <label
                      class="btn btn-outline-warning"
                      :class="{ active: opinionForm.rating === n }"
                      :for="'rating' + n"
                  >
                    {{ n }} ★
                  </label>
                </template>
              </div>
              <div class="invalid-feedback" v-if="errors.rating">
                {{ errors.rating }}
              </div>
            </div>
          </div>

          <div class="mb-3">
            <label class="form-label">Treść opinii</label>
            <textarea
                class="form-control"
                v-model="opinionForm.content"
                rows="3"
                required
                :class="{'is-invalid': errors.content}"
            ></textarea>
            <div class="invalid-feedback">
              {{ errors.content }}
            </div>
          </div>

          <button
              type="submit"
              class="btn btn-primary"
              :disabled="isSubmitting"
          >
            {{ isSubmitting ? 'Wysyłanie...' : 'Dodaj opinię' }}
          </button>
        </form>
      </div>

      <div v-if="opinions.length > 0">
        <h5 class="mb-3">Opinie</h5>
        <div class="opinions-list">
          <div v-for="opinion in opinions" :key="opinion._id" class="card mb-3">
            <div class="card-body">
              <div class="d-flex justify-content-between align-items-center mb-2">
                <div class="rating-display">
                  <span class="text-warning">
                    {{ '★'.repeat(opinion.rating) }}{{ '☆'.repeat(5 - opinion.rating) }}
                  </span>
                  <span class="ms-2 text-muted">{{ opinion.rating }}/5</span>
                </div>
                <small class="text-muted">
                  {{ formatDate(opinion.createdAt) }}
                </small>
              </div>
              <p class="card-text">{{ opinion.content }}</p>
              <small class="text-muted">
                Autor: {{ opinion.username }}
              </small>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="text-muted">
        Brak opinii dla tego zamówienia.
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useAuthStore } from '../stores/auth'

export default {
  name: 'OrderOpinion',
  props: {
    orderId: {
      type: String,
      required: true
    },
    orderStatus: {
      type: String,
      required: true
    }
  },
  setup(props) {
    const authStore = useAuthStore()
    const opinions = ref([])
    const isSubmitting = ref(false)
    const errors = ref({})
    const opinionForm = ref({
      rating: null,
      content: ''
    })

    const canAddOpinion = computed(() => {
      const isCompleted = ['ZREALIZOWANE', 'ANULOWANE'].includes(props.orderStatus)
      const hasNoOpinion = !opinions.value.some(o => o.username === authStore.user?.username)
      return isCompleted && hasNoOpinion && authStore.isAuthenticated
    })

    const fetchOpinions = async () => {
      try {
        const response = await axios.get(`/api/orders/${props.orderId}/opinions`)
        opinions.value = response.data
      } catch (error) {
        console.error('Error fetching opinions:', error)
      }
    }

    const submitOpinion = async () => {
      if (!canAddOpinion.value) return

      isSubmitting.value = true
      errors.value = {}

      try {
        await axios.post(`/api/orders/${props.orderId}/opinions`, opinionForm.value)
        await fetchOpinions()
        opinionForm.value = { rating: null, content: '' }
      } catch (error) {
        if (error.response?.data?.errors) {
          errors.value = error.response.data.errors
        } else {
          alert('Wystąpił błąd podczas dodawania opinii')
        }
      } finally {
        isSubmitting.value = false
      }
    }

    const formatDate = (date) => {
      return new Date(date).toLocaleString()
    }

    onMounted(() => {
      fetchOpinions()
    })

    return {
      opinions,
      opinionForm,
      isSubmitting,
      errors,
      canAddOpinion,
      submitOpinion,
      formatDate
    }
  }
}
</script>

<style scoped>
.rating .btn-group {
  gap: 0.5rem;
}

.rating-display {
  font-size: 1.2rem;
}

.opinions-list {
  max-height: 500px;
  overflow-y: auto;
}
</style>
