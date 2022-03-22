package com.jaymorelli.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.springframework.data.domain.Sort;
import java.time.LocalDateTime;

import com.jaymorelli.dto.OrderDTO;
import com.jaymorelli.model.Category;
import com.jaymorelli.model.Item;
import com.jaymorelli.model.Order;
import com.jaymorelli.repository.OrderRepository;
import com.jaymorelli.validator.OrderValidator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ServerWebInputException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



@ExtendWith(SpringExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @InjectMocks
    private OrderService service;

    // Dummy data
    private Item[] items = new Item[] {new Item("Item 1", 19.99, new Category("Product"))};
    private OrderDTO dto = new OrderDTO(
        "id",
        items,
        "table 4"
    );
    private Order order = new Order(
        items,
        "table 4"
    );
    private Order order2 = new Order(
        items,
        "table 2"
    );


    @Test
    void testCreateOrder() {
        //1. Mock repository 'save'
        when(repository.save(any(Order.class))).thenReturn(Mono.just(order));

        //2. Call service method synchronously
        Order result = service.createOrder(Mono.just(dto)).block();

        //3. Assert result
        assertEquals(19.99, result.getTotalCost(), 0.1);
        assertEquals("table 4", result.getTable());
        assertArrayEquals(items, result.getItems());
    }

    @Test
    void testUpdateOrder() {
        //1. Mock repository 'save' and 'findById' calls.
        when(repository.findById("id")).thenReturn(Mono.just(order2));
        when(repository.save(order2)).thenReturn(Mono.just(order2));


        //2. Call service method synchronously
        Order result = service.updateOrder("id", Mono.just(dto)).block();

        //3. Assert result
        assertEquals(19.99, result.getTotalCost(), 0.1);
        assertEquals("table 4", result.getTable()); //dto Table value
        assertArrayEquals(items, result.getItems());
    }

    @Test
    void testGetOrder() {
        //1. Mock repository 'findAll'
        when(repository.findAll()).thenReturn(Flux.just(order, order2));

        //2. Call service method synchronously
        Order result1 = service.getOrders().blockFirst();
        Order result2 = service.getOrders().blockLast();

        //3. Assert result
        assertEquals(19.99, result1.getTotalCost(), 0.1);
        assertEquals("table 4", result1.getTable());
        assertArrayEquals(items, result1.getItems());
        assertEquals(19.99, result2.getTotalCost(), 0.1);
        assertEquals("table 2", result2.getTable());
        assertArrayEquals(items, result2.getItems());
    }

    @Test
    void testGetOrder_Sort() {
        //1. Mock repository 'findAll'
        when(repository.findAll(Sort.by(Sort.Direction.fromString("asc"), "table"))).thenReturn(Flux.just(order2, order));

        //2. Call service method synchronously
        Order result1 = service.getOrders("asc", "table").blockFirst();
        Order result2 = service.getOrders("asc", "table").blockLast();

        //3. Assert result
        assertEquals(19.99, result1.getTotalCost(), 0.1);
        assertEquals("table 2", result1.getTable());
        assertArrayEquals(items, result1.getItems());
        assertEquals(19.99, result2.getTotalCost(),  0.1);
        assertEquals("table 4", result2.getTable());
        assertArrayEquals(items, result2.getItems());
    }

    @Test
    void testGetOrder_id() {
        //1. Mock repository 'findById'
        when(repository.findById("id")).thenReturn(Mono.just(order));

        //2. Call service method synchronously
        Order result = service.getOrder("id").block();

        //3. Assert result
        assertEquals(19.99, result.getTotalCost(), 0.1);
        assertEquals("table 4", result.getTable());
        assertArrayEquals(items, result.getItems());
    }

    @Test
    void testMapperOrderDTOToEntity() {
        //1. Call service method
        Order result = service.mapperOrderDTOToEntity(dto);

        //2. Assert result
        assertEquals(19.99, result.getTotalCost(), 0.1);
        assertEquals("table 4", result.getTable());
        assertArrayEquals(items, result.getItems());
    }



    @Test
    void testRemoveOrder() {
        //1. Mock repository 'findAll'
        when(repository.findById("id")).thenReturn(Mono.just(order));
        when(repository.delete(order)).thenReturn(Mono.empty());

        //2. Call service method synchronously
        service.removeOrder("id").block();

        //3. Assert
        verify(repository, times(1)).delete(order);
        verify(repository, times(1)).findById("id");
    }


    @Test
    void testValidate_Error() {
        //1. Set table to an invalid value.
        dto.setTable(null);

        //2. Assert exception is thrown
        Throwable exception = assertThrows(ServerWebInputException.class, () -> service.createOrder(Mono.just(dto)).block());
        assertTrue(exception.getMessage().contains("400 BAD_REQUEST"));
    }

    @Test
    void testValidator_Supports() {
        //1. Create a validator instance.
        OrderValidator validator = new OrderValidator();

        //2. Asserts
        assertFalse(validator.supports(Order.class));
        assertTrue(validator.supports(OrderDTO.class));
    }
}
