package br.com.gestao.alunos.service;

import br.com.gestao.alunos.dto.CursoDTO;
import br.com.gestao.alunos.exception.BusinessException;
import br.com.gestao.alunos.exception.ResourceNotFoundException;
import br.com.gestao.alunos.model.Curso;
import br.com.gestao.alunos.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CursoServiceTest {
    
    @Mock
    private CursoRepository cursoRepository;
    
    @InjectMocks
    private CursoService cursoService;
    
    private Curso curso;
    private CursoDTO cursoDTO;
    
    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNome("Ciência da Computação");
        curso.setDescricao("Curso de CC");
        
        cursoDTO = new CursoDTO();
        cursoDTO.setNome("Ciência da Computação");
        cursoDTO.setDescricao("Curso de CC");
    }
    
    @Test
    void criarCurso_Sucesso() {
        when(cursoRepository.existsByNome(anyString())).thenReturn(false);
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);
        
        CursoDTO resultado = cursoService.criarCurso(cursoDTO);
        
        assertNotNull(resultado);
        assertEquals("Ciência da Computação", resultado.getNome());
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }
    
    @Test
    void criarCurso_NomeJaExiste_DeveLancarException() {
        when(cursoRepository.existsByNome(anyString())).thenReturn(true);
        
        assertThrows(BusinessException.class, () -> cursoService.criarCurso(cursoDTO));
        verify(cursoRepository, never()).save(any(Curso.class));
    }
    
    @Test
    void listarTodosCursos_Sucesso() {
        Curso curso2 = new Curso();
        curso2.setId(2L);
        curso2.setNome("Engenharia de Software");
        curso2.setDescricao("Curso de ES");
        
        when(cursoRepository.findAll()).thenReturn(Arrays.asList(curso, curso2));
        
        List<CursoDTO> resultado = cursoService.listarTodosCursos();
        
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(cursoRepository, times(1)).findAll();
    }
    
    @Test
    void buscarCursoPorId_Sucesso() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        
        CursoDTO resultado = cursoService.buscarCursoPorId(1L);
        
        assertNotNull(resultado);
        assertEquals("Ciência da Computação", resultado.getNome());
        verify(cursoRepository, times(1)).findById(1L);
    }
    
    @Test
    void buscarCursoPorId_NaoEncontrado_DeveLancarException() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> cursoService.buscarCursoPorId(1L));
    }
    
    @Test
    void atualizarCurso_Sucesso() {
        CursoDTO cursoDTOAtualizado = new CursoDTO();
        cursoDTOAtualizado.setNome("Ciência da Computação Atualizado");
        cursoDTOAtualizado.setDescricao("Descrição atualizada");
        
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.existsByNome(anyString())).thenReturn(false);
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);
        
        CursoDTO resultado = cursoService.atualizarCurso(1L, cursoDTOAtualizado);
        
        assertNotNull(resultado);
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }
    
    @Test
    void atualizarCurso_NomeJaExiste_DeveLancarException() {
        CursoDTO cursoDTOAtualizado = new CursoDTO();
        cursoDTOAtualizado.setNome("Engenharia de Software"); // Nome de outro curso
        cursoDTOAtualizado.setDescricao("Descrição");
        
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.existsByNome("Engenharia de Software")).thenReturn(true);
        
        assertThrows(BusinessException.class, () -> cursoService.atualizarCurso(1L, cursoDTOAtualizado));
        verify(cursoRepository, never()).save(any(Curso.class));
    }
    
    @Test
    void atualizarCurso_MesmoNome_DevePermitir() {
        CursoDTO cursoDTOAtualizado = new CursoDTO();
        cursoDTOAtualizado.setNome("Ciência da Computação"); // Mesmo nome
        cursoDTOAtualizado.setDescricao("Descrição atualizada");
        
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(cursoRepository.existsByNome("Ciência da Computação")).thenReturn(true);
        when(cursoRepository.save(any(Curso.class))).thenReturn(curso);
        
        // Não deve lançar exceção pois é o mesmo curso
        CursoDTO resultado = cursoService.atualizarCurso(1L, cursoDTOAtualizado);
        
        assertNotNull(resultado);
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }
    
    @Test
    void buscarOuCriarCurso_CursoExistente_DeveRetornarExistente() {
        when(cursoRepository.findByNome("Ciência da Computação")).thenReturn(Optional.of(curso));
        
        Curso resultado = cursoService.buscarOuCriarCurso("Ciência da Computação");
        
        assertNotNull(resultado);
        assertEquals(curso.getId(), resultado.getId());
        verify(cursoRepository, times(1)).findByNome("Ciência da Computação");
        verify(cursoRepository, never()).save(any(Curso.class));
    }
    
    @Test
    void buscarOuCriarCurso_CursoNaoExistente_DeveCriarNovo() {
        Curso novoCurso = new Curso();
        novoCurso.setId(2L);
        novoCurso.setNome("Novo Curso");
        
        when(cursoRepository.findByNome("Novo Curso")).thenReturn(Optional.empty());
        when(cursoRepository.save(any(Curso.class))).thenReturn(novoCurso);
        
        Curso resultado = cursoService.buscarOuCriarCurso("Novo Curso");
        
        assertNotNull(resultado);
        assertEquals("Novo Curso", resultado.getNome());
        verify(cursoRepository, times(1)).findByNome("Novo Curso");
        verify(cursoRepository, times(1)).save(any(Curso.class));
    }
    
    @Test
    void atualizarCurso_NaoEncontrado_DeveLancarException() {
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> cursoService.atualizarCurso(1L, cursoDTO));
    }
    
    @Test
    void deletarCurso_Sucesso() {
        when(cursoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cursoRepository).deleteById(1L);
        
        cursoService.deletarCurso(1L);
        
        verify(cursoRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void deletarCurso_NaoEncontrado_DeveLancarException() {
        when(cursoRepository.existsById(1L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> cursoService.deletarCurso(1L));
        verify(cursoRepository, never()).deleteById(anyLong());
    }
}

