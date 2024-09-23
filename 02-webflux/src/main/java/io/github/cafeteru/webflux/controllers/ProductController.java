package io.github.cafeteru.webflux.controllers;

import io.github.cafeteru.webflux.models.Product;
import io.github.cafeteru.webflux.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping({"/products", "/"})
    public String list(Model model) {
        var products = productService.findAll();
        products.subscribe(product -> log.info(product.toString()));
        model.addAttribute("products", products);
        model.addAttribute("title", "Products");
        return "products";
    }

    @GetMapping("/form")
    public Mono<String> create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("title", "Product form");
        return Mono.just("form");
    }

    @PostMapping("/form")
    public Mono<String> save(Product product) {
        return productService.save(product)
                .doOnNext(p -> log.info("Product saved: " + p))
                .thenReturn("redirect:/products");
    }

    @GetMapping("/products-data-driver")
    public String listDataDriver(Model model) {
        var products = productService.findAll().delayElements(Duration.ofSeconds(1));
        products.subscribe(product -> log.info(product.toString()));
        // Para ir mostrando los productos conforme se van obteniendo de dos en dos, no esperar a obtener todos
        // No pagina
        var attributeValue = new ReactiveDataDriverContextVariable(products, 2);
        model.addAttribute("products", attributeValue);
        model.addAttribute("title", "Products data driver");
        return "products";
    }

    @GetMapping("/products-full")
    public String listFull(Model model) {
        var products = productService.findAll().repeat(500);
        products.subscribe(product -> log.info(product.toString()));
        model.addAttribute("products", products);
        model.addAttribute("title", "Products full");
        return "products";
    }

    @GetMapping("/products-chunked")
    public String listChunked(Model model) {
        var products = productService.findAll().repeat(1_000);
        products.subscribe(product -> log.info(product.toString()));
        model.addAttribute("products", products);
        model.addAttribute("title", "Products chunked");
        return "products-chunked";
    }
}
