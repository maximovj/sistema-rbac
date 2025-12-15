import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

//import PrimeVuePlugin from '@plugins/primevue'
import PrimeVue from "primevue/config";
import Aura from '@primeuix/themes/aura';
import '@styles/main.css'

import { CustomIconsPlugin } from '@plugins/custom-icons'

const app = createApp(App)

app.use(createPinia())

app.use(router)

app.use(PrimeVue, {
        theme: {
        preset: Aura,
        options: {
            prefix: 'p',
            darkModeSelector: 'none', // none o system
            cssLayer: false
        }
        }
})

app.use(CustomIconsPlugin)

app.mount('#app')
