package br.edu.ibmec.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

/**
 * Configuração do Swagger/OpenAPI para documentação da API.
 * Fornece interface web interativa para testar endpoints.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema Universitário - API REST")
                        .description("API REST para gestão de universidade com alunos, cursos, disciplinas, turmas e inscrições. " +
                                   "Sistema migrado para Spring Boot com JPA/Hibernate e MySQL seguindo princípios Clean Code.")
                        .version("2.0")
                        .contact(new Contact()
                                .name("Equipe de Desenvolvimento IBMEC")
                                .email("suporte@ibmec.edu.br")
                                .url("https://www.ibmec.br"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addServersItem(new Server()
                        .url("http://localhost:8080")
                        .description("Servidor de Desenvolvimento"))
                .addServersItem(new Server()
                        .url("https://api.universidade.ibmec.br")
                        .description("Servidor de Produção"));
    }
}