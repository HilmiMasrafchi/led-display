/**
 * 
 */
package me.hmasrafchi.leddisplay.administration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author michelin
 *
 */
@SpringBootApplication
@EnableAutoConfiguration
public class SpringBootApp {
	public static void main(final String[] args) {
		SpringApplication.run(SpringBootApp.class, args);
	}
}