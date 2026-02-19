<script setup>
import { ref, defineEmits, defineProps } from "vue";
import { usePermisoForm } from "./composables/usePermisoForm";

const initialState = () => ({
    accion: "",
    modulo: "",
});

const formulario = ref(initialState());

const emits = defineEmits(["guardar", "cancelar"]);

const {
  visible,
  accion,
  modulo,
  errors,
  formularioVacio,
  formularioConCambios,
  abrirFormulario,
  guardarFormulario,
  cancelarFormulario
} = usePermisoForm(formulario);
</script>

<template>
    <Button label="Nuevo" icon="pi pi-plus" @click="abrirFormulario" />

    <CustomCuadroDialogo
        v-model="visible"
        tituloHeader="Crear Nuevo Permiso"
        :btnGuardarDisable="Object.keys(errors).length > 0 || formularioVacio"
        @guardar="guardarFormulario(emits)"

        @cancelar="cancelarFormulario(emits)"
    >
        <template #formulario>
            <!-- Tu formulario aquí -->
            <Fieldset legend="Acción" :toggleable="false">
                <InputText autofocus id="accion" placeholder="Escribe nombre de la acción" v-model="accion" class="w-full" />
                <small v-if="errors.accion" class="text-red-500">{{ errors.accion }}</small>
            </Fieldset>

            <Fieldset legend="Módulo" :toggleable="false">
                <InputText id="modulo" placeholder="Escribe nombre del módulo" v-model="modulo" class="w-full" />
                <small v-if="errors.modulo" class="text-red-500">{{ errors.modulo }}</small>
            </Fieldset>

            <Fieldset legend="Información" :toggleable="true">
                <p class="m-0 text-sm text-gray-400">
                    El estado del permiso se asignará automáticamente como "Inactivo" al momento de su creación.
                    Si deseas modificar el estado, podrás hacerlo posteriormente con soporte técnico.
                </p>
            </Fieldset>
        </template>
    </CustomCuadroDialogo>
</template>
