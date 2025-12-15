import * as Icons from "@/components/common/icons"

export const CustomIconsPlugin = {
    install(app){
        app.component('SaveIcon', Icons.SaveIcon)
        app.component('BookmarkIcon', Icons.BookmarkIcon)
    }
}

export const DynamicIconsPlugin = {
    install(app) {
        // Registrar todos los iconos exportados automáticamente
        Object.entries(Icons).forEach(([key, component]) => {
            if (key.endsWith('Icon') && typeof component === 'object') {
                // Remove 'Icon' suffix if present and register
                const name = key.endsWith('Icon') ? key : `${key}Icon`
                app.component(name, component)
            }
        })
    }
}

export const AutoIconsPlugin = {
    async install(app) {
        // Cargar todos los iconos dinámicamente
        const iconModules = import.meta.glob('@/components/common/icons/*.vue')
        
        for (const path in iconModules) {
            const module = await iconModules[path]()
            const fileName = path.split('/').pop().replace('.vue', '')
            
            // Registrar el componente
            app.component(fileName, module.default)
        }
    }
}