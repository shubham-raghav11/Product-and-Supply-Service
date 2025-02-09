package com.example.ProductAndSupplyService.RequestDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class ImageUploadRequestDto {

    @NotEmpty(message = "Images list cannot be empty") // Ensures the list is not empty
    private List<@NotBlank(message = "Image URL cannot be blank") String> images;
}
