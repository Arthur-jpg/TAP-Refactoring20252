package br.edu.ibmec.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuração JPA para persistência de dados.
 * Habilita transações e repositórios JPA.
 */
@Configuration
@EnableJpaRepositories(basePackages = "br.edu.ibmec.dao")
@EnableTransactionManagement
public class JpaConfig {
    // Configurações adicionais podem ser adicionadas aqui se necessário
}