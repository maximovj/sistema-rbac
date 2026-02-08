<!-- AccionesCell.vue -->
<script setup>
import { scopedLogger } from '@/common/utils/loggerUtils';
const logger = scopedLogger("CellAccionesUsuario.vue");

defineProps({ row: Object });

const editar = () => { logger.info("editar", "row.usuario_id", row.usuario_id) }
const eliminar = () => { logger.info("eliminar", "row.usuario_id", row.usuario_id) }
</script>

<template>
  <EditarUsuarios :usuarioId="row.usuario_id" />
</template>
