package com.jaymorelli.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    private String id;
    private Item[] items;
    private String table;
    private double totalCost;
    private LocalDateTime createdAt;

    public Order(Item[] items, String table) {
        this.items = items;
        this.table = table;
        this.totalCost = this.calculateTotalCost();
        this.createdAt = LocalDateTime.now();
    }

    public double calculateTotalCost() {
        //1. Prepare result;
        double result = 0;

        //2. Sum all items;
        List<Item> itemsList = Arrays.asList(items);
        for (Item item : itemsList) {
            result += item.getPrice();
        }
        
        //3. Return value
        return result;
    }
}
