package br.com.gestao.alunos.repository;

import br.com.gestao.alunos.model.Curso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {
    
    Optional<Curso> findByNome(String nome);
    
    boolean existsByNome(String nome);
}

