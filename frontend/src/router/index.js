import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/acceder',
      name: 'acceder',
      component: () => import('@/views/AccederView.vue'),
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

// Guard para rutas protegidas usando Pinia
router.beforeEach(async (to, from, next) => {
  const auth = useAuthStore()
  console.log("⚠️", "auth.token", auth.token);
  if (to.meta.requiresAuth && !auth.isLoggedIn()) {
    await window.$alert.alert({ message: 'Debe iniciar sesión para continuar' });
    next('/acceder')
  } else if (to.path === '/acceder' && auth.isLoggedIn()) {
    next('/panel')
  } else {
    next()
  }
})

export default router
