package io.github.cafeteru.webflux;

import io.github.cafeteru.webflux.models.Product;
import io.github.cafeteru.webflux.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.ArrayList;

@SpringBootApplication
@Slf4j

public class WebfluxApplication implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final ReactiveMongoTemplate mongoTemplate;

    @Autowired
    public WebfluxApplication(ProductRepository productRepository, ReactiveMongoTemplate mongoTemplate) {
        this.productRepository = productRepository;
        this.mongoTemplate = mongoTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(WebfluxApplication.class, args);
    }

    @Override
    public void run(String... args) {
        var products = new ArrayList<Product>();
        for (int i = 0; i < 25; i++) {
            products.add(new Product("Product " + i, 100.0 * i));
        }
        mongoTemplate.dropCollection("products")
                .thenMany(Flux.fromIterable(products))
                .flatMap(productRepository::save)
                .doOnNext(product -> log.info("Product saved: {}", product))
                .subscribe();
    }
}
