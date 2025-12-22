// stores/auth.js
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const user = ref(null) // info del usuario o null si no está logueado
  const token = ref(null)

  const login = ({ email, password }) => {
    // Simulación de login
    token.value = 'mi-token-falso'
    user.value = { email } // puedes agregar más info
  }

  const logout = () => {
    token.value = null
    user.value = null
  }

  const isLoggedIn = () => !!token.value

  return { user, token, login, logout, isLoggedIn }
})
