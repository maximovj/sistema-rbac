package com.github.maximovj.rhhub_app.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.dto.request.GrupoRequest;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.mapper.GrupoMapper;
import com.github.maximovj.rhhub_app.repository.GrupoRepository;
import com.github.maximovj.rhhub_app.repository.specification.GrupoSpecification;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class GrupoService {

    private final GrupoRepository repo;

    public ResponseEntity<?> verGrupos(Integer page, Integer size) {
        ApiResponse<GrupoEntity> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(
            page, 
            size, 
            Sort.by("nombre").ascending());

        Page<GrupoEntity> pageGrupo = this.repo.findAll(pageable);
        return response.okPage("Lista de entidades", pageGrupo);
    }

    public ResponseEntity<?> busquedaPorPropiedades(Integer page, Integer size, GrupoEntity e) {
        ApiResponse<GrupoEntity> response = new ApiResponse<>();
        Pageable pageable = PageRequest.of(
            page, 
            size, 
            Sort.by("nombre").ascending());

        Page<GrupoEntity> pageGrupo = this.repo.findAll(
            new GrupoSpecification().filtro(e), pageable
        );
        return response.okPage("Filtro de entidades", pageGrupo);
    }

    public ResponseEntity<?> verGrupo(Long grupoId) {
        Objects.requireNonNull(grupoId);
        GrupoEntity grupo =  this.repo.qBuscarPorIdRelaciones(grupoId).orElse(null);

        if(grupo == null) { 
            return ApiResponse.notFound("Entidad no encontrada", null);
        }

        return ApiResponse.ok("Info del grupo", GrupoMapper.toDTO(grupo));
    }

    public ResponseEntity<?> actualizarUnGrupo(Long grupoId, GrupoRequest req) {
        Objects.requireNonNull(grupoId);
        GrupoEntity grupo =  this.repo.findById(grupoId).orElse(null);

        if(grupo == null) { 
            return ApiResponse.notFound("Entidad no encontrada", null);
        }

        if(req.getEsActivo() != null) {
            grupo.setEsActivo(req.getEsActivo());
        }

        if(req.getNombre() != null && !req.getNombre().isBlank()) {
            grupo.setNombre(req.getNombre().trim());
        }

        if(req.getDescripcion() != null && !req.getDescripcion().isBlank()) {
            grupo.setDescripcion(req.getDescripcion().trim());
        }

        GrupoEntity updated = this.repo.save(grupo);
        return ApiResponse.ok("Entidad actualizada correctamente", updated);
    }

    public ResponseEntity<?> crearUnGrupo(GrupoRequest req)
    {
        try {
            Objects.requireNonNull(req.getNombre().trim(), "El campo nombre es obligatoria");
            Objects.requireNonNull(req.getDescripcion().trim(), "El campo descripcion es obligatoria");

            if(this.repo.existsByNombre(req.getNombre().trim())) {
                return ApiResponse.conflict("El nombre de la entidad existe", null);
            }
            
            GrupoEntity entidad = new GrupoEntity();
            entidad.setNombre(req.getNombre());
            entidad.setDescripcion(req.getDescripcion());
            entidad.setEsActivo((req.getEsActivo() == null) ? true : req.getEsActivo());

            GrupoEntity guardado = this.repo.save(entidad);
            return ApiResponse.ok("Entidad creada correctamente", guardado);
        } catch (Exception e) {
            return ApiResponse.internalServerError(e.getMessage(), null);
        }
    }

    public ResponseEntity<?> eliminarUnGrupo(Long grupoId)
    {
        Objects.requireNonNull(grupoId);
        GrupoEntity grupo =  this.repo.findById(grupoId).orElse(null);

        if(grupo == null) { 
            return ApiResponse.notFound("Entidad no encontrada", null);
        }

        this.repo.deleteById(grupoId);
        return ApiResponse.ok("Entidad eliminada correctamente", grupo);
    }
    
}
