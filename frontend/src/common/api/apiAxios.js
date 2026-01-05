// common/api/api.js
import axios from 'axios'
import { useRouter } from 'vue-router';
import { useSettingsStore } from '@/common/stores/settingsStore'
import { useAuthStore } from '@/common/stores/authStore'
import autenticacionService from '../services/autenticacion.service'

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
  withCredentials: true // 🔥 NECESARIO para cookies HttpOnly
})

// ---------------- REQUEST ----------------
// 👉 Access token automático
api.interceptors.request.use((config) => {
  const auth = useAuthStore()
  if (auth.acceso_token) {
    config.headers.Authorization = `Bearer ${auth.acceso_token}`
  }
  return config
})

// 👉 Refresh automático si expira
let refreshing = false
let config_retry = false;
let queue = []

// ---------------- RESPONSE ----------------
api.interceptors.response.use(
  success => {
    console.log("api.interceptors.response.use", {success, config: success.config});
    return Promise.resolve(success);
  }, 
  async e => {
    const { config, response } = e;

    if (response?.status === 401 && !config_retry) {
      if (refreshing) {
        return new Promise(resolve => {
          queue.push(token => {
            config.headers.Authorization = `Bearer ${token}`
            resolve(api(config))
          })
        })
      }

      config_retry = true;
      refreshing = true;

      try {
        const resRefresh = await autenticacionService.refresh();
        
        if(resRefresh?.data) {
          const resRefreshData = resRefresh?.data;
          const acceso_token = resRefreshData?.contenido?.acceso_token;

          const auth = useAuthStore();
          auth.acceso_token = acceso_token;
          queue.forEach(cb => cb(acceso_token))
          queue = []

          return api(original)
        }
      } catch (e) {
        console.log("apiAxios.js::e", {e});
        const auth = useAuthStore();
        await auth.logout();

        const router = useRouter();
        router.push('/acceder');
      } finally {
        refreshing = false
      }
    }

    console.log("api.interceptors.response.use", {e, config, response});
    return Promise.reject(e);
});

export default api
