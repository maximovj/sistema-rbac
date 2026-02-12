package com.github.maximovj.rhhub_app.service;

import org.springframework.stereotype.Service;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.repository.GrupoRepository;
import com.github.maximovj.rhhub_app.service.integration.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GrupoService extends BaseServiceImpl<GrupoEntity, Long, GrupoRepository> {
    
    public GrupoService(GrupoRepository jpaBaseRepository) {
        super(jpaBaseRepository);
    } 

    public boolean esoExiste(String nombre) {
        return this.jpaBaseRepository.existsByNombre(nombre);
    }

}
