package br.com.gestao.alunos.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlunoResponseDTO {
    
    private Long id;
    private String nome;
    private String matricula;
    private String email;
    private String cursoNome;
    private Long cursoId;
    private Integer semestre;
    private Double mediaGeral;
}

