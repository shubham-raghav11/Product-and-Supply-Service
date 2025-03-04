package com.example.ProductAndSupplyService.RequestDTO;

import com.example.ProductAndSupplyService.Enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ProductCreationRequestDto {

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Brand Name is required")
    private String brandName;

    @NotEmpty(message = "Images list cannot be empty") // Ensures the list is not empty
    private List<@NotBlank(message = "Image URL cannot be blank") String> images; // Ensures each URL is not blank

    @NotNull(message = "Category is required")
    private Category category;

    public @NotBlank(message = "Product name is required") String getProductName() {
        return productName;
    }

    public void setProductName(@NotBlank(message = "Product name is required") String productName) {
        this.productName = productName;
    }

    public @NotBlank(message = "Description is required") String getDescription() {
        return description;
    }

    public void setDescription(@NotBlank(message = "Description is required") String description) {
        this.description = description;
    }

    public @NotBlank(message = "Brand Name is required") String getBrand_Name() {
        return brandName;
    }

    public void setBrand_Name(@NotBlank(message = "Brand Name is required") String brandName) {
        this.brandName = brandName;
    }

    public @NotEmpty(message = "Images list cannot be empty") List<@NotBlank(message = "Image URL cannot be blank") String> getImages() {
        return images;
    }

    public void setImages(@NotEmpty(message = "Images list cannot be empty") List<@NotBlank(message = "Image URL cannot be blank") String> images) {
        this.images = images;
    }

    public @NotBlank(message = "Brand Name is required") String getBrandName() {
        return brandName;
    }

    public void setBrandName(@NotBlank(message = "Brand Name is required") String brandName) {
        this.brandName = brandName;
    }

    public @NotNull(message = "Category is required") Category getCategory() {
        return category;
    }

    public void setCategory(@NotNull(message = "Category is required") Category category) {
        this.category = category;
    }
}

