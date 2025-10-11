package br.edu.ibmec.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/** Habilita gerenciamento de transações (@Transactional). */
@Configuration
@EnableTransactionManagement
public class TransactionConfig {
    // Configuração automática pelo Spring Boot
}