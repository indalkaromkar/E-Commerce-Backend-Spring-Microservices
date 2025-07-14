package org.example.productservice.service;

import org.example.productservice.domain.dto.ProductDTO;
import org.example.productservice.domain.entity.Product;
import org.example.productservice.exception.ProductNotFoundException;
import org.example.productservice.mapper.ProductMapping;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapping productMapping;

    @InjectMocks
    private ProductServiceImplementation productService;

    @Test
    void shouldFindAllProducts() {
        // Given
        List<Product> products = Arrays.asList(
                Product.builder().productId(1).productName("Laptop").build(),
                Product.builder().productId(2).productName("Phone").build()
        );
        List<ProductDTO> productDTOs = Arrays.asList(
                ProductDTO.builder().productId(1).productName("Laptop").build(),
                ProductDTO.builder().productId(2).productName("Phone").build()
        );

        when(productRepository.findAll()).thenReturn(products);
        when(productMapping.mapToDto(products.get(0))).thenReturn(productDTOs.get(0));
        when(productMapping.mapToDto(products.get(1))).thenReturn(productDTOs.get(1));

        // When
        List<ProductDTO> result = productService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getProductName()).isEqualTo("Laptop");
        verify(productRepository).findAll();
    }

    @Test
    void shouldFindProductById() {
        // Given
        Product product = Product.builder().productId(1).productName("Laptop").build();
        ProductDTO productDTO = ProductDTO.builder().productId(1).productName("Laptop").build();

        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(productMapping.mapToDto(product)).thenReturn(productDTO);

        // When
        ProductDTO result = productService.findById(1);

        // Then
        assertThat(result.getProductName()).isEqualTo("Laptop");
        verify(productRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        when(productRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> productService.findById(1))
                .isInstanceOf(ProductNotFoundException.class);
    }

    @Test
    void shouldSaveProduct() {
        // Given
        ProductDTO productDTO = ProductDTO.builder().productName("Laptop").build();
        Product product = Product.builder().productName("Laptop").build();
        Product savedProduct = Product.builder().productId(1).productName("Laptop").build();
        ProductDTO savedProductDTO = ProductDTO.builder().productId(1).productName("Laptop").build();

        when(productMapping.mapToEntity(productDTO)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapping.mapToDto(savedProduct)).thenReturn(savedProductDTO);

        // When
        ProductDTO result = productService.save(productDTO);

        // Then
        assertThat(result.getProductId()).isEqualTo(1);
        verify(productRepository).save(product);
    }

    @Test
    void shouldDeleteProduct() {
        // Given
        when(productRepository.existsById(1)).thenReturn(true);

        // When
        productService.deleteById(1);

        // Then
        verify(productRepository).deleteById(1);
    }
}