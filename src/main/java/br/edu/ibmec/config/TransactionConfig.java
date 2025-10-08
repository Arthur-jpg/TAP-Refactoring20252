package br.edu.ibmec.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Configuração de transações para o Spring Boot
 * 
 * Esta classe configura o gerenciamento de transações do Spring,
 * permitindo o uso de @Transactional nos serviços
 */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    // A configuração é feita automaticamente pelo Spring Boot
    // Esta classe serve apenas para garantir que @EnableTransactionManagement
    // esteja explicitamente habilitado
}