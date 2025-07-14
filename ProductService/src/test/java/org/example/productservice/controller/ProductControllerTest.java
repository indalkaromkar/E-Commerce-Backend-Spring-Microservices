package org.example.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.productservice.domain.dto.ProductDTO;
import org.example.productservice.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllProducts() throws Exception {
        // Given
        List<ProductDTO> products = Arrays.asList(
                ProductDTO.builder().productId(1).productName("Laptop").price(999.99).build(),
                ProductDTO.builder().productId(2).productName("Phone").price(599.99).build()
        );
        when(productService.findAll()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collection").isArray())
                .andExpect(jsonPath("$.collection[0].productName").value("Laptop"));
    }

    @Test
    void shouldGetProductById() throws Exception {
        // Given
        ProductDTO product = ProductDTO.builder().productId(1).productName("Laptop").price(999.99).build();
        when(productService.findById(1)).thenReturn(product);

        // When & Then
        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Laptop"))
                .andExpect(jsonPath("$.price").value(999.99));
    }

    @Test
    void shouldCreateProduct() throws Exception {
        // Given
        ProductDTO productDTO = ProductDTO.builder().productName("Laptop").price(999.99).build();
        ProductDTO savedProduct = ProductDTO.builder().productId(1).productName("Laptop").price(999.99).build();
        when(productService.save(any(ProductDTO.class))).thenReturn(savedProduct);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(1))
                .andExpect(jsonPath("$.productName").value("Laptop"));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        // Given
        ProductDTO productDTO = ProductDTO.builder().productId(1).productName("Updated Laptop").build();
        when(productService.update(eq(1), any(ProductDTO.class))).thenReturn(productDTO);

        // When & Then
        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value("Updated Laptop"));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}