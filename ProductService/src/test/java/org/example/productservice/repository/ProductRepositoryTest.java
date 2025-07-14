package org.example.productservice.repository;

import org.example.productservice.domain.entity.Category;
import org.example.productservice.domain.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndFindProduct() {
        // Given
        Category category = Category.builder()
                .categoryName("Electronics")
                .build();
        entityManager.persistAndFlush(category);

        Product product = Product.builder()
                .productName("Laptop")
                .price(999.99)
                .quantity(10)
                .category(category)
                .build();

        // When
        Product saved = productRepository.save(product);
        Optional<Product> found = productRepository.findById(saved.getProductId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getProductName()).isEqualTo("Laptop");
        assertThat(found.get().getPrice()).isEqualTo(999.99);
        assertThat(found.get().getCategory().getCategoryName()).isEqualTo("Electronics");
    }

    @Test
    void shouldDeleteProduct() {
        // Given
        Product product = Product.builder()
                .productName("Phone")
                .price(599.99)
                .build();
        Product saved = entityManager.persistAndFlush(product);

        // When
        productRepository.deleteById(saved.getProductId());

        // Then
        Optional<Product> found = productRepository.findById(saved.getProductId());
        assertThat(found).isEmpty();
    }
}