<template>
    <PlantillaBase>
        <div class="card">
            <CustomHeaderPagina title="Gestión de Usuarios" subtitle="Lista de usuarios disponibles">
                <template #actions>
                    <CrearUsuario />
                    <Button icon="pi pi-refresh" @click="aplicarBusqueda" outlined />
                    <!-- Botón menú -->
                    <Button icon="pi pi-ellipsis-v" class="p-button-sm p-button-text" @click="toggleMenu"
                        aria-haspopup="true" aria-controls="menu_header" />

                    <!-- Popup Menu -->
                    <Menu id="menu_header" ref="menu" :model="menuItems" popup />
                </template>
            </CustomHeaderPagina>

            <!-- 🔎 Barra de filtros - Expandible -->
            <FiltrosUsuarios v-model="searchForm" :loading="cargandoRegistros" :visible="filtersVisible"
                :estados="estados" :active-filters="activeFilters" @update:visible="filtersVisible = $event"
                @search="aplicarBusqueda" @clear="clearFilters" />

            <!-- 📋 Tabla -->
            <TablaUsuarios :value="usuarios" :filters="filters" :rows="dataTableRows" :loading="cargandoRegistros"
                :total-records="totalRecords" :first="first" @page="onPage" @actualizar="mtdActualizarUsuario"
                @delete="mtdEliminarUsuario" />

        </div>
    </PlantillaBase>
</template>

<script>
import { FilterMatchMode } from '@primevue/core/api'

import { now } from '@vueuse/core'
import usuariosService from '@/common/services/usuarios.service'
import { useExcelExport } from '@/common/composables/useExcelExport'
import { useAlertStore } from '@/common/stores/alertStore'

import { scopedLogger } from '@/common/utils/loggerUtils'
const logger = scopedLogger('UsuariosView.vue');

