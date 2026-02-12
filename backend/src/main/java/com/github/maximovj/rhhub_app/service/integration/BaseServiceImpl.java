package com.github.maximovj.rhhub_app.service.integration;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.github.maximovj.rhhub_app.dto.response.ApiErrorDto;
import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;


@Transactional(readOnly = true)
public abstract class BaseServiceImpl<E, ID, R extends JpaBaseRepository<E, ID>>
        implements BaseService<E, ID> {

    protected final R jpaBaseRepository;

    public BaseServiceImpl(R jpaBaseRepository) {
        this.jpaBaseRepository = jpaBaseRepository;
    }

    @Override
    public List<E> findAll() {
        return this.jpaBaseRepository.findAll();
    }

    @Override
    public E findById(ID id) {
        return this.jpaBaseRepository.findById(id)
            .orElseThrow(() ->
                new ResourceNotFoundException(
                    "Recurso no encontrado",
                    List.of(new ApiErrorDto("id", "No existe un recurso con id " + id))
                )
            );
    }

    @Override
    @Transactional
    public E create(E entity) {
        return this.jpaBaseRepository.save(entity);
    }

    @Override
    @Transactional
    public E update(ID id, E entity) {
        if (!this.jpaBaseRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "Recurso no encontrado",
                List.of(new ApiErrorDto("id", "No existe un recurso con id " + id))
            );
        }
        return this.jpaBaseRepository.save(entity);
    }

    @Override
    @Transactional
    public void delete(ID id) {
        if (!this.jpaBaseRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                "Recurso no encontrado",
                List.of(new ApiErrorDto("id", "No existe un recurso con id " + id))
            );
        }
        this.jpaBaseRepository.deleteById(id);
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return this.jpaBaseRepository.findAll(pageable);
    }

    @Override
    public Page<E> findBySpecification(Specification<E> spec, Pageable pageable) {
        return this.jpaBaseRepository.findAll(spec, pageable);
    }
    
}