package com.jaymorelli.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jaymorelli.service.OrderService;

import org.junit.jupiter.api.Test;

public class OrderTest {

    // Dummy data
    private Item[] items1 = new Item[] {new Item("Item 1", 19.99, new Category("Product"))};
    private Item[] items2 = 
        new Item[] {
            new Item("Item 1", 10.01, new Category("Product")), 
            new Item("Item 2", 19.99, new Category("Product"))
        };
    private Item[] items3 = 
        new Item[] {
            new Item("Item 1", 10.01, new Category("Product")), 
            new Item("Item 2", 70.99, new Category("Product")), 
            new Item("Item 3", 19.99, new Category("Product"))
        };

    private Order order = new Order(
        items1,
        "table 4"
    );

    @Test
    void testCalculateTotalCost_1_item() {
        //1. Call method to calculate total cost
        Double result = order.calculateTotalCost();

        //2. Assert result
        assertEquals(19.99, result, 0.001);
    }

    @Test
    void testCalculateTotalCost_2_item() {
        //1. Set order items
        order.setItems(items2);

        //2. Call method to calculate total cost
        Double result = order.calculateTotalCost();

        //3. Assert result
        assertEquals(30.0, result, 0.001);
    }

    @Test
    void testCalculateTotalCost_3_item() {
        //1. Set order items
        order.setItems(items3);

        //2. Call method to calculate total cost
        Double result = order.calculateTotalCost();

        //3. Assert result
        assertEquals(100.99, result, 0.001);
    }
}
