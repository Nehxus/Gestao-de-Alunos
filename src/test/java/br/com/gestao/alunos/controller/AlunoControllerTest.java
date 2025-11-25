package br.com.gestao.alunos.controller;

import br.com.gestao.alunos.dto.AlunoDTO;
import br.com.gestao.alunos.dto.AlunoResponseDTO;
import br.com.gestao.alunos.service.AlunoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AlunoController.class)
class AlunoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AlunoService alunoService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private AlunoDTO alunoDTO;
    private AlunoResponseDTO alunoResponseDTO;
    
    @BeforeEach
    void setUp() {
        alunoDTO = new AlunoDTO();
        alunoDTO.setNome("João Silva");
        alunoDTO.setMatricula("2024001");
        alunoDTO.setEmail("joao@email.com");
        alunoDTO.setCursoId(1L);
        alunoDTO.setSemestre(3);
        alunoDTO.setMediaGeral(8.5);
        
        alunoResponseDTO = new AlunoResponseDTO();
        alunoResponseDTO.setId(1L);
        alunoResponseDTO.setNome("João Silva");
        alunoResponseDTO.setMatricula("2024001");
        alunoResponseDTO.setEmail("joao@email.com");
        alunoResponseDTO.setCursoId(1L);
        alunoResponseDTO.setCursoNome("Ciência da Computação");
        alunoResponseDTO.setSemestre(3);
        alunoResponseDTO.setMediaGeral(8.5);
    }
    
    @Test
    void criarAluno_Sucesso() throws Exception {
        when(alunoService.criarAluno(any(AlunoDTO.class))).thenReturn(alunoResponseDTO);
        
        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alunoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.matricula").value("2024001"));
        
        verify(alunoService, times(1)).criarAluno(any(AlunoDTO.class));
    }
    
    @Test
    void criarAluno_DadosInvalidos_DeveRetornarBadRequest() throws Exception {
        alunoDTO.setNome(""); // Nome vazio
        
        mockMvc.perform(post("/api/alunos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alunoDTO)))
                .andExpect(status().isBadRequest());
        
        verify(alunoService, never()).criarAluno(any(AlunoDTO.class));
    }
    
    @Test
    void listarTodosAlunos_Sucesso() throws Exception {
        List<AlunoResponseDTO> alunos = Arrays.asList(alunoResponseDTO);
        when(alunoService.listarTodosAlunos()).thenReturn(alunos);
        
        mockMvc.perform(get("/api/alunos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
        
        verify(alunoService, times(1)).listarTodosAlunos();
    }
    
    @Test
    void buscarAlunoPorId_Sucesso() throws Exception {
        when(alunoService.buscarAlunoPorId(1L)).thenReturn(alunoResponseDTO);
        
        mockMvc.perform(get("/api/alunos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));
        
        verify(alunoService, times(1)).buscarAlunoPorId(1L);
    }
    
    @Test
    void atualizarAluno_Sucesso() throws Exception {
        when(alunoService.atualizarAluno(anyLong(), any(AlunoDTO.class))).thenReturn(alunoResponseDTO);
        
        mockMvc.perform(put("/api/alunos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(alunoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        
        verify(alunoService, times(1)).atualizarAluno(anyLong(), any(AlunoDTO.class));
    }
    
    @Test
    void deletarAluno_Sucesso() throws Exception {
        doNothing().when(alunoService).deletarAluno(1L);
        
        mockMvc.perform(delete("/api/alunos/1"))
                .andExpect(status().isNoContent());
        
        verify(alunoService, times(1)).deletarAluno(1L);
    }
    
    @Test
    void buscarAlunosComFiltro_PorCurso_Sucesso() throws Exception {
        List<AlunoResponseDTO> alunos = Arrays.asList(alunoResponseDTO);
        when(alunoService.buscarAlunosPorCurso(1L)).thenReturn(alunos);
        
        mockMvc.perform(get("/api/alunos/filtro")
                        .param("cursoId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(alunoService, times(1)).buscarAlunosPorCurso(1L);
    }
    
    @Test
    void buscarAlunosComFiltro_PorSemestre_Sucesso() throws Exception {
        List<AlunoResponseDTO> alunos = Arrays.asList(alunoResponseDTO);
        when(alunoService.buscarAlunosPorSemestre(3)).thenReturn(alunos);
        
        mockMvc.perform(get("/api/alunos/filtro")
                        .param("semestre", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(alunoService, times(1)).buscarAlunosPorSemestre(3);
    }
    
    @Test
    void buscarAlunosComFiltro_PorMediaMinima_Sucesso() throws Exception {
        List<AlunoResponseDTO> alunos = Arrays.asList(alunoResponseDTO);
        when(alunoService.buscarAlunosPorMediaMinima(8.0)).thenReturn(alunos);
        
        mockMvc.perform(get("/api/alunos/filtro")
                        .param("mediaMinima", "8.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(alunoService, times(1)).buscarAlunosPorMediaMinima(8.0);
    }
    
    @Test
    void buscarAlunosComFiltro_SemFiltro_DeveListarTodos() throws Exception {
        List<AlunoResponseDTO> alunos = Arrays.asList(alunoResponseDTO);
        when(alunoService.listarTodosAlunos()).thenReturn(alunos);
        
        mockMvc.perform(get("/api/alunos/filtro"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
        
        verify(alunoService, times(1)).listarTodosAlunos();
    }
}

