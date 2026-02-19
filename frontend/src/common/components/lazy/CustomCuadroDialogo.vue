<template>
    <Dialog
        :visible="modelValue"
        @update:visible="val => emit('update:modelValue', val)"
        maximizable
        modal
        :header="tituloHeader"
        :style="{ width: '50rem' }"
        :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
    >
        <form @submit.prevent="fnGuardar" class="h-full">
            <div class="flex flex-col gap-4 h-full">

                <div class="flex flex-col gap-2 p-4 overflow-auto">
                    <slot name="formulario">
                        <p>Aquí se mostrará el formulario</p>
                    </slot>
                </div>

                <div class="flex justify-end gap-2 mt-auto">
                    <Button type="button" label="Cancelar" severity="secondary" @click="fnCancelar" />
                    <Button type="submit" label="Guardar" :disabled="btnGuardarDisable" />
                </div>

            </div>
        </form>
    </Dialog>
</template>

<script setup>
import { defineProps, defineEmits } from "vue";

const props = defineProps({
    modelValue: Boolean,
    tituloHeader: {
        type: String,
        default: "Crear recurso",
    },
    btnGuardarDisable: {
        type: Boolean,
        default: false,
    },
    btnCancelarDisable: {
        type: Boolean,
        default: false,
    },
});

const emit = defineEmits(["update:modelValue", "update:btnGuardarDisable", "update:btnCancelarDisable", "guardar", "cancelar"]);

const fnGuardar = () => {
    emit("guardar");
    emit("update:btnGuardarDisable", false);
    emit("update:btnCancelarDisable", false);
    emit("update:modelValue", false);
};

const fnCancelar = () => {
    emit("cancelar");
    emit("update:modelValue", false);
};
</script>

<style>
.p-dialog-maximized {
    background-color: transparent !important;
}

.p-dialog-content {
    background-color: white !important;
}
</style>