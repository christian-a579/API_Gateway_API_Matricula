package com.example.treino_matricula_apis.service;

import com.example.treino_matricula_apis.model.Matricula;
import com.example.treino_matricula_apis.repository.MatriculaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatriculaService {

    private final MatriculaRepository repository;

    public MatriculaService(MatriculaRepository repository) {
        this.repository = repository;
    }

    public Matricula cadastrar(Matricula matricula){
        if(matricula.getIdade() < 16){
            throw new RuntimeException ("Idade minima incorreta");
        }

        if(matricula.getIdade() >= 60 && !Boolean.TRUE.equals(matricula.getLaudo())){
            throw new RuntimeException("Idade necessita de laudo");
        }

        return repository.save(matricula);
    }

    public Matricula buscarPorCpf(String cpf){
        return repository.findByCpf(cpf) .orElseThrow(()-> new RuntimeException(
                "Matricula não encontrada"
        ));
    }

    public List<Matricula> exibirTodasMatriculas() {
        return repository.findAll();
    }

}