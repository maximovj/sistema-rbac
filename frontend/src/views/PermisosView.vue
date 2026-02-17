<template>
  <PlantillaBase>
  <div class="card">

    <!-- Header -->
    <CustomHeaderPagina
      title="Gestión de Permisos"
      subtitle="Resumen general de la información"
    >
      <template #actions>
        <Button label="Nuevo" icon="pi pi-plus" />
        <Button icon="pi pi-refresh" @click="aplicarBusqueda" outlined />
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
      </template>
    </CustomHeaderPagina>

    <!-- 🔎 Barra de filtros - Expandible -->
    <CustomCardFiltros v-model="filtersVisible" :filters="activeFilters">
      <template #title>
        <i class="pi pi-filter  text-primary" />
        <h4 class="text-sm font-medium">Filtros de búsqueda</h4>
      </template>

      <form @submit.prevent="aplicarBusqueda">
          <div v-if="filtersVisible" class="flex flex-col gap-4">
            <!-- Filtros -->
            
              <div class="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-3">
                
                <span class="p-input-icon-left w-full">
                  <span><i class="pi pi-search" /> <span class="font-bold text-sm">Acción</span></span>
                  <InputText
                    v-model="searchForm.accion"
                    placeholder="Buscar por acción..."
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
                  <span><i class="pi pi-filter" /> <span class="font-bold text-sm">Estado</span></span>
                  <Select
                    v-model="searchForm.es_activo"
                    :options="estados"
                    option-label="label"
                    option-value="value"
                    placeholder="Seleccionar estado..."
                    class="w-full"
                  />
                </span>

                <span class="w-full">
                  <span>
                    <i class="pi pi-calendar" />
                    <span class="font-bold text-sm">Fecha creación (Rango)</span>
                  </span>

                  <DatePicker
                    v-model="searchForm.fecha_creacion"
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
              <CustomFiltrosActivos :filters="activeFilters" />

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
    </CustomCardFiltros> 

    <!-- 📋 Tabla -->
    <GenericDataTable
       :value="permisos"
        :filters="filters"
        v-model:rows="dataTableRows"
        :loading="cargandoRegistros"
        :totalRecords="totalRecords"
        :first="first"
        @page="onPage"
    >
      <Column field="permiso_id" header="ID" sortable style="width: 80px" />
      <Column field="accion" header="Accion" sortable />
      <Column field="modulo" header="Módulo" sortable />
      <Column field="es_activo" header="Estado" style="width: 120px">
        <template #body="{ data }">
          <Tag :value="getEstadoLabel(data.es_activo)" :severity="getEstadoSeverity(data.es_activo)" />
        </template>
      </Column>
      <Column field="creado_en" header="Creación" style="width: 150px">
        <template #body="{ data }">
          {{ formatDate(new Date(data.creado_en)) }}
        </template>
      </Column>
      <Column field="actualizado_en" header="Actualización" style="width: 150px">
        <template #body="{ data }">
          {{ formatDate(new Date(data.actualizado_en)) }}
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
    </GenericDataTable>
    
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
import permisosService from '@/common/services/permisos.service'

import { scopedLogger } from '@/common/utils/loggerUtils'
const logger = scopedLogger('PermisosView.vue');

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
      cargandoRegistros: false,

      dataTableRows: 10,
      first: 0,
      totalRecords: 0,

      page: 0,
      
      permisos: [],

      searchForm: {
        accion: null,
        modulo: null,
        es_activo: null,
        fecha_creacion: [fechaInicio, fechaFin],
      },

      filtersVisible: false,

      filters: {
        accion: { value: null, matchMode: FilterMatchMode.CONTAINS },
        modulo: { value: null, matchMode: FilterMatchMode.CONTAINS },
        es_activo: { value: null, matchMode: FilterMatchMode.EQUALS },
        fecha_creacion: { value: null, matchMode: FilterMatchMode.DATE_BETWEEN },
      },

      estados: [
        { label: 'Activo', value: true, severity: 'success' },
        { label: 'Inactivo', value: false, severity: 'danger' }
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

  computed: {
    optParams() {
      let params = {};
      if (this.searchForm?.accion) {
        params.accion = this.searchForm?.accion
      }
      if (this.searchForm?.modulo) {
        params.modulo = this.searchForm?.modulo
      }
      if (this.searchForm?.es_activo !== null) {
        params.es_activo = this.searchForm?.es_activo
      }

      if (this.searchForm?.fecha_creacion && this.searchForm?.fecha_creacion?.length === 2) {
        params.creado_desde = this.searchForm.fecha_creacion?.[0]?.toISOString()
        params.creado_hasta = this.searchForm.fecha_creacion?.[1]?.toISOString()
      }

      return params;
    },
    
    activeFilters() {
      return [
        this.searchForm.accion && {
          icon: 'pi pi-search',
          key: 'accion',
          label: 'Acción',
          value: this.searchForm.accion,
          onRemove: () => this.searchForm.accion = null
        },
        this.searchForm.modulo && {
          icon: 'pi pi-box',
          key: 'modulo',
          label: 'Módulo',
          value: this.searchForm.modulo,
          onRemove: () => this.searchForm.modulo = null
        },
      ].filter(Boolean)
    },
},

  methods: {
    toggleMenu(event) {
      this.$refs.menu.toggle(event)
    },

    aplicarBusqueda() {
      this.page = 0
      this.first = 0

      this.filters.accion.value = this.searchForm.accion
      this.filters.modulo.value = this.searchForm.modulo
      this.filters.es_activo.value = this.searchForm.es_activo
        if (this.searchForm.fecha_creacion && this.searchForm.fecha_creacion.length === 2) {
          this.filters.fecha_creacion.value = this.searchForm.fecha_creacion
        } else {
          this.filters.fecha_creacion.value = null
        }

      this.cargarPermisos()
    },

    clearFilters() {
      this.searchForm = {
        accion: null,
        modulo: null,
        es_activo: null,
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
    },

    onPage(event) {
      this.page = event.page
      this.dataTableRows = event.rows
      this.cargarPermisos()
    },

    async cargarPermisos() {
      this.cargandoRegistros = true;  

      await permisosService.getBuscar({
          page: this.page,
          size: this.dataTableRows,
          ...this.optParams,
      })
        .then(response => {
          logger.info('cargarPermisos', 'Permisos cargados:', response.data);
          if(response.data?.exitosa) {
            const contenido = response.data?.contenido;
            const content = response.data?.contenido?.content || [];
            logger.info('cargarPermisos', `Permisos obtenidos: ${content.length}`, content);
            
            this.permisos = content;
            this.totalRecords = contenido?.totalElements || content.length;
            this.first = contenido?.page * contenido?.size;

          } else {
            logger.warn('cargarPermisos', 'Respuesta exitosa pero sin datos de permisos:', response.data);
          }
        })
        .catch(error => {
          console.error('Error al cargar permisos:', error)
        })
        .finally(() => {
          this.cargandoRegistros = false;
        });
    }
  },

  mounted() {
    this.cargarPermisos();
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