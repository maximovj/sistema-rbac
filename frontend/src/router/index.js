// router/index.js
import { createRouter, createWebHistory } from 'vue-router'
import { useSettingsStore } from '@/common/stores/settingsStore'
import { useAuthStore } from '@/common/stores/authStore'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/acceder',
      name: 'acceder',
      component: () => import('@/views/AccederView.vue'),
      meta: { requiresGuest: true }
    },
    {
      path: '/panel',
      name: 'panel',
      component: () => import('@/views/PanelView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/usuarios',
      name: 'usuarios',
      component: () => import('@/views/UsuariosView.vue'),
      meta: { requiresAuth: true }
    },
    {
      path: '/configuracion',
      name: 'configuracion',
      component: () => import('@/views/ConfiguracionView.vue'),
      meta: { requiresAuth: true }
    },
    { path: '/:pathMatch(.*)*', redirect: '/acceder'},
  ],
})

router.beforeEach(async (to, from, next) => {
  const auth = useAuthStore()

  // Si la autenticación no está inicializada, inicializarla
  if (!auth.inicializado) {
    try {
      await auth.init()
      auth.inicializado = true
    } catch (error) {
      auth.inicializado = true;
      console.error('Error inicializando autenticación:', error)
    }
  }

  // Si la ruta requiere autenticación y no está logueado
  if (to.meta.requiresAuth && !auth.estaAutenticado) {
    next({ name: 'acceder' })
    return
  }
  
  // Si la ruta es para invitados y ya está autenticado
  if (to.meta.requiresGuest && auth.estaAutenticado) {
    next({ name: 'panel' })
    return
  }
  
  next()
})

export default router