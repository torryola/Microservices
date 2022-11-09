package net.torrydev.microservices.productsservice.service;

import lombok.extern.slf4j.Slf4j;
import net.torrydev.microservices.productsservice.interfaces.IProductService;
import net.torrydev.microservices.productsservice.model.Product;
import net.torrydev.microservices.productsservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    private ProductRepository productRepository;

    /*
    Requirement:
     Brand and Category Name 1st letter is Capital
     */

    @Override
    public Product createNewProduct(Product product) {
        product.setBrand(upCase_FirstCharacter(product.getBrand()));
        product.setCategory(upCase_FirstCharacter(product.getCategory()));
      return productRepository.save(product);
    }

    @Override
    public Product findById(Long id) {
        if (null == id || id <= 0)
            throw new IllegalArgumentException("Invalid id, please enter valid id");
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> findByCategory(String category) {
        if (category == null || category.isBlank())
            throw new IllegalArgumentException("Invalid Category");
      return productRepository.findByCategory(upCase_FirstCharacter(category));
    }

    @Override
    public List<Product> findByBrand(String brand) {
        if (null == brand || brand.isBlank())
            throw new IllegalArgumentException("Unknown Brand");
        return productRepository.findByBrand(upCase_FirstCharacter(brand));
    }

    @Override
    public List<Product> findByBrandAndCategory(String brand, String category) {
        if ((null == brand || brand.isBlank()) || (null == category || category.isBlank()))
            throw new IllegalArgumentException("Invalid Brand or Category");
     return productRepository.findByBrandAndCategory(upCase_FirstCharacter(brand), upCase_FirstCharacter(category));
    }

    @Override
    public List<Product> findByBrandOrCategory(String brand, String category) {
        if ((null == brand || brand.isBlank()) || (null == category || category.isBlank()))
            throw new IllegalArgumentException("Invalid Brand or Category");
       return productRepository.findByBrandOrCategory(upCase_FirstCharacter(brand), upCase_FirstCharacter(category));
    }

    public static String upCase_FirstCharacter(String param){
        char[] charArray = param.toCharArray();
        if (Character.isUpperCase(charArray[0]))
            return param;
        charArray[0] = Character.toUpperCase(charArray[0]);
        StringBuilder builder = new StringBuilder();
        for (char c : charArray)
            builder.append(c);
        return builder.toString();
    }
}
