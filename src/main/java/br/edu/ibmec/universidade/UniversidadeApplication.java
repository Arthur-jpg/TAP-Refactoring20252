package br.edu.ibmec.universidade;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "br.edu.ibmec")
@EnableJpaRepositories(basePackages = "br.edu.ibmec.repository")
@EntityScan(basePackages = "br.edu.ibmec.entity")
public class UniversidadeApplication {

	public static void main(String[] args) {
		SpringApplication.run(UniversidadeApplication.class, args);
	}

}
