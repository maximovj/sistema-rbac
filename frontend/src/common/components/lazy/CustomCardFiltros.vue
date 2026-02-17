<template>
  <Card class="mb-4 overflow-auto">
    <template #title>
      <div
        class="flex items-center justify-between"
      >
        <div @click="toggle" class="flex items-center cursor-pointer gap-2">
          <i 
            class="pi" 
            :class="[visible ? 'pi-chevron-down' : 'pi-chevron-right']"
          ></i>
          <slot name="title" />
        </div>
        <div class="flex items-center gap-2">
          <div class="hidden md:flex gap-1">
            <Tag 
                v-for="filter in filters"
                :value="`${filter.label}: ${filter.value}`" 
                severity="info" 
                rounded
                class="text-xs"
              />
          </div>
        </div>
      </div>
    </template>

    <template #content>
      <Transition name="fade">
        <div v-if="visible">
          <slot />
        </div>
      </Transition>
    </template>
  </Card>
</template>

<script>
export default {
  name: 'FilterPanel',
  props: {
    modelValue: Boolean,
    filters: Array
  },
  emits: ['update:modelValue'],
  computed: {
    visible() {
      return this.modelValue
    }
  },
  methods: {
    toggle() {
      this.$emit('update:modelValue', !this.visible)
    }
  }
}
</script>
