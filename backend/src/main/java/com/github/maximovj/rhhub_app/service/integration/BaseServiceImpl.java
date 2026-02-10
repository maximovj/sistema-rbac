package com.github.maximovj.rhhub_app.service.integration;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;

import jakarta.transaction.Transactional;

@Transactional
public abstract class BaseServiceImpl<E, ID>
        implements BaseService<E, ID> {

    protected abstract JpaBaseRepository<E, ID> getRepository();

    @Override
    public List<E> findAll() {
        return getRepository().findAll();
    }

    @Override
    public E findById(ID id) {
        return getRepository()
                .findById(id)
                .orElse(null);
    }

    @Override
    public E create(E entity) {
        return getRepository().save(entity);
    }

    @Override
    public E update(ID id, E entity) {
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException("Entidad no encontrada");
        }
        return getRepository().save(entity);
    }

    @Override
    public void delete(ID id) {
        if (!getRepository().existsById(id)) {
            throw new ResourceNotFoundException("Entidad no encontrada");
        }
        getRepository().deleteById(id);
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return getRepository().findAll(pageable);
    }

    @Override
    public Page<E> findBySpecification(Specification<E> spec, Pageable pageable) {
        return getRepository().findAll(spec, pageable);
    }
    
}

