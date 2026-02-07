<template>
  <div class="flex items-center gap-4 mb-4">
    <label :for="forId" class="font-semibold w-24">{{ label }}</label>
    <slot />
  </div>
</template>

<script setup>
defineProps({
  label: String,
  forId: String
})
</script>
