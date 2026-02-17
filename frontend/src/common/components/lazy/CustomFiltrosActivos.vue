<template>
  <div class="hidden md:flex gap-1 flex-wrap">
    <Chip
      v-for="filter in filters"
      :icon="filter.icon"
      :key="filter.key"
      :label="filter.value"
      removable
      @remove="filter.onRemove"
    />
  </div>
</template>

<script>
export default {
  props: {
    filters: Array
  }
}
</script>

<style>
.p-chip {
  font-size: 0.75rem;
}
</style>