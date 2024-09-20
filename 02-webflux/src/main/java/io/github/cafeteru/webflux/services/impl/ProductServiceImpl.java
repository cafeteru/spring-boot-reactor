package io.github.cafeteru.webflux.services.impl;

import io.github.cafeteru.webflux.models.Product;
import io.github.cafeteru.webflux.services.ProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductServiceImpl implements ProductService {
    @Override
    public Flux<Product> findAll() {
        return null;
    }

    @Override
    public Mono<Product> findById(String id) {
        return null;
    }

    @Override
    public Mono<Product> save(Product product) {
        return null;
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return null;
    }
}
