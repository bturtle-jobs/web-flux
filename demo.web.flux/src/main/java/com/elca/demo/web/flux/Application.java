package com.elca.demo.web.flux;

import com.elca.demo.web.flux.model.Product;
import com.elca.demo.web.flux.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.beans.BeanProperty;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);
		System.out.println("running in main");
	}

	@Bean
	CommandLineRunner init(ProductRepository repository) {
		return args -> {
			Flux<Product> productFlux = Flux.just(
					new Product(null, "Quui 1", 2),
					new Product(null, "Quui 2", 2.5),
					new Product(null, "Quui 3", 3)
			).flatMap(repository::save);
			productFlux.thenMany(repository.findAll()).subscribe(System.out::println);

		};
	}

}
