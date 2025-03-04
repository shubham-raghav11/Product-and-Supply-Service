package com.example.ProductAndSupplyService.RequestDTO;

import com.example.ProductAndSupplyService.Enums.Category;
import lombok.Data;

import java.util.List;

@Data
public class ProductUpdationRequestDto {

    private String productName;
    private String description;
    private String brandName;
    private Boolean isActive;
    private List<String> images;
    private Category category;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
