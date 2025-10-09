package br.edu.ibmec.config;

import org.springframework.context.annotation.Configuration;

/**
 * Configurações adicionais de JPA podem ser adicionadas aqui se necessário.
 *
 * Observação: @EnableJpaRepositories e @EnableTransactionManagement
 * são configurados centralmente em outras classes (UniversidadeApplication/TransactionConfig)
 * para evitar duplicidade e conflitos de beans.
 */
@Configuration
public class JpaConfig {
    // Intencionalmente vazio
}