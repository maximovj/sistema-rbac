<template>
  <PlantillaBase>
  <div class="card">
    <!-- Header -->
    <header class="flex items-center justify-between mb-4">
      <!-- Título y subtítulo -->
      <div class="space-y-1">
          <h1 class="text-2xl font-semibold text-gray-800">
          Gestión de Permisos
          </h1>
          <p class="text-sm text-gray-500">
          Resumen general de la información
          </p>
      </div>

      <!-- Botonera + menú popup -->
      <div class="flex items-center gap-2">

          <!-- Botón acción -->
          <Button
          label="Nuevo"
          icon="pi pi-plus"
          class="p-button-sm"
          />

          <!-- Botón icono -->
          <Button
          icon="pi pi-refresh"
          class="p-button-sm p-button-outlined"
          aria-label="Actualizar"
          />

          <!-- Botón menú -->
          <Button
          icon="pi pi-ellipsis-v"
          class="p-button-sm p-button-text"
          @click="toggleMenu"
          aria-haspopup="true"
          aria-controls="menu_header"
          />

          <!-- Popup Menu -->
          <Menu
          id="menu_header"
          ref="menu"
          :model="menuItems"
          popup
          />
      </div>
    </header>

    <!-- 🔎 Barra de filtros - Expandible -->
    <Card class="mb-4">
      <template #title>
        <div 
          class="flex items-center justify-between cursor-pointer"
          @click="filtersVisible = !filtersVisible"
        >
          <div class="flex items-center gap-2">
            <i 
              class="pi" 
              :class="[filtersVisible ? 'pi-chevron-down' : 'pi-chevron-right']"
            ></i>
            <i class="pi pi-filter text-primary"></i>
            <h4 class="text-sm font-medium">Filtros de búsqueda</h4>
          </div>
          
          <!-- Resumen visual de filtros activos -->
          <div class="flex items-center gap-2">
            <div class="hidden md:flex gap-1">
              <Tag 
                v-if="searchForm.nombre" 
                :value="'Nombre: ' + searchForm.nombre" 
                severity="info" 
                rounded
                class="text-xs"
              />
              <Tag 
                v-if="searchForm.modulo" 
                :value="'Módulo: ' + searchForm.modulo" 
                severity="info" 
                rounded
                class="text-xs"
              />
              <Tag 
                v-if="searchForm.estado" 
                :value="'Estado: ' + getEstadoLabel(searchForm.estado)" 
                :severity="getEstadoSeverity(searchForm.estado)" 
                rounded
                class="text-xs"
              />
              <Tag 
                v-if="searchForm.fechaCreacion && searchForm.fechaCreacion.length > 0" 
                :value="'Fecha de creación: ' + formatDate(searchForm.fechaCreacion[0]) + ' - ' + formatDate(searchForm.fechaCreacion[1])" 
                severity="info" 
                rounded
                class="text-xs"
              />
            </div>
            
          </div>
        </div>
      </template>

      <template #content>
        <Transition name="fade">
          <form @submit.prevent="aplicarBusqueda">
          <div v-if="filtersVisible" class="flex flex-col gap-4">
            <!-- Filtros -->
            
              <div class="grid grid-cols-1 md:grid-cols-3 gap-3">
                
                <span class="p-input-icon-left w-full">
                  <span><i class="pi pi-search" /> <span class="font-bold text-sm">Nombre</span></span>
                  <InputText
                    v-model="searchForm.nombre"
                    placeholder="Buscar por nombre..."
                    class="w-full"
                  />
                </span>

                <span class="p-input-icon-left w-full">
                  <span><i class="pi pi-box" /> <span class="font-bold text-sm">Módulo</span></span>
                  <InputText
                    v-model="searchForm.modulo"
                    placeholder="Buscar por módulo..."
                    class="w-full"
                  />
                </span>

                <span class="p-input-icon-left w-full">
                  <span><i class="pi pi-tag" /> <span class="font-bold text-sm">Estado</span></span>
                  <Select
                    v-model="searchForm.estado"
                    :options="estados"
                    optionLabel="label"
                    optionValue="value"
                    placeholder="Seleccionar estado..."
                    showClear
                    class="w-full"
                  >
                    <!-- Templates existentes -->
                  </Select>
                </span>

                <span class="w-full">
                  <span>
                    <i class="pi pi-calendar" />
                    <span class="font-bold text-sm">Fecha creación (Rango)</span>
                  </span>

                  <DatePicker
                    v-model="searchForm.fechaCreacion"
                    selectionMode="range"
                    dateFormat="yy-mm-dd"
                    showIcon
                    class="w-full"
                  />
                </span>

              </div>
            

            <!-- Acciones -->
            <div class="flex justify-between items-center">
              <Button
                label="Limpiar todo"
                icon="pi pi-filter-slash"
                severity="secondary"
                text
                size="small"
                @click="clearFilters"
              />

              <!-- Badges de filtros activos (interactivos) -->
              <div class="flex gap-1 flex-wrap">
                <Chip
                  v-if="searchForm.nombre"
                  :label="searchForm.nombre"
                  icon="pi pi-search"
                  removable
                  @remove="searchForm.nombre = ''"
                />
                <Chip
                  v-if="searchForm.modulo"
                  :label="searchForm.modulo"
                  icon="pi pi-box"
                  removable
                  @remove="searchForm.modulo = ''"
                />
                <Chip
                  v-if="searchForm.estado"
                  :label="getEstadoLabel(searchForm.estado)"
                  icon="pi pi-tag"
                  removable
                  @remove="searchForm.estado = null"
                />
              </div>

              <div class="flex gap-2">
                <Button
                  label="Buscar"
                  icon="pi pi-search"
                  severity="secondary"
                  size="small"
                  type="submit"
                />
              </div>
            </div>
          </div>
        </form>
        </Transition>
      </template>
    </Card> 

    <!-- 📋 Tabla -->
    <DataTable
      v-model:filters="filters"
      :value="permisos"
      paginator
      :rows="rowsDataTable"
      :rowsPerPageOptions="[2, 10, 50, 100]"
      removableSort
      stripedRows
      showGridlines
      paginatorTemplate="CurrentPageReport FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink RowsPerPageDropdown"
      currentPageReportTemplate="Mostrando {first} / {last} registros de {totalRecords} registros"
    >
      <Column field="id" header="ID" sortable style="width: 80px" />
      <Column field="nombre" header="Nombre" sortable />
      <Column field="modulo" header="Módulo" sortable />
      <Column field="estado" header="Estado" sortable>
        <template #body="{ data }">
          <Tag
            :value="data.estado"
            :severity="getEstadoSeverity(data.estado)"
          />
        </template>
      </Column>
      <Column field="fechaCreacion" header="Fecha Creación" sortable>
        <template #body="{ data }">
          {{ formatDate(data.fechaCreacion) }}
        </template>
      </Column>

      <Column header="Acciones" style="width: 180px">
        <template #body="{ data }">
          <div class="flex gap-2">
            <!-- Editar -->
            <Button
              icon="pi pi-pencil"
              severity="warning"
              rounded
              text
              @click="editarPermiso(data)"
            />

            <!-- Eliminar -->
            <Button
              icon="pi pi-trash"
              severity="danger"
              rounded
              text
              @click="eliminarPermiso(data)"
            />
          </div>
        </template>
      </Column>

