// stores/auth.js
import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  // Estado reactivo
  const user = ref(null)
  const token = ref(null)
  const tempUser = ref(null)
  const tempToken = ref(null)
  const rememberMe = ref(false) // Importante: inicializar como false

  // Acciones
  const login = ({ usuario, password, remember }) => {
    const fakeToken = 'mi-token-falso'
    const userData = { 
      usuario, 
      id: Date.now(),
      timestamp: new Date().toISOString()
    }

    // Limpiar estado anterior
    logout()

    // Configurar según "remember"
    if (remember) {
      token.value = fakeToken
      user.value = userData
      rememberMe.value = true
    } else {
      tempToken.value = fakeToken
      tempUser.value = userData
      rememberMe.value = false
    }
  }

  const logout = () => {
    token.value = null
    user.value = null
    tempToken.value = null
    tempUser.value = null
    // NO resetear rememberMe aquí para mantener la preferencia del usuario
  }

  const clearPersistedData = () => {
    // Solo limpia datos persistentes, mantiene rememberMe
    token.value = null
    user.value = null
  }

  // Computed properties (más reactivas)
  const isLoggedIn = computed(() => {
    if (rememberMe.value) {
      return !!token.value
    }
    return !!tempToken.value
  })

  const currentUser = computed(() => {
    if (rememberMe.value) {
      return user.value
    }
    return tempUser.value
  })

  // Para saber qué tipo de sesión tenemos
  const sessionType = computed(() => {
    return rememberMe.value ? 'persistent' : 'temporary'
  })

  return { 
    user, 
    token, 
    tempUser, 
    tempToken, 
    rememberMe,
    login, 
    logout, 
    clearPersistedData,
    isLoggedIn, 
    currentUser,
    sessionType
  }
}, {
  persist: {
    // Usando pick como en la documentación
    pick: ['user', 'token', 'rememberMe'],
    
    storage: sessionStorage, // por defecto: localStorage
    
    // Hook después de restaurar
    afterRestore: (ctx) => {
      console.log('Estado restaurado:', ctx.store.$id)
      
      // Si rememberMe es false pero hay datos persistentes, limpiarlos
      if (!ctx.store.rememberMe && ctx.store.token) {
        console.log('Limpiando datos persistentes porque rememberMe es false')
        ctx.store.clearPersistedData()
      }
    }
  }
})