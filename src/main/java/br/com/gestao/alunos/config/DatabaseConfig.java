package br.com.gestao.alunos.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
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
 * 
 * IMPORTANTE: Esta classe deve ser registrada como listener no SpringApplication
 * para garantir que seja executada antes da auto-configuração do DataSource.
 */
public class DatabaseConfig implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment env = event.getEnvironment();
        
        // Verificar se o perfil "prod" está ativo
        String[] activeProfiles = env.getActiveProfiles();
        boolean isProdProfile = false;
        for (String profile : activeProfiles) {
            if ("prod".equals(profile)) {
                isProdProfile = true;
                break;
            }
        }
        
        // Também verificar se o perfil será ativado via propriedade
        String springProfilesActive = env.getProperty("spring.profiles.active");
        if (springProfilesActive != null && springProfilesActive.contains("prod")) {
            isProdProfile = true;
        }
        
        // Se não é perfil prod, não faz nada (deixa o H2 funcionar em dev)
        if (!isProdProfile) {
            logger.debug("Perfil 'prod' não está ativo. DatabaseConfig não será aplicado.");
            return;
        }
        
        logger.info("🔧 DatabaseConfig: Configurando PostgreSQL para perfil 'prod'...");
        
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
        
        // Log para debug - mostra se DATABASE_URL foi encontrada (sem mostrar a senha)
        if (databaseUrl != null && !databaseUrl.isEmpty()) {
            String maskedUrl = databaseUrl.replaceAll(":([^:@]+)@", ":****@");
            logger.info("DATABASE_URL encontrada: {}", maskedUrl);
        } else {
            logger.warn("DATABASE_URL não encontrada nas variáveis de ambiente");
            // Tenta também verificar outras variáveis comuns do Render
            logger.info("Verificando outras variáveis de ambiente...");
            String[] envVars = {"DATABASE_URL", "POSTGRES_URL", "POSTGRESQL_URL"};
            for (String var : envVars) {
                String value = System.getenv(var);
                if (value != null) {
                    logger.info("Variável {} encontrada", var);
                }
            }
        }
        
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
        
        // Se está no formato postgres:// ou postgresql://, converte para jdbc: e configura
        if (databaseUrl.startsWith("postgres://") || databaseUrl.startsWith("postgresql://")) {
            logger.info("Convertendo DATABASE_URL de postgresql:// para formato JDBC...");
            try {
                String jdbcUrl = convertToJdbcUrl(databaseUrl);
                String[] credentials = extractCredentials(databaseUrl);
                
                logger.info("JDBC URL gerada: jdbc:postgresql://{}:****/{}", 
                    extractHost(databaseUrl), extractDatabase(databaseUrl));
                
                props.put("spring.datasource.url", jdbcUrl);
                
                if (credentials != null && credentials[0] != null) {
                    props.put("spring.datasource.username", credentials[0]);
                    logger.info("Username configurado: {}", credentials[0]);
                }
                if (credentials != null && credentials.length > 1 && credentials[1] != null) {
                    props.put("spring.datasource.password", credentials[1]);
                    logger.info("Password configurado (oculto)");
                }
                
                env.getPropertySources().addFirst(new MapPropertySource("databaseConfig", props));
                logger.info("✅ DATABASE_URL convertida e configurada com sucesso - PostgreSQL será usado");
            } catch (Exception e) {
                logger.error("❌ Erro ao converter DATABASE_URL: {}", e.getMessage(), e);
                logger.error("URL original: {}", databaseUrl.replaceAll(":([^:@]+)@", ":****@"));
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
    
    private String extractHost(String postgresUrl) {
        try {
            String url = postgresUrl.replace("postgres://", "http://")
                    .replace("postgresql://", "http://");
            URI dbUri = new URI(url);
            return dbUri.getHost();
        } catch (URISyntaxException e) {
            return "unknown";
        }
    }
    
    private String extractDatabase(String postgresUrl) {
        try {
            String url = postgresUrl.replace("postgres://", "http://")
                    .replace("postgresql://", "http://");
            URI dbUri = new URI(url);
            String path = dbUri.getPath();
            if (path != null && path.startsWith("/")) {
                return path.substring(1);
            }
            return path != null ? path : "unknown";
        } catch (URISyntaxException e) {
            return "unknown";
        }
    }
}

