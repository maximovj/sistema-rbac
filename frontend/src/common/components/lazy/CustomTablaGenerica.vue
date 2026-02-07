<script setup>
import { defineProps } from 'vue';

const props = defineProps({
  data: { type: Array, default: () => [] },       // Datos a mostrar
  columns: { type: Array, default: () => [] },    // Definición de columnas
  loading: { type: Boolean, default: false },     // Mostrar Skeleton
});
</script>

<template>
  <DataTable :value="loading ? Array.from({ length: 5 }) : data" class="p-datatable-sm p-datatable-striped">
    <Column
      v-for="col in columns"
      :key="col.field"
      :header="col.header"
      :style="{ width: col.width || 'auto' }"
    >
      <template #body="slotProps">
        <CustomTableCell :loading="loading" :width="col.width">
          <template v-if="!loading">
            <!-- Renderiza componente personalizado si existe -->
            <component
              v-if="col.cellComponent"
              :is="col.cellComponent"
              :row="slotProps.data"
            />
            <!-- Si no, muestra el valor del campo -->
            <span v-else>{{ slotProps.data[col.field] }}</span>
          </template>
        </CustomTableCell>
      </template>
    </Column>
  </DataTable>
</template>
