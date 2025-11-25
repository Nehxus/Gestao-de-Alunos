package br.com.gestao.alunos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuração do banco de dados para produção.
 * Converte DATABASE_URL do formato postgres:// para jdbc:postgresql://
 * e injeta as propriedades no ambiente ANTES do Spring Boot fazer auto-configuração
 */
@Configuration
@Profile("prod")
public class DatabaseConfig implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment env = event.getEnvironment();
        Map<String, Object> props = new HashMap<>();
        
        // CRÍTICO: Desabilitar completamente a auto-configuração do H2
        // Isso força o Spring Boot a usar apenas PostgreSQL
        props.put("spring.autoconfigure.exclude", 
            "org.springframework.boot.autoconfigure.h2.H2ConsoleAutoConfiguration");
        
        // IMPORTANTE: Forçar uso do PostgreSQL e desabilitar H2 em produção
        // Isso deve ser feito ANTES de qualquer auto-configuração do Spring Boot
        props.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
        props.put("spring.h2.console.enabled", "false");
        props.put("spring.jpa.database-platform", "org.hibernate.dialect.PostgreSQLDialect");
        // Garantir que o Spring Boot não tente usar H2 como fallback
        props.put("spring.datasource.continue-on-error", "false");
        // Desabilitar auto-configuração de DataSource embutido
        props.put("spring.datasource.embedded", "false");
        
        String databaseUrl = System.getenv("DATABASE_URL");
        
        // Se não tem DATABASE_URL, falha explicitamente (não usa H2 como fallback)
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            logger.error("================================================");
            logger.error("ERRO: DATABASE_URL não configurada no Render!");
            logger.error("================================================");
            logger.error("Para corrigir:");
            logger.error("1. No Render Dashboard, vá em seu serviço Web");
            logger.error("2. Vá em 'Environment'");
            logger.error("3. Adicione a variável DATABASE_URL com o valor do seu banco PostgreSQL");
            logger.error("   (Você encontra essa URL no dashboard do seu banco PostgreSQL no Render)");
            logger.error("4. O formato deve ser: postgres://user:password@host:port/database");
            logger.error("================================================");
            // Define uma URL inválida para forçar erro de PostgreSQL (não H2)
            // Isso garante que o Spring Boot não tente usar H2 como fallback
            props.put("spring.datasource.url", "jdbc:postgresql://localhost:5432/INVALID_DATABASE_URL_NOT_CONFIGURED");
            props.put("spring.datasource.username", "INVALID");
            props.put("spring.datasource.password", "INVALID");
            env.getPropertySources().addFirst(new MapPropertySource("databaseConfig", props));
            return;
        }
        
        // Se já está em formato JDBC, configura direto
        if (databaseUrl.startsWith("jdbc:")) {
            logger.info("DATABASE_URL já está no formato JDBC, configurando PostgreSQL...");
            props.put("spring.datasource.url", databaseUrl);
            
            // Tenta usar DATABASE_USERNAME e DATABASE_PASSWORD se disponíveis
            String username = System.getenv("DATABASE_USERNAME");
            String password = System.getenv("DATABASE_PASSWORD");
            if (username != null) {
                props.put("spring.datasource.username", username);
            }
            if (password != null) {
                props.put("spring.datasource.password", password);
            }
            
            env.getPropertySources().addFirst(new MapPropertySource("databaseConfig", props));
            logger.info("Configuração do PostgreSQL aplicada (formato JDBC)");
            return;
        }
        
        // Se está no formato postgres://, converte para jdbc: e configura
        if (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://")) {
            logger.info("Convertendo DATABASE_URL de postgres:// para formato JDBC...");
            try {
                String jdbcUrl = convertToJdbcUrl(databaseUrl);
                String[] credentials = extractCredentials(databaseUrl);
                
                props.put("spring.datasource.url", jdbcUrl);
                
                if (credentials != null && credentials[0] != null) {
                    props.put("spring.datasource.username", credentials[0]);
                }
                if (credentials != null && credentials.length > 1 && credentials[1] != null) {
                    props.put("spring.datasource.password", credentials[1]);
                }
                
                env.getPropertySources().addFirst(new MapPropertySource("databaseConfig", props));
                logger.info("DATABASE_URL convertida e configurada com sucesso - PostgreSQL será usado");
            } catch (Exception e) {
                logger.error("Erro ao converter DATABASE_URL: {}", e.getMessage(), e);
                // Mesmo com erro, força PostgreSQL para evitar H2
                env.getPropertySources().addFirst(new MapPropertySource("databaseConfig", props));
            }
        } else {
            // URL em formato desconhecido, mas força PostgreSQL
            logger.warn("Formato de DATABASE_URL desconhecido: {}. Forçando uso do PostgreSQL.", databaseUrl);
            env.getPropertySources().addFirst(new MapPropertySource("databaseConfig", props));
        }
    }

    private String convertToJdbcUrl(String postgresUrl) throws URISyntaxException {
        String url = postgresUrl.replace("postgres://", "http://")
                .replace("postgresql://", "http://");
        URI dbUri = new URI(url);
        
        String host = dbUri.getHost();
        int port = dbUri.getPort() > 0 ? dbUri.getPort() : 5432;
        String path = dbUri.getPath();
        
        if (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        
        return String.format("jdbc:postgresql://%s:%d/%s", host, port, path);
    }

    private String[] extractCredentials(String postgresUrl) {
        try {
            String url = postgresUrl.replace("postgres://", "http://")
                    .replace("postgresql://", "http://");
            URI dbUri = new URI(url);
            
            if (dbUri.getUserInfo() != null) {
                String[] userInfo = dbUri.getUserInfo().split(":");
                if (userInfo.length >= 2) {
                    return new String[]{userInfo[0], userInfo[1]};
                } else if (userInfo.length == 1) {
                    return new String[]{userInfo[0], ""};
                }
            }
            return null;
        } catch (URISyntaxException e) {
            logger.warn("Erro ao extrair credenciais da URL: {}", e.getMessage());
            return null;
        }
    }
}

