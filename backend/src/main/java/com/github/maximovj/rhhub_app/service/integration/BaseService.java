package com.github.maximovj.rhhub_app.service.integration;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

public interface BaseService<E, ID> {

    List<E> findAll();

    E findById(ID id);

    E create(E entity);

    E update(ID id, E entity);

    void delete(ID id);

    /**
     * Devuelve todos los registros paginados.
     *
     * @param pageable información de paginación (página, tamaño, orden)
     * @return página de registros
     */
    Page<E> findAll(Pageable pageable);

     /**
     * Devuelve los registros que cumplen la Specification, paginados.
     *
     * @param spec     criterios de búsqueda dinámicos
     * @param pageable información de paginación (página, tamaño, orden)
     * @return página de registros que cumplen la Specification
     */
    Page<E> findBySpecification(Specification<E> spec, Pageable pageable);
    
}
