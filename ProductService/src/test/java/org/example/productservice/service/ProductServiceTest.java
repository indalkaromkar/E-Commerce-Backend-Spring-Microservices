package org.example.productservice.service;

import org.example.productservice.domain.dto.ProductDTO;
import org.example.productservice.domain.dto.CategoryDTO;
import org.example.productservice.domain.entity.Product;
import org.example.productservice.domain.entity.Category;

import org.example.productservice.repository.ProductRepository;
import org.example.productservice.service.impl.ProductServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImplementation productService;

    @Test
    void shouldFindAllProducts() {
        // Given
        Category category = Category.builder().categoryId(1).categoryName("Electronics").build();
        List<Product> products = Arrays.asList(
                Product.builder().productId(1).productName("Laptop").category(category).build(),
                Product.builder().productId(2).productName("Phone").category(category).build()
        );
        List<ProductDTO> productDTOs = Arrays.asList(
                ProductDTO.builder().productId(1).productTitle("Laptop").build(),
                ProductDTO.builder().productId(2).productTitle("Phone").build()
        );

        when(productRepository.findAll()).thenReturn(products);
        // Mock static method calls are not needed for static methods

        // When
        List<ProductDTO> result = productService.findAll();

        // Then
        assertThat(result).hasSize(2);
        verify(productRepository).findAll();
    }

    @Test
    void shouldFindProductById() {
        // Given
        Category category = Category.builder().categoryId(1).categoryName("Electronics").build();
        Product product = Product.builder().productId(1).productName("Laptop").category(category).build();
        ProductDTO productDTO = ProductDTO.builder().productId(1).productTitle("Laptop").build();

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        // Mock static method calls are not needed for static methods

        // When
        ProductDTO result = productService.findById(1);

        // Then
        assertThat(result).isNotNull();
        verify(productRepository).findById(1);
    }

    @Test
    void shouldSaveProduct() {
        // Given
        CategoryDTO categoryDTO = CategoryDTO.builder().categoryId(1).categoryName("Electronics").build();
        ProductDTO productDTO = ProductDTO.builder().productTitle("Laptop").categoryDto(categoryDTO).build();
        Category category = Category.builder().categoryId(1).categoryName("Electronics").build();
        Product product = Product.builder().productName("Laptop").category(category).build();
        Product savedProduct = Product.builder().productId(1).productName("Laptop").category(category).build();
        ProductDTO savedProductDTO = ProductDTO.builder().productId(1).productTitle("Laptop").build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        // When
        ProductDTO result = productService.save(productDTO);

        // Then
        assertThat(result).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void shouldDeleteProduct() {
        // When
        productService.deleteById(1);

        // Then
        verify(productRepository).deleteById(1);
    }
}