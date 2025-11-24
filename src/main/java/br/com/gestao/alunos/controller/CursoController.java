package br.com.gestao.alunos.controller;

import br.com.gestao.alunos.dto.CursoDTO;
import br.com.gestao.alunos.service.CursoService;
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
@RequestMapping("/api/cursos")
@Tag(name = "Cursos", description = "API para gerenciamento de cursos")
public class CursoController {
    
    private final CursoService cursoService;
    
    @Autowired
    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }
    
    @PostMapping
    @Operation(summary = "Criar um novo curso", description = "Cria um novo curso no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<CursoDTO> criarCurso(@Valid @RequestBody CursoDTO cursoDTO) {
        CursoDTO cursoCriado = cursoService.criarCurso(cursoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoCriado);
    }
    
    @GetMapping
    @Operation(summary = "Listar todos os cursos", description = "Retorna uma lista com todos os cursos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso")
    })
    public ResponseEntity<List<CursoDTO>> listarTodosCursos() {
        List<CursoDTO> cursos = cursoService.listarTodosCursos();
        return ResponseEntity.ok(cursos);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar curso por ID", description = "Retorna os dados de um curso específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<CursoDTO> buscarCursoPorId(
            @Parameter(description = "ID do curso") @PathVariable Long id) {
        CursoDTO curso = cursoService.buscarCursoPorId(id);
        return ResponseEntity.ok(curso);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar curso", description = "Atualiza os dados de um curso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<CursoDTO> atualizarCurso(
            @Parameter(description = "ID do curso") @PathVariable Long id,
            @Valid @RequestBody CursoDTO cursoDTO) {
        CursoDTO cursoAtualizado = cursoService.atualizarCurso(id, cursoDTO);
        return ResponseEntity.ok(cursoAtualizado);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar curso", description = "Remove um curso do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<Void> deletarCurso(
            @Parameter(description = "ID do curso") @PathVariable Long id) {
        cursoService.deletarCurso(id);
        return ResponseEntity.noContent().build();
    }
}

