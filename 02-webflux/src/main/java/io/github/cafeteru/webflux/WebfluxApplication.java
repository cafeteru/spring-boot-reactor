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
        Product[] products = {new Product("Product 1", 100.0),
                new Product("Product 2", 200.0)};
        mongoTemplate.dropCollection("products").subscribe();
        Flux.just(products)
                .flatMap(productRepository::save)
                .subscribe(product -> log.info("Product saved: {}", product));
    }
}
