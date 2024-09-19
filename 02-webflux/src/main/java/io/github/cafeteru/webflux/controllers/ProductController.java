package io.github.cafeteru.webflux.controllers;

import io.github.cafeteru.webflux.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductRepository productRepository;

    @GetMapping({"/products", "/"})
    public String listProducts(Model model) {
        var products = productRepository.findAll();
        model.addAttribute("products", products);
        model.addAttribute("title", "Products");
        return "products";
    }
}
