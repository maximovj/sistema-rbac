package com.github.maximovj.rhhub_app.service.integration;

import java.util.List;

public interface BaseService<E, ID> {

    List<E> findAll();

    E findById(ID id);

    E create(E entity);

    E update(ID id, E entity);

    void delete(ID id);
    
}
