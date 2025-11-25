package br.com.gestao.alunos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CursoDTO {
    
    private Long id;
    
    @NotBlank(message = "Nome do curso é obrigatório")
    @Size(min = 3, max = 100, message = "Nome do curso deve ter entre 3 e 100 caracteres")
    private String nome;
    
    @Size(max = 255, message = "Descrição deve ter no máximo 255 caracteres")
    private String descricao;
}

