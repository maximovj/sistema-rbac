<template>
  <div class="h-screen bg-gray-50 dark:bg-gray-900 text-gray-800 dark:text-gray-200">
    <div class="flex flex-col h-full">

      <!-- HEADER -->
      <header class="sticky top-0 z-40 relative">

        <!-- Toolbar principal -->
        <div class="flex justify-between items-center p-4 bg-white dark:bg-gray-800 border-b dark:border-gray-700">

          <!-- Izquierda -->
          <div class="flex items-center gap-2 sm:gap-3">
            <Button
              icon="pi pi-th-large"
              class="p-button-text"
              @click="showIconToolbar = !showIconToolbar"
            />

            <!-- Oculto en mobile -->
            <div class="hidden sm:flex items-center gap-2">
              <Button icon="pi pi-search" class="p-button-text" />
              <input
                type="text"
                placeholder="Buscar..."
                class="px-3 py-1 border rounded dark:bg-gray-700 dark:border-gray-600 focus:outline-none"
              />
            </div>
          </div>

          <!-- Derecha -->
          <div class="flex items-center gap-2 sm:gap-3">
            <ThemeToggle />
            <Button icon="pi pi-bell" class="p-button-text" />
            <Button
              label="Salir"
              icon="pi pi-power-off"
              class="p-button-danger p-button-text hidden sm:inline-flex"
            />
          </div>
        </div>

        <!-- Toolbar flotante -->
        <transition name="fade-slide">
          <div
            v-if="showIconToolbar"
            class="absolute left-0 right-0 top-full
                   bg-white dark:bg-gray-800
                   border-b dark:border-gray-700
                   shadow-lg z-50"
          >
            <div class="flex justify-around sm:justify-start sm:gap-3 px-4 py-2">

              <RouterLink to="/panel">
                <Button
                  icon="pi pi-home"
                  class="p-button-text p-button-rounded"
                  :class="isActive('/panel')"
                  v-tooltip.bottom="'Dashboard'"
                />
              </RouterLink>

              <RouterLink to="/usuarios">
                <Button
                  icon="pi pi-users"
                  class="p-button-text p-button-rounded"
                  :class="isActive('/usuarios')"
                  v-tooltip.bottom="'Usuarios'"
                />
              </RouterLink>
              
              <RouterLink to="/configuracion">
                <Button
                  icon="pi pi-cog"
                  class="p-button-text p-button-rounded"
                  :class="isActive('/configuracion')"
                  v-tooltip.bottom="'Configuración'"
                />
              </RouterLink>

            </div>
          </div>
        </transition>

      </header>

      <!-- MAIN -->
      <main class="p-6 flex-1 overflow-auto space-y-6">

        <!-- Cards -->
        <div class="grid grid-cols-1 sm:grid-cols-3 gap-6">
          <div class="bg-white dark:bg-gray-800 p-4 rounded">
            <p class="text-gray-500 text-sm">Usuarios</p>
            <p class="text-xl font-semibold">1,245</p>
          </div>
          <div class="bg-white dark:bg-gray-800 p-4 rounded">
            <p class="text-gray-500 text-sm">Ventas</p>
            <p class="text-xl font-semibold">$34,210</p>
          </div>
          <div class="bg-white dark:bg-gray-800 p-4 rounded">
            <p class="text-gray-500 text-sm">Productos</p>
            <p class="text-xl font-semibold">320</p>
          </div>
        </div>

        <!-- Tabla -->
        <div class="bg-white dark:bg-gray-800 p-4 rounded">
          <h2 class="text-md font-medium mb-2">Usuarios recientes</h2>

          <DataTable :value="users" class="p-datatable-sm p-datatable-striped
         bg-gray-800 text-gray-200">
            <Column field="id" header="ID" />
            <Column field="name" header="Nombre" />
            <Column field="email" header="Email" />
            <Column field="role" header="Rol" />
            <Column header="Acciones">
              <template #body>
                <Button icon="pi pi-pencil" class="p-button-text p-button-info mr-2" />
                <Button icon="pi pi-trash" class="p-button-text p-button-danger" />
              </template>
            </Column>
          </DataTable>
        </div>

      </main>
    </div>
  </div>
</template>

<script>
export default {
  // Definir propiedades
  data() {
    return {
      metaTitle: 'Usuarios',
      showIconToolbar: false,
      users: [
        { id: 1, name: 'Juan Pérez', email: 'juan@example.com', role: 'Admin' },
        { id: 2, name: 'María López', email: 'maria@example.com', role: 'Editor' },
        { id: 3, name: 'Carlos Sánchez', email: 'carlos@example.com', role: 'Usuario' },
      ]
    }
  },

  // Definir propiedades computadas
  computed: {
    route() {
      return this.$route
    }
  },

  // Definir métodos
  methods: {
    isActive(path) {
      return this.route.path.startsWith(path) ? 'p-button-primary' : ''
    }
  },

  // Montaje
  mounted() {
    this.$utils.setMetaTitleWelcome(this.metaTitle);
  }
}
</script>
