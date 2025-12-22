import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/acceder',
      name: 'acceder',
      component: () => import('@/views/AccederView.vue'),
      meta: { title: 'Iniciar sesión' }
    },
    {
      path: '/panel',
      name: 'panel',
      component: () => import('@/views/PanelView.vue')
    },
    {
      path: '/usuarios',
      name: 'usuarios',
      component: () => import('@/views/UsuariosView.vue')
    },
    {
      path: '/configuracion',
      name: 'configuracion',
      component: () => import('@/views/ConfiguracionView.vue')
    },
    { path: '/:pathMatch(.*)*', redirect: '/acceder'},
  ],
})

export default router
