<script setup>
import { ref } from 'vue';

const isDarkMode = ref(false);

const toggleDarkMode = () => {
    isDarkMode.value = !isDarkMode.value;
    document.documentElement.setAttribute(
        'data-theme',
        isDarkMode.value ? 'dark' : 'light'
    );
}

</script>

<template>
    <button
        @click="toggleDarkMode"
        :class="[
            'group relative w-16 h-8 rounded-full',
            'transition-all duration-300',
            'focus:outline-none focus:ring-2 focus:ring-blue-500',
            isDarkMode ? 'bg-gray-800' : 'bg-gray-200'
        ]"
        aria-label="Toggle dark mode"
    >
        <!-- Track -->
        <div class="absolute inset-0 flex items-center justify-between px-2">
            <i class="pi pi-sun text-yellow-500 text-sm"></i>
            <i class="pi pi-moon text-blue-400 text-sm"></i>
        </div>
        
        <!-- Thumb -->
        <div
            :class="[
                'absolute top-1 w-6 h-6 rounded-full',
                'transition-transform duration-300',
                'bg-white shadow-lg',
                'flex items-center justify-center',
                isDarkMode ? 'transform translate-x-9' : 'transform translate-x-1'
            ]"
        >
            <i
                :class="[
                    'text-xs transition-opacity duration-300',
                    isDarkMode ? 'pi pi-moon text-blue-600' : 'pi pi-sun text-yellow-500'
                ]"
            ></i>
        </div>
    </button>
</template>