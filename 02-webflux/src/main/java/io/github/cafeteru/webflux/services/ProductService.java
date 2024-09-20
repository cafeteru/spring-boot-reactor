package io.github.cafeteru.webflux.services;

import io.github.cafeteru.webflux.models.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ProductService {
    Flux<Product> findAll();

    Mono<Product> findById(String id);

    Mono<Product> save(Product product);

    Mono<Void> deleteById(String id);
}
