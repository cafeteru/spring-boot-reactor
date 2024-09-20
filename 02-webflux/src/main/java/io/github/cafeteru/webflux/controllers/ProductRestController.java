package io.github.cafeteru.webflux.controllers;

import io.github.cafeteru.webflux.models.Product;
import io.github.cafeteru.webflux.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/api/products")
@RequiredArgsConstructor
@RestController
@Slf4j
public class ProductRestController {
    private final ProductRepository productRepository;

    @GetMapping
    public Flux<Product> index() {
        return productRepository.findAll()
                .map(product -> {
                    product.setName(product.getName().toUpperCase());
                    return product;
                })
                .doOnNext(product -> log.info(product.toString()));
    }

    @GetMapping("/{id}")
    public Mono<Product> show(@PathVariable String id) {
        return productRepository.findById(id)
                .doOnNext(product -> log.info(product.toString()))
                .switchIfEmpty(Mono.empty());
    }

}
