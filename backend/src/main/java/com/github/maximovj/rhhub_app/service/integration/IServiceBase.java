package com.github.maximovj.rhhub_app.service.integration;

import org.springframework.http.ResponseEntity;

    public interface IServiceBase<T> {

        public ResponseEntity<?> respVerTodos(Integer page, Integer size, T entidadRequest);

        public ResponseEntity<?> respBusqueda(Integer page, Integer size, T entidadRequest);

        public ResponseEntity<?> respVerEntidad(Long entidadId);

        public ResponseEntity<?> respVerDetallesEntidad(Long entidadId);

        public ResponseEntity<?> respCrearEntidad(T entidadRequest);

        public ResponseEntity<?> respActualizarEntidad(Long entidadId, T entidadRequest);

        public ResponseEntity<?> respEliminarEntidad(Long entidadId);
        
    }
