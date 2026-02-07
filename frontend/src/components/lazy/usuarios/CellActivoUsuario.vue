<script setup>
defineProps({ row: Object });
</script>

<template>
  <span>{{ row.es_activo ? '🟢' : '🔴' }}</span>
</template>
