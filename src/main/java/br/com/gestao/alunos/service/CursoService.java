package br.com.gestao.alunos.service;

import br.com.gestao.alunos.dto.CursoDTO;
import br.com.gestao.alunos.exception.BusinessException;
import br.com.gestao.alunos.exception.ResourceNotFoundException;
import br.com.gestao.alunos.model.Curso;
import br.com.gestao.alunos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CursoService {
    
    private final CursoRepository cursoRepository;
    
    @Autowired
    public CursoService(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }
    
    @Transactional
    public CursoDTO criarCurso(CursoDTO cursoDTO) {
        if (cursoRepository.existsByNome(cursoDTO.getNome())) {
            throw new BusinessException("Já existe um curso com o nome: " + cursoDTO.getNome());
        }
        
        Curso curso = new Curso();
        curso.setNome(cursoDTO.getNome());
        curso.setDescricao(cursoDTO.getDescricao());
        
        Curso cursoSalvo = cursoRepository.save(curso);
        return converterParaDTO(cursoSalvo);
    }
    
    public List<CursoDTO> listarTodosCursos() {
        return cursoRepository.findAll().stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    public CursoDTO buscarCursoPorId(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com ID: " + id));
        return converterParaDTO(curso);
    }
    
    @Transactional
    public CursoDTO atualizarCurso(Long id, CursoDTO cursoDTO) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Curso não encontrado com ID: " + id));
        
        if (cursoRepository.existsByNome(cursoDTO.getNome()) && 
            !curso.getNome().equals(cursoDTO.getNome())) {
            throw new BusinessException("Já existe um curso com o nome: " + cursoDTO.getNome());
        }
        
        curso.setNome(cursoDTO.getNome());
        curso.setDescricao(cursoDTO.getDescricao());
        
        Curso cursoAtualizado = cursoRepository.save(curso);
        return converterParaDTO(cursoAtualizado);
    }
    
    @Transactional
    public void deletarCurso(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Curso não encontrado com ID: " + id);
        }
        cursoRepository.deleteById(id);
    }
    
    public Curso buscarOuCriarCurso(String nomeCurso) {
        return cursoRepository.findByNome(nomeCurso)
                .orElseGet(() -> {
                    Curso novoCurso = new Curso();
                    novoCurso.setNome(nomeCurso);
                    return cursoRepository.save(novoCurso);
                });
    }
    
    private CursoDTO converterParaDTO(Curso curso) {
        CursoDTO dto = new CursoDTO();
        dto.setId(curso.getId());
        dto.setNome(curso.getNome());
        dto.setDescricao(curso.getDescricao());
        return dto;
    }
}

