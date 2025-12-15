import PrimeVue from 'primevue/config'

import 'primeicons/primeicons.css'

// Form
import Button from 'primevue/button'
import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Checkbox from 'primevue/checkbox'
import RadioButton from 'primevue/radiobutton'
import Dropdown from 'primevue/dropdown'
import Calendar from 'primevue/calendar'
import Textarea from 'primevue/textarea'
import InputNumber from 'primevue/inputnumber'
import ToggleSwitch from 'primevue/toggleswitch'

// Data
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Paginator from 'primevue/paginator'

// Overlay
import Dialog from 'primevue/dialog'
import ConfirmDialog from 'primevue/confirmdialog'
import Sidebar from 'primevue/sidebar'
import Toast from 'primevue/toast'

// Menu
import Menubar from 'primevue/menubar'
import Menu from 'primevue/menu'
import TieredMenu from 'primevue/tieredmenu'
import Breadcrumb from 'primevue/breadcrumb'

// Feedback
import ToastService from 'primevue/toastservice'
import ConfirmationService from 'primevue/confirmationservice'

// Theme
import Aura from '@primeuix/themes/aura'

export default {
    install(app) {
        app.use(PrimeVue, { ripple: true })

        // Services
        app.use(ToastService)
        app.use(ConfirmationService)

        // Form
        app.component('PButton', Button)
        app.component('PInputText', InputText)
        app.component('PPassword', Password)
        app.component('PCheckbox', Checkbox)
        app.component('PRadioButton', RadioButton)
        app.component('PDropdown', Dropdown)
        app.component('PCalendar', Calendar)
        app.component('PTextarea', Textarea)
        app.component('PInputNumber', InputNumber)
        app.component('PToggleSwitch', ToggleSwitch)

        // Data
        app.component('PDataTable', DataTable)
        app.component('PColumn', Column)
        app.component('PPaginator', Paginator)

        // Overlay
        app.component('PDialog', Dialog)
        app.component('PConfirmDialog', ConfirmDialog)
        app.component('PSidebar', Sidebar)
        app.component('PToast', Toast)

        // Menu
        app.component('PMenubar', Menubar)
        app.component('PMenu', Menu)
        app.component('PTieredMenu', TieredMenu)
        app.component('PBreadcrumb', Breadcrumb)
    }
}
