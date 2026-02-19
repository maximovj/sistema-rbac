<script setup>
import { defineEmits, defineProps } from "vue";
import { usePermisoForm } from "./composables/usePermisoForm";

const props = defineProps({
  permiso: { type: Object, required: true }
});
const emits = defineEmits(["guardar", "cancelar"]);

const {
  visible,
  accion,
  modulo,
  errors,
  formularioConCambios,
  formularioVacio,
  abrirFormulario,
  guardarFormulario,
  cancelarFormulario
} = usePermisoForm(props.permiso);
</script>

<template>
    <Button 
        icon="pi pi-pencil"
        severity="warning"
        rounded
        text 
        @click="abrirFormulario" 
    />

    <CustomCuadroDialogo
        v-model="visible"
        tituloHeader="Editar Permiso"
        :btnGuardarDisable="Object.keys(errors).length > 0 || formularioVacio || !formularioConCambios"
        @guardar="guardarFormulario(emits)"
        @cancelar="cancelarFormulario(emits)"
    >
        <template #formulario>
            <!-- Acción -->
            <Fieldset legend="Acción" :toggleable="false">
                <InputText
                    autofocus
                    id="accion"
                    placeholder="Escribe nombre de la acción"
                    v-model="accion"
                    class="w-full"
                />
                <small v-if="errors.accion" class="text-red-500">{{ errors.accion }}</small>
            </Fieldset>

            <!-- Módulo -->
            <Fieldset legend="Módulo" :toggleable="false">
                <InputText
                    id="modulo"
                    placeholder="Escribe nombre del módulo"
                    v-model="modulo"
                    class="w-full"
                />
                <small v-if="errors.modulo" class="text-red-500">{{ errors.modulo }}</small>
            </Fieldset>

            <!-- Información adicional -->
            <Fieldset legend="Información" :toggleable="true">
                <p class="m-0 text-sm text-gray-400">
                    El estado del permiso se asignará automáticamente como "Inactivo" al momento de su creación.
                    Si deseas modificar el estado, podrás hacerlo posteriormente con soporte técnico.
                </p>
            </Fieldset>
        </template>
    </CustomCuadroDialogo>
</template>

