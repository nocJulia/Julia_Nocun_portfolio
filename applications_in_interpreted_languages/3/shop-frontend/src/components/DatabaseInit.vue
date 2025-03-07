<template>
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-md-8">
        <div class="card">
          <div class="card-header">
            <h3 class="mb-0">Inicjalizacja bazy danych</h3>
          </div>
          <div class="card-body">
            <div v-if="isLoadingCategories" class="alert alert-info">
              Ładowanie kategorii...
            </div>

            <div v-if="categoriesError" class="alert alert-danger">
              {{ categoriesError }}
            </div>

            <div v-if="!isLoadingCategories && !categoriesError">
              <div class="mb-4">
                <label class="form-label">Wybierz plik z produktami (CSV lub JSON)</label>
                <input
                    type="file"
                    class="form-control"
                    accept=".csv,.json"
                    @change="handleFileSelect"
                    :class="{'is-invalid': fileError}"
                >
                <div class="invalid-feedback">{{ fileError }}</div>
                <small class="form-text text-muted">
                  Dostępne kategorie: {{ availableCategories.join(', ') }}
                </small>
              </div>

              <div v-if="validationResults.length > 0" class="mb-4">
                <h5>Wyniki walidacji:</h5>
                <div class="alert" :class="allValid ? 'alert-success' : 'alert-danger'">
                  <div v-for="(result, index) in validationResults" :key="index">
                    <span v-if="result.valid" class="text-success">✓</span>
                    <span v-else class="text-danger">✗</span>
                    {{ result.message }}
                  </div>
                </div>
              </div>

              <div v-if="parsedData.length > 0" class="mb-4">
                <h5>Podgląd danych:</h5>
                <div class="table-responsive">
                  <table class="table table-sm">
                    <thead>
                    <tr>
                      <th>Nazwa</th>
                      <th>Kategoria</th>
                      <th>Cena</th>
                      <th>Waga</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(item, index) in parsedData.slice(0, 5)" :key="index">
                      <td>{{ item.name }}</td>
                      <td>{{ item.category }}</td>
                      <td>{{ item.price }} PLN</td>
                      <td>{{ item.weight }} kg</td>
                    </tr>
                    </tbody>
                  </table>
                  <div v-if="parsedData.length > 5" class="text-muted">
                    Pokazano 5 z {{ parsedData.length }} produktów
                  </div>
                </div>
              </div>

              <button
                  class="btn btn-primary"
                  @click="initializeDatabase"
                  :disabled="!canInitialize || isInitializing"
              >
                {{ isInitializing ? 'Inicjalizacja...' : 'Zainicjalizuj bazę danych' }}
              </button>

              <div v-if="initializationError" class="alert alert-danger mt-3">
                <h5>Błąd inicjalizacji:</h5>
                <p>{{ initializationError }}</p>
                <div v-if="failedProducts.length > 0">
                  <h6>Szczegóły błędów:</h6>
                  <ul>
                    <li v-for="(product, index) in failedProducts" :key="index">
                      {{ product.name }}: {{ product.error }}
                    </li>
                  </ul>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import axios from 'axios'

