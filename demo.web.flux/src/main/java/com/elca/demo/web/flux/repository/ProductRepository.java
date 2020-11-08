package com.elca.demo.web.flux.repository;

import com.elca.demo.web.flux.model.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product,String> {
}
