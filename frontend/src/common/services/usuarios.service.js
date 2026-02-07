// common/services/usuarios.service.js
import BaseService from '@/common/services/base.service'
import { useUiStore } from '../stores/uiStore'
import { useAlertStore } from '../stores/alertStore'

class UsuariosService extends BaseService {
  constructor() {
    super('/usuarios')
  }

  async getqBusqueda() {
    return await this.custom('get', '/q/busqueda')
    .then(res => {
        if(res.data?.exitosa) {
          return res.data?.contenido;
        }
        return null;
    })
    .catch( async err => {
        const alert = useAlertStore();
        await alert.alert({ 
          title:'Iniciar sesión',
          message: err?.response?.data?.error || err.message,
        });
        return null;
      })
    .finally(() => {/* Código */});
  }

}

export default new UsuariosService()
