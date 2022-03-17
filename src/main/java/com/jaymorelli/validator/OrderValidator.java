package com.jaymorelli.validator;


import com.jaymorelli.dto.OrderDTO;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class OrderValidator implements org.springframework.validation.Validator {

    /**
     * This Validator validates *only* Order instances
     */
    public boolean supports(Class<?> clazz) {
        return OrderDTO.class.equals(clazz);
    }

    /**
     * Validate OrderDTO object. It will reject if the items or table are set to null or empty
     * @param obj
     * @param e
     */
    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "table", "table.empty");
        ValidationUtils.rejectIfEmpty(e, "items", "items.empty");
    }
    
}
