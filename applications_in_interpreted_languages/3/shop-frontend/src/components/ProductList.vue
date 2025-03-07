<template>
  <div>
    <div class="row mb-4">
      <div class="col-md-4">
        <input
            type="text"
            class="form-control"
            v-model="searchQuery"
            placeholder="Szukaj produktów..."
        >
      </div>
      <div v-if="products.length === 0 && authStore.isEmployee" class="alert alert-info">
        <p>Baza danych produktów jest pusta.</p>
        <router-link to="/init" class="btn btn-primary">
          Zainicjalizuj bazę danych
        </router-link>
      </div>
      <div class="col-md-4">
        <select class="form-select" v-model="selectedCategory">
          <option value="">Wszystkie kategorie</option>
          <option v-for="category in categories" :key="category._id" :value="category._id">
            {{ category.name }}
          </option>
        </select>
      </div>
    </div>

    <table class="table">
      <thead>
      <tr>
        <th>Nazwa</th>
        <th>Opis</th>
        <th>Cena</th>
        <th>Akcje</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="product in filteredProducts" :key="product._id">
        <td>{{ product.name }}</td>
        <td>{{ product.description }}</td>
        <td>{{ product.price }} PLN</td>
        <td>
          <button
              class="btn btn-primary me-2"
              @click="addToCart(product)"
          >
            Dodaj do koszyka
          </button>
          <router-link
              :to="'/product/edit/' + product._id"
              class="btn btn-secondary"
          >
            Edytuj
          </router-link>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from 'axios'
import { useAuthStore } from '../stores/auth'

export default {
  name: 'ProductListView',
  setup() {
    const authStore = useAuthStore()
    return { authStore }
  },
  data() {
    return {
      products: [],
      categories: [],
      searchQuery: '',
      selectedCategory: '',
      cart: []
    }
  },
  computed: {
    filteredProducts() {
      return this.products.filter(product => {
        const matchesSearch = product.name.toLowerCase().includes(this.searchQuery.toLowerCase())
        const matchesCategory = !this.selectedCategory || product.category._id === this.selectedCategory
        return matchesSearch && matchesCategory
      })
    }
  },
  methods: {
    async fetchProducts() {
      try {
        const response = await axios.get('/api/products')
        this.products = response.data
      } catch (error) {
        console.error('Error fetching products:', error)
      }
    },
    async fetchCategories() {
      try {
        const response = await axios.get('/api/categories')
        this.categories = response.data
      } catch (error) {
        console.error('Error fetching categories:', error)
      }
    },
    addToCart(product) {
      const cart = JSON.parse(localStorage.getItem('cart') || '[]')
      const existingItem = cart.find(item => item.product._id === product._id)

      if (existingItem) {
        existingItem.quantity++
      } else {
        cart.push({ product, quantity: 1 })
      }

      localStorage.setItem('cart', JSON.stringify(cart))
      alert('Produkt dodany do koszyka')
    }
  },
  mounted() {
    this.fetchProducts()
    this.fetchCategories()
  }
}
</script>
