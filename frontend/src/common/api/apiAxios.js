// common/api/api.js
import axios from 'axios'
import { useRouter } from 'vue-router';
import { useSettingsStore } from '@/common/stores/settingsStore'
import { useAuthStore } from '@/common/stores/authStore'
import autenticacionService from '../services/autenticacion.service'

import { scopedLogger } from '../utils/loggerUtils';
const logger = scopedLogger("apiAxios.js");

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
  withCredentials: true // 🔥 NECESARIO para cookies HttpOnly
})

// ---------------- REQUEST ----------------
// 👉 Access token automático
api.interceptors.request.use(async (config) => {
  await new Promise(resolve => setTimeout(resolve, 2400));
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

          // Redirigir hacia login
          if(!acceso_token) {
            logger.error("response.error::if", {resRefresh});
            const auth = useAuthStore();
            await auth.logout();

            //const router = useRouter();
            //router.replace({ name: 'acceder' });
            window.location.href = '/acceder';
          }

          return api(original)
        }
      } catch (e) {
        logger.error("response.error", {e});
        const auth = useAuthStore();
        await auth.logout();

        // Redirigir hacia login
        //const router = useRouter();
        //router.replace({ name: 'acceder' });
        window.location.href = '/acceder';
      } finally {
        refreshing = false
      }
    }
    return Promise.reject(e);
});

export default api
