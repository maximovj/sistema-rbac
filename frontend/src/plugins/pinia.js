import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia();

// Persistencia automatica
pinia.use(piniaPluginPersistedstate)

export default pinia;