// common/services/autenticacion.service.js
import BaseService from '@/common/services/base.service'
import { useSettingsStore } from '@/common/stores/settingsStore'
import { useUiStore } from '../stores/uiStore'
import { useAlertStore } from '../stores/alertStore'
import { useAuthStore } from '../stores/authStore'

import { scopedLogger } from '../utils/loggerUtils'
const logger = scopedLogger("autentication.service.js");

class AutenticacionService extends BaseService {
  constructor() {
    super('/autenticacion')
  }

  async login(usuario, contrasena, recuerdame) {
    const ui = useUiStore();
    ui.loading = true;

    return await this.custom('post', '/login', { usuario, contrasena, recuerdame })
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

    return await this.custom('post', '/logout', {usuario: settings.usuario})
      .catch((e) => logger.error("logout", {e}))
      .finally(() => {
        auth.desloguearse();
        ui.loading = false;
      })
  }

  async refresh() {
    const ui = useUiStore();
    ui.loading = true;
    logger.info("refresh", "Ejecutando...");

    return await this.custom('post', '/refresh')
    .then(res => {
        const auth = useAuthStore();
        auth.loguearse(
          res.data?.contenido?.acceso_token,
          res.data?.contenido?.info_usuario
        );
        return res
    })
    .catch( async err => {
      logger.error("refresh", {err});
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
