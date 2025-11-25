package br.com.gestao.alunos.controller;

import br.com.gestao.alunos.dto.CursoDTO;
import br.com.gestao.alunos.service.CursoService;
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

@WebMvcTest(CursoController.class)
class CursoControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private CursoService cursoService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private CursoDTO cursoDTO;
    
    @BeforeEach
    void setUp() {
        cursoDTO = new CursoDTO();
        cursoDTO.setId(1L);
        cursoDTO.setNome("Ciência da Computação");
        cursoDTO.setDescricao("Curso de CC");
    }
    
    @Test
    void criarCurso_Sucesso() throws Exception {
        when(cursoService.criarCurso(any(CursoDTO.class))).thenReturn(cursoDTO);
        
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Ciência da Computação"));
        
        verify(cursoService, times(1)).criarCurso(any(CursoDTO.class));
    }
    
    @Test
    void listarTodosCursos_Sucesso() throws Exception {
        List<CursoDTO> cursos = Arrays.asList(cursoDTO);
        when(cursoService.listarTodosCursos()).thenReturn(cursos);
        
        mockMvc.perform(get("/api/cursos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].nome").value("Ciência da Computação"));
        
        verify(cursoService, times(1)).listarTodosCursos();
    }
    
    @Test
    void buscarCursoPorId_Sucesso() throws Exception {
        when(cursoService.buscarCursoPorId(1L)).thenReturn(cursoDTO);
        
        mockMvc.perform(get("/api/cursos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Ciência da Computação"));
        
        verify(cursoService, times(1)).buscarCursoPorId(1L);
    }
    
    @Test
    void atualizarCurso_Sucesso() throws Exception {
        when(cursoService.atualizarCurso(anyLong(), any(CursoDTO.class))).thenReturn(cursoDTO);
        
        mockMvc.perform(put("/api/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        
        verify(cursoService, times(1)).atualizarCurso(anyLong(), any(CursoDTO.class));
    }
    
    @Test
    void deletarCurso_Sucesso() throws Exception {
        doNothing().when(cursoService).deletarCurso(1L);
        
        mockMvc.perform(delete("/api/cursos/1"))
                .andExpect(status().isNoContent());
        
        verify(cursoService, times(1)).deletarCurso(1L);
    }
    
    @Test
    void criarCurso_DadosInvalidos_DeveRetornarBadRequest() throws Exception {
        cursoDTO.setNome(""); // Nome vazio
        
        mockMvc.perform(post("/api/cursos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isBadRequest());
        
        verify(cursoService, never()).criarCurso(any(CursoDTO.class));
    }
    
    @Test
    void atualizarCurso_DadosInvalidos_DeveRetornarBadRequest() throws Exception {
        cursoDTO.setNome(""); // Nome vazio
        
        mockMvc.perform(put("/api/cursos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cursoDTO)))
                .andExpect(status().isBadRequest());
        
        verify(cursoService, never()).atualizarCurso(anyLong(), any(CursoDTO.class));
    }
}

