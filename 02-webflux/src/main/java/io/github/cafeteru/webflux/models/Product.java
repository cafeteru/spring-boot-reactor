package io.github.cafeteru.webflux.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;

    private String name;
    private double price;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;

    public Product(String name, double price) {
        this.name = name;
        this.price = price;
        this.createdAt = new Date();
    }
}
