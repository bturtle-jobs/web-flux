package com.elca.demo.web.flux;

import com.elca.demo.web.flux.handler.ProductHandler;
import com.elca.demo.web.flux.model.Product;
import com.elca.demo.web.flux.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

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

	@Bean
	RouterFunction<ServerResponse> routes(ProductHandler handler) {
		return route(GET("/products").
				and(accept(APPLICATION_JSON)), handler::getAllProducts)
				.andRoute(POST("/products").and(contentType(APPLICATION_JSON)), handler::saveProduct)
				.andRoute(DELETE("/products").and(accept(APPLICATION_JSON)), handler::deleteAllProducts)
				.andRoute(GET("/products/{id}").and(accept(APPLICATION_JSON)), handler::getProduct)
				.andRoute(PUT("/products/{id}").and(contentType(APPLICATION_JSON)), handler::updateProduct)
				.andRoute(DELETE("/products/{id}").and(accept(APPLICATION_JSON)), handler::deleteProduct)
				.andRoute(GET("/products/events").and(accept(TEXT_EVENT_STREAM)), handler::getProductEvents)
				;
	}

}
