package br.com.gestao.alunos;

import br.com.gestao.alunos.config.DatabaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GestaoAlunosApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(GestaoAlunosApplication.class);
        // Registrar o DatabaseConfig como listener ANTES de qualquer auto-configuração
        app.addListeners(new DatabaseConfig());
        app.run(args);
    }
}

