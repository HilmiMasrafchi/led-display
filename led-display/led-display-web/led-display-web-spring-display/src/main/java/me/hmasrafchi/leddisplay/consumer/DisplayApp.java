/**
 * 
 */
package me.hmasrafchi.leddisplay.consumer;

import javax.jms.ConnectionFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

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
public class DisplayApp {
	public static void main(final String[] args) {
		SpringApplication.run(DisplayApp.class, args);
	}

	@Bean
	public BeanMapper<MatrixEntity> beanMapper() {
		return new BeanMapperJpa();
	}

	@Bean
	public JmsListenerContainerFactory<?> myFactory(ConnectionFactory connectionFactory,
			DefaultJmsListenerContainerFactoryConfigurer configurer) {
		final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
		configurer.configure(factory, connectionFactory);
		return factory;
	}

	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
}