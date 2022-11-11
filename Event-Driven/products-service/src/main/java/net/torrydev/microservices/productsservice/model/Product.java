package net.torrydev.microservices.productsservice.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.math.BigDecimal;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor @NoArgsConstructor @Data @Builder
@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long Id;
    String name;
    String description;
    String picurl;
    String brand;
    String category;
    BigDecimal price;
}
