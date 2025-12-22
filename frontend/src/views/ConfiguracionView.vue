<template>
  <div class="h-screen bg-gray-50 dark:bg-gray-900 text-gray-800 dark:text-gray-200">
    <div class="flex flex-col h-full">

      <!-- HEADER -->
      <header class="sticky top-0 z-40 relative">
        <div class="flex justify-between items-center p-4 bg-white dark:bg-gray-800 border-b dark:border-gray-700">
            
          <h1 class="text-lg font-semibold">Configuración</h1>
          <Button 
              label="Atrás" 
              icon="pi pi-arrow-left" 
              class="p-button-text"
              @click="$router.back()" 
            />
        </div>
      </header>

      <!-- MAIN -->
      <main class="p-6 flex-1 overflow-auto space-y-6">

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
                  <label class="flex items-center gap-2">
                    <input type="checkbox" class="form-checkbox"/>
                    Habilitar notificaciones
                  </label>
                  <label class="flex items-center gap-2">
                    <input type="checkbox" class="form-checkbox"/>
                    Modo oscuro por defecto
                  </label>
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

      </main>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      metaTitle: 'Configuración'
    }
  },
  mounted() {
    this.$utils.setMetaTitleWelcome(this.metaTitle);
  }
}
</script>
