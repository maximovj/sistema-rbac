import { createApp } from 'vue'

import CommonPlugin from './plugins/common'

import PiniaPlugin from './plugins/pinia'

import App from './App.vue'
import router from './router'

import PrimeVue from "primevue/config";
import SportYellowPreset from './themes/SportYellowTheme'
import '@styles/main.css'
import '@styles/theme-primevue.css'
import '@styles/gradients.css'

const app = createApp(App)

app.use(PiniaPlugin)

app.use(router)

app.use(PrimeVue, {
  ripple: true,
  theme: {
    preset: SportYellowPreset,
    options: {
      prefix: 'p',
      darkModeSelector: '[data-theme="dark"]',
      cssLayer: false
    }
  }
})

app.use(CommonPlugin)

app.mount('#app')
