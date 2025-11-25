package br.com.gestao.alunos.repository;

import br.com.gestao.alunos.model.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    
    Optional<Aluno> findByMatricula(String matricula);
    
    Optional<Aluno> findByEmail(String email);
    
    boolean existsByMatricula(String matricula);
    
    boolean existsByEmail(String email);
    
    List<Aluno> findByCursoId(Long cursoId);
    
    List<Aluno> findBySemestre(Integer semestre);
    
    @Query("SELECT a FROM Aluno a WHERE a.curso.nome = :cursoNome")
    List<Aluno> findByCursoNome(@Param("cursoNome") String cursoNome);
    
    @Query("SELECT a FROM Aluno a WHERE a.mediaGeral >= :mediaMinima ORDER BY a.mediaGeral DESC")
    List<Aluno> findByMediaGeralGreaterThanEqual(@Param("mediaMinima") Double mediaMinima);
}

