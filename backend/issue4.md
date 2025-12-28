¡Perfecto! 😎 Vamos a integrar tu **backend con JWT + refresh token** usando **Vue 3 + Axios + Pinia**, con refresh token almacenado en **HttpOnly cookie** para mayor seguridad. Te voy a dar un flujo completo y seguro.

---

# 1️⃣ Configuración de Axios

Creamos un archivo `api.js`:

```javascript
import axios from 'axios';
import { useAuthStore } from './stores/auth';

const api = axios.create({
  baseURL: 'http://localhost:8667/api/v1',
  withCredentials: true // 🔑 para enviar cookies HttpOnly automáticamente
});

// Agregar Access Token a cada request
api.interceptors.request.use(config => {
  const authStore = useAuthStore();
  if (authStore.accessToken) {
    config.headers['Authorization'] = `Bearer ${authStore.accessToken}`;
  }
  return config;
}, error => Promise.reject(error));

// Renovación automática de Access Token
api.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;
    const authStore = useAuthStore();

    if (error.response && error.response.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;
      try {
        // Backend usará refresh token desde HttpOnly cookie
        const res = await axios.post('http://localhost:8667/api/v1/autenticacion/refresh', {}, { withCredentials: true });
        const newAccessToken = res.data.accessToken;

        authStore.setAccessToken(newAccessToken);

        originalRequest.headers['Authorization'] = `Bearer ${newAccessToken}`;
        return axios(originalRequest);
      } catch (err) {
        // Refresh token inválido → logout
        authStore.logout();
        window.location.href = '/login';
      }
    }
    return Promise.reject(error);
  }
);

export default api;
```

---

# 2️⃣ Pinia Store para autenticación

```javascript
// stores/auth.js
import { defineStore } from 'pinia';
import api from '../api';

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null,
    accessToken: null
  }),
  actions: {
    async login(usuario, contrasena) {
      try {
        const res = await api.post('/autenticacion/login', { usuario, contrasena });
        this.accessToken = res.data.accessToken;
        this.user = usuario;
        return true;
      } catch (error) {
        console.error('Error login', error);
        return false;
      }
    },
    logout() {
      this.user = null;
      this.accessToken = null;
      api.post('/autenticacion/logout').catch(() => {});
    },
    setAccessToken(token) {
      this.accessToken = token;
    }
  }
});
```

---

# 3️⃣ Login en Vue Component

```vue
<template>
  <div>
    <input v-model="usuario" placeholder="Usuario">
    <input type="password" v-model="contrasena" placeholder="Contraseña">
    <button @click="loginUser">Login</button>
  </div>
</template>

<script>
import { useAuthStore } from '../stores/auth';

export default {
  data() {
    return { usuario: '', contrasena: '' };
  },
  setup() {
    const authStore = useAuthStore();
    return { authStore };
  },
  methods: {
    async loginUser() {
      const success = await this.authStore.login(this.usuario, this.contrasena);
      if (success) {
        this.$router.push('/dashboard');
      } else {
        alert('Credenciales inválidas');
      }
    }
  }
};
</script>
```

---

# 4️⃣ Logout

```javascript
const authStore = useAuthStore();
authStore.logout();
```

* El backend revoca el refresh token
* Pinia limpia `accessToken` y `user`
* Frontend redirige a login

---

# 5️⃣ Peticiones protegidas

```javascript
import api from '../api';

async function getDatosProtegidos() {
  try {
    const res = await api.get('/protegido');
    console.log(res.data);
  } catch (e) {
    console.error(e);
  }
}
```

* Si el access token expira → Axios interceptor renueva automáticamente usando refresh token (HttpOnly cookie)

---

# 6️⃣ Flujo seguro resumido

1. Login → Backend devuelve:

   * `accessToken` en respuesta JSON
   * `refreshToken` en **HttpOnly cookie**

2. Cada request → Axios envía:

   * `Authorization: Bearer accessToken`
   * Cookie HttpOnly automática

3. Access token expira → Axios interceptor:

   * Llama `/refresh` → backend valida cookie → genera nuevo access token
   * Guarda nuevo token en Pinia → reintenta la request

4. Logout → backend revoca refresh token → frontend limpia Pinia

---

Si quieres, puedo hacer un **diagrama visual completo del flujo Vue + Pinia + Axios + HttpOnly**, mostrando **login → request → refresh → logout**, para que tengas **guía completa lista para implementar**.

¿Quieres que haga ese diagrama?
