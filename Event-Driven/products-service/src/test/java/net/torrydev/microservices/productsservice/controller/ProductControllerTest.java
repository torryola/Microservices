package net.torrydev.microservices.productsservice.controller;

import com.google.gson.Gson;
import net.torrydev.microservices.productsservice.model.Product;
import net.torrydev.microservices.productsservice.service.ProductServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static net.torrydev.microservices.productsservice.controller.ProductController.PRODUCTS_BASE_URI;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ActiveProfiles("test")
class ProductControllerTest {
    @MockBean
    private ProductServiceImpl productService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    private Product product;
    private List<Product> productList;
    private static final String BASE_URL = PRODUCTS_BASE_URI;
    @BeforeEach
    void setUp() {
        product = Product.builder().Id(100L).brand("Adidas").category("Trainers").description("Lightweight Trainers")
                .picurl(null).price(new BigDecimal("65.0")).name("TrainFeather").build();
        productList = List.of(product);
    }

    @Test
    void createNewProduct() throws Exception {
        // when
        when(productService.createNewProduct(any(Product.class))).thenReturn(product);
        // then
        mockMvc.perform(post(BASE_URL+"/").accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(product))).andExpect(status().isCreated())
                .andExpect(jsonPath("$", Matchers.is("http://localhost/api/v1/products/product/"+product.getId())));
        //verify
        verify(productService).createNewProduct(any(Product.class));
    }

    @Test
    void findById() throws Exception {
        //given
        var id = 100;
        //when
        when(productService.findById((long) id)).thenReturn(product);
        //then
        mockMvc.perform(get(BASE_URL+"/{id}", id).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(id)))
                .andExpect(jsonPath("$.description", Matchers.is(product.getDescription())));
        // verify
        verify(productService).findById(anyLong());
    }

    @Test
    void findByCategory() throws Exception {
        //given
        var category = "Trainers";
        // when
        when(productService.findByCategory(category)).thenReturn(productList);
        // then
        mockMvc.perform(get(BASE_URL+"/category/{category}", category)
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(100)));
        // verify
        verify(productService).findByCategory(any());
    }

    @Test
    void findByBrand() {
        //given
        Stream.of("adidas", "Adidas").forEach(s -> {
            // when
            when(productService.findByBrand(s)).thenReturn(productList);
            // then
            try {
                mockMvc.perform(get(BASE_URL+"/brand/{brand}", s)
                                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.size()", Matchers.is(1)))
                        .andExpect(jsonPath("$[0].id", Matchers.is(100)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // verify
        verify(productService, times(2)).findByBrand(any());
    }

    @Test
    void findByBrandAndCategory() throws Exception {
        // given
        String brand = "adidas", category = "trainers";
        // when
        when(productService.findByBrandAndCategory(brand, category)).thenReturn(productList);
        // then
        mockMvc.perform(get(BASE_URL+"/brand/{brand}/category/{category}", brand, category)
                        .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$.[0].brand", Matchers.is(product.getBrand())))
                .andExpect(jsonPath("$.[0].description", Matchers.is(product.getDescription())));
        // verify
        verify(productService).findByBrandAndCategory(any(), any());
    }

    @Test
    void findByBrandOrCategory() throws Exception {
        // given
        String brand = "adidas", category = "trainers";
        // when
        when(productService.findByBrandOrCategory(brand, category)).thenReturn(productList);
        // then
        mockMvc.perform(get(BASE_URL+"/category/{category}/brand/{brand}", category, brand)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$.[0].brand", Matchers.is(product.getBrand())))
                .andExpect(jsonPath("$.[0].description", Matchers.is(product.getDescription())));
        // verify
        verify(productService).findByBrandOrCategory(any(), any());
    }
}