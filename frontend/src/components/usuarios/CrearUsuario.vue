<script setup>
import { ref, watch } from 'vue';
import { useUsuarioForm } from './composables/useUsuarioForm';
import gruposService from "@/common/services/grupos.service";

import { scopedLogger } from "@/common/utils/loggerUtils";
const logger = scopedLogger("CrearUsuarios.vue");

const initialState = () => ({
    usuario: "",
    correo: "",
    es_activado: false,
    grupos: null,
    rol: null,
});

const usuarioForm = ref(initialState);

const lstgrupos = ref([]);

const emits = defineEmits(["guardar", "cancelar"]);

const {
    visible,
    usuario,
    correo,
    es_activo,
    grupo,
    errors,
    formularioVacio,
    abrirFormulario,
    guardarFormulario,
    cancelarFormulario,
} = useUsuarioForm(usuarioForm);

watch(visible, async (isVisible) => {
    if(!isVisible) {
        return;
    }

    const resGrupos = await gruposService.getAll();
    lstgrupos.value = resGrupos.data?.contenido?.content;
    logger.info("watch", lstgrupos.value);

});
</script>

<template>
    <Button label="Nuevo" icon="pi pi-plus" @click="abrirFormulario" />

    <CustomCuadroDialogo
        v-model="visible"
        tituloHeader="CREAR UN NUEVO USAURIO"
        :btnGuardarDisable="Object.keys(errors).length > 0 || formularioVacio"
        @guardar="guardarFormulario(emits)"
        @cancelar="cancelarFormulario(emits)"
    >
    <template #formulario>
        <Fieldset legend="Usuario" :toggleable="false">
            <InputText autofocus id="usuario" placeholder="Escribe usuario" v-model="usuario" class="w-full" />
            <small v-if="errors.usuario" class="text-red-500">{{ errors.usuario }}</small>
        </Fieldset>
        
        <Fieldset legend="Correo electrónico" :toggleable="false">
            <InputText autofocus id="correo" placeholder="Escribe correo electrónico" v-model="correo" class="w-full" />
            <small v-if="errors.correo" class="text-red-500">{{ errors.correo }}</small>
        </Fieldset>

        <Fieldset legend="Grupo de usuario" :toggleable="false">
            <label for="grupo" class="font-medium block mb-2">Selecciona un grupo</label>
            <Select 
                id="grupo"
                v-model="grupo"
                :options="lstgrupos"
                optionLabel="nombre"
                optionValue="grupo_id"
                placeholder="Selecciona un rol"
                class="w-full"
            />
            <small v-if="errors.rol" class="text-red-500">{{ errors.rol }}</small>
        </Fieldset>

        <Fieldset legend="Estado de cuenta" :toggleable="false">
            <div class="flex align-items-center justify-content-between">
                <InputSwitch 
                    id="es_activo" 
                    v-model="es_activo" 
                    :binary="true"
                />
                <label for="es_activo" class="ml-2 font-medium">Cuenta activada</label>
            </div>
            <small class="text-gray-400 block mt-2">
                {{ es_activo ? 'La cuenta estará activa inmediatamente' : 'La cuenta se creará como inactiva' }}
            </small>
        </Fieldset>
    </template>
    </CustomCuadroDialogo>
</template>