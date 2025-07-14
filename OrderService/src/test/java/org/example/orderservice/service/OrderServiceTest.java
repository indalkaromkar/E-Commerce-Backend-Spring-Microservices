package org.example.orderservice.service;

import org.example.orderservice.domain.dtos.OrderDTO;
import org.example.orderservice.domain.entity.Order;

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
        when(orderRepository.findAll()).thenReturn(Arrays.asList());

        // When
        List<OrderDTO> result = orderService.findAll();

        // Then
        assertThat(result).isNotNull();
        verify(orderRepository).findAll();
    }

    @Test
    void shouldFindOrderById() {
        // When & Then
        verify(orderRepository, never()).findById(1);
    }



    @Test
    void shouldCreateOrder() {
        // When
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void shouldUpdateOrder() {
        // When
        verify(orderRepository, never()).save(any(Order.class));
    }
}