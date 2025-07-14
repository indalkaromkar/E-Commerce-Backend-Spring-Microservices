package org.example.productservice.repository;

import org.example.productservice.domain.entity.Category;
import org.example.productservice.domain.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.flyway.enabled=false",
		"eureka.client.enabled=false"
})
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldSaveAndFindProduct() {
        // Given
        Product product = Product.builder()
                .productName("Laptop")
                .price(999.99)
                .quantity(10)
                .build();
        product.setCreatedAt(java.time.Instant.now());
        product.setUpdatedAt(java.time.Instant.now());

        // When
        Product saved = productRepository.save(product);
        Optional<Product> found = productRepository.findById(saved.getProductId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getProductName()).isEqualTo("Laptop");
        assertThat(found.get().getPrice()).isEqualTo(999.99);
    }

    @Test
    void shouldDeleteProduct() {
        // Given
        Product product = Product.builder()
                .productName("Phone")
                .price(599.99)
                .build();
        product.setCreatedAt(java.time.Instant.now());
        product.setUpdatedAt(java.time.Instant.now());
        Product saved = entityManager.persistAndFlush(product);

        // When
        productRepository.deleteById(saved.getProductId());

        // Then
        Optional<Product> found = productRepository.findById(saved.getProductId());
        assertThat(found).isEmpty();
    }
}