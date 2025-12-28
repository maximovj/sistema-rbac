// common/stores/ui.js
import { defineStore } from 'pinia'

export const useUiStore = defineStore('ui', {
  state: () => ({
    loading: false,
    loadingCount: 0 // muy importante para múltiples requests
  }),
  actions: {
    showLoading() {
      this.loadingCount++
      this.loading = true
    },
    hideLoading() {
      this.loadingCount--
      if (this.loadingCount <= 0) {
        this.loading = false
        this.loadingCount = 0
      }
    }
  }
})
