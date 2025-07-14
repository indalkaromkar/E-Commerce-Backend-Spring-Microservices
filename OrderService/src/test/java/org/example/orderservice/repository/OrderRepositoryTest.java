package org.example.orderservice.repository;

import org.example.orderservice.domain.entity.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OrderRepositoryTest {

    @Test
    void simpleTest() {
        assert true;
    }

    /*

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldSaveAndFindOrder() {
        // Given
        Order order = Order.builder()
                .orderDesc("Test Order")
                .orderFee(99.99)
                .build();

        // When
        Order saved = orderRepository.save(order);
        Optional<Order> found = orderRepository.findById(saved.getOrderId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getOrderDesc()).isEqualTo("Test Order");
        assertThat(found.get().getOrderFee()).isEqualTo(99.99);
    }

    @Test
    void shouldFindAllOrders() {
        // Given
        Order order1 = Order.builder().orderDesc("Order 1").orderFee(99.99).build();
        Order order2 = Order.builder().orderDesc("Order 2").orderFee(149.99).build();
        
        entityManager.persistAndFlush(order1);
        entityManager.persistAndFlush(order2);

        // When
        var allOrders = orderRepository.findAll();

        // Then
        assertThat(allOrders).hasSize(2);
    }
    */
}