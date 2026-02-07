<script setup>
const props = defineProps({
  loading: {
    type: Boolean,
    default: false
  },
  width: {
    type: String,
    default: null
  }
});
</script>

<template>
  <div :style="{ width: width }">
    <Skeleton v-if="loading" />
    <slot v-else />
  </div>
</template>
