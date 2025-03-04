package com.example.ProductAndSupplyService.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ImageUpdateRequestDto {

    @NotBlank(message = "Image URL cannot be empty")
    private String newImage;
}