<template #paginatorcontainer="{ first, last, page, pageCount, prevPageCallback, nextPageCallback, rowChangeCallback, totalRecords }">
  <div class="flex items-center justify-between w-full">
    <!-- Dropdown con etiqueta visible -->
    <div class="flex items-center gap-3">
      <span class="text-sm font-medium text-gray-700">Filas por página</span>
      <Dropdown 
        :options="[2, 10, 50, 100]" 
        v-model="rowsDataTable" 
        @change="rowChangeCallback(rowsDataTable)"
        class="w-24"
        style="height: 40px;"
      >
        <template #value="slotProps">
          <span class="text-sm font-medium">{{ slotProps.value }}</span>
        </template>
      </Dropdown>
    </div>
    
    <!-- Barra de progreso con etiquetas -->
    <div class="flex-1 max-w-md mx-8">
      <div class="flex items-center justify-between mb-1">
        <span class="text-xs font-medium text-gray-500">PROGRESO DE PÁGINAS</span>
        <span class="text-sm font-semibold text-blue-600">{{ Math.round(((page + 1) / pageCount) * 100) }}%</span>
      </div>
      <div class="h-2 bg-gray-100 rounded-full overflow-hidden">
        <div 
          class="h-full bg-blue-600 transition-all duration-300"
          :style="{ width: `${((page + 1) / pageCount) * 100}%` }"
        ></div>
      </div>
      <div class="flex justify-between mt-1 text-xs text-gray-400">
        <span>Página 1</span>
        <span>Página {{ pageCount }}</span>
      </div>
    </div>
    
    <!-- Navegación completa -->
    <div class="flex items-center gap-4">
      <!-- Información de registros -->
      <div class="text-sm text-gray-600 bg-gray-50 px-4 py-2 rounded-lg">
        <span class="font-semibold text-gray-900">{{ first }}</span> 
        <span class="text-gray-400">—</span> 
        <span class="font-semibold text-gray-900">{{ last }}</span>
        <span class="text-gray-400 mx-2">de</span>
        <span class="font-semibold text-gray-900">{{ totalRecords }}</span>
      </div>
      
      <!-- Controles de página -->
      <div class="flex items-center gap-2">
        <Button 
          icon="pi pi-chevron-left" 
          severity="secondary" 
          outlined
          @click="prevPageCallback" 
          :disabled="page === 0"
          class="w-10 h-10 border-gray-300"
        />
        <span class="text-sm text-gray-700 min-w-[90px] text-center">
          Página <span class="font-semibold text-gray-900">{{ page + 1 }}</span> de {{ pageCount }}
        </span>
        <Button 
          icon="pi pi-chevron-right" 
          severity="secondary" 
          outlined
          @click="nextPageCallback" 
          :disabled="page === pageCount - 1"
          class="w-10 h-10 border-gray-300"
        />
      </div>
    </div>
  </div>
