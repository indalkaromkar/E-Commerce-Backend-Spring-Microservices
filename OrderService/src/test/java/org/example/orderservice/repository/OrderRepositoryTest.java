package org.example.orderservice.repository;

import org.example.orderservice.domain.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveAndFindOrder() {
        // Given
        Order order = Order.builder()
                .userId(1)
                .totalAmount(BigDecimal.valueOf(99.99))
                .status("PENDING")
                .build();

        // When
        Order saved = orderRepository.save(order);
        Optional<Order> found = orderRepository.findById(saved.getOrderId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getUserId()).isEqualTo(1);
        assertThat(found.get().getTotalAmount()).isEqualTo(BigDecimal.valueOf(99.99));
        assertThat(found.get().getStatus()).isEqualTo("PENDING");
    }

    @Test
    void shouldFindOrdersByUserId() {
        // Given
        Order order1 = Order.builder().userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();
        Order order2 = Order.builder().userId(1).totalAmount(BigDecimal.valueOf(149.99)).build();
        Order order3 = Order.builder().userId(2).totalAmount(BigDecimal.valueOf(199.99)).build();
        
        entityManager.persistAndFlush(order1);
        entityManager.persistAndFlush(order2);
        entityManager.persistAndFlush(order3);

        // When
        var userOrders = orderRepository.findByUserId(1);

        // Then
        assertThat(userOrders).hasSize(2);
        assertThat(userOrders).allMatch(order -> order.getUserId().equals(1));
    }
}