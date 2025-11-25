-- Dados iniciais para desenvolvimento (apenas perfil dev)

-- Inserir cursos
INSERT INTO cursos (nome, descricao) VALUES 
('Ciência da Computação', 'Curso de Ciência da Computação'),
('Engenharia de Software', 'Curso de Engenharia de Software'),
('Sistemas de Informação', 'Curso de Sistemas de Informação'),
('Análise e Desenvolvimento de Sistemas', 'Curso de ADS');

-- Inserir alunos
INSERT INTO alunos (nome, matricula, email, curso_id, semestre, media_geral, data_matricula) VALUES 
('João Silva', '2024001', 'joao.silva@email.com', 1, 3, 8.5, CURRENT_DATE),
('Maria Santos', '2024002', 'maria.santos@email.com', 1, 2, 9.0, CURRENT_DATE),
('Pedro Oliveira', '2024003', 'pedro.oliveira@email.com', 2, 4, 7.8, CURRENT_DATE),
('Ana Costa', '2024004', 'ana.costa@email.com', 2, 3, 9.2, CURRENT_DATE),
('Carlos Souza', '2024005', 'carlos.souza@email.com', 3, 5, 8.0, CURRENT_DATE);