</template>


      
    </DataTable>
  </div>
  </PlantillaBase>
</template>


<script>
import { FilterMatchMode } from '@primevue/core/api'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import Select from 'primevue/select'
import Tag from 'primevue/tag'
import { now } from '@vueuse/core'

export default {
  name: 'GestionPermisos',

  components: {
    DataTable,
    Column,
    InputText,
    Button,
    Select,
    Tag
  },

  data() {
    const fechaFin = new Date(now());
    const fechaInicio = new Date();
    fechaInicio.setFullYear(fechaFin.getFullYear() - 10);

    return {
      rowsDataTable: 2,
      permisos: [
        { id: 1, nombre: 'Ver Usuarios', modulo: 'Usuarios', estado: 'Activo', fechaCreacion: new Date('2024-01-10') },
        { id: 2, nombre: 'Crear Usuario', modulo: 'Usuarios', estado: 'Activo', fechaCreacion: new Date('2024-01-15') },
        { id: 3, nombre: 'Eliminar Usuario', modulo: 'Usuarios', estado: 'Inactivo', fechaCreacion: new Date('2024-02-01') },
        { id: 4, nombre: 'Ver Reportes', modulo: 'Reportes', estado: 'Activo', fechaCreacion: new Date('2024-02-10') },
        { id: 5, nombre: 'Exportar Reportes', modulo: 'Reportes', estado: 'Inactivo', fechaCreacion: new Date('2024-03-05') },
        { id: 6, nombre: 'Editar Perfil', modulo: 'Perfil', estado: 'Activo', fechaCreacion: new Date('2024-03-15') },
        { id: 7, nombre: 'Cambiar Contraseña', modulo: 'Perfil', estado: 'Activo', fechaCreacion: new Date('2024-04-01') },
        { id: 8, nombre: 'Acceso Dashboard', modulo: 'Dashboard', estado: 'Activo', fechaCreacion: new Date('2024-04-10') }
      ],

      searchForm: {
        nombre: null,
        modulo: null,
        estado: null,
        fechaCreacion: [fechaInicio, fechaFin]
      },

      filtersVisible: true,

      filters: {
        nombre: { value: null, matchMode: FilterMatchMode.CONTAINS },
        modulo: { value: null, matchMode: FilterMatchMode.CONTAINS },
        estado: { value: null, matchMode: FilterMatchMode.EQUALS },
        fechaCreacion: { 
          value: [fechaInicio, fechaFin],
          matchMode: FilterMatchMode.BETWEEN 
        }
      },

      estados: [
        { label: 'Activo', value: 'Activo', severity: 'success' },
        { label: 'Inactivo', value: 'Inactivo', severity: 'danger' }
      ],

      menu: null,

      menuItems: [
        { label: 'Editar', icon: 'pi pi-pencil' },
        { label: 'Eliminar', icon: 'pi pi-trash' },
        { separator: true },
        { label: 'Exportar', icon: 'pi pi-upload' }
      ]
    }
  },

  methods: {
    toggleMenu(event) {
      this.$refs.menu.toggle(event)
    },

    aplicarBusqueda() {
      this.filters.nombre.value = this.searchForm.nombre
      this.filters.modulo.value = this.searchForm.modulo
      this.filters.estado.value = this.searchForm.estado
      this.filters.fechaCreacion.value = this.searchForm.fechaCreacion
    },

    clearFilters() {
      this.searchForm = {
        nombre: null,
        modulo: null,
        estado: null
      }
      this.aplicarBusqueda()
    },

    getEstadoSeverity(value) {
      const estado = this.estados.find(e => e.value === value)
      return estado ? estado.severity : null
    },

    getEstadoLabel(value) {
      const estado = this.estados.find(e => e.value === value)
      return estado ? estado.label : value
    },

    editarPermiso(permiso) {
      console.log('Editar:', permiso)
    },

    eliminarPermiso(permiso) {
      console.log('Eliminar:', permiso)
    },

    crearPermiso() {
      console.log('Crear nuevo permiso')
    },

    formatDate(date) {
      if (!date) return ''
      return date.toLocaleDateString()
    }
  }
}
</script>

<style scoped>
.card {
  padding: 1.5rem;
}
.fade-enter-active, .fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from, .fade-leave-to {
  opacity: 0;
}
.p-chip {
  font-size: 0.75rem;
}
</style>