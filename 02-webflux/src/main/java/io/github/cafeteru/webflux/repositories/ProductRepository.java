package io.github.cafeteru.webflux.repositories;

import io.github.cafeteru.webflux.models.Product;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {
}
