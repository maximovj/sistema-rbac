<script setup>
import { ref, defineProps, watch, computed } from "vue";
import usuariosService from "@/common/services/usuarios.service";
import { useAlertStore } from "@/common/stores/alertStore";

import { scopedLogger } from "@/common/utils/loggerUtils";
const logger = scopedLogger("EditarUsuarios.vue");

import accion from "@/common/acciones/usuarios.acciones";

const props = defineProps({
    usuarioId: {
        type: Number,
        required: true,
        default: null
    }
});

function createInitialState() {
  return {
    visible: false,
    usuario: {
        confirmar_contrasena: '',
    },
    cargandoDatos: false,
    tituloCabecera: "MODIFICAR USUARIO",
    grupoSeleccionado: 1,
    grupos: [{ name: 'ADMINISTRADOR', code: 1 }],
  };
}

const data = ref(createInitialState());

function resetData() {
  data.value = createInitialState();
}

const esAdmin = computed(() => data.value?.usuario?.grupo?.rol?.rol_es_administrador);

const habilitarCampos = computed(() => !accion.value.root && esAdmin.value || !data.value.usuario.es_activo && accion.value.update);

const fnGuardar = async () => {
    const useAlert = useAlertStore();

    const confirmar = await useAlert.confirm({
        title: 'CONFIRMAR',
        message: '¿SEGURO QUE DESEAS MODIFICAR LA INFORMACIÓN DEL USUARIO?',
        buttons: {
            yes: "SI",
            no: "NO",
        }
    });

    if(confirmar == 'yes') {

        data.value.usuario.contrasena = data.value.usuario?.contrasena?.trim() || null;
        data.value.usuario.confirmar_contrasena = data.value.usuario?.confirmar_contrasena?.trim() || null;

        try {
            data.value.cargandoDatos = true;
            const res = await usuariosService.update(data.value.usuario.usuario_id, data.value.usuario);
            if(res.data?.exitosa) {
                data.value.usuario.contrasena = '';
                data.value.usuario.confirmar_contrasena = '';

                await useAlert.alert({
                    title: data.value.tituloCabecera,
                    message: res.data?.mensaje || 'Usuario modificado correctamente.',
                });
            }
        } catch (exerr) {
            logger.error("fnGuardar::catch", {exerr});
            if(exerr?.response?.data){
                const errores = exerr.response.data?.errores;
                const mensajeError = errores ? errores[0]?.context : exerr.response.data?.error; 
                await useAlert.alert({
                    title: data.value.tituloCabecera,
                    message: mensajeError || 'Hubo un error interno en el servidor.',
                });
            } else {
                data.value.visible = false;
                await useAlert.alert({
                    title: data.value.tituloCabecera,
                    message: exerr?.message || 'Hubo un error interno en el servidor.',
                });
            }
        } finally {
            data.value.cargandoDatos = false;
        }
    }

}

watch(() => data.value.visible, async (isVisible) => {
    if (!isVisible) {
        resetData(); // reset solo al cerrar
        return;
    }

    data.value.cargandoDatos = true;
    const res = await usuariosService.getById(props.usuarioId);
    logger.info("watch", {res});
    
    if(res.data?.exitosa) {
        data.value.usuario = res.data?.contenido;
        logger.info("watch::if", "data.value.usuario", data.value.usuario);
        data.value.grupoSeleccionado = data.value.usuario?.grupo?.usuario_grupo_id;
    } else {
        data.value.visible = false;
    }

    data.value.cargandoDatos = false;
});
</script>

<template>
        <Button 
            v-show="accion.update" 
            :disabled="esAdmin || !accion.update"
            icon="pi pi-pencil"
            class="p-button-text p-button-warning mr-2" 
            @click="() => { if(accion.update) data.visible = true; }" />

        <Dialog
            v-model:visible="data.visible"
            modal
            :header="data.tituloCabecera"
            :style="{ width: '50rem' }"
            :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
            >
                <span class="text-surface-500 dark:text-surface-400 block mb-8">
                Actualizar información del usuario
                </span>
                
                <CustomField label="Nombre de usuario" forId="usuario">
                <Skeleton v-if="data.cargandoDatos" />
                <InputText
                    v-else
                    id="usuario"
                    class="flex-auto"
                    :disabled="habilitarCampos"
                    v-model="data.usuario.usuario"
                />
                </CustomField>

                <CustomField label="Correo electrónico" forId="correo">
                <Skeleton v-if="data.cargandoDatos" />
                <InputText
                    v-else
                    id="correo"
                    class="flex-auto"
                    :disabled="habilitarCampos"
                    v-model="data.usuario.correo"
                />
                </CustomField>

                <Divider align="center" type="dotted">
                <b>Opciones avanzadas</b>
                </Divider>

                <CustomField label="¿Es activo?">
                <Skeleton v-if="data.cargandoDatos" width="12rem" />
                <ToggleSwitch :disabled="esAdmin" v-else v-model="data.usuario.es_activo" />
                </CustomField>

                
                <!-- <CustomField label="Seleccione un grupo">
                <Skeleton v-if="data.cargandoDatos" width="12rem" />
                <Select
                    v-else
                    :disabled="habilitarCampos"
                    v-model="data.grupoSeleccionado"
                    :options="data.grupos"
                    optionValue="code"
                    optionLabel="name"
                />
                </CustomField> -->

                <CustomField label="Grupo" forId="rol">
                <Skeleton v-if="data.cargandoDatos" />
                <InputText
                    v-else
                    id="rol"
                    class="flex-auto"
                    :disabled="esAdmin || !data.usuario.es_activo"
                    v-model="data.usuario.grupo.nombre"
                />
                </CustomField>

                <CustomField label="Rol" forId="rol">
                <Skeleton v-if="data.cargandoDatos" />
                <InputText
                    v-else
                    id="rol"
                    class="flex-auto"
                    :disabled="esAdmin || !data.usuario.es_activo"
                    v-model="data.usuario.grupo.rol.rol_nombre"
                />
                </CustomField>

                <CustomField label="Contraseña">
                <Skeleton v-if="data.cargandoDatos" width="12rem" />
                <Password v-else :disabled="habilitarCampos" v-model="data.usuario.contrasena" toggleMask />
                </CustomField>

                <CustomField label="Confirmar contraseña">
                <Skeleton v-if="data.cargandoDatos" width="12rem" />
                <Password v-else :disabled="habilitarCampos" v-model="data.usuario.confirmar_contrasena" toggleMask />
                </CustomField>

                <template  v-if="!data.cargandoDatos" #footer>
                    <Button type="button" label="Cancelar" severity="secondary" @click="data.visible = false"></Button>
                    <Button :disabled="esAdmin || !accion.update" type="button" label="Guardar" @click="fnGuardar"></Button>
                </template>
        </Dialog>
</template>

