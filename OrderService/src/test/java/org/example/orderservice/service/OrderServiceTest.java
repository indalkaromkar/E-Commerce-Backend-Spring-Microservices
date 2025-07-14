package org.example.orderservice.service;

import org.example.orderservice.domain.dto.OrderDTO;
import org.example.orderservice.domain.entity.Order;
import org.example.orderservice.exception.OrderNotFoundException;
import org.example.orderservice.mapper.OrderMapping;
import org.example.orderservice.repository.OrderRepository;
import org.example.orderservice.service.impl.OrderServiceImplementation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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

    @Mock
    private OrderMapping orderMapping;

    @InjectMocks
    private OrderServiceImplementation orderService;

    @Test
    void shouldFindAllOrders() {
        // Given
        List<Order> orders = Arrays.asList(
                Order.builder().orderId(1).userId(1).totalAmount(BigDecimal.valueOf(99.99)).build(),
                Order.builder().orderId(2).userId(2).totalAmount(BigDecimal.valueOf(149.99)).build()
        );
        List<OrderDTO> orderDTOs = Arrays.asList(
                OrderDTO.builder().orderId(1).userId(1).totalAmount(BigDecimal.valueOf(99.99)).build(),
                OrderDTO.builder().orderId(2).userId(2).totalAmount(BigDecimal.valueOf(149.99)).build()
        );

        when(orderRepository.findAll()).thenReturn(orders);
        when(orderMapping.mapToDto(orders.get(0))).thenReturn(orderDTOs.get(0));
        when(orderMapping.mapToDto(orders.get(1))).thenReturn(orderDTOs.get(1));

        // When
        List<OrderDTO> result = orderService.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTotalAmount()).isEqualTo(BigDecimal.valueOf(99.99));
        verify(orderRepository).findAll();
    }

    @Test
    void shouldFindOrderById() {
        // Given
        Order order = Order.builder().orderId(1).userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();
        OrderDTO orderDTO = OrderDTO.builder().orderId(1).userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();

        when(orderRepository.findById(1)).thenReturn(Optional.of(order));
        when(orderMapping.mapToDto(order)).thenReturn(orderDTO);

        // When
        OrderDTO result = orderService.findById(1);

        // Then
        assertThat(result.getTotalAmount()).isEqualTo(BigDecimal.valueOf(99.99));
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
        OrderDTO orderDTO = OrderDTO.builder().userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();
        Order order = Order.builder().userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();
        Order savedOrder = Order.builder().orderId(1).userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();
        OrderDTO savedOrderDTO = OrderDTO.builder().orderId(1).userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();

        when(orderMapping.mapToEntity(orderDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(savedOrder);
        when(orderMapping.mapToDto(savedOrder)).thenReturn(savedOrderDTO);

        // When
        OrderDTO result = orderService.save(orderDTO);

        // Then
        assertThat(result.getOrderId()).isEqualTo(1);
        verify(orderRepository).save(order);
    }

    @Test
    void shouldUpdateOrderStatus() {
        // Given
        Order existingOrder = Order.builder().orderId(1).status("PENDING").build();
        Order updatedOrder = Order.builder().orderId(1).status("CONFIRMED").build();
        OrderDTO updatedOrderDTO = OrderDTO.builder().orderId(1).status("CONFIRMED").build();

        when(orderRepository.findById(1)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);
        when(orderMapping.mapToDto(updatedOrder)).thenReturn(updatedOrderDTO);

        // When
        OrderDTO result = orderService.updateStatus(1, "CONFIRMED");

        // Then
        assertThat(result.getStatus()).isEqualTo("CONFIRMED");
        verify(orderRepository).save(existingOrder);
    }
}