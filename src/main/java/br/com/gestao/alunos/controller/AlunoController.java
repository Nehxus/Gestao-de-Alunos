package br.com.gestao.alunos.controller;

import br.com.gestao.alunos.dto.AlunoDTO;
import br.com.gestao.alunos.dto.AlunoResponseDTO;
import br.com.gestao.alunos.service.AlunoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alunos")
@Tag(name = "Alunos", description = "API para gerenciamento de alunos")
public class AlunoController {
    
    private final AlunoService alunoService;
    
    @Autowired
    public AlunoController(AlunoService alunoService) {
        this.alunoService = alunoService;
    }
    
    @PostMapping
    @Operation(summary = "Criar um novo aluno", description = "Cria um novo aluno no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Aluno criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "Matrícula ou email já existe")
    })
    public ResponseEntity<AlunoResponseDTO> criarAluno(@Valid @RequestBody AlunoDTO alunoDTO) {
        AlunoResponseDTO alunoCriado = alunoService.criarAluno(alunoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(alunoCriado);
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os alunos", description = "Retorna uma lista com todos os alunos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de alunos retornada com sucesso")
    })
    public ResponseEntity<List<AlunoResponseDTO>> listarTodosAlunos() {
        List<AlunoResponseDTO> alunos = alunoService.listarTodosAlunos();
        return ResponseEntity.ok(alunos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar aluno por ID", description = "Retorna os dados de um aluno específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno encontrado"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<AlunoResponseDTO> buscarAlunoPorId(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {
        AlunoResponseDTO aluno = alunoService.buscarAlunoPorId(id);
        return ResponseEntity.ok(aluno);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar aluno", description = "Atualiza os dados de um aluno existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Aluno atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<AlunoResponseDTO> atualizarAluno(
            @Parameter(description = "ID do aluno") @PathVariable Long id,
            @Valid @RequestBody AlunoDTO alunoDTO) {
        AlunoResponseDTO alunoAtualizado = alunoService.atualizarAluno(id, alunoDTO);
        return ResponseEntity.ok(alunoAtualizado);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar aluno", description = "Remove um aluno do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Aluno deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Aluno não encontrado")
    })
    public ResponseEntity<Void> deletarAluno(
            @Parameter(description = "ID do aluno") @PathVariable Long id) {
        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/filtro")
    @Operation(summary = "Buscar alunos com filtros", description = "Busca alunos por curso, semestre ou média mínima")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Alunos encontrados")
    })
    public ResponseEntity<List<AlunoResponseDTO>> buscarAlunosComFiltro(
            @Parameter(description = "ID do curso") @RequestParam(required = false) Long cursoId,
            @Parameter(description = "Semestre") @RequestParam(required = false) Integer semestre,
            @Parameter(description = "Média mínima") @RequestParam(required = false) Double mediaMinima) {
        
        List<AlunoResponseDTO> alunos;
        
        if (cursoId != null) {
            alunos = alunoService.buscarAlunosPorCurso(cursoId);
        } else if (semestre != null) {
            alunos = alunoService.buscarAlunosPorSemestre(semestre);
        } else if (mediaMinima != null) {
            alunos = alunoService.buscarAlunosPorMediaMinima(mediaMinima);
        } else {
            alunos = alunoService.listarTodosAlunos();
        }
        
        return ResponseEntity.ok(alunos);
    }
}

