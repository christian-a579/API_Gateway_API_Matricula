package com.example.treino_matricula_apis.repository;

import com.example.treino_matricula_apis.model.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula, Long> {
    Optional<Matricula> findByCpf(String cpf);
}