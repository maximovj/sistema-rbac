// common/services/permisos.service.js
import BaseService from '@/common/services/base.service'

class PermisosService extends BaseService {
  constructor() {
    super('/permisos')
  }

  async getBuscar(params) {
      return await this.custom('get', '/q/buscar', null, { params });
  }

}

export default new PermisosService()