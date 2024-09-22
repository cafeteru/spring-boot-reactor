package io.github.cafeteru.webflux.controllers;

import io.github.cafeteru.webflux.models.Product;
import io.github.cafeteru.webflux.services.ProductService;
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
    private final ProductService productService;

    @GetMapping
    public Flux<Product> index() {
        return productService.findAll()
                .doOnNext(product -> log.info(product.toString()));
    }

    @GetMapping("/{id}")
    public Mono<Product> show(@PathVariable String id) {
        return productService.findById(id)
                .doOnNext(product -> log.info(product.toString()))
                .switchIfEmpty(Mono.empty());
    }

}
