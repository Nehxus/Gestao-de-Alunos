package br.com.gestao.alunos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

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
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        DataSourceProperties properties = new DataSourceProperties();
        
        String databaseUrl = System.getenv("DATABASE_URL");
        
        if (databaseUrl == null || databaseUrl.isEmpty()) {
            logger.warn("DATABASE_URL não configurada");
            return properties;
        }
        
        // Se já está em formato JDBC, usa direto
        if (databaseUrl.startsWith("jdbc:")) {
            logger.info("DATABASE_URL já está no formato JDBC");
            properties.setUrl(databaseUrl);
            return properties;
        }
        
        // Se está no formato postgres://, converte
        if (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://")) {
            logger.info("Convertendo DATABASE_URL de postgres:// para JDBC...");
            try {
                String jdbcUrl = convertToJdbcUrl(databaseUrl);
                String[] credentials = extractCredentials(databaseUrl);
                
                properties.setUrl(jdbcUrl);
                
                if (credentials != null && credentials[0] != null) {
                    properties.setUsername(credentials[0]);
                }
                if (credentials != null && credentials.length > 1 && credentials[1] != null) {
                    properties.setPassword(credentials[1]);
                }
                
                logger.info("DATABASE_URL convertida com sucesso");
            } catch (Exception e) {
                logger.error("Erro ao converter DATABASE_URL: {}", e.getMessage(), e);
            }
        }
        
        return properties;
    }

    @Bean
    @Primary
    public DataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().build();
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

