package br.com.gestao.alunos.service;

import br.com.gestao.alunos.dto.AlunoDTO;
import br.com.gestao.alunos.dto.AlunoResponseDTO;
import br.com.gestao.alunos.exception.BusinessException;
import br.com.gestao.alunos.exception.ResourceNotFoundException;
import br.com.gestao.alunos.model.Aluno;
import br.com.gestao.alunos.model.Curso;
import br.com.gestao.alunos.repository.AlunoRepository;
import br.com.gestao.alunos.repository.CursoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {
    
    @Mock
    private AlunoRepository alunoRepository;
    
    @Mock
    private CursoRepository cursoRepository;
    
    @InjectMocks
    private AlunoService alunoService;
    
    private Curso curso;
    private Aluno aluno;
    private AlunoDTO alunoDTO;
    
    @BeforeEach
    void setUp() {
        curso = new Curso();
        curso.setId(1L);
        curso.setNome("Ciência da Computação");
        curso.setDescricao("Curso de CC");
        
        aluno = new Aluno();
        aluno.setId(1L);
        aluno.setNome("João Silva");
        aluno.setMatricula("2024001");
        aluno.setEmail("joao@email.com");
        aluno.setCurso(curso);
        aluno.setSemestre(3);
        aluno.setMediaGeral(8.5);
        aluno.setDataMatricula(LocalDate.now());
        
        alunoDTO = new AlunoDTO();
        alunoDTO.setNome("João Silva");
        alunoDTO.setMatricula("2024001");
        alunoDTO.setEmail("joao@email.com");
        alunoDTO.setCursoId(1L);
        alunoDTO.setSemestre(3);
        alunoDTO.setMediaGeral(8.5);
    }
    
    @Test
    void criarAluno_Sucesso() {
        when(alunoRepository.existsByMatricula(anyString())).thenReturn(false);
        when(alunoRepository.existsByEmail(anyString())).thenReturn(false);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);
        
        AlunoResponseDTO resultado = alunoService.criarAluno(alunoDTO);
        
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        assertEquals("2024001", resultado.getMatricula());
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }
    
    @Test
    void criarAluno_MatriculaJaExiste_DeveLancarException() {
        when(alunoRepository.existsByMatricula(anyString())).thenReturn(true);
        
        assertThrows(BusinessException.class, () -> alunoService.criarAluno(alunoDTO));
        verify(alunoRepository, never()).save(any(Aluno.class));
    }
    
    @Test
    void criarAluno_EmailJaExiste_DeveLancarException() {
        when(alunoRepository.existsByMatricula(anyString())).thenReturn(false);
        when(alunoRepository.existsByEmail(anyString())).thenReturn(true);
        
        assertThrows(BusinessException.class, () -> alunoService.criarAluno(alunoDTO));
        verify(alunoRepository, never()).save(any(Aluno.class));
    }
    
    @Test
    void criarAluno_CursoNaoEncontrado_DeveLancarException() {
        when(alunoRepository.existsByMatricula(anyString())).thenReturn(false);
        when(alunoRepository.existsByEmail(anyString())).thenReturn(false);
        when(cursoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> alunoService.criarAluno(alunoDTO));
        verify(alunoRepository, never()).save(any(Aluno.class));
    }
    
    @Test
    void listarTodosAlunos_Sucesso() {
        Aluno aluno2 = new Aluno();
        aluno2.setId(2L);
        aluno2.setNome("Maria Santos");
        aluno2.setMatricula("2024002");
        aluno2.setEmail("maria@email.com");
        aluno2.setCurso(curso);
        aluno2.setSemestre(2);
        aluno2.setMediaGeral(9.0);
        
        when(alunoRepository.findAll()).thenReturn(Arrays.asList(aluno, aluno2));
        
        List<AlunoResponseDTO> resultado = alunoService.listarTodosAlunos();
        
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(alunoRepository, times(1)).findAll();
    }
    
    @Test
    void buscarAlunoPorId_Sucesso() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        
        AlunoResponseDTO resultado = alunoService.buscarAlunoPorId(1L);
        
        assertNotNull(resultado);
        assertEquals("João Silva", resultado.getNome());
        verify(alunoRepository, times(1)).findById(1L);
    }
    
    @Test
    void buscarAlunoPorId_NaoEncontrado_DeveLancarException() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> alunoService.buscarAlunoPorId(1L));
    }
    
    @Test
    void atualizarAluno_Sucesso() {
        AlunoDTO alunoDTOAtualizado = new AlunoDTO();
        alunoDTOAtualizado.setNome("João Silva Atualizado");
        alunoDTOAtualizado.setMatricula("2024001");
        alunoDTOAtualizado.setEmail("joao.novo@email.com");
        alunoDTOAtualizado.setCursoId(1L);
        alunoDTOAtualizado.setSemestre(4);
        alunoDTOAtualizado.setMediaGeral(9.0);
        
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.existsByMatricula(anyString())).thenReturn(false);
        when(alunoRepository.existsByEmail(anyString())).thenReturn(false);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);
        
        AlunoResponseDTO resultado = alunoService.atualizarAluno(1L, alunoDTOAtualizado);
        
        assertNotNull(resultado);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }
    
    @Test
    void atualizarAluno_MatriculaJaExiste_DeveLancarException() {
        Aluno outroAluno = new Aluno();
        outroAluno.setId(2L);
        outroAluno.setMatricula("2024002");
        
        AlunoDTO alunoDTOAtualizado = new AlunoDTO();
        alunoDTOAtualizado.setNome("João Silva");
        alunoDTOAtualizado.setMatricula("2024002"); // Matrícula de outro aluno
        alunoDTOAtualizado.setEmail("joao@email.com");
        alunoDTOAtualizado.setCursoId(1L);
        alunoDTOAtualizado.setSemestre(3);
        alunoDTOAtualizado.setMediaGeral(8.5);
        
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.existsByMatricula("2024002")).thenReturn(true);
        
        assertThrows(BusinessException.class, () -> alunoService.atualizarAluno(1L, alunoDTOAtualizado));
        verify(alunoRepository, never()).save(any(Aluno.class));
    }
    
    @Test
    void atualizarAluno_EmailJaExiste_DeveLancarException() {
        AlunoDTO alunoDTOAtualizado = new AlunoDTO();
        alunoDTOAtualizado.setNome("João Silva");
        alunoDTOAtualizado.setMatricula("2024001");
        alunoDTOAtualizado.setEmail("maria@email.com"); // Email de outro aluno
        alunoDTOAtualizado.setCursoId(1L);
        alunoDTOAtualizado.setSemestre(3);
        alunoDTOAtualizado.setMediaGeral(8.5);
        
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.existsByMatricula("2024001")).thenReturn(false);
        when(alunoRepository.existsByEmail("maria@email.com")).thenReturn(true);
        
        assertThrows(BusinessException.class, () -> alunoService.atualizarAluno(1L, alunoDTOAtualizado));
        verify(alunoRepository, never()).save(any(Aluno.class));
    }
    
    @Test
    void atualizarAluno_CursoNaoEncontrado_DeveLancarException() {
        AlunoDTO alunoDTOAtualizado = new AlunoDTO();
        alunoDTOAtualizado.setNome("João Silva");
        alunoDTOAtualizado.setMatricula("2024001");
        alunoDTOAtualizado.setEmail("joao@email.com");
        alunoDTOAtualizado.setCursoId(999L); // Curso inexistente
        alunoDTOAtualizado.setSemestre(3);
        alunoDTOAtualizado.setMediaGeral(8.5);
        
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.existsByMatricula("2024001")).thenReturn(false);
        when(alunoRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(cursoRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> alunoService.atualizarAluno(1L, alunoDTOAtualizado));
        verify(alunoRepository, never()).save(any(Aluno.class));
    }
    
    @Test
    void criarAluno_MediaGeralNull_DeveDefinirZero() {
        alunoDTO.setMediaGeral(null);
        
        when(alunoRepository.existsByMatricula(anyString())).thenReturn(false);
        when(alunoRepository.existsByEmail(anyString())).thenReturn(false);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(alunoRepository.save(any(Aluno.class))).thenAnswer(invocation -> {
            Aluno alunoSalvo = invocation.getArgument(0);
            alunoSalvo.setId(1L);
            return alunoSalvo;
        });
        
        AlunoResponseDTO resultado = alunoService.criarAluno(alunoDTO);
        
        assertNotNull(resultado);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }
    
    @Test
    void atualizarAluno_MediaGeralNull_DeveManterValorAnterior() {
        aluno.setMediaGeral(8.5);
        AlunoDTO alunoDTOAtualizado = new AlunoDTO();
        alunoDTOAtualizado.setNome("João Silva");
        alunoDTOAtualizado.setMatricula("2024001");
        alunoDTOAtualizado.setEmail("joao@email.com");
        alunoDTOAtualizado.setCursoId(1L);
        alunoDTOAtualizado.setSemestre(3);
        alunoDTOAtualizado.setMediaGeral(null); // Media null
        
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(alunoRepository.existsByMatricula("2024001")).thenReturn(false);
        when(alunoRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(cursoRepository.findById(1L)).thenReturn(Optional.of(curso));
        when(alunoRepository.save(any(Aluno.class))).thenReturn(aluno);
        
        AlunoResponseDTO resultado = alunoService.atualizarAluno(1L, alunoDTOAtualizado);
        
        assertNotNull(resultado);
        verify(alunoRepository, times(1)).save(any(Aluno.class));
    }
    
    @Test
    void atualizarAluno_NaoEncontrado_DeveLancarException() {
        when(alunoRepository.findById(1L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> alunoService.atualizarAluno(1L, alunoDTO));
    }
    
    @Test
    void deletarAluno_Sucesso() {
        when(alunoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(alunoRepository).deleteById(1L);
        
        alunoService.deletarAluno(1L);
        
        verify(alunoRepository, times(1)).deleteById(1L);
    }
    
    @Test
    void deletarAluno_NaoEncontrado_DeveLancarException() {
        when(alunoRepository.existsById(1L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> alunoService.deletarAluno(1L));
        verify(alunoRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void buscarAlunosPorCurso_Sucesso() {
        when(alunoRepository.findByCursoId(1L)).thenReturn(Arrays.asList(aluno));
        
        List<AlunoResponseDTO> resultado = alunoService.buscarAlunosPorCurso(1L);
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(alunoRepository, times(1)).findByCursoId(1L);
    }
    
    @Test
    void buscarAlunosPorSemestre_Sucesso() {
        when(alunoRepository.findBySemestre(3)).thenReturn(Arrays.asList(aluno));
        
        List<AlunoResponseDTO> resultado = alunoService.buscarAlunosPorSemestre(3);
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(alunoRepository, times(1)).findBySemestre(3);
    }
    
    @Test
    void buscarAlunosPorMediaMinima_Sucesso() {
        when(alunoRepository.findByMediaGeralGreaterThanEqual(8.0)).thenReturn(Arrays.asList(aluno));
        
        List<AlunoResponseDTO> resultado = alunoService.buscarAlunosPorMediaMinima(8.0);
        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(alunoRepository, times(1)).findByMediaGeralGreaterThanEqual(8.0);
    }
}

