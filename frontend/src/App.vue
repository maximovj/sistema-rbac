<template>
  <router-view />
  
  <AlertCustom ref="alertRef" class="fixed inset-0 bg-black/40 backdrop-blur-sm z-[9999]" />

  <div
    v-if="ui.loading"
    class="fixed inset-0 bg-black/40 backdrop-blur-sm z-[9999] grid place-items-center"
  >
    <div class="flex flex-col items-center gap-3">
      <ProgressSpinner
        style="width:50px;height:50px"
        strokeWidth="4"
        animationDuration=".7s"
      />
      <span class="text-white font-semibold text-sm">
        Cargando...
      </span>
    </div>
  </div>

</template>

<script>
import { useUiStore } from "@/stores/ui";

export default {
  components: { },
  setup() {
    const ui = useUiStore();
    return { ui };
  },
  mounted() {
    window.$alert = this.$refs.alertRef;
  }
};
</script>
