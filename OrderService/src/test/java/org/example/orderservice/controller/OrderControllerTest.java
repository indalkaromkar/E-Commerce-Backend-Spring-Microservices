package org.example.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orderservice.domain.dto.OrderDTO;
import org.example.orderservice.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetAllOrders() throws Exception {
        // Given
        List<OrderDTO> orders = Arrays.asList(
                OrderDTO.builder().orderId(1).userId(1).totalAmount(BigDecimal.valueOf(99.99)).build(),
                OrderDTO.builder().orderId(2).userId(2).totalAmount(BigDecimal.valueOf(149.99)).build()
        );
        when(orderService.findAll()).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.collection").isArray())
                .andExpect(jsonPath("$.collection[0].totalAmount").value(99.99));
    }

    @Test
    void shouldGetOrderById() throws Exception {
        // Given
        OrderDTO order = OrderDTO.builder().orderId(1).userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();
        when(orderService.findById(1)).thenReturn(order);

        // When & Then
        mockMvc.perform(get("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(99.99));
    }

    @Test
    void shouldCreateOrder() throws Exception {
        // Given
        OrderDTO orderDTO = OrderDTO.builder().userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();
        OrderDTO savedOrder = OrderDTO.builder().orderId(1).userId(1).totalAmount(BigDecimal.valueOf(99.99)).build();
        when(orderService.save(any(OrderDTO.class))).thenReturn(savedOrder);

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(1))
                .andExpect(jsonPath("$.totalAmount").value(99.99));
    }

    @Test
    void shouldUpdateOrderStatus() throws Exception {
        // Given
        OrderDTO orderDTO = OrderDTO.builder().orderId(1).status("CONFIRMED").build();
        when(orderService.updateStatus(eq(1), eq("CONFIRMED"))).thenReturn(orderDTO);

        // When & Then
        mockMvc.perform(put("/api/orders/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CONFIRMED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    @Test
    void shouldCancelOrder() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/orders/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }
}