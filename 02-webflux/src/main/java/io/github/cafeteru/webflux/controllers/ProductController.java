package io.github.cafeteru.webflux.controllers;

import io.github.cafeteru.webflux.models.Product;
import io.github.cafeteru.webflux.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
@RequiredArgsConstructor
@SessionAttributes("product")
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
        model.addAttribute("button", "Add");
        return Mono.just("form");
    }

    @PostMapping("/form")
    public Mono<String> save(@Valid Product product, BindingResult result, Model model, SessionStatus status) {
        if (result.hasErrors()) {
            model.addAttribute("title", "Errors in product form");
            model.addAttribute("button", "Add");
            return Mono.just("form");
        }
        status.setComplete();
        return productService.save(product)
                .doOnNext(p -> log.info("Product saved: {}", p))
                .thenReturn("redirect:/products");
    }

    @GetMapping("/edit/{id}")
    public Mono<String> edit(@PathVariable String id, Model model) {
        var product = productService.findById(id).defaultIfEmpty(new Product());
        model.addAttribute("product", product);
        model.addAttribute("title", "Edit product");
        model.addAttribute("button", "Edit");
        return Mono.just("form");
    }

    @GetMapping("/edit-reactive/{id}")
    public Mono<String> editReactive(@PathVariable String id, Model model) {
        return productService.findById(id)
                .doOnNext(p -> {
                    model.addAttribute("product", p);
                    model.addAttribute("title", "Edit product");
                    model.addAttribute("button", "Edit");
                }).switchIfEmpty(Mono.error(new InterruptedException("Product not found")))
                .thenReturn("form")
                .onErrorResume(ex -> Mono.just("redirect:/products?error=Product+not+found"));
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

    @GetMapping("/delete/{id}")
    public Mono<String> delete(@PathVariable String id) {
        return productService.findById(id)
                .switchIfEmpty(Mono.error(new InterruptedException("Product not found")))
                .flatMap(product -> productService.deleteById(id)
                        .thenReturn("redirect:/products?success=Product+deleted"))
                .onErrorResume(ex -> Mono.just("redirect:/products?error=Product+not+found"));
    }

}
