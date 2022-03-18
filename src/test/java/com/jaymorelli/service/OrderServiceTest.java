package com.jaymorelli.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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


    @Test
    void testCreateOrder() {
        //1. Mock repository 'save'
        when(repository.save(any(Order.class))).thenReturn(Mono.just(order));

        //2. Call service method synchronously
        Order result = service.createOrder(Mono.just(dto)).block();

        //3. Assert result
        assertEquals(result.getTotalCost(), 19.99, 0.1);
        assertEquals(result.getTable(), "table 4");
        assertArrayEquals(result.getItems(), items);
    }

    @Test
    void testMapperOrderDTOToEntity() {
        //1. Call service method
        Order result = service.mapperOrderDTOToEntity(dto);

        //2. Assert result
        assertEquals(result.getTotalCost(), 19.99, 0.1);
        assertEquals(result.getTable(), "table 4");
        assertArrayEquals(result.getItems(), items);
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
