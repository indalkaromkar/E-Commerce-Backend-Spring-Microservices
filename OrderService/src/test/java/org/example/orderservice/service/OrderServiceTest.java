package org.example.orderservice.service;

import org.example.orderservice.domain.dtos.OrderDTO;
import org.example.orderservice.domain.entity.Order;
import org.example.orderservice.exception.OrderNotFoundException;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.service.impl.OrderServiceImpl;
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
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void shouldFindAllOrders() {
        // Given
        List<Order> orders = Arrays.asList(
                Order.builder().orderId(1).orderDesc("Order 1").orderFee(99.99).build(),
                Order.builder().orderId(2).orderDesc("Order 2").orderFee(149.99).build()
        );

        when(orderRepository.findAll()).thenReturn(orders);

        // When
        List<OrderDTO> result = orderService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getOrderFee()).isEqualTo(99.99);
        verify(orderRepository).findAll();
    }

    @Test
    void shouldFindOrderById() {
        // Given
        Order order = Order.builder().orderId(1).orderDesc("Order 1").orderFee(99.99).build();

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));

        // When
        OrderDTO result = orderService.findById(1);

        // Then
        assertThat(result.getOrderFee()).isEqualTo(99.99);
        verify(orderRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        // Given
        when(orderRepository.findById(1)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> orderService.findById(1))
                .isInstanceOf(OrderNotFoundException.class);
    }

    @Test
    void shouldCreateOrder() {
        // Given
        OrderDTO orderDTO = OrderDTO.builder().orderDesc("New Order").orderFee(99.99).build();
        Order savedOrder = Order.builder().orderId(1).orderDesc("New Order").orderFee(99.99).build();

        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);

        // When
        OrderDTO result = orderService.save(orderDTO);

        // Then
        assertThat(result.getOrderId()).isEqualTo(1);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void shouldUpdateOrder() {
        // Given
        OrderDTO orderDTO = OrderDTO.builder().orderId(1).orderDesc("Updated Order").build();
        Order updatedOrder = Order.builder().orderId(1).orderDesc("Updated Order").build();

        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        // When
        OrderDTO result = orderService.update(orderDTO);

        // Then
        assertThat(result.getOrderDesc()).isEqualTo("Updated Order");
        verify(orderRepository).save(any(Order.class));
    }
}