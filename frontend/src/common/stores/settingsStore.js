// common/stores/settingsStore.js
import { defineStore } from 'pinia'

export const useSettingsStore = defineStore('settings', {
    state: () => ({
        usuario: null,
        recuerdame: false,
        tema: 'light',
    }),

    getters: { },

    actions: { },

    persist: {
        pick: [
            'usuario',
            'recuerdame',
            'tema',
        ],
        storage: localStorage
    }
})
