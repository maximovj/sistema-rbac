import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

import PrimeVuePlugin from '@plugins/primevue'
import '@styles/main.css'

import { CustomIconsPlugin } from '@plugins/custom-icons'

const app = createApp(App)

app.use(createPinia())

app.use(router)

app.use(PrimeVuePlugin)

app.use(CustomIconsPlugin)

app.mount('#app')
