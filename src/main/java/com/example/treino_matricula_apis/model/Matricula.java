package com.example.treino_matricula_apis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private Integer idade;
    private String cpf;
    private String email;


    @Enumerated(EnumType.STRING)
    private Plano plano;

    private Boolean laudo;
}
