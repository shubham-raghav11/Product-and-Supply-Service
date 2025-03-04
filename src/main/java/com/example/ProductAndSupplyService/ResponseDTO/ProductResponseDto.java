package com.example.ProductAndSupplyService.ResponseDTO;


import com.example.ProductAndSupplyService.Entity.Images;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ProductResponseDto {

    private String productName;
    private String description;
    private String brandName;
    private List<Images> images;
    private Double price;
//    private String message;


    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

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

    public void setBrand_Name(String brandName) {
        this.brandName = brandName;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }
}
