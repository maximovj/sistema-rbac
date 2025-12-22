// src/plugins/common.js
import { defineAsyncComponent } from 'vue'

export default {
  install(app) {
    // 1️⃣ Componentes normales (carga estática)
    const normalComponents = import.meta.glob('@components/*.vue', { eager: true })
    for (const path in normalComponents) {
      const component = normalComponents[path].default
      const name = path.split('/').pop().replace('.vue', '')
      app.component(name, component)
    }

    // 2️⃣ Componentes grandes / lazy (carga dinámica)
    const lazyComponents = import.meta.glob('@components/lazy/*.vue')
    for (const path in lazyComponents) {
      const name = path.split('/').pop().replace('.vue', '')

      // Evitar sobrescribir los que ya se cargaron como normales
      if (!(normalComponents[path])) {
        app.component(name, defineAsyncComponent(lazyComponents[path]))
      }
    }
  }
}
