package br.com.gestao.alunos.service;

import br.com.gestao.alunos.dto.AlunoDTO;
import br.com.gestao.alunos.dto.AlunoResponseDTO;
import br.com.gestao.alunos.exception.BusinessException;
import br.com.gestao.alunos.exception.ResourceNotFoundException;
import br.com.gestao.alunos.model.Aluno;
import br.com.gestao.alunos.model.Curso;
import br.com.gestao.alunos.repository.AlunoRepository;
import br.com.gestao.alunos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlunoService {
    
    private final AlunoRepository alunoRepository;
    private final CursoRepository cursoRepository;
    
    @Autowired
    public AlunoService(AlunoRepository alunoRepository, CursoRepository cursoRepository) {
        this.alunoRepository = alunoRepository;
        this.cursoRepository = cursoRepository;
    }
    
    @Transactional
    public AlunoResponseDTO criarAluno(AlunoDTO alunoDTO) {
        if (alunoRepository.existsByMatricula(alunoDTO.getMatricula())) {
            throw new BusinessException("Já existe um aluno com a matrícula: " + alunoDTO.getMatricula());
        }
        
        if (alunoRepository.existsByEmail(alunoDTO.getEmail())) {
            throw new BusinessException("Já existe um aluno com o email: " + alunoDTO.getEmail());
        }
        
        Curso curso = cursoRepository.findById(alunoDTO.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com ID: " + alunoDTO.getCursoId()));
        
        Aluno aluno = new Aluno();
        aluno.setNome(alunoDTO.getNome());
        aluno.setMatricula(alunoDTO.getMatricula());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setCurso(curso);
        aluno.setSemestre(alunoDTO.getSemestre());
        aluno.setMediaGeral(alunoDTO.getMediaGeral() != null ? alunoDTO.getMediaGeral() : 0.0);
        aluno.setDataMatricula(LocalDate.now());
        
        Aluno alunoSalvo = alunoRepository.save(aluno);
        return converterParaResponseDTO(alunoSalvo);
    }
    
    public List<AlunoResponseDTO> listarTodosAlunos() {
        return alunoRepository.findAll().stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    public AlunoResponseDTO buscarAlunoPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));
        return converterParaResponseDTO(aluno);
    }
    
    @Transactional
    public AlunoResponseDTO atualizarAluno(Long id, AlunoDTO alunoDTO) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado com ID: " + id));
        
        if (alunoRepository.existsByMatricula(alunoDTO.getMatricula()) && 
            !aluno.getMatricula().equals(alunoDTO.getMatricula())) {
            throw new BusinessException("Já existe um aluno com a matrícula: " + alunoDTO.getMatricula());
        }
        
        if (alunoRepository.existsByEmail(alunoDTO.getEmail()) && 
            !aluno.getEmail().equals(alunoDTO.getEmail())) {
            throw new BusinessException("Já existe um aluno com o email: " + alunoDTO.getEmail());
        }
        
        Curso curso = cursoRepository.findById(alunoDTO.getCursoId())
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com ID: " + alunoDTO.getCursoId()));
        
        aluno.setNome(alunoDTO.getNome());
        aluno.setMatricula(alunoDTO.getMatricula());
        aluno.setEmail(alunoDTO.getEmail());
        aluno.setCurso(curso);
        aluno.setSemestre(alunoDTO.getSemestre());
        aluno.setMediaGeral(alunoDTO.getMediaGeral() != null ? alunoDTO.getMediaGeral() : aluno.getMediaGeral());
        
        Aluno alunoAtualizado = alunoRepository.save(aluno);
        return converterParaResponseDTO(alunoAtualizado);
    }
    
    @Transactional
    public void deletarAluno(Long id) {
        if (!alunoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aluno não encontrado com ID: " + id);
        }
        alunoRepository.deleteById(id);
    }
    
    public List<AlunoResponseDTO> buscarAlunosPorCurso(Long cursoId) {
        return alunoRepository.findByCursoId(cursoId).stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<AlunoResponseDTO> buscarAlunosPorSemestre(Integer semestre) {
        return alunoRepository.findBySemestre(semestre).stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    public List<AlunoResponseDTO> buscarAlunosPorMediaMinima(Double mediaMinima) {
        return alunoRepository.findByMediaGeralGreaterThanEqual(mediaMinima).stream()
                .map(this::converterParaResponseDTO)
                .collect(Collectors.toList());
    }
    
    private AlunoResponseDTO converterParaResponseDTO(Aluno aluno) {
        AlunoResponseDTO dto = new AlunoResponseDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setMatricula(aluno.getMatricula());
        dto.setEmail(aluno.getEmail());
        dto.setCursoId(aluno.getCurso().getId());
        dto.setCursoNome(aluno.getCurso().getNome());
        dto.setSemestre(aluno.getSemestre());
        dto.setMediaGeral(aluno.getMediaGeral());
        return dto;
    }
}

