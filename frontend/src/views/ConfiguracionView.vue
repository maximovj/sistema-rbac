<template>
  <PlantillaBase>
      <template v-slot:header>
        <!-- HEADER -->
        <div class="flex justify-between items-center p-4 bg-white dark:bg-gray-800 border-b dark:border-gray-700">
          <h1 class="text-lg font-semibold">Configuración</h1>
          <Button 
              label="Atrás" 
              icon="pi pi-arrow-left" 
              class="p-button-text"
              @click="$router.back()" 
            />
        </div>
      </template>

      <!-- MAIN -->
      <!-- Tabs de Configuración -->
      <TabView>
        <!-- General -->
        <TabPanel header="General">
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">
            
            <div class="bg-white dark:bg-gray-800 p-4 rounded shadow">
              <h2 class="font-medium mb-2">Información del sitio</h2>
              <div class="space-y-2">
                <label class="block text-sm">Nombre del sitio</label>
                <input type="text" class="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"/>
              </div>
            </div>

            <div class="bg-white dark:bg-gray-800 p-4 rounded shadow">
              <h2 class="font-medium mb-2">Preferencias de usuario</h2>
              <div class="space-y-2">
                <ThemeToggle />
                <label class="flex items-center gap-2">
                  <input type="checkbox" class="form-checkbox"/>
                  Habilitar notificaciones
                </label>
                <label class="flex items-center gap-2">
                  <input type="checkbox" class="form-checkbox"/>
                  Modo oscuro por defecto
                </label>
                <div class="flex items-center gap-2 sm:gap-3">
                  <Button
                    label="Salir"
                    icon="pi pi-power-off"
                    @click="salir"
                    class="p-button-danger p-button-text hidden sm:inline-flex"
                  />
                </div>
              </div>
            </div>

          </div>
        </TabPanel>

        <!-- Seguridad -->
        <TabPanel header="Seguridad">
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">

            <div class="bg-white dark:bg-gray-800 p-4 rounded shadow">
              <h2 class="font-medium mb-2">Contraseña</h2>
              <div class="space-y-2">
                <label class="block text-sm">Contraseña actual</label>
                <input type="password" class="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"/>
                <label class="block text-sm">Nueva contraseña</label>
                <input type="password" class="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"/>
                <label class="block text-sm">Confirmar contraseña</label>
                <input type="password" class="w-full p-2 border rounded dark:bg-gray-700 dark:border-gray-600"/>
                <Button label="Actualizar contraseña" class="mt-2 p-button-primary"/>
              </div>
            </div>

            <div class="bg-white dark:bg-gray-800 p-4 rounded shadow">
              <h2 class="font-medium mb-2">Autenticación</h2>
              <div class="space-y-2">
                <label class="flex items-center gap-2">
                  <input type="checkbox" class="form-checkbox"/>
                  Autenticación en dos pasos
                </label>
                <label class="flex items-center gap-2">
                  <input type="checkbox" class="form-checkbox"/>
                  Permitir inicio de sesión desde otros dispositivos
                </label>
              </div>
            </div>

          </div>
        </TabPanel>

        <!-- Notificaciones -->
        <TabPanel header="Notificaciones">
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-6">

            <div class="bg-white dark:bg-gray-800 p-4 rounded shadow">
              <h2 class="font-medium mb-2">Email</h2>
              <div class="space-y-2">
                <label class="flex items-center gap-2">
                  <input type="checkbox" class="form-checkbox"/>
                  Recibir notificaciones de sistema
                </label>
                <label class="flex items-center gap-2">
                  <input type="checkbox" class="form-checkbox"/>
                  Recibir promociones
                </label>
              </div>
            </div>

            <div class="bg-white dark:bg-gray-800 p-4 rounded shadow">
              <h2 class="font-medium mb-2">Push</h2>
              <div class="space-y-2">
                <label class="flex items-center gap-2">
                  <input type="checkbox" class="form-checkbox"/>
                  Notificaciones en la app
                </label>
              </div>
            </div>

          </div>
        </TabPanel>

      </TabView>
  </PlantillaBase>
</template>

<script>
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/common/stores/authStore';
import { useAlertStore } from '@/common/stores/alertStore';
import autenticacionService from '@/common/services/autenticacion.service';

export default {
  data() {
    return {
      metaTitle: 'Configuración'
    }
  },
  methods: {
    async salir() {
      const alert = useAlertStore();
      const desicion = await alert.confirm({ 
      message: '¿Seguro que deseas salir?',
      buttons: {visible: ["yes", "cancel"] } 
      });

      if(desicion == 'yes') {
          const res = await autenticacionService.logout();
          console.log("ConfiguracionView.vue::res",{ res });

          const router = useRouter();
          router.push('/');
      }
    }
  },
  mounted() {
    this.$utils.setMetaTitleWelcome(this.metaTitle);
  }
}
</script>
