package com.github.maximovj.rhhub_app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.OficinaEntity;
import com.github.maximovj.rhhub_app.repository.OficinaRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
@CrossOrigin("*")
public class OficinaController {

    @Autowired
    OficinaRepository repository;

    @GetMapping("/oficina")
    public ResponseEntity<?> getTodo() {
        return ApiResponse.ok("Lista de oficinas", repository.findAll());
    }

}