export default {
    components: { },

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

            usuarios: [],

            searchForm: {
                usuario: null,
                correo: null,
                es_activo: null,
                fecha_creacion: [fechaInicio, fechaFin],
            },

            filtersVisible: true,

            filters: {
                usuario: { value: null, matchMode: FilterMatchMode.CONTAINS },
                correo: { value: null, matchMode: FilterMatchMode.CONTAINS },
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
                { label: 'Exportar', icon: 'pi pi-upload', command: () => this.exportarExcel() }
            ]
        }
    },

    computed: {
        optParams() {
            let params = {};
            if (this.searchForm?.usuario) {
                params.usuario = this.searchForm?.usuario
            }
            if (this.searchForm?.correo) {
                params.correo = this.searchForm?.correo
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
                this.searchForm.usuario && {
                    icon: 'pi pi-search',
                    key: 'nombre',
                    label: 'Nombre',
                    value: this.searchForm.usuario,
                    onRemove: () => this.searchForm.usuario = null
                },
                this.searchForm.correo && {
                    icon: 'pi pi-box',
                    key: 'correo',
                    label: 'Correo',
                    value: this.searchForm.correo,
                    onRemove: () => this.searchForm.correo = null
                },
            ].filter(Boolean)
        },
    },

    methods: {
        toggleMenu(event) {
            this.$refs.menu.toggle(event)
        },

        aplicarBusqueda() {
            this.filtersVisible = false
            this.page = 0
            this.first = 0

            this.filters.usuario.value = this.searchForm.usuario
            this.filters.correo.value = this.searchForm.correo
            this.filters.es_activo.value = this.searchForm.es_activo

            // Para el filtro de fecha, verificamos que haya un rango válido
            if (this.searchForm.fecha_creacion && this.searchForm.fecha_creacion.length === 2) {
                this.filters.fecha_creacion.value = this.searchForm.fecha_creacion
            } else {
                this.filters.fecha_creacion.value = null
            }

            this.cargarUsuarios()
        },

        clearFilters() {
            this.searchForm = {
                usuario: null,
                correo: null,
                es_activo: null,
            }
            //this.aplicarBusqueda()
        },

        getEstadoSeverity(value) {
            const estado = this.estados.find(e => e.value === value)
            return estado ? estado.severity : null
        },

        getEstadoLabel(value) {
            const estado = this.estados.find(e => e.value === value)
            return estado ? estado.label : value
        },

        async mtdGuardarPermiso({ datos, esVacio }) {
            const customAlerta = useAlertStore();

            if (esVacio) {
                await customAlerta.alert({
                    title: 'Gestión de Usuarios',
                    message: 'No se han ingresado datos para el nuevo permiso.'
                });
                return;
            }

            console.log('Guardando nuevo permiso:', { datos, esVacio });
        },

        async mtdActualizarUsuario({ datos, esVacio, esDiferente }) {
            const customAlerta = useAlertStore();

            if (esVacio) {
                await customAlerta.alert({
                    title: 'Gestión de Usuarios',
                    message: 'No se han ingresado datos para el nuevo permiso.'
                });
                return;
            } else if (!esDiferente) {
                await customAlerta.alert({
                    title: 'Gestión de Usuarios',
                    message: 'No se han detectado cambios en el formulario.'
                });
                return;
            }

            console.log('Guardando nuevo permiso:', { datos, esVacio, esDiferente });
        },

        async mtdEliminarUsuario(usuario) {
            const customAlerta = useAlertStore();

            const decision = await customAlerta.confirm({
                title: 'Confirmar eliminación',
                message: `¿Estás seguro de que deseas eliminar el usuario "${usuario.usuario}"?`,
                buttons: {
                    yes: 'Sí, eliminar',
                    cancel: 'No, cancelar',
                    visible: ["yes", "cancel"]
                },
            });

            if (decision === 'yes') {
                console.log('Permiso eliminado:', usuario)
            }
        },

        async exportarExcel() {
            const { exportToExcel } = useExcelExport();

            await exportToExcel({
                data: this.usuarios,

                sheetName: 'Usuarios',
                fileName: `permisos_${Date.now()}.xlsx`,
                creator: 'Sistema de Usuarios',

                columns: [
                    { header: 'Id', key: 'usuario_id', width: 10 },
                    { header: 'Usuario', key: 'usuario', width: 25 },
                    { header: 'Correo', key: 'correo', width: 20 },
                    { header: 'Estado', key: 'estado', width: 15 },
                    { header: 'Fecha Creación', key: 'creado_en', width: 20 },
                    { header: 'Fecha Actualización', key: 'actualizado_en', width: 20 }
                ],

                mapRow: p => ({
                    usuario_id: p.usuario_id,
                    usuario: p.usuario,
                    correo: p.correo,
                    estado: p.es_activo ? 'Activo' : 'Inactivo',
                    creado_en: p.creado_en ? new Date(p.creado_en) : null,
                    actualizado_en: p.actualizado_en ? new Date(p.actualizado_en) : null
                }),

                styles: {
                    creado_en: { numFmt: 'dd/mm/yyyy' },
                    actualizado_en: { numFmt: 'dd/mm/yyyy' },
                    usuario_id: { alignment: { horizontal: 'center' } },
                    estado: { alignment: { horizontal: 'center' } }
                },

                /*
                rowStyle: {
                  estado: { fill: {
                      type: 'pattern',
                      pattern: 'solid',
                      fgColor: { argb: 'FFD1FAE5' } 
                    } 
                  }
                },
                */

                rowStyle: ({ row }) => {
                    const cell = row.getCell('estado')

                    if (cell.value === 'Activo') {
                        cell.fill = {
                            type: 'pattern',
                            pattern: 'solid',
                            fgColor: { argb: 'FFD1FAE5' }
                        }
                    } else {
                        cell.fill = {
                            type: 'pattern',
                            pattern: 'solid',
                            fgColor: { argb: 'FFFEE2E2' }
                        }
                    }
                }

            });
        },

        editarPermiso(usuario) {
            console.log('Editar:', usuario)
        },

        eliminarPermiso(usuario) {
            console.log('Eliminar:', usuario)
        },

        crearPermiso() {
            console.log('Crear nuevo usuario')
        },

        formatDate(date) {
            if (!date) return ''
            return date.toLocaleDateString()
        },

        onPage(event) {
            this.page = event.page
            this.dataTableRows = event.rows
            this.cargarUsuarios()
        },

        async cargarUsuarios() {
            this.cargandoRegistros = true;

            await usuariosService.getBuscar({
                page: this.page,
                size: this.dataTableRows,
                ...this.optParams,
            })
                .then(response => {
                    logger.info('cargarUsuarios', 'Usuarios cargados:', response.data);
                    if (response.data?.exitosa) {
                        const contenido = response.data?.contenido;
                        const content = response.data?.contenido?.content || [];
                        logger.info('cargarUsuarios', `Usuarios obtenidos: ${content.length}`, content);

                        this.usuarios = content;
                        this.totalRecords = contenido?.totalElements || content.length;
                        this.first = contenido?.page * contenido?.size;

                    } else {
                        logger.warn('cargarUsuarios', 'Respuesta exitosa pero sin datos de permisos:', response.data);
                    }
                })
                .catch(error => {
                    console.error('Error al cargar usuarios:', error)
                })
                .finally(() => {
                    this.cargandoRegistros = false;
                });
        }
    },

    mounted() {
        this.cargarUsuarios();
    },

}
</script>