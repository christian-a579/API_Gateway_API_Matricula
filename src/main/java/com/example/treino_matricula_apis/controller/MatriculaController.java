package com.example.treino_matricula_apis.controller;

import com.example.treino_matricula_apis.model.Matricula;
import com.example.treino_matricula_apis.service.MatriculaService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matriculas")
public class MatriculaController{

    private final MatriculaService service;

    public MatriculaController(MatriculaService service){
        this.service = service;
    }

    @PostMapping
    public Matricula cadastrar(@RequestBody Matricula matricula){
        return service.cadastrar(matricula);
    }

    @GetMapping("/{cpf}")
    public Matricula buscarPorCpf(@PathVariable String cpf){
        return service.buscarPorCpf(cpf);
    }
}

