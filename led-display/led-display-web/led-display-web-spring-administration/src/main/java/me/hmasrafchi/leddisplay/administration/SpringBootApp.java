/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

import me.hmasrafchi.leddisplay.data.jpa.MatrixEntity;
import me.hmasrafchi.leddisplay.model.mapping.BeanMapper;
import me.hmasrafchi.leddisplay.model.mapping.BeanMapperJpa;

/**
 * @author michelin
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
@EntityScan(basePackages = "me.hmasrafchi.leddisplay.data.jpa")
public class SpringBootApp {
	public static void main(final String[] args) {
		SpringApplication.run(SpringBootApp.class, args);
	}

	@Bean
	public BeanMapper<MatrixEntity> beanMapper() {
		return new BeanMapperJpa();
	}
}