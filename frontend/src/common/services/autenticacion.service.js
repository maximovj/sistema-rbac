// common/services/autenticacion.service.js
import BaseService from '@/common/services/base.service'
import { useSettingsStore } from '@/common/stores/settingsStore'
import { useUiStore } from '../stores/uiStore'
import { useAlertStore } from '../stores/alertStore'
import { useAuthStore } from '../stores/authStore'

class AutenticacionService extends BaseService {
  constructor() {
    super('/autenticacion')
  }

  async login(usuario, contrasena, recuerdame) {
    const ui = useUiStore();
    ui.loading = true;

    return this.custom('post', '/login', { usuario, contrasena, recuerdame })
      .then(res => {
        const auth = useAuthStore();
        auth.loguearse(
          res.data?.contenido?.acceso_token,
          res.data?.contenido?.info_usuario
        );
        return res
      })
      .catch( async err => {
        const alert = useAlertStore();
        await alert.alert({ 
          title:'Iniciar sesión',
          message: err?.response?.data?.error || err.message,
        })
      })
      .finally(() => {
        ui.loading = false;
      })
  }

  async logout() {
    const settings = useSettingsStore();
    const auth = useAuthStore();
    const ui = useUiStore();
    ui.loading = true;

    return this.custom('post', '/logout', settings.usuario, { headers: { 'Content-Type': 'text/plain' } })
      .catch((e) => console.log("Hubo un error","autentication.service.js::logout", {e}))
      .finally(() => {
        auth.desloguearse();
        ui.loading = false;
      })
  }

  async refresh() {
    const ui = useUiStore();
    ui.loading = true;

    return this.custom('post', '/refresh')
    .then(res => {
        const auth = useAuthStore();
        auth.loguearse(
          res.data?.contenido?.acceso_token,
          res.data?.contenido?.info_usuario
        );
        return res
    })
    .catch( async err => {
      console.log("Hubo un error","autentication.service.js::refresh", {err});
      await this.logout();
      const auth = useAuthStore();
      auth.desloguearse();

      const alert = useAlertStore();
      await alert.alert({ 
        title:'Renovar token',
        message: err?.response?.data?.error || err.message,
      });

      return err;
    })
    .finally(async () => {       
      ui.loading = false;
    });
  }
}

export default new AutenticacionService()
