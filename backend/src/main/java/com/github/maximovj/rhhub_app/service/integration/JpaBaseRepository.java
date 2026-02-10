package com.github.maximovj.rhhub_app.service.integration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface JpaBaseRepository<E, ID> extends 
    JpaRepository<E, ID>, 
    JpaSpecificationExecutor<E>  { }
