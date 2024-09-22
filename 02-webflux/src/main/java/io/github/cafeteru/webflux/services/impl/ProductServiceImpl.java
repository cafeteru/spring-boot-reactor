package io.github.cafeteru.webflux.services.impl;

import io.github.cafeteru.webflux.models.Product;
import io.github.cafeteru.webflux.repositories.ProductRepository;
import io.github.cafeteru.webflux.services.ProductService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Flux<Product> findAll() {
        return productRepository.findAll()
                .map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                });
    }

    @Override
    public Mono<Product> findById(String id) {
        return productRepository.findById(id);
    }

    @Override
    public Mono<Product> save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return productRepository.deleteById(id);
    }
}
