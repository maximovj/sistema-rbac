<template>
  <div class="h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900">
    <div class="w-full max-w-md bg-white dark:bg-gray-800 p-8 rounded-lg shadow-lg">
      <!-- Formulario -->
      <form @submit.prevent="login" class="space-y-4">
        
        <!-- Usuario -->
        <div>
          <label for="usuario" class="font-semibold block text-gray-700 dark:text-gray-300 mb-1">Usuario</label>
          <input
            id="usuario"
            v-model="usuario"
            type="text"
            placeholder="demo"
            class="w-full px-4 py-2 border rounded dark:bg-gray-700 dark:border-gray-600 dark:text-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <!-- Contraseña -->
        <div>
          <label for="contrasena" class="font-semibold block text-gray-700 dark:text-gray-300 mb-1">Contraseña</label>
          <input
            id="contrasena"
            v-model="contrasena"
            type="password"
            placeholder="********"
            class="w-full px-4 py-2 border rounded dark:bg-gray-700 dark:border-gray-600 dark:text-gray-200 focus:outline-none focus:ring-2 focus:ring-blue-500"
            required
          />
        </div>

        <!-- Recordar y Olvidé -->
        <div class="flex justify-between items-center text-sm text-gray-600 dark:text-gray-400">
          <label class="flex items-center gap-2">
            <input type="checkbox" v-model="remember" class="accent-blue-500" />
            Recordarme
          </label>
          <a href="#" class="hover:underline">¿Olvidaste tu contraseña?</a>
        </div>

        <!-- Botón de login -->
        <Button
          type="submit"
          :loading="cargando"
          label="Ingresar"
          icon="pi pi-sign-in"
          class="w-full p-button-primary mt-4"
        />
      </form>

      <!-- Opciones de registro -->
      <p class="text-center text-gray-600 dark:text-gray-400 mt-6">
        ¿No tienes cuenta?
        <a href="#" class="text-blue-500 hover:underline">Regístrate aquí</a>
      </p>
    </div>
  </div>
</template>

<script>
import { useAuthStore } from '@stores/auth'

export default {
  data() {
    return {
      cargando: false,
      metaTitle: 'Inciar Sesión',
      usuario: '',
      contrasena: '',
      remember: false,
    };
  },

  methods: {
    async login() {
      this.cargando = true;

      const data = await this.$utils.apiExecutarV1({
        tryFetch:{ 
          data: { size: 10, page: 0 }
        },
        finallyFn: () => {
          this.cargando = false;
        },
      });
      console.log("⚠️", "ApiExcete", "data", data);

      // Aquí pondrías la lógica de autenticación
      console.log('Usuario:', this.usuario);
      console.log('Contraseña:', this.contrasena);
      console.log('Remember:', this.remember);

      const authStore = useAuthStore();
      authStore.login(this.usuario, this.contrasena);
    }
  },

  mounted() {
    this.$utils.setMetaTitle(this.metaTitle);
  }
};
</script>

<style>
/* Animación opcional al aparecer el login */
div[data-v-app] {
  transition: background-color 0.3s ease;
}
</style>
