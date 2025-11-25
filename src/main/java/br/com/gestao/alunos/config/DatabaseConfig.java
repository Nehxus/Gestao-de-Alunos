package br.com.gestao.alunos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.jdbc.DataSourceBuilder;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Configuração do banco de dados para produção.
 * Converte DATABASE_URL do formato postgres:// para jdbc:postgresql://
 */
@Configuration
@Profile("prod")
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    public DataSource dataSource() {
        String databaseUrl = System.getenv("DATABASE_URL");
        String databaseUsername = System.getenv("DATABASE_USERNAME");
        String databasePassword = System.getenv("DATABASE_PASSWORD");

        // Se DATABASE_URL está no formato postgres://, extrai username e password
        if (databaseUrl != null && (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://"))) {
            logger.info("DATABASE_URL detectada no formato postgres://, extraindo credenciais...");
            String[] credentials = extractCredentialsFromUrl(databaseUrl);
            if (credentials != null) {
                if (databaseUsername == null || databaseUsername.isEmpty()) {
                    databaseUsername = credentials[0];
                    logger.info("Username extraído da DATABASE_URL");
                }
                if (databasePassword == null || databasePassword.isEmpty()) {
                    databasePassword = credentials[1];
                    logger.info("Password extraído da DATABASE_URL");
                }
            }
        }

        String jdbcUrl = buildJdbcUrl(databaseUrl);
        
        logger.info("=== Configuração do Banco de Dados ===");
        logger.info("DATABASE_URL (original): {}", databaseUrl != null ? maskUrl(databaseUrl) : "NÃO CONFIGURADA");
        logger.info("JDBC URL (convertida): {}", maskUrl(jdbcUrl));
        logger.info("DATABASE_USERNAME: {}", databaseUsername != null && !databaseUsername.isEmpty() ? "***" : "NÃO CONFIGURADO");
        logger.info("DATABASE_PASSWORD: {}", databasePassword != null && !databasePassword.isEmpty() ? "***" : "NÃO CONFIGURADO");

        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            String errorMsg = "DATABASE_URL não configurada. Configure a variável de ambiente DATABASE_URL no Render.";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        if (databaseUsername == null || databaseUsername.isEmpty()) {
            String errorMsg = "DATABASE_USERNAME não configurada. Configure a variável de ambiente DATABASE_USERNAME no Render ou use DATABASE_URL no formato postgres://user:pass@host:port/db";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        if (databasePassword == null || databasePassword.isEmpty()) {
            String errorMsg = "DATABASE_PASSWORD não configurada. Configure a variável de ambiente DATABASE_PASSWORD no Render ou use DATABASE_URL no formato postgres://user:pass@host:port/db";
            logger.error(errorMsg);
            throw new RuntimeException(errorMsg);
        }

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(databaseUsername)
                .password(databasePassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }

    private String buildJdbcUrl(String databaseUrl) {
        // Se DATABASE_URL está vazia, tenta construir a partir de variáveis individuais
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            logger.warn("DATABASE_URL não configurada, tentando construir a partir de variáveis individuais");
            return buildFromIndividualVars();
        }

        // Se já está no formato jdbc:, retorna como está
        if (databaseUrl.startsWith("jdbc:postgresql://")) {
            logger.info("DATABASE_URL já está no formato JDBC");
            return databaseUrl;
        }

        // Se está no formato postgres://, converte para jdbc:postgresql://
        if (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://")) {
            logger.info("Convertendo DATABASE_URL de postgres:// para jdbc:postgresql://");
            return convertPostgresUrl(databaseUrl);
        }

        logger.warn("Formato de DATABASE_URL não reconhecido: {}", databaseUrl);
        return databaseUrl;
    }

    private String[] extractCredentialsFromUrl(String postgresUrl) {
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

    private String convertPostgresUrl(String postgresUrl) {
        try {
            // Remove o prefixo postgres:// ou postgresql://
            String url = postgresUrl.replace("postgres://", "http://")
                    .replace("postgresql://", "http://");

            URI dbUri = new URI(url);

            String host = dbUri.getHost();
            int port = dbUri.getPort() > 0 ? dbUri.getPort() : 5432;
            String path = dbUri.getPath();

            // Remove a barra inicial do path
            if (path != null && path.startsWith("/")) {
                path = path.substring(1);
            }

            String jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, path);
            logger.info("URL convertida com sucesso");
            return jdbcUrl;
        } catch (URISyntaxException e) {
            logger.error("Erro ao converter DATABASE_URL: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar DATABASE_URL: " + e.getMessage(), e);
        }
    }

    private String buildFromIndividualVars() {
        String host = System.getenv("DATABASE_HOST");
        if (host == null || host.isEmpty()) {
            host = "localhost";
        }

        String port = System.getenv("DATABASE_PORT");
        if (port == null || port.isEmpty()) {
            port = "5432";
        }

        String database = System.getenv("DATABASE_NAME");
        if (database == null || database.isEmpty()) {
            database = "gestaoalunos";
        }

        return String.format("jdbc:postgresql://%s:%s/%s", host, port, database);
    }

    private String maskUrl(String url) {
        if (url == null) return "null";
        // Mascara senha na URL se existir
        return url.replaceAll("password=[^&;]*", "password=***")
                  .replaceAll(":[^@]*@", ":***@");
    }
}