export default {
  name: 'DatabaseInit',
  setup() {
    const router = useRouter()
    const authStore = useAuthStore()

    if (!authStore.isEmployee) {
      router.push('/')
      return
    }

    const parsedData = ref([])
    const validationResults = ref([])
    const fileError = ref('')
    const isInitializing = ref(false)
    const isLoadingCategories = ref(true)
    const categoriesError = ref('')
    const categories = ref([])
    const initializationError = ref('')
    const failedProducts = ref([])

    const availableCategories = computed(() => categories.value.map(c => c.name))

    const allValid = computed(() =>
        validationResults.value.length > 0 &&
        validationResults.value.every(r => r.valid)
    )

    const canInitialize = computed(() =>
        parsedData.value.length > 0 && allValid.value && !isInitializing.value
    )

    const fetchCategories = async () => {
      try {
        isLoadingCategories.value = true
        const response = await axios.get('/api/categories')
        categories.value = response.data
      } catch (error) {
        categoriesError.value = 'Nie udało się pobrać kategorii. Odśwież stronę i spróbuj ponownie.'
        console.error('Error fetching categories:', error)
      } finally {
        isLoadingCategories.value = false
      }
    }

    const validateProduct = (product, index) => {
      const results = []

      const requiredFields = ['name', 'description', 'price', 'weight', 'category']
      requiredFields.forEach(field => {
        if (!product[field]) {
          results.push({
            valid: false,
            message: `Produkt ${index + 1}: Brak wymaganego pola: ${field}`
          })
        }
      })

      if (isNaN(parseFloat(product.price)) || parseFloat(product.price) <= 0) {
        results.push({
          valid: false,
          message: `Produkt ${index + 1}: Nieprawidłowa cena: ${product.price}`
        })
      }

      if (isNaN(parseFloat(product.weight)) || parseFloat(product.weight) <= 0) {
        results.push({
          valid: false,
          message: `Produkt ${index + 1}: Nieprawidłowa waga: ${product.weight}`
        })
      }

      const categoryExists = availableCategories.value
          .some(cat => cat.toLowerCase() === product.category.toLowerCase())
      if (!categoryExists) {
        results.push({
          valid: false,
          message: `Produkt ${index + 1}: Nieprawidłowa kategoria: ${product.category}. Dozwolone kategorie: ${availableCategories.value.join(', ')}`
        })
      }

      if (results.length === 0) {
        results.push({
          valid: true,
          message: `Produkt ${index + 1}: ${product.name} - OK`
        })
      }

      return results
    }

    const parseCSV = (content) => {
      const lines = content.split('\n')
      const headers = lines[0].split(',').map(h => h.trim())

      return lines.slice(1)
          .filter(line => line.trim())
          .map(line => {
            const values = line.split(',').map(v => v.trim())
            return headers.reduce((obj, header, index) => {
              obj[header] = values[index]
              return obj
            }, {})
          })
    }

    const handleFileSelect = async (event) => {
      const file = event.target.files[0]
      if (!file) return

      fileError.value = ''
      validationResults.value = []
      parsedData.value = []
      initializationError.value = ''
      failedProducts.value = []

      try {
        const content = await file.text()
        let data

        if (file.name.endsWith('.json')) {
          data = JSON.parse(content)
          if (!Array.isArray(data)) {
            throw new Error('Plik JSON musi zawierać tablicę produktów')
          }
        } else if (file.name.endsWith('.csv')) {
          data = parseCSV(content)
        } else {
          throw new Error('Nieobsługiwany format pliku')
        }

        // Validate all products
        const allValidations = []
        data.forEach((product, index) => {
          const productValidations = validateProduct(product, index)
          allValidations.push(...productValidations)
        })

        validationResults.value = allValidations
        if (allValidations.every(v => v.valid)) {
          parsedData.value = data
        }
      } catch (error) {
        fileError.value = `Błąd przetwarzania pliku: ${error.message}`
        console.error('File processing error:', error)
      }
    }

    const initializeDatabase = async () => {
      if (!canInitialize.value) return

      isInitializing.value = true
      initializationError.value = ''
      failedProducts.value = []

      try {
        const transformedData = parsedData.value.map(product => ({
          ...product,
          category: product.category
        }))

        const response = await axios.post('/api/init', transformedData)
        if (response.status === 206) { // Partial success
          initializationError.value = response.data.message
          failedProducts.value = response.data.failedProducts
        } else {
          alert('Baza danych została zainicjalizowana pomyślnie')
          router.push('/')
        }
      } catch (error) {
        console.error('Initialization error:', error)
        initializationError.value = error.response?.data?.message || 'Wystąpił błąd podczas inicjalizacji bazy danych'
        if (error.response?.data?.error?.extensions?.failedProducts) {
          failedProducts.value = error.response.data.error.extensions.failedProducts
        }
      } finally {
        isInitializing.value = false
      }
    }

    onMounted(() => {
      fetchCategories()
    })

    return {
      parsedData,
      validationResults,
      fileError,
      isInitializing,
      isLoadingCategories,
      categoriesError,
      availableCategories,
      initializationError,
      failedProducts,
      allValid,
      canInitialize,
      handleFileSelect,
      initializeDatabase
    }
  }
}
</script>
