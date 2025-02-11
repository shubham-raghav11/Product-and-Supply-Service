package com.example.ProductAndSupplyService.SuccessResponse;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL) // This removes `data` if it's null
public class SuccessResponseDto {
    private String message;
    private Object data;

    public SuccessResponseDto(String message) {
        this.message = message;
        this.data = null; // Set data as null when not provided
    }

    public SuccessResponseDto(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }
}
