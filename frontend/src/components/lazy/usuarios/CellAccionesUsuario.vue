<!-- AccionesCell.vue -->
<script setup>
defineProps({ row: Object });

const editar = () => { console.log("Editar", row.usuario_id) }
const eliminar = () => { console.log("Eliminar", row.usuario_id) }
</script>

<template>
  <EditarUsuarios :usuarioId="row.usuario_id" />
  <Button icon="pi pi-trash" class="p-button-text p-button-danger" />
</template>
