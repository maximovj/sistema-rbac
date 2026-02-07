<script setup>
import { defineProps } from 'vue';
import EditarUsuarios from './EditarUsuarios.vue';

const props = defineProps({
    cargandoDatos: {
        type: Boolean,
        default: false,
    },
    users: {
        type: Array,
        default: () => [],
    }
});

const skeletonRows = Array.from({ length: 5 });
</script>

<template>
<DataTable
  :value="cargandoDatos ? skeletonRows : users"
  class="p-datatable-sm p-datatable-striped bg-gray-800 text-gray-200"
>
  <Column header="ID">
    <template #body="slotProps">
      <CustomTableCell :loading="cargandoDatos">{{ slotProps.data.usuario_id }}</CustomTableCell>
    </template>
  </Column>

  <Column header="Nombre">
    <template #body="slotProps">
      <CustomTableCell :loading="cargandoDatos">{{ slotProps.data.usuario }}</CustomTableCell>
    </template>
  </Column>

  <Column header="Correo">
    <template #body="slotProps">
      <CustomTableCell :loading="cargandoDatos">{{ slotProps.data.correo }}</CustomTableCell>
    </template>
  </Column>

  <Column header="Activo">
    <template #body="slotProps">
      <CustomTableCell :loading="cargandoDatos" width="2rem">
        {{ slotProps.data.es_activo ? '🟢' : '🔴' }}
      </CustomTableCell>
    </template>
  </Column>

  <Column header="Acciones">
    <template #body="slotProps">
      <CustomTableCell :loading="cargandoDatos" width="6rem">
        <div v-if="!cargandoDatos" class="flex gap-2">
          <EditarUsuarios :usuarioId="slotProps.data.usuario_id" />
          <Button icon="pi pi-trash" class="p-button-text p-button-danger" />
        </div>
      </CustomTableCell>
    </template>
  </Column>
</DataTable>
</template>