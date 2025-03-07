<template>
  <div>
    <h2>Edycja produktu</h2>

    <form @submit.prevent="saveProduct" class="row g-3">
      <div class="col-md-6">
        <label class="form-label">Nazwa</label>
        <input
            type="text"
            class="form-control"
            v-model="product.name"
            :class="{'is-invalid': errors.name}"
        >
        <div class="invalid-feedback">{{ errors.name }}</div>
      </div>

      <div class="col-md-6">
        <label class="form-label">Kategoria</label>
        <select
            class="form-select"
            v-model="product.category"
            :class="{'is-invalid': errors.category}"
        >
          <option v-for="category in categories" :key="category._id" :value="category._id">
            {{ category.name }}
          </option>
        </select>
        <div class="invalid-feedback">{{ errors.category }}</div>
      </div>

      <div class="col-md-6">
        <label class="form-label">Cena</label>
        <input
            type="number"
            step="0.01"
            class="form-control"
            v-model="product.price"
            :class="{'is-invalid': errors.price}"
        >
        <div class="invalid-feedback">{{ errors.price }}</div>
      </div>

      <div class="col-md-6">
        <label class="form-label">Waga</label>
        <input
            type="number"
            step="0.01"
            class="form-control"
            v-model="product.weight"
            :class="{'is-invalid': errors.weight}"
        >
        <div class="invalid-feedback">{{ errors.weight }}</div>
      </div>

      <div class="col-12">
        <label class="form-label">Opis</label>
        <div class="input-group">
          <textarea
              class="form-control"
              v-model="product.description"
              rows="3"
              :class="{'is-invalid': errors.description}"
          ></textarea>
          <button
              type="button"
              class="btn btn-secondary"
              @click="optimizeDescription"
              :disabled="isOptimizing"
          >
            {{ isOptimizing ? 'Optymalizacja...' : 'Optymalizuj opis' }}
          </button>
          <div class="invalid-feedback">{{ errors.description }}</div>
        </div>
      </div>

      <div class="modal fade" id="seoPreviewModal" tabindex="-1" ref="previewModal">
        <div class="modal-dialog modal-lg">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Podgląd zoptymalizowanego opisu SEO</h5>
              <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
              <div v-if="optimizedDescription" class="mb-3">
                <div v-html="optimizedDescription"></div>
              </div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Anuluj</button>
              <button type="button" class="btn btn-primary" @click="applyOptimizedDescription">
                Zastosuj opis
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="col-12">
        <button type="submit" class="btn btn-primary me-2">Zapisz zmiany</button>
        <router-link to="/" class="btn btn-secondary">Anuluj</router-link>
      </div>
    </form>
  </div>
</template>

<script>
import axios from 'axios';
import { Modal } from 'bootstrap';

export default {
  name: 'ProductEditForm',
  data() {
    return {
      product: {
        name: '',
        description: '',
        price: 0,
        weight: 0,
        category: ''
      },
      categories: [],
      errors: {},
      isOptimizing: false,
      optimizedDescription: null,
      previewModal: null
    }
  },
  methods: {
    async fetchProduct() {
      try {
        const response = await axios.get(`/api/products/${this.$route.params.id}`);
        this.product = {
          ...response.data,
          category: response.data.category._id
        };
      } catch (error) {
        console.error('Error fetching product:', error);
        alert('Nie udało się pobrać danych produktu');
      }
    },
    async fetchCategories() {
      try {
        const response = await axios.get('/api/categories');
        this.categories = response.data;
      } catch (error) {
        console.error('Error fetching categories:', error);
      }
    },
    async saveProduct() {
      try {
        await axios.put(`/api/products/${this.$route.params.id}`, this.product);
        this.$router.push('/');
      } catch (error) {
        if (error.response && error.response.data.errors) {
          this.errors = error.response.data.errors.reduce((acc, error) => {
            const field = error.path[0];
            acc[field] = error.message;
            return acc;
          }, {});
        } else {
          alert('Wystąpił błąd podczas zapisywania zmian');
        }
      }
    },
    async optimizeDescription() {
      this.isOptimizing = true;
      try {
        const response = await axios.get(`/api/products/${this.$route.params.id}/seo-description`);
        this.optimizedDescription = response.data;
        this.previewModal.show();
      } catch (error) {
        alert('Nie udało się wygenerować zoptymalizowanego opisu');
        console.error('Error optimizing description:', error);
      } finally {
        this.isOptimizing = false;
      }
    },
    applyOptimizedDescription() {
      const tempDiv = document.createElement('div');
      tempDiv.innerHTML = this.optimizedDescription;

      const mainDescription = tempDiv.querySelector('article p, section p, .description, #description');
      if (mainDescription) {
        this.product.description = mainDescription.textContent.trim();
      } else {
        const descriptions = Array.from(tempDiv.getElementsByTagName('p'))
            .map(p => p.textContent.trim())
            .filter(text => text.length > 0);
        this.product.description = descriptions.join('\n\n');
      }

      this.previewModal.hide();
    }
  },
  mounted() {
    this.fetchProduct();
    this.fetchCategories();
    this.previewModal = new Modal(this.$refs.previewModal);
  }
}
</script>
