<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useRoute } from 'vue-router';
import { useAlertStore } from '@/common/stores/alertStore';
import autenticacionService from '@/common/services/autenticacion.service';

// Referencias computadas
const router = useRouter();
const route = useRoute();
const showIconToolbar = ref(false);

// Métodos
const isActive = (path) => {
    return route.path.startsWith(path) ? 'p-button-primary' : ''
}

const salir =  async () =>  { 
    const alert = useAlertStore();
    const desicion = await alert.confirm({ 
    message: '¿Seguro que deseas salir?',
    buttons: {visible: ["yes", "cancel"] }
    });

    if(desicion == 'yes') {
        await autenticacionService.logout();
        router.push('/');
    }
};

</script>

<template>
<!-- Toolbar principal -->
<div class="flex justify-between items-center p-4 bg-white dark:bg-gray-800 border-b dark:border-gray-700">

    <!-- Izquierda -->
    <div class="flex items-center gap-2 sm:gap-3">
    <Button
        icon="pi pi-th-large"
        class="p-button-text"
        @click="showIconToolbar = !showIconToolbar"
    />

    <!-- Oculto en mobile -->
    <div class="hidden sm:flex items-center gap-2">
        <Button icon="pi pi-search" class="p-button-text" />
        <input
        type="text"
        placeholder="Buscar..."
        class="px-3 py-1 border rounded dark:bg-gray-700 dark:border-gray-600 focus:outline-none"
        />
    </div>
    </div>

    <!-- Derecha -->
    <div class="flex items-center gap-2 sm:gap-3">
    <ThemeToggle />
    <Button icon="pi pi-bell" class="p-button-text" />
    <Button
        label="Salir"
        icon="pi pi-power-off"
        @click="salir"
        class="p-button-danger p-button-text hidden sm:inline-flex"
    />
    </div>
</div>

<!-- Toolbar flotante -->
<transition name="fade-slide">
    <div
    v-if="showIconToolbar"
    class="absolute left-0 right-0 top-full
            bg-white dark:bg-gray-800
            border-b dark:border-gray-700
            shadow-lg z-50"
    >
    <div class="flex justify-around sm:justify-start sm:gap-3 px-4 py-2">

        <RouterLink to="/panel">
        <Button
            icon="pi pi-home"
            class="p-button-text p-button-rounded"
            :class="isActive('/panel')"
            v-tooltip.bottom="'Dashboard'"
        />
        </RouterLink>

        <RouterLink to="/usuarios">
        <Button
            icon="pi pi-users"
            class="p-button-text p-button-rounded"
            :class="isActive('/usuarios')"
            v-tooltip.bottom="'Usuarios'"
        />
        </RouterLink>

        <RouterLink to="/configuracion">
        <Button
            icon="pi pi-cog"
            class="p-button-text p-button-rounded"
            :class="isActive('/configuracion')"
            v-tooltip.bottom="'Configuración'"
        />
        </RouterLink>

    </div>
    </div>
</transition>
</template>