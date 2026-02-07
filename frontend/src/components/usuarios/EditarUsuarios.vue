<script setup>
import { ref, defineProps, watch } from "vue";
import { onMounted, onUpdated, onUnmounted } from "vue";
import usuariosService from "@/common/services/usuarios.service";

const props = defineProps({
    usuarioId: {
        type: Number,
        required: true,
        default: null
    }
});

const tituloCabecera = ref(""); 
const visible = ref(false);
const cargandoDatos = ref(false);
const usuario = ref({});
const grupoSeleccionado = ref(1);
const grupos = ref([
    { name: 'ADMINISTRADOR', code: 1 },
]);

onMounted(async () => {
    tituloCabecera.value = "Usuario: ...";
    console.log("Componente montado");
});

onUpdated(() => {
  console.log("Componente actualizado");
});

onUnmounted(() => {
  console.log("Componente destruido");
});

watch(visible, async (isVisible) => {
    if(!isVisible) return;
    cargandoDatos.value = true;
    const res = await usuariosService.getById(props.usuarioId);
    console.log("🧟","EditarUsuario.vue::watch", res);
    
    if(res.data?.exitosa) {
        usuario.value = res.data?.contenido;
        console.log("🧟", "EditarUsuario.vue::watch", "usuario.value => ", usuario.value);
        tituloCabecera.value = "Usuario: " + usuario.value.usuario;
        grupoSeleccionado.value = usuario.value?.grupo?.usuario_grupo_id;
    }

    cargandoDatos.value = false;
});
</script>

<template>
        <Button icon="pi pi-pencil" class="p-button-text p-button-warning mr-2" @click="visible = true"  />
        <Dialog
            v-model:visible="visible"
            modal
            :header="tituloCabecera"
            :style="{ width: '50rem' }"
            :breakpoints="{ '1199px': '75vw', '575px': '90vw' }"
            >
                <span class="text-surface-500 dark:text-surface-400 block mb-8">
                Actualizar información del usuario
                </span>
                
                <CustomField label="Nombre de usuario" forId="usuario">
                <Skeleton v-if="cargandoDatos" />
                <InputText
                    v-else
                    id="usuario"
                    class="flex-auto"
                    v-model="usuario.usuario"
                />
                </CustomField>

                <CustomField label="Correo electrónico" forId="correo">
                <Skeleton v-if="cargandoDatos" />
                <InputText
                    v-else
                    id="correo"
                    class="flex-auto"
                    v-model="usuario.correo"
                />
                </CustomField>

                <Divider align="center" type="dotted">
                <b>Opciones avanzadas</b>
                </Divider>

                <CustomField label="¿Es activo?">
                <Skeleton v-if="cargandoDatos" width="12rem" />
                <ToggleSwitch v-else v-model="usuario.es_activo" />
                </CustomField>

                <CustomField label="Seleccione un grupo">
                <Skeleton v-if="cargandoDatos" width="12rem" />
                <Select
                    v-else
                    :disabled="!usuario.es_activo"
                    v-model="grupoSeleccionado"
                    :options="grupos"
                    optionValue="code"
                    optionLabel="name"
                />
                </CustomField>
                <template #footer>
                    <Button type="button" label="Cancelar" severity="secondary" @click="visible = false"></Button>
                    <Button type="button" label="Guardar" @click="visible = false"></Button>
                </template>
        </Dialog>
</template>

