// stores/auth.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null)
  const token = ref(null)

  const login = ({ usuario, password }) => {
    token.value = 'mi-token-falso'
    user.value = { usuario }
  }

  const logout = () => {
    token.value = null
    user.value = null
  }

  const isLoggedIn = () => !!token.value

  return { user, token, login, logout, isLoggedIn }
}, {
  persist: {
    paths: ['token', 'user'] // <--- persistir solo estas propiedades
  } 
})
